package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import java.security.MessageDigest

val md5: MessageDigest = MessageDigest.getInstance("MD5")

// Why md5 again??
@PuzzleEntry
fun PuzzleSet.day5() = puzzle {
    fun seq() = generateSequence(0) { it + 1 }
        .map { i ->
            val (a, b, c, d) = md5.digest((input + i.toString()).encodeToByteArray())
                .take(4).map { it.toInt() and 0xFF }

            // need 7 hex digits -> 28 bits -> 3 bytes + 1 nibble
            (a shl 20) or (b shl 12) or (c shl 4) or (d shr 4 and 0xF)
        }.filter { it shr 8 == 0 } // check if first 20 bits are 0

    val length = 8
    partOne = seq().map { (it shr 4 and 0xF).toString(16) }.take(length).joinToString("")
    partTwo = buildMap {
        seq().onEach {
            val pos = it shr 4 and 0xF
            if (pos !in this && pos in 0..<length) put(pos, (it and 0xF).toString(16).first())
        }.takeWhile { size != length }.toList()
    }.toList().sortedBy { it.first }.joinToString("") { it.second.toString() }
}