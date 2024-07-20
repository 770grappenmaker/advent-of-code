package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day14() = puzzle(day = 14) {
    fun List<Int>.toBooleans() = flatMap { (7 downTo 0).map { i -> it shr i and 1 == 1 } }
    val hashes = (0..127).map { totalKnotHash("$input-$it".map(Char::code)).dense().toBooleans() }

    with (grid(128, 128) { (x, y) -> hashes[y][x] }) {
        val enabled = filterTrue().toSet()
        partOne = enabled.size.s()

        val seen = hashSetOf<Point>()
        var count = 0

        for (p in enabled) {
            if (!seen.add(p)) continue
            floodFill(p, condition = { it in enabled && seen.add(it) })
            count++
        }

        partTwo = count.s()
    }
}