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

import androidx.annotation.WorkerThread
import com.skydoves.pokedex.compose.core.model.DexPayload
import kotlinx.coroutines.flow.Flow

interface NotInterestingRepository {

  @WorkerThread
  fun fetchDex(
    onStart: () -> Unit,
    onComplete: (DexPayload) -> Unit,
    onError: (String?) -> Unit,
  ): Flow<DexPayload>
}