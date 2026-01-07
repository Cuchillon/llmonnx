package com.ferick.llmonnx.controllers

import com.ferick.llmonnx.model.CompletionRequest
import com.ferick.llmonnx.service.models.InferenceService
import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/completion")
class InferenceController(
    private val inferenceServices: Set<InferenceService>
) {

    @PostMapping("/stream", produces = [MediaType.TEXT_EVENT_STREAM_VALUE, MediaType.TEXT_PLAIN_VALUE])
    suspend fun streamCompletion(@RequestBody request: CompletionRequest): Flow<String> =
        inferenceServices
            .find { it.supports(request.model) }?.generateStream(request)
            ?: throw IllegalStateException("There is no model with name ${request.model}")
}
