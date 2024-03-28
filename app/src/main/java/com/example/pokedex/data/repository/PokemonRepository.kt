package com.example.pokedex.data.repository

import com.example.pokedex.data.api.PokemonApiService
import com.example.pokedex.data.model.Pokemon
import com.example.pokedex.data.model.PokemonResponse

// Repositorio que maneja las operaciones de obtención de datos relacionadas con Pokémon.
class PokemonRepository(private val apiService: PokemonApiService) {

    // Método para obtener un Pokémon aleatorio.
    suspend fun getRandomPokemon(): PokemonResponse {
        return try {
            // Genera un ID aleatorio entre 1 y 500.
            val randomId = (1..500).random()
            // Llama al método de la API para obtener un Pokémon aleatorio.
            apiService.getRandomPokemon(randomId)
        } catch (e: Exception) {
            // En caso de error, lanza la excepción.
            throw e
        }
    }

    // Método para buscar un Pokémon en función de la latitud y longitud proporcionadas.
    suspend fun searchPokemon(latitude: Double, longitude: Double): Pokemon {
        return try {
            // Llama al método de la API para buscar un Pokémon según la latitud y longitud proporcionadas.
            val pokemonResponse = apiService.searchPokemon(latitude, longitude)
            // Mapea la respuesta de la API a un objeto Pokemon.
            mapPokemonResponseToPokemon(pokemonResponse)
        } catch (e: Exception) {
            // En caso de error, lanza la excepción.
            throw e
        }
    }

    // Método privado para mapear la respuesta de la API a un objeto Pokemon.
    private fun mapPokemonResponseToPokemon(pokemonResponse: PokemonResponse): Pokemon {
        // Obtiene la URL del sprite frontal predeterminado del Pokémon, o una cadena vacía si no está presente.
        val frontDefaultUrl = pokemonResponse.sprites?.front_default ?: ""
        // Obtiene el nombre del Pokémon, o una cadena vacía si no está presente.
        val pokemonName = pokemonResponse.name ?: ""
        // Retorna un objeto Pokemon con el nombre y la URL del sprite frontal predeterminado.
        return Pokemon(pokemonName, frontDefaultUrl)
    }

}
