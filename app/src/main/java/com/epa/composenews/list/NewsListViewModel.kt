package com.epa.composenews.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epa.composenews.NewsRepository
import com.epa.composenews.common.News
import com.epa.composenews.common.Status
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsListViewModel(
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.FullScreenProgress)
    val uiState = _uiState.asStateFlow()

    private val news = mutableListOf<News>()
    private var loadMoreJob: Job? = null

    init {
        retry()
    }

    fun loadMore() {
        if (loadMoreJob?.isActive != true) {
            loadMoreJob = viewModelScope.launch {
                val result = buildList<NewsListItem> {
                    addAll(news.map { NewsListItem.Item(it) })
                    runCatching { repository.news(news.last().date) }
                        .onSuccess {
                            if (it.status == Status.ERROR) {
                                add(NewsListItem.Error)
                            } else {
                                news.addAll(it.news)
                                addAll(it.news.map { news -> NewsListItem.Item(news) })
                                if (it.news.size < it.total) {
                                    add(NewsListItem.Progress)
                                }
                            }
                        }
                        .onFailure { add(NewsListItem.Error) }
                }
                _uiState.emit(NewsUiState.Content(result))
            }
        }
    }

    fun retryLoadMore() {
        loadMoreJob?.cancel()
        _uiState.tryEmit(NewsUiState.Content(
            buildList {
                addAll(news.map { NewsListItem.Item(it) })
                add(NewsListItem.Progress)
            }
        ))
        loadMore()
    }

    fun retry() {
        viewModelScope.launch {
            _uiState.emit(NewsUiState.FullScreenProgress)
            runCatching { repository.news() }
                .onSuccess {
                    if (it.status == Status.ERROR) {
                        _uiState.emit(NewsUiState.FullScreenError(it.message.orEmpty()))
                    } else {
                        news.addAll(it.news)
                        val items = buildList {
                            addAll(it.news.map { news -> NewsListItem.Item(news) })
                            if (it.news.size < it.total) {
                                add(NewsListItem.Progress)
                            }
                        }
                        _uiState.emit(NewsUiState.Content(items))
                    }
                }
                .onFailure { _uiState.emit(NewsUiState.FullScreenError(it.message.orEmpty())) }
        }
    }
}