package com.grappenmaker.aoc.practice24.year15

import com.grappenmaker.aoc.PuzzleSet
import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
fun PuzzleSet.day04() = puzzle(day = 4) {
    var curr = 0
    fun solve(zeros: Int): Int {
        val md = MessageDigest.getInstance("MD5")
        val test = "0".repeat(zeros)

        while (true) {
            if (md.digest((input + curr).encodeToByteArray()).toHexString().startsWith(test)) return curr
            curr++
        }
    }

    partOne = solve(5)
    partTwo = solve(6)
}