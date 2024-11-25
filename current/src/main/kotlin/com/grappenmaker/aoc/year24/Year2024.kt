@file:Suppress("PackageDirectoryMismatch")

package com.grappenmaker.aoc.years

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year15.year2015
import com.grappenmaker.aoc.year16.year2016
import com.grappenmaker.aoc.year20.year2020
import com.grappenmaker.aoc.year21.year2021
import com.grappenmaker.aoc.year22.year2022
import com.grappenmaker.aoc.year23.year2023
import com.grappenmaker.aoc.year24.dayxx

// For speed, handwritten
val year2024 = PuzzleSet(2024).apply { dayxx() }

val years = listOf(
    year2015,
    year2016,
    year2020,
    year2021,
    year2022,
    year2023,
    year2024,
)

val puzzles = years.flatMap { it.puzzles }