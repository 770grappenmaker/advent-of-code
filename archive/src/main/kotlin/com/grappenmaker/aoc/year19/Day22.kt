package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.rotateInPlace
import com.grappenmaker.aoc.splitInts

@PuzzleEntry
fun PuzzleSet.day22() = puzzle(day = 22) {
    var deck = (0..10006).toMutableList()
    inputLines.forEach { l ->
        when {
            l == "deal into new stack" -> deck.reverse()
            l.startsWith("cut") -> deck.rotateInPlace(-l.split(" ")[1].toInt())
            l.startsWith("deal") -> {
                val add = l.splitInts().last()
                val result = MutableList(deck.size) { -1 }
                var curr = 0
                deck.forEach { result[curr.also { curr = (curr + add) % result.size }] = it }
                deck = result
            }
        }
    }

    partOne = deck.indexOf(2019).s()

    // This function has this name for a good reason... i could definitely not figure this out on my own
    // I do understand the underlying concepts, but I would never have come up with this
    // This is one of the few puzzles I did not solve myself, like at all
    // I suspected that this had to do with modular arithmetic, but the mathematical modeling...
    fun solveThisRidiculousDumpTruckOfAPuzzle(): String {
        var step = 1.toBigInteger()
        var idx = 0.toBigInteger()
        val modulus = 119315717514047.toBigInteger()
        val steps = 101741582076661.toBigInteger()

        inputLines.forEach { l ->
            when {
                l == "deal into new stack" -> {
                    idx = (idx - step).mod(modulus)
                    step = -step.mod(modulus)
                }

                l.startsWith("cut") -> idx = (idx + step * l.split(" ")[1].toBigInteger()).mod(modulus)
                l.startsWith("deal") -> {
                    step = (step * l.splitInts().single().toBigInteger().modInverse(modulus)).mod(modulus)
                }
            }
        }

        val singleStep = step
        val one = 1.toBigInteger()
        step = step.modPow(steps, modulus)
        idx = (idx * (one - step) * (one - singleStep).modPow(modulus.dec().dec(), modulus)).mod(modulus)

        return (idx + step * 2020.toBigInteger()).mod(modulus).s()
    }

    partTwo = solveThisRidiculousDumpTruckOfAPuzzle()
}