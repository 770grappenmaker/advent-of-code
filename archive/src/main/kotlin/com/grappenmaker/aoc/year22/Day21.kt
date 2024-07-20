package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.random.Random

@PuzzleEntry
fun PuzzleSet.day21() = puzzle {
    val monkeys = inputLines.associate { line ->
        val (name, job) = line.split(": ")
        val sp = job.split(" ")
        name to (job.toDoubleOrNull()?.let { NumberJob(it) } ?: OperatorJob(
            lhs = sp[0],
            rhs = sp[2],
            operator = when (sp[1]) {
                "+" -> Double::plus
                "-" -> Double::minus
                "/" -> Double::div
                "*" -> Double::times
                else -> error("Invalid operator $sp")
            }
        ))
    }

    fun eval(monke: String, humanValue: Long = -1L): Double = when {
        humanValue != -1L && monke == "humn" -> humanValue.toDouble()
        else -> when (val job = monkeys.getValue(monke)) {
            is NumberJob -> job.number
            is OperatorJob -> job.operator(eval(job.lhs, humanValue), eval(job.rhs, humanValue))
        }
    }

    partOne = eval("root").toLong().s()

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
data class NumberJob(val number: Double) : Job
data class OperatorJob(val lhs: String, val rhs: String, val operator: (Double, Double) -> Double) : Job