package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.splitInts

@PuzzleEntry
fun PuzzleSet.day24() = puzzle(day = 24) {
    data class Component(val portA: Int, val portB: Int)
    val allComponents = inputLines.map { l -> l.splitInts().let { (a, b) -> Component(a, b) } }

    data class State(
        val components: List<Component> = emptyList(),
        val left: List<Component> = allComponents,
        val endPort: Int = 0
    )

    fun State.strength() = components.sumOf { it.portA + it.portB }

    // TODO: pruning
    fun recurse(curr: State, comparator: Comparator<State>): State = when {
        curr.left.isEmpty() -> curr
        else -> curr.left.flatMap { next ->
            listOfNotNull(
                if (curr.endPort == next.portA) recurse(
                    State(curr.components + next, curr.left - next, next.portB),
                    comparator
                ) else null,
                if (curr.endPort == next.portB) recurse(
                    State(curr.components + next, curr.left - next, next.portA),
                    comparator
                ) else null,
                curr
            )
        }.maxWith(comparator)
    }

    // Slow strategy for part two, but it generalizes nicely
    fun solve(comparator: Comparator<State>) = recurse(State(), comparator).strength().s()
    partOne = solve(compareBy { it.strength() })
    partTwo = solve(compareBy<State> { it.components.size }.thenBy { it.strength() })
}