package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day11() = puzzle(day = 11) {
    val elementsRegex = """(, (and )?)|( and )""".toRegex()

    data class Component(val element: String, val chip: Boolean)
    data class State(val floors: List<Set<Component>>, val elevator: Int = 0)

    fun Set<Component>.isInvalid(): Boolean {
        if (isEmpty()) return false
        val (chips, generators) = partition { it.chip }.mapBoth { it.map(Component::element).toSet() }
        return chips.any { it !in generators && generators.isNotEmpty() }
    }

    // TODO: binary representation
    fun State.encode(): Pair<Int, List<Set<Int>>> {
        var i = 0
        val map = mutableMapOf<String, Int>()
        return elevator to floors.map { floor ->
            floor.map { (if (it.chip) 16 else 0) or map.getOrPut(it.element) { i++ } }.toSet()
        }
    }

    val floors = inputLines.take(3).map { l ->
        val (_, relevant) = l.removeSuffix(".").split("contains ")
        relevant.replace("a ", "").split(elementsRegex).map { component ->
            val (elementPart, typePart) = component.split(" ")
            Component(elementPart.substringBefore('-'), typePart == "microchip")
        }.toSet()
    }.plusElement(emptySet())

    fun solve(initialState: State): String {
        val totalChips = initialState.floors.sumOf { it.size }
        val queue = queueOf(initialState to 0)
        val seen = hashSetOf(initialState.encode())

        queue.drain { (curr, dist) ->
            if (curr.floors.last().size == totalChips) return dist.s()
            val currentFloor = curr.floors[curr.elevator]
            val poss = (currentFloor.permPairsExclusive { a, b -> setOf(a, b) }
                    + currentFloor.map { setOf(it) }).toSet()

            listOf(curr.elevator - 1, curr.elevator + 1).filter { it in curr.floors.indices }.forEach { newElevator ->
                val nextFloor = curr.floors[newElevator]
                poss.forEach a@{ moved ->
                    val nextCurrent = currentFloor - moved
                    val nextNext = nextFloor + moved
                    if (nextCurrent.isInvalid() || nextNext.isInvalid()) return@a

                    val updated = curr.floors.toMutableList()
                    updated[curr.elevator] = nextCurrent
                    updated[newElevator] = nextNext
                    State(updated, newElevator).let { if (seen.add(it.encode())) queue.addFirst(it to dist + 1) }
                }
            }
        }

        error("Impossible")
    }

    partOne = solve(State(floors))
    partTwo = solve(State(floors.toMutableList().also {
        it[0] += setOf(
            Component("elerium", chip = false),
            Component("elerium", chip = true),
            Component("dilithium", chip = false),
            Component("dilithium", chip = true),
        )
    }))
}