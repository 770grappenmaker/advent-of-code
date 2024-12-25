package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import java.math.BigInteger

@PuzzleEntry
fun PuzzleSet.day25() = puzzle(day = 25) {
    val modulus = 20201227.toBigInteger()
    val base = 7.toBigInteger()
    fun solve(subject: BigInteger, n: Int) = subject.modPow(n.toBigInteger(), modulus)

    val (a, b) = inputLines.map(String::toBigInteger)
    val loopA = generateSequence(1, Int::inc).first { solve(base, it) == a }
    partOne = solve(b, loopA).toString()
}