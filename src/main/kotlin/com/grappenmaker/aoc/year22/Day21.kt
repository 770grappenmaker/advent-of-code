package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import kotlin.random.Random

fun PuzzleSet.day21() = puzzle {
    val monkeys = inputLines.associate { line ->
        val (name, job) = line.split(": ")
        val sp = job.split(" ")
        name to (job.toLongOrNull()?.let { NumberJob(it) } ?: OperatorJob(
            lhs = sp[0],
            rhs = sp[2],
            operator = when (sp[1]) {
                "+" -> Long::plus
                "-" -> Long::minus
                "/" -> Long::div
                "*" -> Long::times
                else -> error("Invalid operator $sp")
            }
        ))
    }

    fun eval(monke: String, humanValue: Long = -1): Long = when {
        humanValue != -1L && monke == "humn" -> humanValue
        else -> when (val job = monkeys.getValue(monke)) {
            is NumberJob -> job.number
            is OperatorJob -> job.operator(eval(job.lhs, humanValue), eval(job.rhs, humanValue))
        }
    }

    partOne = eval("root").s()

    fun bs(): Long {
        val rootJob = monkeys.getValue("root") as OperatorJob
        val changes = eval(rootJob.rhs, Random.nextLong()) != eval(rootJob.rhs)
        val target = eval(if (changes) rootJob.lhs else rootJob.rhs)
        val variable = if (changes) rootJob.rhs else rootJob.lhs

        var min = 0L
        var max = 1L shl 43

        while (true) {
            val pivot = (min + max) / 2
            val found = eval(variable, pivot)
            val s = target - found

            when {
                s < 0L -> min = pivot
                s > 0L -> max = pivot
                else -> return pivot
            }
        }
    }

    partTwo = bs().s()
}

sealed interface Job
data class NumberJob(val number: Long) : Job
data class OperatorJob(val lhs: String, val rhs: String, val operator: (Long, Long) -> Long) : Job