package com.ferick.llmonnx.common.extensions

import ai.onnxruntime.genai.GeneratorParams
import com.ferick.llmonnx.model.CompletionRequest

private const val DEFAULT_TEMPERATURE = 0.7
private const val DEFAULT_TOP_P = 0.9
private const val DEFAULT_REPETITION_PENALTY = 1.05
private const val DEFAULT_MAX_TOKENS = 1024.0

fun GeneratorParams.setOptionsFrom(request: CompletionRequest) {
    this.apply {
        setSearchOption("do_sample", true)
        setSearchOption("temperature", request.temperature?.toDouble() ?: DEFAULT_TEMPERATURE)
        setSearchOption("top_p", request.topP?.toDouble() ?: DEFAULT_TOP_P)
        setSearchOption(
            "repetition_penalty",
            request.repetitionPenalty?.toDouble() ?: DEFAULT_REPETITION_PENALTY
        )
        setSearchOption("max_length", request.maxTokens?.toDouble() ?: DEFAULT_MAX_TOKENS)
    }
}
