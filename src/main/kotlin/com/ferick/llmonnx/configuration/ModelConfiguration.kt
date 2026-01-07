package com.ferick.llmonnx.configuration

import com.ferick.llmonnx.configuration.properties.ModelProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ModelProperties::class)
class ModelConfiguration
