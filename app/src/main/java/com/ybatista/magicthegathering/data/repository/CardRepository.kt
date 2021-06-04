package com.ybatista.magicthegathering.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ybatista.magicthegathering.data.api.CardApiService
import com.ybatista.magicthegathering.data.paging.CardPagingSource
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val cardApiService: CardApiService
) {
    fun getCards() = Pager(
        config = PagingConfig(pageSize = 100),
        pagingSourceFactory = { CardPagingSource(cardApiService) }
    ).flow

}