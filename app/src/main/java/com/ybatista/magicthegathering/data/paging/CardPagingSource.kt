package com.ybatista.magicthegathering.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ybatista.magicthegathering.data.api.CardApiService
import com.ybatista.magicthegathering.domain.Card

private const val INDEX = 1

class CardPagingSource(private val cardApiService: CardApiService) : PagingSource<Int, Card>() {
    override fun getRefreshKey(state: PagingState<Int, Card>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Card> {
        val position = params.key ?: INDEX

        return try {
            val response = cardApiService.getUsers(position)
            val cards = response.body()!!.cards

            LoadResult.Page(
                data = cards,
                prevKey = if (position == INDEX) null else position - 1,
                nextKey = if (cards.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}