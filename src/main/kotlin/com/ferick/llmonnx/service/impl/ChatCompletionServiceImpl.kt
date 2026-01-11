package com.ferick.llmonnx.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.ferick.llmonnx.model.dto.ChatCompletionChunk
import com.ferick.llmonnx.model.dto.ChatCompletionRequest
import com.ferick.llmonnx.model.dto.ChatCompletionResponse
import com.ferick.llmonnx.model.dto.Choice
import com.ferick.llmonnx.model.dto.ChunkChoice
import com.ferick.llmonnx.model.dto.Delta
import com.ferick.llmonnx.model.dto.Message
import com.ferick.llmonnx.service.ChatCompletionService
import com.ferick.llmonnx.service.models.InferenceService
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.toList
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChatCompletionServiceImpl(
    private val inferenceServices: Set<InferenceService>,
    private val objectMapper: ObjectMapper
) : ChatCompletionService {

    override suspend fun generate(request: ChatCompletionRequest): ResponseEntity<Any> {
        val inferenceService = inferenceServices
            .find { it.supports(request.model) }
            ?: throw IllegalStateException("There is no model with name ${request.model}")
        return if (request.stream) {
            val responseId = "chatcmpl-${UUID.randomUUID()}"
            var isFirstToken = true
            ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(
                    inferenceService.generateStream(request)
                        .map { token ->
                            val chunk = ChatCompletionChunk(
                                id = responseId,
                                model = request.model,
                                choices = listOf(
                                    ChunkChoice(
                                        delta = if (isFirstToken) {
                                            isFirstToken = false
                                            Delta(role = RESPONSE_ROLE, content = token)
                                        } else {
                                            Delta(content = token)
                                        }
                                    )
                                )
                            )
                            "data: ${objectMapper.writeValueAsString(chunk)}\n\n"
                        }
                        .onCompletion {
                            val finalChunk = ChatCompletionChunk(
                                id = responseId,
                                model = request.model,
                                choices = listOf(ChunkChoice(delta = Delta(), finishReason = "stop"))
                            )
                            emit("data: ${objectMapper.writeValueAsString(finalChunk)}\n\n")
                            emit(" [DONE]\n\n")
                        }
                )
        } else {
            val fullResponse = inferenceService.generateStream(request)
                .toList()
                .joinToString("")

            val response = ChatCompletionResponse(
                model = request.model,
                choices = listOf(
                    Choice(
                        message = Message(role = RESPONSE_ROLE, content = fullResponse)
                    )
                )
            )
            ResponseEntity.ok(response)
        }
    }

    companion object {
        private const val RESPONSE_ROLE = "assistant"
    }
}
