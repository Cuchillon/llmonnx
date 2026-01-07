package com.ferick.llmonnx.service.models

import ai.onnxruntime.genai.Tokenizer
import com.ferick.llmonnx.configuration.properties.ModelProperties
import com.ferick.llmonnx.model.CompletionRequest
import com.ferick.llmonnx.model.LLM
import org.springframework.stereotype.Service

@Service
class SmolLM2InferenceService(
    override val modelProperties: ModelProperties
) : InferenceService() {
    override val llm = LLM.SMOLLM2_360M
    override val model by lazy {
        loadModel()
    }

    override fun formatPrompt(tokenizer: Tokenizer, request: CompletionRequest): String {
        val builder = StringBuilder()
        for (message in request.messages) {
            when (message.role) {
                "user" -> builder.append("User: ${message.content}\n")
                "assistant" -> builder.append("Assistant: ${message.content}\n")
            }
        }
        builder.append("Assistant:")
        return builder.toString()
    }

    override fun isSpecial(token: String, tokenId: Int) = token.isBlank() || tokenId == 0
}
