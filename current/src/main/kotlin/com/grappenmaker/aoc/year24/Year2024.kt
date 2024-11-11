@file:Suppress("PackageDirectoryMismatch")

package com.grappenmaker.aoc.years

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year16.day02
import com.grappenmaker.aoc.year24.day01
import com.grappenmaker.aoc.year24.dayxx

// For speed, handwritten
val year2024 = PuzzleSet(2024).apply { dayxx() }
val year2016 = PuzzleSet(2016).apply { day02() }

val years = listOf(year2024, year2015, year2016)
val puzzles = years.flatMap { it.puzzles }