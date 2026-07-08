package com.chaos.mangaties.presentation.dashboard.manga.screen.mangadetailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaos.mangaties.domain.repository.manga.FavouriteRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MangaChaptersViewModel @Inject constructor(
    private val favouriteRepository: FavouriteRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _isFavourite = MutableStateFlow(false)
    val isFavourite: StateFlow<Boolean> = _isFavourite.asStateFlow()

    fun loadFavouriteStatus(mangaId: String){
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            favouriteRepository.isFavourite(mangaId, userId).collect { isFav ->
                _isFavourite.value = isFav
            }
        }
    }

    fun toggleFavourite(mangaId: String){
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            if (_isFavourite.value){
                favouriteRepository.removeFavourite(mangaId,userId)
            } else{
                favouriteRepository.addFavourite(mangaId, userId)
            }
        }
    }

}