package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import java.util.PriorityQueue

@PuzzleEntry
fun PuzzleSet.day16() = puzzle(day = 16) {
    val (rulesPart, yourPart, othersPart) = input.doubleLines().map(String::lines)

    data class Rule(val name: String, val firstRange: IntRange, val secondRange: IntRange)
    fun Rule.isValid(value: Int) = value in firstRange || value in secondRange

    val rules = rulesPart.map { l ->
        val (name, rest) = l.split(": ")
        val (r1, r2) = rest.split(" or ").map { r ->
            val (a, b) = r.split("-").map(String::toInt)
            a..b
        }

        Rule(name, r1, r2)
    }

    val otherTickets = othersPart.drop(1).map { it.split(",").map(String::toInt) }
    partOne = otherTickets.sumOf { ticket -> ticket.sumOf { v -> if (rules.none { it.isValid(v) }) v else 0 } }.toString()

    val us = yourPart[1].split(",").map(String::toInt)
    val valid = otherTickets.filter { t -> t.all { v -> rules.any { it.isValid(v) } } }.plusElement(us)

    fun solve(): List<String> {
        val initial = rules to emptyList<String>()
        val seen = hashSetOf(initial)
        val queue = PriorityQueue(compareBy<Pair<List<Rule>, List<String>>> { it.first.size })
            .also { it.offer(initial) }

        queue.drain { (left, found) ->
            if (left.isEmpty()) return found
            else {
                val currIdx = found.size
                left.forEach { next ->
                    if (valid.all { t -> next.isValid(t[currIdx]) }) {
                        val n = left - next to found + next.name
                        if (seen.add(n)) queue.offer(n)
                    }
                }
            }
        }

        error("Impossible")
    }

    val found = solve()
    partTwo = found.withIndex().filter { "departure" in it.value }.map { us[it.index].toLong() }.product().toString()
}