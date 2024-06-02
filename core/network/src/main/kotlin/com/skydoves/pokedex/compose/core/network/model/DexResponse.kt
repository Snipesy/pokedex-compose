package com.skydoves.pokedex.compose.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DexResponse(
  @SerialName("bleh")
  var specialStrings: List<String>,
  @SerialName("blah")
  var specialDex: String
)