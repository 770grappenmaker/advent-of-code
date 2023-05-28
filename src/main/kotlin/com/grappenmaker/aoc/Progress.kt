package com.grappenmaker.aoc

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    fun Double.percent() = "%.0f%%".format(this * 100.0)

    val totalCount = years.sumOf { it.puzzles.size }
    val fullyDone = totalCount == years.size * 25

    // Horrible formatting down here
    println(
"""
## Progress (as of ${DateTimeFormatter.ISO_DATE.format(LocalDate.now())})
${
    years.joinToString(System.lineSeparator()) {
        "- [${if (it.puzzles.size == 25) "x" else " "}] ${it.year}: ${it.puzzles.size}/25 (${(it.puzzles.size / 25.0).percent()})"
    }
}
- [${if (fullyDone) "x" else " "}] Total: $totalCount/${years.size * 25} (${(totalCount / years.size / 25.0).percent()})
"""
    )
}