package com.skydoves.pokedex.compose.core.network.service

import com.skydoves.pokedex.compose.core.network.model.DexResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface GitService {
  @GET("not_interesting.json")
  suspend fun getJson(): ApiResponse<DexResponse>

}