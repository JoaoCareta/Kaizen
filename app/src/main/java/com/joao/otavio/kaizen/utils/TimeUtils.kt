package com.joao.otavio.kaizen.utils

object TimeUtils {
    fun getCurrentTimeInSeconds(): Long {
        return System.currentTimeMillis() / 1000
    }
}