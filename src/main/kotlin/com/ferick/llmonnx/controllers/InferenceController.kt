package com.ferick.llmonnx.controllers

import com.ferick.llmonnx.model.dto.ChatCompletionRequest
import com.ferick.llmonnx.service.ChatCompletionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class InferenceController(
    private val chatCompletionService: ChatCompletionService
) {

    @PostMapping("/chat/completions")
    suspend fun completion(@RequestBody request: ChatCompletionRequest): ResponseEntity<Any> =
        chatCompletionService.generate(request)
}
