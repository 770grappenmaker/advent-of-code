package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day23() = puzzle(day = 23) {
    fun solve(initial: List<Int>, n: Int): DLL<Int> {
        val dll = initial.toDLLS()
        val sorted = dll.sortedBy { it.value }
        var curr = dll.first()
        val max = initial.max()

        repeat(n) {
            val todo = curr.value
            val pickedUp = curr.advance().take(3)
            val pickValues = pickedUp.map { it.value }
            val target = sorted[generateSequence(todo) { (it - 2).mod(max) + 1 }
                .drop(1).first { it !in pickValues } - 1]

            target.insertAfter(pickedUp)
            curr = curr.advance()
        }

        return sorted.first().advance()
    }

    val parsed = input.map(Char::digitToInt)
    val max = parsed.max()
    partOne = solve(parsed, 100).takeUntilFirst().dropLast(1).joinToString("")
    partTwo = solve(parsed + List(1000000 - parsed.size) { max + it + 1 }, 10000000)
        .take(2).map { it.value.toLong() }.product().toString()
}