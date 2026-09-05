package com.example.exploreats.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

data class UserLocationResult(
    val latLng: LatLng,
    val cityName: String,
    val regionName: String
)

object LocationHelper {

    fun hasLocationPermissions(context: Context): Boolean {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineGranted || coarseGranted
    }

    suspend fun getCurrentLocation(context: Context): UserLocationResult? {
        if (!hasLocationPermissions(context)) return null

        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        return suspendCancellableCoroutine { continuation ->
            val cancellationTokenSource = CancellationTokenSource()

            try {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        val addressDetails = getCityNameFromCoordinates(context, location.latitude, location.longitude)
                        if (continuation.isActive) {
                            continuation.resume(
                                UserLocationResult(
                                    latLng = latLng,
                                    cityName = addressDetails.first,
                                    regionName = addressDetails.second
                                )
                            )
                        }
                    } else {
                        // Fallback to last known location if current location is null
                        fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc: Location? ->
                            if (lastLoc != null) {
                                val latLng = LatLng(lastLoc.latitude, lastLoc.longitude)
                                val addressDetails = getCityNameFromCoordinates(context, lastLoc.latitude, lastLoc.longitude)
                                if (continuation.isActive) {
                                    continuation.resume(
                                        UserLocationResult(
                                            latLng = latLng,
                                            cityName = addressDetails.first,
                                            regionName = addressDetails.second
                                        )
                                    )
                                }
                            } else {
                                if (continuation.isActive) continuation.resume(null)
                            }
                        }.addOnFailureListener {
                            if (continuation.isActive) continuation.resume(null)
                        }
                    }
                }.addOnFailureListener {
                    if (continuation.isActive) continuation.resume(null)
                }
            } catch (_: SecurityException) {
                if (continuation.isActive) continuation.resume(null)
            }
        }
    }

    private fun getCityNameFromCoordinates(context: Context, latitude: Double, longitude: Double): Pair<String, String> {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val city = address.locality ?: address.subAdminArea ?: address.adminArea ?: "Ubicación actual"
                val country = address.countryName ?: "Zona activa"
                val region = if (!address.adminArea.isNullOrEmpty()) "${address.adminArea} · $country" else country
                Pair("$city, $country", region)
            } else {
                Pair("Ubicación detectada", "Zona activa")
            }
        } catch (_: Exception) {
            Pair("Ubicación detectada", "Zona activa")
        }
    }
}
