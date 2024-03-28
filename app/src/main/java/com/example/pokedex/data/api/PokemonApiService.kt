package com.example.pokedex.data.api

import com.example.pokedex.data.model.PokemonResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Interfaz que define las operaciones disponibles en la API de Pokémon.
interface PokemonApiService {

    // Obtiene un Pokémon aleatorio según el ID proporcionado.
    @GET("pokemon/{id}")
    suspend fun getRandomPokemon(@Path("id") id: Int): PokemonResponse

    // Busca un Pokémon en función de la latitud y longitud proporcionadas.
    @GET("pokemon/search")
    suspend fun searchPokemon(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): PokemonResponse

    companion object {
        // URL base de la API de Pokémon.
        private const val BASE_URL = "https://pokeapi.co/api/v2/"

        // Instancia única de la interfaz PokemonApiService.
        @Volatile
        private var instance: PokemonApiService? = null

        // Método para obtener la instancia de PokemonApiService.
        fun getInstance(): PokemonApiService {
            // Si la instancia ya existe, la devuelve.
            // De lo contrario, crea una nueva instancia de Retrofit y la instancia de PokemonApiService asociada.
            return instance ?: synchronized(this) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val newInstance = retrofit.create(PokemonApiService::class.java)
                instance = newInstance
                newInstance
            }
        }
    }
}
