package com.neotelemetrixgdscunand.kamekapp.data.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.neotelemetrixgdscunand.kamekapp.domain.common.LocationError
import com.neotelemetrixgdscunand.kamekapp.domain.common.Result
import com.neotelemetrixgdscunand.kamekapp.domain.data.LocationManager
import com.neotelemetrixgdscunand.kamekapp.domain.model.Location
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume

class LocationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val geoCoder: Geocoder
) : LocationManager {

    @SuppressLint("MissingPermission") // Make Sure Permission is Already Handled
    override fun getLocationUpdated(): Flow<Result<Location, LocationError>> {
        return callbackFlow {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val locationRequest = getLocationRequest()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation
                    launch(Dispatchers.IO) {
                        location?.let {
                            val cityName = getCityNameOfLocation(
                                location.latitude,
                                location.longitude
                            )

                            send(
                                Result.Success(
                                    Location(
                                        latitude = location.latitude,
                                        longitude = location.longitude,
                                        name = cityName
                                    )
                                )
                            )
                        }
                    }
                }

            }

            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

            val client = LocationServices.getSettingsClient(context)
            client.checkLocationSettings(builder.build())
                .addOnSuccessListener {
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        null
                    )
                }
                .addOnFailureListener { exception ->
                    when (exception) {
                        is ResolvableApiException -> {
                            trySend(
                                Result.Error(
                                    LocationError.ResolvableSettingsError(
                                        exception
                                    )
                                )
                            )
                        }

                        else -> {
                            val message = exception.localizedMessage
                            if (message != null) {
                                trySend(
                                    Result.Error(
                                        LocationError.UnexpectedErrorWithMessage(
                                            message
                                        )
                                    )
                                )
                            } else trySend(Result.Error(LocationError.UnknownError))
                        }
                    }
                }

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    private fun getLocationRequest(): LocationRequest {
        val priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        val interval = TimeUnit.MINUTES.toMillis(10)
        val maxWaitTime = TimeUnit.MINUTES.toMillis(30)

        val locationRequest = LocationRequest.Builder(
            priority,
            interval
        ).apply {
            setMaxUpdateDelayMillis(maxWaitTime)
        }.build()

        return locationRequest
    }

    @Suppress("DEPRECATION")
    private suspend fun getCityNameOfLocation(
        latitude: Double,
        longitude: Double
    ): String? {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { continuation ->
                geoCoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                ) { addresses ->
                    if (continuation.isActive) {
                        continuation.resume(addresses.firstOrNull()?.locality)
                    }
                }
            }

        } else {
            val addresses = geoCoder.getFromLocation(
                latitude,
                longitude,
                1
            )

            coroutineContext.ensureActive()

            if (addresses?.isNotEmpty() == true) {
                val cityName = addresses[0].locality
                cityName
            } else null
        }
    }
}