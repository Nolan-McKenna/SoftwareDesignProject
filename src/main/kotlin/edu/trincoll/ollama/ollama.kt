package edu.trincoll.ollama
import kotlinx.serialization.Serializable
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

@Serializable
data class OllamaRequest (
    val model : String,
    val prompt : String,
    val stream : Boolean,
)

@Serializable
data class OllamaResponse (
    val model : String,
    val response : String,
)

suspend fun main() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    try {
        val response = client.post("http://localhost:11434/api/generate") {
            contentType(ContentType.Application.Json)
            setBody(OllamaRequest("llama3.2:1b", "How are you?", false))
        }

        val ollamaResponse: OllamaResponse = response.body()
        print(ollamaResponse.model + "\n")
        print(ollamaResponse.response)

    } catch (e: Exception) {
        print(e.message)
    } finally{
        client.close()
    }

}