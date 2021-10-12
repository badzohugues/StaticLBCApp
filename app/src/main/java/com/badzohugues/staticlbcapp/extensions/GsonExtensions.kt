package com.badzohugues.staticlbcapp.extensions

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

inline fun <reified T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)

inline fun <reified T>Gson.fromJson(fileName: String) =
    getJsonDataFromAsset(fileName)?.let { jsonString ->
        Gson.fromJson<T>(json, object : TypeToken<T>() {}.type)
    }

fun Context.getJsonDataFromAsset(fileName: String): String? {
    return try {
        assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        null
    }
}
