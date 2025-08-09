package com.grappenmaker.aoc.practice25.everybodycodes

import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.sqrt

fun ECSolveContext.day08() {
    val partOneNum = partOneInput.input.toInt()
    partOne = ceil(sqrt(partOneNum.toDouble())).toInt()
        .let { n -> (n * n - partOneNum) * (2 * n - 1) }

    fun ECInput.solve(mod: BigInteger, avail: BigInteger, part: Boolean) {
        val num = input.toBigInteger()

        var total = 0.toBigInteger()
        var curr = 1.toBigInteger()
        var currWidth = 1.toBigInteger()
        var lastLayer = emptyList<BigInteger>()

        while (total < avail) {
            total += currWidth * curr
            lastLayer = List(currWidth.toInt() / 2 + 1) { idx ->
                if (idx >= 1 && idx <= lastLayer.size) lastLayer[idx - 1] + curr else curr
            }

            curr = (curr * num) % mod
            if (!part) curr += mod

            currWidth += 2.toBigInteger()
        }

        val actualWidth = currWidth - 2.toBigInteger()
        if (part) {
            partTwo = (total - avail) * actualWidth
        } else {
            val factor = (num * actualWidth) % mod
            val removed = lastLayer.subList(1, lastLayer.lastIndex)
                .sumOf { (factor * it) % mod } * 2.toBigInteger() + (lastLayer.last() * factor) % mod

            val built = total - removed
            partThree = built - avail
        }
    }

    partTwoInput.solve(1111.toBigInteger(), 20240000.toBigInteger(), true)
    partThreeInput.solve(10.toBigInteger(), 202400000.toBigInteger(), false)
}