package fr.alteca.monalteca.textrazor.conectivity

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        val BASE_URL= "https://api.textrazor.com/"
        fun getClient():Retrofit{
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}