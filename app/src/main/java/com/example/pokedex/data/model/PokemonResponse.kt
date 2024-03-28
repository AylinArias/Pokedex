package com.example.pokedex.data.model

import com.google.gson.annotations.SerializedName

// Clase de datos que representa la respuesta de la API para un Pokémon.
data class PokemonResponse(
    // Nombre del Pokémon.
    @SerializedName("name") val name: String,
    // Sprites del Pokémon.
    @SerializedName("sprites") val sprites: Sprites
)

// Clase de datos que representa los sprites de un Pokémon.
data class Sprites(
    // URL del sprite frontal predeterminado del Pokémon.
    @SerializedName("front_default") val front_default: String
)
