/*
 * Copyright 2016-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package kotlinx.coroutines

import platform.CoreFoundation.*
import kotlin.native.concurrent.*
import kotlin.native.internal.test.*
import kotlin.system.*

// This is a separate entry point for tests in background
fun mainBackground(args: Array<String>) {
    initMainThread() // not really needed, since runtime is initialized by the main thread here anyway
    val worker = Worker.start(name = "main-background")
    worker.execute(TransferMode.SAFE, { args.freeze() }) {
        val result = testLauncherEntryPoint(it)
        exitProcess(result)
    }
    CFRunLoopRun()
    error("CFRunLoopRun should never return")
}

// This is a separate entry point for tests with leak checker
fun mainNoExit(args: Array<String>) {
    testLauncherEntryPoint(args)
    mainThread.shutdown()
}