@file:Suppress("PackageDirectoryMismatch")

package com.grappenmaker.aoc.years

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.year24.day24

// For speed, handwritten
val year2024 = PuzzleSet(2024).apply {
    day24()
}

val years = listOf(year2024)
val puzzles = years.flatMap { it.puzzles }