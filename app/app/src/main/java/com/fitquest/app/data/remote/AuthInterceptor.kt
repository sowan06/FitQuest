package com.fitquest.app.data.remote

import com.fitquest.app.data.auth.TokenStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Attaches the JWT to every outbound request and clears it on 401 so the UI
 * can react via TokenStore.authState.
 */
class AuthInterceptor @Inject constructor(
    private val tokenStore: TokenStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = tokenStore.token()?.let {
            original.newBuilder().addHeader("Authorization", "Bearer $it").build()
        } ?: original

        val response = chain.proceed(request)
        if (response.code == 401 && tokenStore.token() != null) {
            tokenStore.clear()
        }
        return response
    }
}
