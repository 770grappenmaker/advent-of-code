package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.product

fun PuzzleSet.day11() = puzzle {
    val monkeys = input.split("\n\n").map { m ->
        val (items, op, test, t, f) = m.lines().drop(1)
        val (operator, operand) = op.split(" ").takeLast(2)
        val opLiteral = operand.toLongOrNull()

        Monkey(
            items.substringAfter(": ").split(", ").map(String::toLong),
            { old ->
                val opa = opLiteral ?: old
                when (operator) {
                    "*" -> old * opa
                    "+" -> old + opa
                    else -> error("Should not happen")
                }
            },
            test.split(" ").last().toLong(),
            t.split(" ").last().toInt(),
            f.split(" ").last().toInt()
        )
    }

    fun solve(partTwo: Boolean): String {
        val currMonkeys = monkeys.map { it.items.toMutableList() }
        val throws = currMonkeys.map { 0 }.toMutableList()
        val lcm = monkeys.map { it.test }.product()

        repeat(if (!partTwo) 20 else 10000) {
            monkeys.forEachIndexed { idx, m ->
                val currItems = currMonkeys[idx]
                currItems.forEach {
                    val newLevel = m.operation(it)
                    val acLevel = if (partTwo) newLevel % lcm else newLevel / 3
                    when {
                        acLevel % m.test == 0L -> currMonkeys[m.ifTrue] += acLevel
                        else -> currMonkeys[m.ifFalse] += acLevel
                    }
                }

                throws[idx] += currItems.size
                currItems.clear()
            }
        }

        return throws.sortedDescending().take(2).map { it.toLong() }.product().s()
    }

    partOne = solve(false)
    partTwo = solve(true)
}

data class Monkey(
    val items: List<Long>,
    val operation: (Long) -> Long,
    val test: Long,
    val ifTrue: Int,
    val ifFalse: Int
)