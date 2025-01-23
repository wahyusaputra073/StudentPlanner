package com.wahyusembiring.habit.util

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination

/**
 * Simple name of the class route without package name, param or query
 * */

val NavBackStackEntry?.routeSimpleClassName: String?
    get() = this?.destination?.route?.substringBefore("?")?.substringBefore("/")  // Ambil bagian sebelum "?" dan "/"
        ?.substringAfterLast(".")  // Ambil nama kelas setelah titik terakhir (misalnya "HomeScreen" dari "com.example.HomeScreen")
