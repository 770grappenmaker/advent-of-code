package com.grappenmaker.aoc2021

fun Context.solveDay2() {
    // Part one
    val instructions = inputLines.map {
        val split = it.split(' ')
        split[0] to split[1].toInt()
    }

    val sub1 = Submarine()
    instructions.forEach { (insn, value) ->
        when (insn) {
            "forward" -> sub1.horizontal += value
            "down" -> sub1.depth += value
            "up" -> sub1.depth -= value
        }
    }

    println("Part one: ${sub1.horizontal * sub1.depth}")

    // Part two
    val sub2 = Submarine()
    instructions.forEach { (insn, value) ->
        when (insn) {
            "down" -> sub2.aim += value
            "up" -> sub2.aim -= value
            "forward" -> {
                sub2.horizontal += value
                sub2.depth += sub2.aim * value
            }
        }
    }

    println("Part two: ${sub2.horizontal * sub2.depth}")
}

// Aim is for part two, unused in part one
data class Submarine(var horizontal: Int = 0, var depth: Int = 0, var aim: Int = 0)