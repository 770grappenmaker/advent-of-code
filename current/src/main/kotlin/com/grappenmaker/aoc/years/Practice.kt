package com.grappenmaker.aoc.years

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.practice25.day01
import com.grappenmaker.aoc.practice25.day02
import com.grappenmaker.aoc.practice25.day03
import com.grappenmaker.aoc.practice25.day04
import com.grappenmaker.aoc.practice25.day05
import com.grappenmaker.aoc.practice25.day06

val years = listOf(PuzzleSet(2017).apply {
    day01()
    day02()
    day03()
    day04()
    day05()
    day06()
})

val puzzles = years.flatMap { it.puzzles }