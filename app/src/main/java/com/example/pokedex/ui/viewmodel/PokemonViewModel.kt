package com.example.pokedex.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.api.PokemonApiService
import com.example.pokedex.data.model.Pokemon
import com.example.pokedex.data.repository.PokemonRepository
import com.example.pokedex.utils.CustomLocation
import com.example.pokedex.utils.LocationClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

// ViewModel para la pantalla de Pokémon
class PokemonViewModel(private val locationClient: LocationClient) : ViewModel() {

    // Servicio de la API de Pokémon
    private val apiService = PokemonApiService.getInstance()

    // Repositorio de Pokémon
    private val pokemonRepository = PokemonRepository(apiService)

    // Pokémon actual
    private val _currentPokemon = MutableLiveData<Pokemon>()
    val currentPokemon: LiveData<Pokemon> = _currentPokemon

    // Última ubicación conocida del usuario
    private var lastLocation: CustomLocation? = null

    // Excepciones en las corrutinas
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        // Manejar errores de manera más específica según el caso de uso
        _errorMessage.value = "Error: ${exception.message}"
    }

    // Indica si se ha encontrado un Pokémon
    private val _pokemonFound = MutableLiveData<Boolean>()
    val pokemonFound: LiveData<Boolean> = _pokemonFound

    // Mensaje de error
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // Función para obtener un Pokémon aleatorio
    fun getRandomPokemon() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val pokemonResponse = pokemonRepository.getRandomPokemon()
            val imageUrl = pokemonResponse.sprites?.front_default
            _currentPokemon.value = imageUrl?.let { Pokemon(pokemonResponse.name, it) }
        }
    }

    // Función para buscar Pokémon utilizando GPS
    fun searchPokemonWithGPS() {
        locationClient.startLocationUpdates(::handleLocationUpdate)
    }

    // Función para las actualizaciones de ubicación
    internal fun handleLocationUpdate(location: CustomLocation) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val pokemon = pokemonRepository.searchPokemon(location.latitude, location.longitude)
            _currentPokemon.value = pokemon

            // Verificar si se ha movido al menos 10 metros desde la última ubicación conocida
            if (lastLocation != null && distanceMoved(location)) {
                // Vibra el dispositivo
                locationClient.vibrateDevice()

                // Muestra la alerta de "Pokémon encontrado"
                showAlert("¡Pokémon encontrado!", pokemon)

                // Marca que se ha encontrado un Pokémon
                _pokemonFound.value = true
            }

            // Actualizar la última ubicación conocida del usuario
            lastLocation = location
        }
    }

    // Función para verificar si la ubicación del usuario ha cambiado al menos 10 metros
    private fun distanceMoved(newLocation: CustomLocation): Boolean {
        return (lastLocation?.let { calculateDistance(newLocation, it) } ?: false) >= 10 as Nothing
    }

    // Función para calcular la distancia entre dos ubicaciones
    private fun calculateDistance(newLocation: CustomLocation, lastLocation: CustomLocation): Float {
        // Utiliza alguna fórmula adecuada para calcular la distancia entre dos ubicaciones GPS
        // Por ejemplo, podrías utilizar la fórmula de Haversine
        return 0f
    }

    // Función para mostrar una alerta con el mensaje y el Pokémon encontrado
    private fun showAlert(message: String, pokemon: Pokemon) {
        _errorMessage.value = "$message: ${pokemon.name}"
    }
}
