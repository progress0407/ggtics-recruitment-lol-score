package com.your.lol.support

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.springframework.core.io.ClassPathResource
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

object JsonFileConverter {

    private val gson: Gson = Gson()

    fun <T> fromJsonFile(filePath: String, type: Class<T>): T {
        val bufferedReader = readJsonFile(filePath)
        return gson.fromJson(bufferedReader, type)
    }

    fun <T> fromJsonFile(filePath: String, typeToken: TypeToken<T>): T {
        val bufferedReader = readJsonFile(filePath)
        return gson.fromJson(bufferedReader, typeToken)
    }

    private fun readJsonFile(filePath: String): BufferedReader {
        try {
            val jsonFile: File = ClassPathResource(filePath).file
            return BufferedReader(FileReader(jsonFile))
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }
}