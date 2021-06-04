package com.ybatista.magicthegathering.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ybatista.magicthegathering.data.repository.CardRepository
import com.ybatista.magicthegathering.domain.Card
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val cardRepository: CardRepository
) : ViewModel() {

    val cards: LiveData<PagingData<Card>> = cardRepository.getCards()
        .cachedIn(viewModelScope)
        .asLiveData(Dispatchers.IO)
}