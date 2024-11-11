package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day07() = puzzle(day = 7) {
    val gates = hashMapOf<String, () -> Int>()
    val res = hashMapOf<String, Int>()

    fun String.eval() = res.getOrPut(this) { gates.getValue(this)() }
    fun String.parse() = if (first().isDigit()) toInt() else eval()

    for (line in inputLines) {
        val (b, a) = line.split(" -> ")
        gates[a] = {
            val words = b.split(' ')

            when (words.size) {
                1 -> words.first().parse()
                2 -> words[1].parse().inv()

                else -> {
                    val lhs = words[0].parse()
                    val rhs = words[2].parse()

                    when (words[1]) {
                        "AND" -> lhs and rhs
                        "OR" -> lhs or rhs
                        "LSHIFT" -> lhs shl rhs
                        "RSHIFT" -> lhs ushr rhs
                        else -> error("HELP")
                    }
                }
            } and 0xFFFF
        }
    }

    val p1 = "a".eval()
    partOne = p1
    res.clear()
    gates["b"] = { p1 }
    partTwo = "a".eval()
}