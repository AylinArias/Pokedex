package com.example.pokedex.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.pokedex.data.model.Pokemon
import com.example.pokedex.ui.viewmodel.PokemonViewModel
import com.example.pokedex.utils.LocationClient

// Pantalla principal de Pokémon
@Composable
fun PokemonScreen(viewModel: PokemonViewModel, locationClient: LocationClient) {
    var showPokemon by remember { mutableStateOf(false) }

    // Observa el estado actual del Pokémon desde el ViewModel
    val pokemon by viewModel.currentPokemon.observeAsState()

    // Solicita permisos de ubicación al presionar el botón "Buscar Pokémon"
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Inicia las actualizaciones de ubicación si se otorgan los permisos
                locationClient.startLocationUpdates(viewModel::handleLocationUpdate)
                // Llama a la función de búsqueda de Pokémon con GPS si se otorgan los permisos
                viewModel.searchPokemonWithGPS()
            }
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PokemonContent(
            pokemon = pokemon,
            showPokemon = showPokemon,
            onShowPokemonClick = {
                // Al hacer clic en el botón "Mostrar Pokémon", solicita un nuevo Pokémon y lo muestra
                viewModel.getRandomPokemon()
                showPokemon = true
            },
            onSearchPokemonClick = {
                // Al hacer clic en el botón "Buscar Pokémon", lanza el proceso de solicitud de permisos de ubicación
                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        )
    }

}

@Composable
fun PokemonContent(
    pokemon: Pokemon?,
    showPokemon: Boolean,
    onShowPokemonClick: () -> Unit,
    onSearchPokemonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Pokedex",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Botón para mostrar un nuevo Pokémon al presionarlo
        Button(onClick = { onShowPokemonClick() }) {
            Text(text = "Mostrar Pokémon")
        }

        // Botón para buscar Pokémon utilizando GPS
        Button(onClick = { onSearchPokemonClick() }) {
            Text(text = "Buscar Pokémon")
        }

        // Verifica si se debe mostrar el Pokémon y si está disponible
        if (showPokemon && pokemon != null) {
            // Muestra la tarjeta del Pokémon
            PokemonCard(pokemon = pokemon)
        }
    }
}

// Mostrar la información de un Pokémon
@Composable
fun PokemonCard(pokemon: Pokemon) {
    Card(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Muestra la imagen del Pokémon si la URL es válida
            if (isValidImageUrl(pokemon.imageUrl)) {
                Image(
                    painter = rememberImagePainter(pokemon.imageUrl),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Muestra un mensaje de error si la URL de la imagen no es válida
                Text("URL de imagen incorrecta: Verifica que la URL de la imagen sea válida y apunte a una imagen existente en el servidor.")
            }

            // Muestra el nombre del Pokémon
            Text(
                text = "Nombre: ${pokemon.name}",
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

// Función para verificar si la URL de la imagen del Pokémon es válida
private fun isValidImageUrl(imageUrl: String?): Boolean {
    // Devuelve true para todas las URL de imagen.
    return true
}

@Preview
@Composable
fun PreviewPokedexScreen() {
    val context = LocalContext.current
    val locationClient = LocationClient(context)
    val viewModel = PokemonViewModel(locationClient)
    PokemonScreen(viewModel, locationClient)
}
