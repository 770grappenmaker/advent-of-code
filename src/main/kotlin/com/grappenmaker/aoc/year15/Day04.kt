package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import java.security.MessageDigest

val md5 = MessageDigest.getInstance("MD5")

// I really hate this one. There is just magic going on.
// You learn nothing. D:
fun PuzzleSet.day4() = puzzle {
    println("I'm telling you, this is going to take a while (~1 minute)")

    val findSolution = { startingPoint: Int, zeroes: Int ->
        val starting = "0".repeat(zeroes)
        generateSequence(startingPoint) { it + 1 }
            .first { (input + it).md5Hex().startsWith(starting) }
    }

    val partOneEnd = findSolution(0, 5)
    partOne = partOneEnd.s()
    partTwo = findSolution(partOneEnd, 6).s()
}

fun String.md5Hex() = md5.digest(encodeToByteArray()).joinToString("") { "%02x".format(it) }