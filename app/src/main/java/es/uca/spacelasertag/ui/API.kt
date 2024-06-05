package es.uca.spacelasertag.ui
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.kotlinx.serializer.KotlinxSerializer
import io.ktor.client.request.delete
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import kotlinx.serialization.Serializable

class API {
    private val client = HttpClient(Android)

    // suspend indica que esta función es una función de suspensión, lo que
    // significa que puede hacer llamadas de red u otras operaciones de E/S
    // de manera asíncrona sin bloquear el subproceso principal.

    suspend fun createReserva(sala: String, nickname: String, numeroCarnet: String, telefono: String,
                              email: String, fecha: String, horaInicio: String, horaFin: String,
                              numPersonas: String, comentario: String): HttpResponse {
        return client.submitForm(
            url = "http://10.0.2.2:8080/reservas",
            formParameters = Parameters.build {
                append("sala", sala)
                append("nickname", nickname)
                append("numeroCarnet", numeroCarnet)
                append("telefono", telefono)
                append("email", email)
                append("fecha", fecha)
                append("horaInicio", horaInicio)
                append("horaFin", horaFin)
                append("numPersonas", numPersonas)
                append("comentario", comentario)
            },
            encodeInQuery = false
        )
    }

    suspend fun getReserva(id: String): HttpResponse {
        // Obtiene la respuesta HTTP como antes
        val response = client.get("http://10.0.2.2:8080/reservas/$id")
        return response
    }
    suspend fun getReservas(): HttpResponse {
        // Obtiene la respuesta HTTP como antes
        val response = client.get("http://10.0.2.2:8080/reservas")
        return response
    }
    suspend fun eliminarReserva(id: String): HttpResponse {
        // Obtiene la respuesta HTTP como antes
        val response = client.delete("http://10.0.2.2:8080/reservas/$id")
        return response
    }

}