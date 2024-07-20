package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry
import java.util.*

@PuzzleEntry
fun PuzzleSet.day15() = puzzle(day = 15) {
    val initialGrid = inputLines.asCharGrid().mapIndexedElements { p, v ->
        when (v) {
            '.' -> EmptySquare
            '#' -> Wall
            else -> Battler(v == 'E', p)
        }
    }

    val toDiscuss = initialGrid.points.filterNot { initialGrid[it] == Wall }
    data class State(val grid: Grid<Square>, val canContinue: Boolean = true, val elfDied: Boolean = false)

    // stupidly bad code ahead
    fun State.step(): State {
        var newElfDied = elfDied

        return State(grid.asMutableGrid().apply {
            val readingOrder = compareBy<Point> { it.y }.thenBy { it.x }
            val neighbors = { p: Point -> p.adjacentSides().filter { this[it] == EmptySquare } }

            val justMoved = hashSetOf<Point>()

            for (p in toDiscuss) {
                if (p in justMoved) continue

                val battler = this[p] as? Battler ?: continue
                val targets = elements.filterIsInstance<Battler>().filter { it.elf != battler.elf }
                if (targets.isEmpty()) return State(asGrid(), false, newElfDied)

                val inRange = targets.flatMap {
                    it.point.adjacentSides().filter { t -> this[t] == EmptySquare || t == p }
                }

                val newPosition = if (p !in inRange) {
                    val ourNeighbors = neighbors(p).toSet()
                    val chosen = inRange.minWithOrNull(compareBy<Point> { t ->
                        bfsDistance(p, { it == t }, neighbors).dist.takeIf { it != -1 } ?: Int.MAX_VALUE
                    }.then(readingOrder)) ?: continue

                    val poss = fillDistance(chosen, neighbors).filterKeys { it in ourNeighbors }
                    val dist = poss.values.minOrNull() ?: continue
                    val moveTo = poss.filterValues { it == dist }.keys.minWith(readingOrder)

                    Collections.swap(elements, p.toIndex(), moveTo.toIndex())
                    this[moveTo] = battler.copy(point = moveTo)
                    moveTo.also { justMoved += it }
                } else p

                val adjacent = newPosition.adjacentSides().toSet()
                val toAttack = targets.filter { it.point in adjacent }.minWithOrNull(
                    compareBy<Battler> { it.hp }.thenBy(readingOrder) { it.point }
                ) ?: continue

                val newHP = toAttack.hp - battler.power
                this[toAttack.point] = if (newHP <= 0) {
                    if (toAttack.elf) newElfDied = true
                    EmptySquare
                } else toAttack.copy(hp = newHP)
            }
        }.asGrid(), true, newElfDied)
    }

    fun Grid<Square>.healthSum() = elements.filterIsInstance<Battler>().sumOf { it.hp }
    fun IndexedValue<State>.outcome() = (index - 1) * value.grid.healthSum()

    partOne = generateSequence(State(initialGrid)) { it.step() }
        .takeUntil { it.canContinue }.withIndex().last().outcome().s()

    partTwo = generateSequence(4, Int::inc).firstNotNullOf { elfPower ->
        val improvedGrid = initialGrid.mapElements { if (it is Battler && it.elf) it.copy(power = elfPower) else it }
        val result = generateSequence(State(improvedGrid)) { it.step() }
            .takeUntil { it.canContinue && !it.elfDied }.withIndex().last()

        if (result.value.elfDied) null else result.outcome()
    }.s()
}

sealed interface Square
data class Battler(val elf: Boolean, val point: Point, val hp: Int = 200, val power: Int = 3) : Square
data object EmptySquare : Square
data object Wall : Square