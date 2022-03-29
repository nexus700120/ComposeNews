package com.epa.composenews

interface UiState {
    object FullScreenProgress : UiState
    class FullScreenError(val text: String) : UiState
}