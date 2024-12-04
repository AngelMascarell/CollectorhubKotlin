package com.angelmascarell.collectorhub.core.network

import com.angelmascarell.collectorhub.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Obtener el token de forma sincrónica
        val token = kotlinx.coroutines.runBlocking {
            tokenManager.getToken()
        }

        // Construir la solicitud con el token, si está disponible
        val originalRequest: Request = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        if (token.isNotEmpty()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val request: Request = requestBuilder.build()

        // Continuar con la solicitud
        return chain.proceed(request)
    }
}

