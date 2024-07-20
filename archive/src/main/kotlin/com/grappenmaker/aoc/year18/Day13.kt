package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day13() = puzzle(day = 13) {
    val parseGrid = inputLines.asCharGrid()

    data class CartData(
        val pos: Point,
        val direction: Direction,
        val happy: Boolean = true,
        val intersectionDirection: Int = -1
    )

    data class GameState(val carts: List<CartData>, val crashes: List<Point> = listOf())
    val initialCarts = parseGrid.points.mapNotNull { p -> parseGrid[p].toDirectionOrNull()?.let { CartData(p, it) } }

    fun GameState.update(): GameState {
        val toMove = carts.sortedWith(compareBy<CartData> { it.pos.y }.thenBy { it.pos.x }).toMutableList()
        val newCrashes = crashes.toMutableList()

        toMove.mapInPlace { data ->
            val (pos, dir, interactable, intersectDir) = data
            if (!interactable) return@mapInPlace data

            val nextPos = pos + dir
            val crashed = toMove.withIndex().find { it.value.happy && it.value.pos == nextPos }
            if (crashed != null) {
                newCrashes += nextPos
                toMove[crashed.index] = toMove[crashed.index].copy(happy = false)
            }

            data.copy(
                pos = nextPos,
                direction = when(parseGrid[nextPos]) {
                    '\\' -> dir.next(if (dir.isVertical) -1 else 1)
                    '/' -> dir.next(if (dir.isHorizontal) -1 else 1)
                    '+' -> dir.next(intersectDir)
                    else -> dir
                },
                happy = crashed == null,
                intersectionDirection = if (parseGrid[nextPos] == '+') ((intersectDir + 2) % 3) - 1 else intersectDir
            )
        }

        return GameState(toMove, newCrashes)
    }

    fun seq() = generateSequence(GameState(initialCarts)) { it.update() }
    fun Point.format() = "$x,$y"

    partOne = seq().flatMap { it.crashes }.first().format()
    partTwo = seq().first { it.carts.count(CartData::happy) == 1 }.carts.single { it.happy }.pos.format()
}