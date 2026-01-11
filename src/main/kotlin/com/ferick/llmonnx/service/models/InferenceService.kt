package com.ferick.llmonnx.service.models

import ai.onnxruntime.genai.Generator
import ai.onnxruntime.genai.GeneratorParams
import ai.onnxruntime.genai.Model
import ai.onnxruntime.genai.Tokenizer
import com.ferick.llmonnx.common.extensions.setOptionsFrom
import com.ferick.llmonnx.configuration.properties.ModelProperties
import com.ferick.llmonnx.model.dto.ChatCompletionRequest
import com.ferick.llmonnx.model.LLM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.springframework.beans.factory.DisposableBean

sealed class InferenceService : DisposableBean {
    abstract val modelProperties: ModelProperties
    abstract val llm: LLM
    abstract val model: Model

    fun supports(model: String) = model == llm.modelName

    protected abstract fun formatPrompt(tokenizer: Tokenizer, request: ChatCompletionRequest): String
    protected abstract fun isSpecial(token: String, tokenId: Int): Boolean

    open suspend fun generateStream(
        request: ChatCompletionRequest
    ): Flow<String> = flow {
        Tokenizer(model).use { tokenizer ->
            val prompt = formatPrompt(tokenizer, request)
            val inputIds = tokenizer.encode(prompt).getSequence(0)

            GeneratorParams(model).use { params ->
                params.setOptionsFrom(request)

                Generator(model, params).use { generator ->
                    generator.appendTokens(inputIds)

                    while (!generator.isDone) {
                        generator.generateNextToken()
                        val tokenId = generator.getLastTokenInSequence(0L)
                        val token = tokenizer.decode(intArrayOf(tokenId))
                        if (tokenId in tokenizer.eosTokenIds) {
                            break
                        } else if (!isSpecial(token, tokenId)) {
                            emit(token)
                        }
                    }
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun destroy() {
        runCatching {
            model.close()
        }
    }

    protected fun loadModel(): Model {
        val modelPath = modelProperties
            .metadata[llm.modelName ]?.path
            ?: throw IllegalStateException("There is no path found for model ${llm.modelName}")
        return Model(modelPath)
    }
}
