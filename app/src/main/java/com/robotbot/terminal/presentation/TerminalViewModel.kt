package com.robotbot.terminal.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotbot.terminal.data.ApiFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TerminalViewModel : ViewModel() {

    private val apiService = ApiFactory.apiService

    private val _state = MutableStateFlow<TerminalScreenState>(TerminalScreenState.Initial)
    val state = _state.asStateFlow()

    private var lastState: TerminalScreenState = TerminalScreenState.Initial

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _state.value = lastState
    }

    init {
        loadBarList()
    }

    fun loadBarList(timeFrame: TimeFrame = TimeFrame.HOUR_1) {
        lastState = state.value
        _state.value = TerminalScreenState.Loading
        viewModelScope.launch(exceptionHandler) {
            val barList = apiService.loadBars(timeFrame = timeFrame.value).barList
            _state.value = TerminalScreenState.Content(barList = barList, timeFrame = timeFrame)
        }
    }

}