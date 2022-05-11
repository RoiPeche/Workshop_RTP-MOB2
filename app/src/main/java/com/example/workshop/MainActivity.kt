package com.example.workshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private data class  TypeInfo (
        val name: String,
        val url: String,
            )

    private data class  PokemonType (
        val type: TypeInfo
            )

    private data class  PokemonSprites (
        val front_default: String,
            )

    private  data class PokemonInfo (
        val id: Int,
        val name: String,
        val sprites: PokemonSprites,
        val types: List<PokemonType>,
            )

    private data class Pokemon (
        val name: String,
        val url: String,
        var info: PokemonInfo,
    )

    private data class PokemonList (
        val results: List<Pokemon>
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var pokemonlist: PokemonList
        loadPokemonList(151) {
            pokemonlist = it
            for (i in 0 until 151) {
                println(pokemonlist.results[i].info.name)
                println(pokemonlist.results[i].info.id)
                println(pokemonlist.results[i].info.types[0].type.name)
                println("--------")
            }
        }
    }

    private fun loadPokemonList (limit: Int, cb: (PokemonList) -> Unit) {

        val httpAsync = "https://pokeapi.co/api/v2/pokemon?limit=${limit}&offset=0"
            .httpGet()
            .responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        val data = result.get()
                        val parsedJson = Gson().fromJson<PokemonList>(data, PokemonList::class.java)
                        for (i in 0 until limit) {
                            loadPokemonProfile(parsedJson.results[i].url) {
                                parsedJson.results[i].info = it
                                if (parsedJson.results[i].info.id == limit) {
                                    cb(parsedJson)
                                }
                            }
                        }
                    }
                }
            }
        httpAsync.join()
    }

    private fun loadPokemonProfile (url: String, cb: (PokemonInfo) -> Unit) {

        val httpAsync = url
            .httpGet()
            .responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        val data = result.get()
                        val pokemon = Gson().fromJson<PokemonInfo>(data, PokemonInfo::class.java)
                        cb(pokemon)
                    }
                }
            }
        httpAsync.join()
    }
}