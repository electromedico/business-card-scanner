package fr.alteca.monalteca.textrazor.conectivity

import android.content.Context
import android.util.Log
import fr.alteca.monalteca.R
import fr.alteca.monalteca.textrazor.model.TextRazorResponse
import okhttp3.FormBody
import okhttp3.FormBody.Builder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


/**
 * Constructor
 * @param context Context
 * @param listener TextRazorOnTaskCompleted
 */
class PostController(val context: Context, val listener: TextRazorOnTaskCompleted): Callback<TextRazorResponse> {
    val apiKey=context.resources.getString(R.string.api_key)
    val client=RetrofitClient.getClient()
    val textRazorApi=client.create(TextRazorApi::class.java)


    fun startNerService(text:String){
        val body:FormBody= Builder().add("text",text).add("extractors", ENTITIES).add("languageOverride", LANG_FR).build()
        val call:Call<TextRazorResponse> = textRazorApi.postQuery(apiKey,body)
        call.enqueue(this)
    }

    override fun onFailure(call: Call<TextRazorResponse>, t: Throwable) {
        Log.e("postController",t.localizedMessage)
    }

    override fun onResponse(call: Call<TextRazorResponse>, response: Response<TextRazorResponse>) {
       if (response.isSuccessful){
           try {
               if ( response.body()!=null){
                 val body=response.body()!!
                 listener.onTaskCompletedGet(body)
               }

           }catch (e:Exception){
               Log.e("PostController", e.printStackTrace().toString())
           }
       }
    }

    companion object {
        val ENTITIES="entities"
        val LANG_FR= "fre"
    }
}