package com.ferick.llmonnx.model

data class CompletionRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Float? = null,
    val topP: Float? = null,
    val repetitionPenalty: Float? = null,
    val maxTokens: Int? = null
)
