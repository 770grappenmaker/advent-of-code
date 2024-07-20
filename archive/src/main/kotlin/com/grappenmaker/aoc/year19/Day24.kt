package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day24() = puzzle(day = 24) {
    val initial = inputLines.asGrid { it == '#' }
    val emptyGrid = initial.mapElements { false }
    val center = Point(initial.width / 2, initial.height / 2)

    fun rule(curr: Boolean, adj: Int) = when {
        curr && adj != 1 -> false
        !curr && (adj == 1 || adj == 2) -> true
        else -> curr
    }

    val config = initial.automaton { p, c -> rule(c, p.adjacentSideElements().countTrue()) }.firstNotDistinct()
    partOne = config.points.zip(generateSequence(1) { it * 2 }.asIterable())
        .sumOf { (p, a) -> if (config[p]) a else 0 }.s()

    partTwo = generateSequence(mapOf(0 to initial)) { layers ->
        val new = layers.toMutableMap()
        val bottomLayer = layers.keys.min()
        val topLayer = layers.keys.max()

        if (layers.getValue(bottomLayer).any { it }) new[bottomLayer - 1] = emptyGrid
        if (layers.getValue(topLayer).any { it }) new[topLayer + 1] = emptyGrid

        for ((layer, grid) in new) {
            new[layer] = grid.mapIndexedElements { point, v ->
                if (point == center) return@mapIndexedElements false

                val directNeighbours = point.adjacentSidesInf().count { it in grid && it != center && grid[it] }
                val lowerNeighbours = layers[layer + 1]?.let { lowerLayer ->
                    when (point) {
                        center + Direction.LEFT -> lowerLayer.columnValues(0).countTrue()
                        center + Direction.RIGHT -> lowerLayer.columnValues(grid.width - 1).countTrue()
                        center + Direction.DOWN -> lowerLayer.rowValues(grid.height - 1).countTrue()
                        center + Direction.UP -> lowerLayer.rowValues(0).countTrue()
                        else -> 0
                    }
                }

                val upperNeighbours = layers[layer - 1]?.let { upperLayer ->
                    var sum = 0
                    if (point.x == 0 && upperLayer[center + Direction.LEFT]) sum++
                    if (point.x == grid.width - 1 && upperLayer[center + Direction.RIGHT]) sum++
                    if (point.y == grid.height - 1 && upperLayer[center + Direction.DOWN]) sum++
                    if (point.y == 0 && upperLayer[center + Direction.UP]) sum++
                    sum
                }

                rule(v, directNeighbours + (upperNeighbours ?: 0) + (lowerNeighbours ?: 0))
            }
        }

        new
    }.nth(200).values.sumOf { it.countTrue() }.s()
}