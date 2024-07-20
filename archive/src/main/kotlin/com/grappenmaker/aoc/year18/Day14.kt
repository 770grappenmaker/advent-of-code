package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.toDigits

@PuzzleEntry
fun PuzzleSet.day14() = puzzle(day = 14) {
    val recipes = mutableListOf(3, 7)
    var elfOne = 0
    var elfTwo = 1

    fun update(end: () -> Boolean) {
        while (true) {
            val sum = recipes[elfOne] + recipes[elfTwo]
            if (sum >= 10 && !end()) recipes += sum / 10
            if (end()) return
            recipes += sum % 10

            elfOne = (elfOne + recipes[elfOne] + 1) % recipes.size
            elfTwo = (elfTwo + recipes[elfTwo] + 1) % recipes.size
        }
    }

    val parsed = input.toInt()
    val target = parsed + 10

    update { recipes.size >= target }
    partOne = recipes.takeLast(10).joinToString("")

    val digits = parsed.toDigits()
    partTwo = (recipes.windowed(digits.size).indexOfFirst { it == digits }.takeIf { it != -1 } ?: run {
        update { recipes.takeLast(digits.size) == digits }
        recipes.size - digits.size
    }).s()
}