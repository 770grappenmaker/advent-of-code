package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year22.Direction.*

fun PuzzleSet.day23() = puzzle {
    val initial = inputLines.asGrid { it == '#' }.filterTrue()

    data class Rule(val dir: Direction, val emptyDirs: List<Point>)
    fun Rule.offset(p: Point) = emptyDirs.map { p + it }

    val rules = listOf(
        Rule(UP, listOf(UP.toPoint(), UP + LEFT, UP + RIGHT)),
        Rule(DOWN, listOf(DOWN.toPoint(), DOWN + LEFT, DOWN + RIGHT)),
        Rule(LEFT, listOf(LEFT.toPoint(), UP + LEFT, DOWN + LEFT)),
        Rule(RIGHT, listOf(RIGHT.toPoint(), UP + RIGHT, DOWN + RIGHT)),
    )

    data class SimulationResult(val newState: Set<Point>, val anyMoved: Boolean)

    fun Set<Point>.simulate(rulesShift: Int): SimulationResult {
        val actualRules = rules.rotate(rulesShift)
        fun fitsFor(p: Point) = actualRules.find { r -> r.offset(p).none { it in this } }
        val (mightMove, noMoveAtAll) = partition { p -> p.allAdjacentInf().any { it in this } }
        val proposals = mightMove.map { p -> p to (fitsFor(p)?.let { p + it.dir } ?: p) }

        val allTo = proposals.groupingBy { it.second }.eachCount()
        val (canMove, shouldStay) = proposals.partition { (_, to) -> allTo[to] == 1 }
        return SimulationResult(
            buildSet {
                this += canMove.map { it.second }
                this += shouldStay.map { it.first }
                this += noMoveAtAll
            },
            canMove.isNotEmpty()
        )
    }

    fun seq() = generateSequence(SimulationResult(initial.toSet(), true) to 0) { (curr, shift) ->
        curr.newState.simulate(shift) to shift - 1
    }

    val (result) = seq().elementAt(10)
    val (newState) = result
    val a = newState.minBound()
    val b = newState.maxBound()
    partOne = Rectangle(a, b).points.count { it !in newState }.s()
    partTwo = seq().indexOfFirst { !it.first.anyMoved }.s()
}