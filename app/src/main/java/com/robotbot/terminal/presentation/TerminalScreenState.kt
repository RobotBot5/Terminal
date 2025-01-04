package com.robotbot.terminal.presentation

import com.robotbot.terminal.data.Bar

sealed interface TerminalScreenState {

    data object Initial : TerminalScreenState

    data class Content(val barList: List<Bar>) : TerminalScreenState

}