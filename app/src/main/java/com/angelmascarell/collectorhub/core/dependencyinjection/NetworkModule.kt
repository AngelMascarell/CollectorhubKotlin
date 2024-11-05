package com.angelmascarell.collectorhub.core.dependencyinjection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.angelmascarell.collectorhubApp.signin.data.network.response.LoginClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val WITH_HEADER = "WithHeader"
    private const val WITHOUT_HEADER = "WithoutHeader"

    @Singleton
    @Provides
    @Named(WITH_HEADER)
    fun provideRetrofitWithHeader(@ApplicationContext context: Context): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()
                val accessToken: String = runBlocking {
                    context.dataStore.data
                        .map { preferences ->
                            preferences[stringPreferencesKey("accessToken")] ?: ""
                        }
                        .first()
                }
                val requestBuilder: Request.Builder = original.newBuilder()
                    .header("auth-token", accessToken)
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Singleton
    @Provides
    @Named(WITHOUT_HEADER)
    fun provideRetrofitWithoutHeader(): Retrofit {
        val httpClient = OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }


    @Singleton
    @Provides
    fun provideLoginClient(@Named(WITHOUT_HEADER) retrofit: Retrofit): LoginClient {
        return retrofit.create(LoginClient::class.java)
    }
}
