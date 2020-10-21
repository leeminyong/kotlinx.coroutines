/*
 * Copyright 2016-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("unused")

package kotlinx.coroutines.linearizability

import kotlinx.coroutines.*
import kotlinx.coroutines.internal.SegmentBasedQueue
import org.jetbrains.kotlinx.lincheck.annotations.*
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.paramgen.*

@Param(name = "value", gen = IntGen::class, conf = "1:5")
class SegmentQueueLCStressTest : AbstractLincheckTest() {
    private val q = SegmentBasedQueue<Int>()

    @Operation
    fun enqueue(@Param(name = "value") x: Int): Boolean {
        return q.enqueue(x) !== null
    }

    @Operation
    fun dequeue(): Int? = q.dequeue()

    @Operation
    fun close() {
        q.close()
    }

    override fun extractState(): Any {
        val elements = ArrayList<Int>()
        while (true) {
            val x = q.dequeue() ?: break
            elements.add(x)
        }
        val closed = q.enqueue(0) === null
        return elements to closed
    }
}