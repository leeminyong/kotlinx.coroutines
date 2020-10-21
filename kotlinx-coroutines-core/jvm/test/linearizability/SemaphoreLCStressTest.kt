/*
 * Copyright 2016-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")
package kotlinx.coroutines.linearizability

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*
import org.jetbrains.kotlinx.lincheck.*
import org.jetbrains.kotlinx.lincheck.annotations.Operation

abstract class SemaphoreLCStressTestBase(permits: Int) : AbstractLincheckTest() {
    private val semaphore = Semaphore(permits)

    @Operation
    fun tryAcquire() = semaphore.tryAcquire()

    @Operation
    suspend fun acquire() = semaphore.acquire()

    @Operation(handleExceptionsAsResult = [IllegalStateException::class])
    fun release() = semaphore.release()

    override fun <O : Options<O, *>> O.customize(isStressTest: Boolean): O =
        actorsBefore(0)

    override fun extractState() = semaphore.availablePermits
}

class Semaphore1LCStressTest : SemaphoreLCStressTestBase(1)
class Semaphore2LCStressTest : SemaphoreLCStressTestBase(2)