package com.grappenmaker.aoc

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun main() {
    fun Double.percent() = "%.1f%%".format(Locale.US, this * 100.0)

    val total = years.sumOf { it.puzzles.size }
    val maxPuzzles = years.size * 25

    // Horrible formatting down here
    println(
"""
## Progress (as of ${DateTimeFormatter.ISO_DATE.format(LocalDate.now())})
${
    years.sortedByDescending { it.year }.joinToString(System.lineSeparator()) {
        val c = it.puzzles.size
        "- [${if (c == 25) "x" else " "}] ${it.year}: $c/25 (${c * 2} 🌟) (${(c / 25.0).percent()})"
    }
}
- [${if (total == maxPuzzles) "x" else " "}] Total: $total/$maxPuzzles (${total * 2} 🌟) (${(total / maxPuzzles.toDouble()).percent()})
"""
    )

    println("Next to work on: ${years.minBy { it.puzzles.size }.year}")
}