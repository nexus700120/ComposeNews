package com.epa.composenews.list

sealed interface NewsUiState {
    object FullScreenProgress : NewsUiState
    class FullScreenError(val text: String) : NewsUiState
    class Content(val items: List<NewsListItem>) : NewsUiState
}