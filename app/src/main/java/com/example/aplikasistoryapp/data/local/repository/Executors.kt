package com.example.aplikasistoryapp.data.local.repository

import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.logging.Handler


class Executors {
    val diskIO: Executor = Executors.newSingleThreadExecutor()

    private class MainThreadExecutor: Executor{
        private val mainThreadExecutor = android.os.Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadExecutor.post(command)
        }
    }
}