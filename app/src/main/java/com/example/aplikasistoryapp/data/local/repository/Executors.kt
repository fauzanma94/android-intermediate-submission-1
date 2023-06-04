package com.example.aplikasistoryapp.data.local.repository

import java.util.concurrent.Executor
import java.util.concurrent.Executors


class Executors {
    val diskIO: Executor = Executors.newSingleThreadExecutor()

}