package com.chaos.mangaties.presentation.dashboard.bookshelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.model.manga.manga.FavouriteManga
import com.chaos.mangaties.domain.repository.manga.FavouriteRepository
import com.chaos.mangaties.domain.repository.manga.MangaRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavouriteUiState(
    val mangas: List<FavouriteManga> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val favouriteRepository: FavouriteRepository,
    private val mangaRepository: MangaRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavouriteUiState(isLoading = true))
    val favouriteMangas: StateFlow<FavouriteUiState> = _uiState.asStateFlow()

    init {
        loadFavourite()
    }

    fun loadFavourite(){
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            favouriteRepository.getFavourite(userId).collect { favouriteIds ->
                val favouriteMangas = mutableListOf<FavouriteManga>()
                var hasError = false

                for (mangaId in favouriteIds){
                    try {
                        val result = mangaRepository.getMangaDetails(mangaId)
                        result.onSuccess { mangaDetails ->
                            favouriteMangas.add(
                                FavouriteManga(
                                    mangaId = mangaId,
                                    title = mangaDetails.title,
                                    coverFileName = mangaDetails.coverFileName
                                )
                            )
                        }.onFailure {
                            hasError = true
                        }
                    } catch (e: Exception) {
                        hasError = true
                    }
                }

                _uiState.value = FavouriteUiState(
                    mangas = favouriteMangas,
                    isLoading = false,
                    error = if (hasError) "Some manga details could not be loaded" else null
                )
            }
        }
    }

    fun refreshFavourites(){
        loadFavourite()
    }

}