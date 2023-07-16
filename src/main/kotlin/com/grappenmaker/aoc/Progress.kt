package com.grappenmaker.aoc

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun main() {
    fun Double.percent() = "%.1f%%".format(Locale.US, this * 100.0)

    val totalCount = years.sumOf { it.puzzles.size }
    val fullyDone = totalCount == years.size * 25

    // Horrible formatting down here
    println(
"""
## Progress (as of ${DateTimeFormatter.ISO_DATE.format(LocalDate.now())})
${
    years.sortedByDescending { it.year }.joinToString(System.lineSeparator()) {
        val c = it.puzzles.size
        "- [${if (c == 25) "x" else " "}] ${it.year}: $c/25 (${c * 2}🌟) (${(c / 25.0).percent()})"
    }
}
- [${if (fullyDone) "x" else " "}] Total: $totalCount/${years.size * 25} (${(totalCount / years.size.toDouble() / 25.0).percent()})
"""
    )
}