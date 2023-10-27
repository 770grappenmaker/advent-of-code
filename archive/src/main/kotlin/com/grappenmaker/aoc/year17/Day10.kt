package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.*

fun PuzzleSet.day10() = puzzle(day = 10) {
    val (a, b) = knotHash(input.split(",").map(String::toInt)).data
    partOne = (a * b).s()
    partTwo = totalKnotHash(input.deepen().map { it.code }).display()
}

private val primeLengths = listOf(17, 31, 73, 47, 23)

fun totalKnotHash(data: List<Int>): KnotHashResult {
    val fullData = data + primeLengths
    return generateSequence(KnotHashResult()) { knotHash(fullData, it.data, it.curr, it.skip) }.nth(64)
}

data class KnotHashResult(val data: List<Int> = (0..0xFF).toList(), val curr: Int = 0, val skip: Int = 0)
fun KnotHashResult.dense() = data.windowed(16, 16) { it.reduce { acc, curr -> acc xor curr } }
fun KnotHashResult.display() = dense().joinToString("") { "%02x".format(it) }

fun knotHash(
    lengths: List<Int>,
    toHash: List<Int> = (0..0xFF).toList(),
    initialCurr: Int = 0,
    initialSkip: Int = 0
): KnotHashResult {
    val nums = toHash.toMutableList()
    var curr = initialCurr
    var skip = initialSkip
    var totalRot = 0

    for (length in lengths) {
        if (curr + length >= nums.size) {
            val rot = -(nums.size - curr - length)
            totalRot += rot
            nums.rotateInPlace(-rot)
            curr = (curr - rot).mod(nums.size)
        }

        nums.addAll(curr, nums.removeNAt(length, curr).asReversed())
        curr = (curr + length + skip) % nums.size
        skip++
    }

    nums.rotateInPlace(totalRot)
    return KnotHashResult(nums, (curr + totalRot) % nums.size, skip)
}