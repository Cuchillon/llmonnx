package com.ferick.llmonnx.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatCompletionRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean = false,
    val temperature: Float? = null,
    @JsonProperty("top_p")
    val topP: Float? = null,
    @JsonProperty("repetition_penalty")
    val repetitionPenalty: Float? = null,
    @JsonProperty("max_tokens")
    val maxTokens: Int? = null,
    val stop: List<String>? = null
)
