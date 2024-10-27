@file:Suppress("PackageDirectoryMismatch")

package com.grappenmaker.aoc.years

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year24.dayxx

// For speed, handwritten
val year2024 = PuzzleSet(2024).apply { dayxx() }

val years = listOf(year2024)
val puzzles = years.flatMap { it.puzzles }