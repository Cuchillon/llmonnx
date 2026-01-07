package com.ferick.llmonnx.service.models

import ai.onnxruntime.genai.Tokenizer
import com.fasterxml.jackson.databind.ObjectMapper
import com.ferick.llmonnx.configuration.properties.ModelProperties
import com.ferick.llmonnx.model.CompletionRequest
import com.ferick.llmonnx.model.LLM
import org.springframework.stereotype.Service

@Service
class Qwen3InferenceService(
    override val modelProperties: ModelProperties,
    private val objectMapper: ObjectMapper
) : InferenceService() {
    override val llm = LLM.QWEN3_4B_INSTRUCT_2507
    override val model by lazy {
        loadModel()
    }

    override fun formatPrompt(tokenizer: Tokenizer, request: CompletionRequest): String =
        tokenizer.applyChatTemplate(
            null,
            objectMapper.writeValueAsString(request.messages),
            null,
            true
        )

    override fun isSpecial(token: String, tokenId: Int) =
        token.isBlank() || token.contains("<|") || token == "<s>" || token == "</s>"
}
