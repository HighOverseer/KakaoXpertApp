package com.neotelemetrixgdscunand.kakaoxpert.domain.data

import com.neotelemetrixgdscunand.kakaoxpert.domain.common.LocationError
import com.neotelemetrixgdscunand.kakaoxpert.domain.common.Result
import com.neotelemetrixgdscunand.kakaoxpert.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationManager {

    fun getLocationUpdated(): Flow<Result<Location, LocationError>>

    companion object {
        // when location is not available
        // Jakarta's Coordinate
        const val FALLBACK_LOCATION_LATITUDE = -6.1944491
        const val FALLBACK_LOCATION_LONGITUDE = 106.8229198
    }

}