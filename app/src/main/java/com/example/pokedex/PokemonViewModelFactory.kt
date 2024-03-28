package com.example.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.ui.viewmodel.PokemonViewModel
import com.example.pokedex.utils.LocationClient

// Fábrica para crear instancias de PokemonViewModel con un LocationClient dado
class PokemonViewModelFactory(private val locationClient: LocationClient) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica si el modelo solicitado es una instancia de PokemonViewModel
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Crea una instancia de PokemonViewModel con el LocationClient dado
            return PokemonViewModel(locationClient) as T
        }
        // Si el modelo solicitado no es una instancia de PokemonViewModel, lanza una excepción
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
