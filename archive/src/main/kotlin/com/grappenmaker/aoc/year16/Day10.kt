package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.product
import kotlin.math.max
import kotlin.math.min

private const val outputMask = 0xff0000

@PuzzleEntry
fun PuzzleSet.day10() = puzzle {
    val insns = inputLines.map { l ->
        val parts = l.split(" ")
        val nums = parts.mapNotNull { it.toIntOrNull() }

        when {
            l.startsWith("bot") -> GiveInstruction(
                nums[0],
                nums[1] or if (parts[5] == "output") outputMask else 0,
                nums[2] or if (parts[10] == "output") outputMask else 0,
            )

            l.startsWith("value") -> ValueInstruction(nums[1], nums[0])
            else -> error("Unexpected insn $l")
        }
    }

    val bots = insns
        .filterIsInstance<ValueInstruction>().groupBy { it.index }
        .mapValues { (_, values) -> values.map { it.value }.toMutableList() }.toMutableMap()
        .withDefault { mutableListOf() }

    fun MutableMap<Int, MutableList<Int>>.bot(index: Int) = getOrPut(index) { mutableListOf() }

    val rules = insns.filterIsInstance<GiveInstruction>()
    while (true) {
        val toApply = rules.filter { (idx) -> bots.bot(idx).size == 2 }
        if (toApply.isEmpty()) break

        toApply.forEach { (index, lowTo, highTo) ->
            val current = bots.bot(index)
            val (a, b) = current
            val low = min(a, b)
            val high = max(a, b)

            if (low == 17 && high == 61) partOne = index.s()

            bots.bot(lowTo).also { assert(it.size < 2) } += low
            bots.bot(highTo).also { assert(it.size < 2) } += high
            current.clear()
        }
    }

    partTwo = (0..2).map { bots.bot(it or outputMask).single() }.product().s()
}

sealed interface BotInstruction
data class GiveInstruction(val index: Int, val lowTo: Int, val highTo: Int) : BotInstruction
data class ValueInstruction(val index: Int, val value: Int) : BotInstruction