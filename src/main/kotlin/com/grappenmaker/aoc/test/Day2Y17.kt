package com.grappenmaker.aoc.test

import com.grappenmaker.aoc.simplePuzzle
import com.grappenmaker.aoc.year22.asGrid
import com.grappenmaker.aoc.year22.permPairsExclusive
import com.grappenmaker.aoc.year22.rows

fun main() = simplePuzzle(2, 2017) {
    val spreadsheet = inputLines.map { it.split("\t").map(String::toInt) }.asGrid()
    val rows = spreadsheet.rows.map { row -> row.map { spreadsheet[it] }.sorted() }
    partOne = rows.sumOf { it.last() - it.first() }.s()
    partTwo = rows.sumOf { it.permPairsExclusive().first { (a, b) -> a % b == 0 }.let { (a, b) -> a / b } }.s()
}