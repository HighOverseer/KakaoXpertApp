package com.neotelemetrixgdscunand.kamekapp.presentation.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neotelemetrixgdscunand.kamekapp.domain.common.LocationError
import com.neotelemetrixgdscunand.kamekapp.domain.common.Result
import com.neotelemetrixgdscunand.kamekapp.domain.common.RootNetworkError
import com.neotelemetrixgdscunand.kamekapp.domain.data.LocationManager
import com.neotelemetrixgdscunand.kamekapp.domain.data.WeatherRepository
import com.neotelemetrixgdscunand.kamekapp.domain.model.Location
import com.neotelemetrixgdscunand.kamekapp.presentation.dui.WeatherForecastItemDui
import com.neotelemetrixgdscunand.kamekapp.presentation.mapper.WeatherDuiMapper
import com.neotelemetrixgdscunand.kamekapp.presentation.utils.UIText
import com.neotelemetrixgdscunand.kamekapp.presentation.utils.toErrorUIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationManager: LocationManager,
    private val mapper: WeatherDuiMapper
) : ViewModel() {

    private val _onMessageEvent = Channel<UIText>()
    val onMessageEvent = _onMessageEvent.receiveAsFlow()

    private var _isGettingWeatherForecastForSeveralDays = MutableStateFlow(false)
    private val _isGettingWeatherForecastOverview = MutableStateFlow(false)
    val isLoading = combine(
        _isGettingWeatherForecastOverview,
        _isGettingWeatherForecastForSeveralDays
    ) { isGettingWeatherOverview, isGettingWeatherForSeveralDays ->
        isGettingWeatherOverview || isGettingWeatherForSeveralDays
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    private var isFirstTimeFetchingData = true

    private val _uiEvent = Channel<WeatherUIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var locationUpdateJob: Job? = null

    fun startLocationUpdates() {
        locationUpdateJob?.cancel()
        locationUpdateJob = viewModelScope.launch {
            locationManager
                .getLocationUpdated()
                .flowOn(Dispatchers.IO)
                .collectLatest { result ->
                    when (result) {
                        is Result.Error -> {
                            when (result.error) {
                                is LocationError.ResolvableSettingsError -> {
                                    _uiEvent.send(WeatherUIEvent.OnLocationResolvableError(result.error.exception))
                                }

                                is LocationError.UnknownError -> {
                                    val errorUIText = result.toErrorUIText()
                                    _uiEvent.send(WeatherUIEvent.OnLocationUnknownError(errorUIText))
                                }

                                is LocationError.UnexpectedErrorWithMessage -> {
                                    val errorUIText = UIText.DynamicString(result.error.message)
                                    _uiEvent.send(WeatherUIEvent.OnLocationUnknownError(errorUIText))
                                }
                            }
                            _currentLocation.update { null }
                        }

                        is Result.Success -> {
                            val location = result.data
                            _currentLocation.update { location }
                        }
                    }
                }
        }
    }

    fun stopLocationUpdates() {
        locationUpdateJob?.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherForecastOverview = _currentLocation.flatMapLatest { location ->
        weatherRepository.getWeatherForecastOverviewAutoUpdate(
            latitude = location?.latitude ?: LocationManager.FALLBACK_LOCATION_LATITUDE,
            longitude = location?.longitude ?: LocationManager.FALLBACK_LOCATION_LONGITUDE
        ).map { result ->
            when (result) {
                is Result.Error -> {
                    val errorUIText = result.toErrorUIText()
                    _onMessageEvent.send(errorUIText)
                    null
                }

                is Result.Success -> {
                    if (isFirstTimeFetchingData) {
                        getWeatherForecastForSeveralDays(
                            location?.latitude ?: LocationManager.FALLBACK_LOCATION_LATITUDE,
                            location?.longitude ?: LocationManager.FALLBACK_LOCATION_LONGITUDE
                        )
                        isFirstTimeFetchingData = false
                    }
                    _isGettingWeatherForecastOverview.update { false }
                    return@map mapper.mapWeatherForecastOverviewToDui(result.data)
                }
            }
        }
    }.onStart { _isGettingWeatherForecastOverview.update { true } }
        .onCompletion { _isGettingWeatherForecastOverview.update { false } }
        .flowOn(Dispatchers.IO)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )


    private val _weatherForecastForSeveralDays =
        MutableStateFlow<ImmutableList<WeatherForecastItemDui>>(
            persistentListOf()
        )
    val weatherForecastForSeveralDays = _weatherForecastForSeveralDays.asStateFlow()

    private var fetchWeatherForecastForSeveralDaysJob: Job? = null

    private fun getWeatherForecastForSeveralDays(
        latitude: Double,
        longitude: Double
    ) {
        suspend fun fetchWeatherForecastForSeveralDays(
            latitude: Double,
            longitude: Double
        ): Boolean {
            coroutineContext.ensureActive()

            val result = weatherRepository.getWeatherForecastForSeveralDays(latitude, longitude)
            return when (result) {
                is Result.Error -> {
                    coroutineContext.ensureActive()

                    val errorUIText = result.toErrorUIText()
                    _onMessageEvent.send(errorUIText)
                    result.error == RootNetworkError.REQUEST_TIMEOUT
                }

                is Result.Success -> {
                    coroutineContext.ensureActive()

                    val weatherForecastItems = result.data.mapNotNull {
                        mapper.mapWeatherForecastItemToDui(it)
                    }
                    _weatherForecastForSeveralDays.update { weatherForecastItems.toImmutableList() }
                    false
                }
            }
        }

        fetchWeatherForecastForSeveralDaysJob?.cancel()
        fetchWeatherForecastForSeveralDaysJob = viewModelScope.launch(Dispatchers.IO) {
            _isGettingWeatherForecastForSeveralDays.update { true }
            do {
                val isTimeout = fetchWeatherForecastForSeveralDays(
                    latitude, longitude
                )
            } while (isTimeout)

            _isGettingWeatherForecastForSeveralDays.update { false }
        }.also {
            it.invokeOnCompletion {
                _isGettingWeatherForecastForSeveralDays.update { false }
            }
        }

    }


}