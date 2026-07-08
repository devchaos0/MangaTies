package com.chaos.mangaties.presentation.dashboard.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.model.manga.manga.SearchManga
import com.chaos.mangaties.domain.repository.manga.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

data class SearchUiState(
    val searchResults: List<SearchManga> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasMoreResults: Boolean = true,
    val totalResults: Int = 0,
    val currentQuery: String = ""
)

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private var currentOffset = 0
    private val pageSize = 10
    private var isSearching = false
    private var totalResults = 0

    @OptIn(FlowPreview::class)
    val searchDebounced = _searchQuery
        .debounce(500L)
        .distinctUntilChanged()
        .filter { it.isNotEmpty() }
        .onEach { query ->
            performSearch(query, reset = true)
        }
        .launchIn(viewModelScope)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            _uiState.value = SearchUiState(
                searchResults = emptyList(),
                isLoading = false,
                hasMoreResults = false
            )
            currentOffset = 0
            totalResults = 0
            isSearching = false
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (!currentState.hasMoreResults || currentState.isLoadingMore || currentState.isLoading) {
            return
        }
        if (currentState.currentQuery.isNotEmpty()) {
            performSearch(currentState.currentQuery, reset = false)
        }
    }

    private fun performSearch(query: String, reset: Boolean) {
        if (isSearching) return
        isSearching = true
        viewModelScope.launch{
            try {
                val isLoadMore = !reset
                if (isLoadMore) {
                    _uiState.value = _uiState.value.copy(isLoadingMore = true)
                } else {
                    _uiState.value = SearchUiState(
                        isLoading = true,
                        currentQuery = query
                    )
                    currentOffset = 0
                }

                val result = searchRepository.searchManga(
                    query = query,
                    offset = currentOffset,
                    limit = pageSize
                )

                result.onSuccess { (results, total) ->
                    totalResults = total

                    val newResults = if (reset) {
                        results
                    } else {
                        (_uiState.value.searchResults + results).distinctBy { it.id }
                    }

                    val hasMore = newResults.size < total

                    _uiState.value = _uiState.value.copy(
                        searchResults = newResults,
                        isLoading = false,
                        isLoadingMore = false,
                        hasMoreResults = hasMore,
                        totalResults = total,
                        error = null,
                        currentQuery = query
                    )

                    currentOffset += results.size
                }.onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = error.message ?: "Failed to search",
                        hasMoreResults = false
                    )
                }
            } finally {
                isSearching = false
            }
        }

    }

}