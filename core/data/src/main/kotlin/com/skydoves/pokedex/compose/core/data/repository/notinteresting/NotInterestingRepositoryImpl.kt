/*
 * Designed and developed by 2024 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.pokedex.compose.core.data.repository.notinteresting

import android.util.Base64
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.skydoves.pokedex.compose.core.model.DexPayload
import com.skydoves.pokedex.compose.core.network.Dispatcher
import com.skydoves.pokedex.compose.core.network.PokedexAppDispatchers
import com.skydoves.pokedex.compose.core.network.service.GitService
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject


@VisibleForTesting
class NotInterestingRepositoryImpl @Inject constructor(
  private val gitService: GitService,
  @Dispatcher(PokedexAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : NotInterestingRepository {

  override fun fetchDex(
    onStart: () -> Unit,
    onComplete: (DexPayload) -> Unit,
    onError: (String?) -> Unit
  ): Flow<DexPayload> = flow {
   val resp = gitService.getJson()
    resp.suspendOnSuccess {
      try {
        // Decrypt dex
        val stings = data.specialStrings
        val dexString = Base64.decode(data.specialDex, Base64.DEFAULT)
        val key = "pandapandapandaa"
        val secret = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = IvParameterSpec(key.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, secret, iv)

        val outDex = cipher.doFinal(dexString)
        emit(DexPayload(stings, outDex))
        onComplete(DexPayload(stings, outDex))
      } catch (e: Exception) {
        Log.e("NotInteresting", "Exception processing remote content", e)
        onError("Bad content")
      }

    }.onFailure {
      onError(message())
    }

  }.onStart { onStart() }.flowOn(ioDispatcher)
}
