package com.stellantis.espw.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/7057103397700170/search/{name}")
    suspend fun getSuperheroes(@Path("name") query: String): Response<SuperHeroDataResponse>

    @GET("api/7057103397700170/{id}")
    suspend fun getSuperheroDetail(@Path("id") id: String): Response<SuperHeroDetailResponse>
}