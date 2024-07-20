package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day20() = puzzle(day = 20) {
    data class Tile(val id: Int, val grid: BooleanGrid)

    val tiles = input.doubleLines().map(String::lines).map { l ->
        Tile(l.first().splitInts().single(), l.drop(1).asGrid { it == '#' })
    }

    // Inefficient, doing double work I suppose...
    val topLeft = tiles.first { curr ->
        (tiles - curr).fold(listOf<Direction>()) { acc, otherTile ->
            val poss = otherTile.grid.orientations().map { otherTile.copy(grid = it) }
            acc + listOfNotNull(
                poss.find { (_, a) -> a.rowValues(a.height - 1) == curr.grid.rowValues(0) }?.let { UP },
                poss.find { (_, a) -> a.rowValues(0) == curr.grid.rowValues(curr.grid.height - 1) }?.let { DOWN },
                poss.find { (_, a) -> a.columnValues(a.width - 1) == curr.grid.columnValues(0) }?.let { LEFT },
                poss.find { (_, a) -> a.columnValues(0) == curr.grid.columnValues(curr.grid.width - 1) }?.let { RIGHT }
            )
        }.toSet() == setOf(DOWN, RIGHT)
    }

    fun seq(
        start: Tile,
        matchA: (Tile) -> List<Boolean>,
        matchB: (Tile) -> List<Boolean>
    ) = generateSequence(start to tiles) { (curr, left) ->
        val toMatch = matchA(curr)
        val newLeft = left.filter { it.id != curr.id }
        newLeft.flatMap { t -> t.grid.orientations().map { t.copy(grid = it) } }
            .find { matchB(it) == toMatch }?.let { it to newLeft }
    }.map { (a) -> a }

    fun BooleanGrid.removeBorders() = rowsValues.drop(1).dropLast(1).map { it.drop(1).dropLast(1) }.asGrid()

    val assembled = seq(
        start = topLeft,
        matchA = { it.grid.columnValues(it.grid.width - 1) },
        matchB = { it.grid.columnValues(0) },
    ).map { top ->
        seq(
            start = top,
            matchA = { it.grid.rowValues(it.grid.height - 1) },
            matchB = { it.grid.rowValues(0) },
        ).toList()
    }.toList()

    partOne = (assembled.first().first().id.toLong() *
            assembled.first().last().id.toLong() *
            assembled.last().last().id.toLong() *
            assembled.last().first().id.toLong()).s()

    val result = assembled.map { col ->
        col.map { it.grid.removeBorders().rowsValues }.reduce { acc, curr -> acc + curr }.swapOrder()
    }.reduce { acc, curr -> acc + curr }.asGrid()

    val monster = """
        |                  # 
        |#    ##    ##    ###
        | #  #  #  #  #  #   
    """.trimMargin().lines().asGrid { it == '#' }
    val monsterPoints = monster.filterTrue()

    val searchArea =
        Rectangle(Point(0, 0), Point(result.width - monster.width - 1, result.height - monster.height - 1)).points

    fun GridLike<Boolean>.monsterAt(at: Point) = monsterPoints.map { it + at }.takeIf { p -> p.all { this[it] } }

    val correctOrientation = result.orientations().maxBy { poss -> searchArea.count { poss.monsterAt(it) != null } }
    val answer = correctOrientation.filterTrue().toMutableSet()

    searchArea.forEach { correctOrientation.monsterAt(it)?.let { set -> answer -= set.toSet() } }
    partTwo = answer.size.s()
}