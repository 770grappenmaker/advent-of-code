package com.grappenmaker.aoc

import java.io.File
import java.net.HttpURLConnection
import java.net.URI
import kotlin.io.path.createDirectories
import kotlin.io.path.outputStream

fun main(args: Array<String>) {
    val token = File(".TOKEN").readText().trim()
    val now = now()
    val day = (args.firstOrNull()?.toIntOrNull() ?: now.dayOfMonth).coerceIn(1..25)
    val year = args.getOrNull(1)?.toIntOrNull() ?: now.year
    println("Fetching day $day year $year")

    val file = inputsDir(year).also { it.createDirectories() }.resolve(inputName(day))
    println("Writing to $file")

    with(URI("https://adventofcode.com/$year/day/$day/input").toURL().openConnection() as HttpURLConnection) {
        requestMethod = "GET"
        setRequestProperty("Cookie", "session=$token")
        setRequestProperty("User-Agent", "HttpURLConnection gh:770grappenmaker")
        inputStream.use { it.copyTo(file.outputStream()) }
    }
}