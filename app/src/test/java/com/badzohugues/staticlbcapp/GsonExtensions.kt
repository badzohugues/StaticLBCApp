package com.badzohugues.staticlbcapp

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

inline fun <reified T> Gson.fromJson(fileName: String): T =
    fromJson(
        getJsonDataFromAsset(
            fileName,
            ClassLoader.getSystemClassLoader()
        ),
        object : TypeToken<T>() {}.type
    )

fun getJsonDataFromAsset(fileName: String, classLoader: ClassLoader): String {
    return File(classLoader.getResource(fileName).path).readText(Charsets.UTF_8)
}
