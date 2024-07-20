package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.asPair
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day7() = puzzle(day = 7) {
    val pairs = inputLines.map {
        it.split(" ")
            .filter { p -> p.length == 1 }
            .map { p -> p.singleOrNull() ?: error("?") }.asPair()
    }

    val rules = pairs.groupBy { it.second }.mapValues { (_, v) -> v.map { it.first } }
    val possibleTasks = pairs.flatMap { it.toList() }.distinct().sorted()

    fun partOne() {
        val tasks = possibleTasks.toMutableList()
        val done = mutableListOf<Char>()

        while (tasks.isNotEmpty()) {
            val task = tasks.first { done.containsAll(rules[it] ?: emptyList()) }
            done += task
            tasks -= task
        }

        partOne = done.joinToString("")
    }

    fun partTwo() {
        val tasks = possibleTasks.toMutableList()
        val done = mutableListOf<Char>()

        data class WorkerInfo(val task: Char? = null, val doneAt: Int = 0)

        var seconds = 0
        val workers = MutableList(5) { WorkerInfo() }

        while (tasks.isNotEmpty()) {
            workers.forEachIndexed { idx, (t, tDone) ->
                if (seconds < tDone) return@forEachIndexed
                tasks -= t ?: return@forEachIndexed
                done += t ?: return@forEachIndexed
                workers[idx] = WorkerInfo()
            }

            if (tasks.isEmpty()) break

            val currentTasks = workers.mapNotNull { it.task }.toHashSet()
            workers.forEachIndexed { idx, w ->
                if (w.task != null) return@forEachIndexed
                val task = tasks.firstOrNull { it !in currentTasks && done.containsAll(rules[it] ?: emptyList()) }
                    ?: return@forEachIndexed

                currentTasks += task
                workers[idx] = WorkerInfo(task, seconds + (task - 'A') + 61)
            }

            seconds++
        }

        partTwo = seconds.s()
    }

    partOne()
    partTwo()
}