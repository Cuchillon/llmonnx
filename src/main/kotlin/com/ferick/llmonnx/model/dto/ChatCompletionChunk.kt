package com.ferick.llmonnx.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class ChatCompletionChunk(
    val id: String,
    @JsonProperty("object")
    val `object`: String = "chat.completion.chunk",
    val created: Long = System.currentTimeMillis() / 1000,
    val model: String,
    val choices: List<ChunkChoice>
)

data class ChunkChoice(
    val index: Int = 0,
    val delta: Delta,
    @JsonProperty("finish_reason")
    val finishReason: String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Delta(
    val role: String? = null,
    val content: String? = null
)
