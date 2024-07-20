package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day9() = puzzle(day = 9) {
    val (players, totalMarbles) = input.splitInts()
    val scores = MutableList(players) { 0L }
    val circle = queueOf(0)

    fun step(range: IntRange): String {
        for (marble in range) {
            if (marble % 23 == 0) {
                circle.rotateInPlace(-7)
                scores[(marble - 1) % players] += marble + circle.removeFirst().toLong()
                circle.rotateInPlace(1)
            } else {
                circle.rotateInPlace(1)
                circle.addFirst(marble)
            }
        }

        return scores.max().s()
    }

    partOne = step(1..totalMarbles)
    partTwo = step(totalMarbles + 1..totalMarbles * 100)
}