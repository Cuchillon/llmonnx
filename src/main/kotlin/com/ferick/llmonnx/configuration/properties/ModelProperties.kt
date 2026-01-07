package com.ferick.llmonnx.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("model")
data class ModelProperties @ConstructorBinding constructor(
    val metadata: Map<String, ModelMetadata>
)

data class ModelMetadata @ConstructorBinding constructor(
    val path: String
)
