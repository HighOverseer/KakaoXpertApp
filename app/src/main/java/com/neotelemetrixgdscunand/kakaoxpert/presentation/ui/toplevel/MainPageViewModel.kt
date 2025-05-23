package com.neotelemetrixgdscunand.kakaoxpert.presentation.ui.toplevel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainPageViewModel @Inject constructor():ViewModel() {
    private val _userMessage = Channel<String>()
    val userMessage = _userMessage.receiveAsFlow()

    fun showUserMessage(message:String){
        viewModelScope.launch {
            _userMessage.send(message)
        }
    }
}