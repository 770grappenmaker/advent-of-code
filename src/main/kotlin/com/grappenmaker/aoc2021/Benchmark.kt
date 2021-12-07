@file:JvmName("Benchmark")

package com.grappenmaker.aoc2021

import java.io.File
import java.io.OutputStream
import java.io.PrintStream
import kotlin.system.measureNanoTime

fun main(args: Array<String>) {
    // Inform user
    if (args.isEmpty()) {
        println("Usage: Benchmark <day> [amount] [inputFile]")
        return
    }

    // Get puzzle index
    val index = args.first().runCatching { toInt() }.getOrNull() ?: run {
        println("Invalid day number/index \"${args.first()}\"")
        return
    }

    // Get benchmark samples
    val amount = args.getOrNull(1)?.runCatching { toInt() }?.onFailure {
        println("Invalid sample amount \"${args[1]}\"")
        return
    }?.getOrNull() ?: 100

    println("Running day $index $amount times.")

    // Disable logging and re-enable later
    val logger = System.out
    System.setOut(PrintStream(OutputStream.nullOutputStream()))

    // Create context
    val context = Context(index, args.getOrNull(2)?.let { File(it) })

    // Run it
    val runtimes = mutableListOf<Long>()
    for (i in 1..amount) {
        runtimes.add(measureNanoTime { context.run() } / 1000)
    }

    // Enable logging again
    System.setOut(logger)

    val measurement = "microseconds"

    val sum = runtimes.sum()
    println("Took $sum $measurement to run $amount times")

    val sorted = runtimes.sorted()
    println("Min: ${sorted.first()} $measurement")
    println("Max: ${sorted.last()} $measurement")
    println("Avg: ${sum.toDouble() / runtimes.size} $measurement")

    val mid = runtimes.size / 2
    val median = runtimes[mid] + if (runtimes.size % 2 != 0) runtimes[mid - 1] else 0
    println("Median: $median $measurement")

    val mode = sorted.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key
    println("Mode: $mode $measurement")
}