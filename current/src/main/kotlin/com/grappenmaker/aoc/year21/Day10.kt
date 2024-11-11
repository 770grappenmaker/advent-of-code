package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*

fun PuzzleSet.day10() = puzzle(day = 10) {
    val oc = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    val score = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

    var ans = 0
    val scores = mutableListOf<Long>()

    a@ for (line in inputLines) {
        val stack = ArrayDeque<Char>()

        for (char in line) {
            if (char in oc) {
                stack.addLast(char)
                continue
            }

            if (oc.getValue(stack.removeLast()) != char) {
                ans += score.getValue(char)
                continue@a
            }
        }

        var t = 0L
        for (c in stack.asReversed()) {
            t *= 5
            t += "([{<".indexOf(c) + 1
        }

        scores += t
    }

    scores.sort()
    partOne = ans
    partTwo = scores[scores.size / 2]
}