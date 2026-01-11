package com.ferick.llmonnx.service

import com.ferick.llmonnx.model.dto.ChatCompletionRequest
import org.springframework.http.ResponseEntity

interface ChatCompletionService {
    suspend fun generate(request: ChatCompletionRequest): ResponseEntity<Any>
}
