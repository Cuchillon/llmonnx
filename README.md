# LLM ONNX inference spring boot application

----

### Как запустить

Клонировать проект

Скачать файлы модели из репозитория и разместить их в плоской структуре

[Qwen3-4B-Instruct-2507-ONNX](https://huggingface.co/onnx-community/Qwen3-4B-Instruct-2507-ONNX/tree/main)

![QWEN3_4B_STRUCTURE](docs/qwen3_4b_structure.png)

[SmolLM2-360M-ONNX](https://huggingface.co/onnx-community/SmolLM2-360M-ONNX/tree/main)

![SMOLLM2_360M_STRUCTURE](docs/smollm2_360m_structure.png)

Указать пути в `src/main/resources/application.yml` или прокинуть через переменные окружения

![PATHS_IN_APPLICATION](docs/paths_in_application.png)

Собрать и запустить JAR-файл

```
cd llmonnx
./gradlew clean build
java -jar build/libs/llmonnx-0.0.1-SNAPSHOT.jar
```

----

### Сделать запрос

```
POST http://localhost:8080/api/v1/completion/stream

Headers:
Accept: text/plain или text/event-stream

Body:
{
  "model": "SmolLM2-360M-ONNX",
  "messages": [
    {
      "role": "user",
      "content": "What color does the sun have?"
    }
  ],
    "temperature": 0.3,
    "topP": 0.9,
    "repetitionPenalty": 1.2,
    "maxTokens": 16
}
```
