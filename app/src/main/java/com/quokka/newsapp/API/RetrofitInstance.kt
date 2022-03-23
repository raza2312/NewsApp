package com.quokka.newsapp.API

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object Factory {
        private var retrofit: Retrofit? = null
        public var api: NewsAPI? = null
        private var apiKey: String? = null

        private const val BASE_URL: String = "https://newsapi.org/v2/"

        private fun buildRetrofit(): Retrofit? {
            
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
            val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
                val original: Request = chain.request()
                val originalHttpUrl: HttpUrl = original.url
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apikey", apiKey)
                    .build()
                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url)
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            }.addInterceptor(logging).build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        }

        @Synchronized
        fun initialize(apiKey: String) {
            this.apiKey = apiKey
            retrofit ?: synchronized(this) {
                retrofit = buildRetrofit()
            }
            api = retrofit?.create(NewsAPI::class.java)
        }

    }

}