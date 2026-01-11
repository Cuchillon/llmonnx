package com.ferick.llmonnx.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class ChatCompletionResponse(
    val id: String = "chatcmpl-${UUID.randomUUID()}",
    @JsonProperty("object")
    val `object`: String = "chat.completion",
    val created: Long = System.currentTimeMillis() / 1000,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage? = null
)

data class Choice(
    val index: Int = 0,
    val message: Message,
    @JsonProperty("finish_reason")
    val finishReason: String = "stop"
)

data class Usage(
    @JsonProperty("prompt_tokens")
    val promptTokens: Int,
    @JsonProperty("completion_tokens")
    val completionTokens: Int,
    @JsonProperty("total_tokens")
    val totalTokens: Int
)
