package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.*

fun PuzzleSet.day24() = puzzle {
    val charGrid = inputLines.asCharGrid()
    val from = charGrid.row(0).single { charGrid[it] == '.' }
    val to = charGrid.row(charGrid.height - 1).single { charGrid[it] == '.' }

    data class Blizard(val pos: Point, val dir: Direction)
    data class SearchState(
        val pos: Point,
        val time: Int = 0,
        val seenEnd: Boolean = false,
        val seenStart: Boolean = false
    )

    fun List<Blizard>.evolve() = map {
        val nextIntended = it.pos + it.dir
        val next = if (charGrid[nextIntended] == '#') {
            (nextIntended + it.dir + it.dir).map { x, y -> x.mod(charGrid.width) to y.mod(charGrid.height) }
        } else nextIntended

        it.copy(pos = next)
    }

    val initialBlizards = charGrid.points
        .filter { charGrid[it] !in listOf('#', '.') }
        .map { Blizard(it, charGrid[it].toDirection()) }

    // Assuming 1000 is max..?
    // Small optimization here... could also simulate every single time again
    val maxTime = 1000
    val states = buildList {
        var last = initialBlizards
        add(initialBlizards.map { it.pos }.toSet())

        repeat(maxTime) {
            last = last.evolve()
            add(last.map { it.pos }.toSet())
        }
    }

    fun Grid<Char>.adj(curr: SearchState) =
        (curr.pos.adjacentSides() + curr.pos).filter { this[it] != '#' && it !in states[curr.time] }

    fun Grid<Char>.p1() = bfs(
        SearchState(from),
        isEnd = { it.pos == to },
        neighbors = { curr -> adj(curr).map { SearchState(it, curr.time + 1) } },
    ).end!!.time - 1

    fun Grid<Char>.p2() = bfs(
        SearchState(from),
        isEnd = { it.pos == to && it.seenEnd && it.seenStart },
        neighbors = { curr ->
            adj(curr).map {
                SearchState(
                    pos = it,
                    time = curr.time + 1,
                    seenEnd = curr.seenEnd || it == to,
                    seenStart = curr.seenStart || (curr.seenEnd && it == from)
                )
            }
        }
    ).end!!.time - 1

    partOne = charGrid.p1().s()
    partTwo = charGrid.p2().s()
}