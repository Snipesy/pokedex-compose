package com.skydoves.pokedex.compose.features.notinteresting

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun NotInterestingOverlay(content:  @Composable () -> Unit
) {
  val avm: NotInterestingViewModel = hiltViewModel()
  val dexState = avm.dexState.collectAsStateWithLifecycle()

  if (avm.check(LocalContext.current) && dexState.value != null) {
    avm.loadDex(LocalContext.current, dexState.value!!)
  }
  avm.fetchAndLoadDex()

  content()
}