package br.com.orcamentofacil.framework

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val retrofit = getApi(Api::class.java)
private const val TIMEOUT: Long = 30

fun <T> getApi(serviceClass: Class<T>): T {

    val loggingInterceptor = HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
    client.readTimeout(TIMEOUT, TimeUnit.SECONDS)
    client.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
    client.addInterceptor(loggingInterceptor)

    val gson = GsonBuilder()
        .setLenient()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'")
        .create()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://easy-budget-api.herokuapp.com/")
        .client(client.build())
        .addConverterFactory(GsonConverterFactory.create(gson))

    return retrofit.build().create(serviceClass)
}//62470aacbd93b41ff9e541c9