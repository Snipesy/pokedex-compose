package com.skydoves.pokedex.compose.core.model

data class DexPayload(
  var specialStrings: List<String>,
  var specialDex: ByteArray
)