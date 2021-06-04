package com.ybatista.magicthegathering.data.api

import com.ybatista.magicthegathering.data.model.CardsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CardApiService {

    @GET("v1/cards")
    suspend fun getUsers(@Query("page") page: Int): Response<CardsResponse>

}