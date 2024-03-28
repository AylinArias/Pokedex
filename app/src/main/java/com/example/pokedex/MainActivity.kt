package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pokedex.ui.screens.PokemonScreen
import com.example.pokedex.ui.viewmodel.PokemonViewModel
import com.example.pokedex.utils.LocationClient

// Actividad principal de la aplicación
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configura la interfaz de usuario de la actividad con Compose
        setContent {
            // Llama a la función PokedexApp para inicializar la aplicación
            PokedexApp(LocationClient(context = applicationContext))
        }
    }
}

// Composable que representa la aplicación de la Pokédex
@Composable
fun PokedexApp(locationClient: LocationClient) {
    // Obtiene una instancia de PokemonViewModel usando viewModel
    val viewModel: PokemonViewModel = viewModel(factory = PokemonViewModelFactory(locationClient))
    // Llama a PokemonScreen para mostrar la pantalla principal de Pokémon
    PokemonScreen(viewModel, locationClient)
}
