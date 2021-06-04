package com.ybatista.magicthegathering.di


import com.ybatista.magicthegathering.App
import com.ybatista.magicthegathering.data.api.CardApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.CacheControl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    companion object {
        const val BASE_URL = "https://api.magicthegathering.io/"
        const val HEADER_CACHE_CONTROL = "Cache-Control"
        const val HEADER_PRAGMA = "Pragma"
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): CardApiService =
        retrofit.create(CardApiService::class.java)

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache())             // we could cache requests using room library
            .addNetworkInterceptor(networkInterceptor())
            .addInterceptor(offlineInterceptor())
            .build()
    }

    private val cacheSize = (5 * 1024 * 1024 // 5 MB
            ).toLong()

    private fun cache(): Cache {
        return Cache(File(App.instance?.cacheDir, "cache"), cacheSize)
    }

    private fun networkInterceptor(): Interceptor {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val response: Response = chain.proceed(chain.request())
                val cacheControl: CacheControl = CacheControl.Builder()
                    .build()
                return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build()
            }
        }
    }

    private fun offlineInterceptor(): Interceptor {
        return object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request: Request = chain.request()

                val cacheControl: CacheControl = CacheControl.Builder()
                    .maxStale(30, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()

                return chain.proceed(request)
            }
        }
    }

}