package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.queueOf

fun Char.eic(other: Char) = equals(other, ignoreCase = true)

@PuzzleEntry
fun PuzzleSet.day5() = puzzle(5) {
    fun update(ignoring: Char? = null): Int {
        val stack = queueOf<Char>()
        input.forEach { c ->
            val last = stack.lastOrNull()
            when {
                ignoring != null && c.eic(ignoring) -> {}
                last != null && last.eic(c) && c.isUpperCase() xor last.isUpperCase() -> stack.removeLast()
                else -> stack.addLast(c)
            }
        }

        return stack.size
    }

    partOne = update().s()
    partTwo = ('a'..'z').minOf { update(it) }.s()
}