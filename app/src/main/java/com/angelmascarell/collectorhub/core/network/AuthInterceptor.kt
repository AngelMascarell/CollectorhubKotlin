package com.angelmascarell.collectorhub.core.network

import com.angelmascarell.collectorhub.data.local.TokenManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    @OptIn(DelicateCoroutinesApi::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var token = ""

        // Usar un coroutine para obtener el token asincrónicamente
        GlobalScope.launch {
            token = tokenManager.getToken()
        }

        // Si el token no está vacío, agregarlo a la cabecera
        val originalRequest: Request = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        if (token.isNotEmpty()) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val request: Request = requestBuilder.build()

        // Continuar la solicitud
        return chain.proceed(request)
    }
}
