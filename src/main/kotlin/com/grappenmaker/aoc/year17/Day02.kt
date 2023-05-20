package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.*

fun PuzzleSet.day2() = puzzle {
    val spreadsheet = inputLines.map { it.split("\t").map(String::toInt) }.asGrid()
    val rows = spreadsheet.rows.map { row -> row.map { spreadsheet[it] }.sorted() }
    partOne = rows.sumOf { it.last() - it.first() }.s()
    partTwo = rows.sumOf { it.permPairsExclusive().first { (a, b) -> a % b == 0 }.let { (a, b) -> a / b } }.s()
}