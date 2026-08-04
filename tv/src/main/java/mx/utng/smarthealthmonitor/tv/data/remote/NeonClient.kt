package mx.utng.smarthealthmonitor.tv.data.remote

import kotlinx.serialization.Serializable
import mx.utng.smarthealthmonitor.domain.model.LecturaFC
import mx.utng.smarthealthmonitor.tv.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

@Serializable
data class NeonRequest(
    val query: String,
    val params: List<Any> = emptyList()
)

@Serializable
data class NeonResponse<T>(
    val rows: List<T> = emptyList(),
    val rowCount: Int = 0,
    val command: String = ""
)

@Serializable
data class LecturaFcDto(
    val id: Int = 0,
    val bpm: Int = 0,
    val estado: String = "",
    val dispositivo: String = "",
    val hora: String = "",
    val fecha: String = "",
    val created_at: String = ""
) {
    fun toLecturaFC(): LecturaFC {
        return LecturaFC(
            id = id,
            bpm = bpm,
            estado = if (dispositivo.isNotBlank()) "$estado ($dispositivo)" else estado,
            hora = hora
        )
    }
}

interface NeonApiService {
    @POST("sql")
    suspend fun executeQuery(
        @Header("Authorization") auth: String? = null,
        @Header("Neon-Connection-String") connStr: String,
        @Body request: NeonRequest
    ): NeonResponse<LecturaFcDto>
}

object NeonClient {
    private val BASE_HOST = if (BuildConfig.NEON_HOST.isNotBlank()) BuildConfig.NEON_HOST else "placeholder.neon.tech"
    private val BASE_URL = "https://$BASE_HOST/"
    val AUTH_HEADER: String? = null
    val CONN_STRING = "postgresql://neondb_owner:npg_yuIgAJ7hFC6P@${BuildConfig.NEON_HOST}/neondb?sslmode=require"

    val api: NeonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()
            .create(NeonApiService::class.java)
    }
}
