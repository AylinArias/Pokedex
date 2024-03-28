package com.example.pokedex.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat

class LocationClient(private val context: Context) {

    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null

    // Inicia las actualizaciones de ubicación
    @SuppressLint("MissingPermission")
    fun startLocationUpdates(callback: (CustomLocation) -> Unit) {
        // Verificar si se tienen los permisos necesarios
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Se llama cuando la ubicación del dispositivo cambia
                val customLocation = CustomLocation(location.latitude, location.longitude)
                callback(customLocation)
            }

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        locationManager?.let {
            // Solicitar actualizaciones de ubicación con el proveedor de GPS
            it.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                locationListener!!
            )
        }
    }


    // Vibra el dispositivo
    fun vibrateDevice() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500)
        }
    }

    companion object {
        // Tiempo mínimo entre actualizaciones de ubicación (en milisegundos)
        private const val MIN_TIME_BETWEEN_UPDATES: Long = 5000 // 5 segundos

        // Distancia mínima requerida para que las actualizaciones se consideren válidas (en metros)
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f // 10 metros
    }
}
