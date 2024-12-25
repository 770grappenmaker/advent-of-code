package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import kotlin.math.ceil

@PuzzleEntry
fun PuzzleSet.day14() = puzzle(day = 14) {
    data class Ratio(val amount: Long, val of: String)
    data class Recipe(val inputs: List<Ratio>, val output: Ratio)

    operator fun Ratio.times(n: Long) = copy(amount = amount * n)
    operator fun Recipe.times(n: Long) = Recipe(inputs.map { it * n }, output * n)

    fun String.parseRatio(): Ratio {
        val (amount, of) = split(" ")
        return Ratio(amount.toLong(), of)
    }

    val recipes = inputLines.map { l ->
        val (inputsPart, outputPart) = l.split(" => ")
        Recipe(inputsPart.split(", ").map(String::parseRatio), outputPart.parseRatio())
    }

    val byOutput = recipes.associateBy { it.output.of }
    fun recurse(need: Ratio, have: MutableMap<String, Long> = mutableMapOf()): Long {
        if (need.of == "ORE") return need.amount

        val alreadyHave = have.getOrPut(need.of) { 0L }
        val needAdditionally = need.amount - alreadyHave
        have[need.of] = maxOf(0, alreadyHave - need.amount)
        if (needAdditionally <= 0) return 0L

        val recipe = byOutput.getValue(need.of)
        val result = recipe * ceil(needAdditionally.toDouble() / recipe.output.amount).toLong()
        have[need.of] = have.getValue(need.of) + result.output.amount - needAdditionally

        return result.inputs.sumOf { recurse(it, have) }
    }

    partOne = recurse(Ratio(1, "FUEL")).toString()

    fun bs(): Long {
        val target = 1000000000000
        var min = 0L
        var max = 1L shl 43

        while (min + 1 < max) {
            val pivot = (min + max) / 2
            val found = recurse(Ratio(pivot, "FUEL"))
            val s = found - target

            when {
                s < 0L -> min = pivot
                s > 0L -> max = pivot
                else -> return pivot
            }
        }

        return min
    }

    partTwo = bs().toString()
}