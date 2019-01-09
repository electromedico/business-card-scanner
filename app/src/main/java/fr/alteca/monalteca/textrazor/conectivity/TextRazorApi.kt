package fr.alteca.monalteca.textrazor.conectivity

import fr.alteca.monalteca.textrazor.model.TextRazorResponse
import okhttp3.FormBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface TextRazorApi {

    @POST("./")
    fun  postQuery(@Header ("x-textrazor-key") apiKey:String, @Body text:FormBody): Call<TextRazorResponse>
}