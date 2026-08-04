package mx.utng.smarthealthmonitor.data.remote

import com.example.smarthealthmonitor.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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
