package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.*

fun PuzzleSet.day16() = puzzle(day = 16) {
    val initialConfiguration = (0..15).toList()
    val insns = input.split(',').map {
        val dropped = it.drop(1)
        val parts = dropped.split('/')

        when (it.first()) {
            's' -> Spin(dropped.toInt())
            'x' -> Exchange(parts[0].toInt(), parts[1].toInt())
            'p' -> Partner(parts[0].single() - 'a', parts[1].single() - 'a')
            else -> error("Impossible")
        }
    }

    fun List<Int>.dance(): List<Int> {
        var curr = this

        insns.forEach {
            when (it) {
                is Spin -> curr = curr.rotate(it.size)
                is Exchange -> {
                    val new = curr.toMutableList()
                    new[it.a] = curr[it.b]
                    new[it.b] = curr[it.a]
                    curr = new
                }

                is Partner -> {
                    val new = curr.toMutableList()
                    val idxA = curr.indexOf(it.a)
                    val idxB = curr.indexOf(it.b)
                    new[idxA] = curr[idxB]
                    new[idxB] = curr[idxA]

                    curr = new
                }
            }
        }

        return curr
    }

    fun List<Int>.format() = joinToString("") { (it.toChar() + 'a'.code).toString() }
    partOne = initialConfiguration.dance().format()
    partTwo = initialConfiguration.patternRepeating(1000000000) { it.dance() }.format()
}

sealed interface DanceInstruction
data class Spin(val size: Int) : DanceInstruction
data class Exchange(val a: Int, val b: Int) : DanceInstruction
data class Partner(val a: Int, val b: Int) : DanceInstruction