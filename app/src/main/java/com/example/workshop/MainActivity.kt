package com.example.workshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Ici, on crée deux data class qui vont matcher la structure des éléments que l'on souhaite récupérer dans le JSON.
        //Il est donc important que l'on nomme les éléments de notre data class de la même manière qu'ils sont nommés dans le JSON.
        data class Pokemon (
            val name: String,
            val url: String,
                )

        data class RequestResult (
            val results: List<Pokemon>
                )

        val limit = 5
        
        val httpAsync = "https://pokeapi.co/api/v2/pokemon?limit=${limit}&offset=0"
            .httpGet()
            .responseString { request, response, result ->
                println(request)
                println(response)
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        val data = result.get()
                        val ParsedJson = Gson().fromJson<RequestResult>(data, RequestResult::class.java)
                        for (i in 0 until limit) {
                            println("My Pokemon name is ${ParsedJson.results[i].name}")
                            println("My Pokemon URL is ${ParsedJson.results[i].url}")
                        }
                    }
                }
            }

        httpAsync.join()
    }
}