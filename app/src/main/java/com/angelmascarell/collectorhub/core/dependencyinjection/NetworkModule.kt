package com.angelmascarell.collectorhub.core.dependencyinjection

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.angelmascarell.collectorhub.core.network.AuthInterceptor
import com.angelmascarell.collectorhub.data.local.TokenManager
import com.angelmascarell.collectorhub.data.network.MangaApiService
import com.angelmascarell.collectorhub.data.repository.MangaRepository
import com.angelmascarell.collectorhub.home.data.network.response.HomeClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val WITH_HEADER = "WithHeader"
    private const val WITHOUT_HEADER = "WithoutHeader"

    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton
    @Provides
    fun provideGson(): Gson {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Ajusta el formato si es necesario
        return GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, JsonDeserializer { json, _, _ ->
                LocalDate.parse(json.asString, dateFormatter)
            })
            .create()
    }

    @Singleton
    @Provides
    @Named(WITH_HEADER)
    fun provideRetrofitWithHeader(@ApplicationContext context: Context, gson: Gson): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val accessToken = kotlinx.coroutines.runBlocking {
                    TokenManager(context).getToken()
                }
                Log.d("Interceptor", "Token: $accessToken")
                if (accessToken.isEmpty()) {
                    Log.e("Interceptor", "Token vacío!")
                }
                val original: Request = chain.request()
                val requestBuilder: Request.Builder = original.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
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

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideLoginClient(@Named(WITHOUT_HEADER) retrofit: Retrofit): HomeClient {
        return retrofit.create(HomeClient::class.java)
    }

    @Provides
    fun provideMangaRepository(
        apiService: MangaApiService,
        dataStore: DataStore<Preferences>  // Agregar DataStore como parámetro
    ): MangaRepository {
        return MangaRepository(apiService, dataStore)  // Pasar DataStore al constructor
    }


    @Singleton
    @Provides
    fun provideMangaApiService(@Named(WITH_HEADER) retrofit: Retrofit): MangaApiService {
        return retrofit.create(MangaApiService::class.java)
    }


/*    @Provides
    fun provideHomeViewModel(repository: MangaRepository): HomeViewModel {
        return HomeViewModel(repository)
    }

 */
}
