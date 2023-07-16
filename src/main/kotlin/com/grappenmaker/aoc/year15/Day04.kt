package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import java.security.MessageDigest

val md5: MessageDigest = MessageDigest.getInstance("MD5")

fun PuzzleSet.day4() = puzzle {
    val findSolution = { startingPoint: Int, zeroes: Int ->
        // n zeroes (hex) = first n * 4 bits are zero
        val toShift = zeroes * 4
        generateSequence(startingPoint) { it + 1 }.first {
            (input + it).md5() shr (24 - toShift) == 0
        }
    }

    val partOneEnd = findSolution(0, 5)
    partOne = partOneEnd.s()
    partTwo = findSolution(partOneEnd, 6).s()
}

// Returns first 3 bytes of md5 digest as int
fun String.md5() = md5.digest(encodeToByteArray())
    .take(3).map { it.toInt() and 0xFF }.asReversed()
    .reduceIndexed { idx, acc, curr -> acc or (curr shl idx * 8) }