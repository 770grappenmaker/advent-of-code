package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import kotlin.math.abs

fun PuzzleSet.day7() = puzzle {
    val normalFiles = mutableMapOf<String, Int>()
    val dirs = buildMap<String, List<String>> {
        val stack = queueOf<String>()
        val current = { stack.joinToString("/") }

        inputLines.forEach { line ->
            val split = line.split(" ")
            if (line.startsWith("$")) {
                if (split[1] == "cd") when {
                    split[2] == ".." -> stack.removeLast()
                    split[2] == "/" -> { /* ignore */ }
                    else -> stack.addLast(split[2])
                }
            } else {
                val curr = current()
                val new = (curr + "/" + split[1]).removePrefix("/")
                this[curr] = getOrDefault(curr, listOf()) + new

                if (split[0] != "dir") normalFiles[new] = split[0].toInt()
            }
        }
    }

    fun getSize(dir: String): Int {
        val contents = dirs[dir] ?: listOf()
        return contents.sumOf { v -> normalFiles[v] ?: getSize(v) }
    }

    val mapped = dirs.mapValues { (k) -> getSize(k) }
    partOne = mapped.filter { (_, v) -> v <= 100000 }.values.sum().s()

    val shouldFree = mapped[""]!! - (70000000 - 30000000)
    val toDelete = mapped.filter { (_, u) -> u > shouldFree }.minBy { (_, u) -> abs(shouldFree - u) }.key
    partTwo = mapped[toDelete].s()
}