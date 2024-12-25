package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.year19.MazeEntity.*
import com.grappenmaker.aoc.Direction.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day15() = puzzle(15) {
    val pc = startComputer(input)
    fun Direction.asLong() = when (this) {
        UP -> 1L
        DOWN -> 2L
        LEFT -> 3L
        RIGHT -> 4L
    }

    fun getResponse(command: Direction): Long {
        pc.addInput(command.asLong())
        return pc.stepUntilOutput()
    }

    fun generate(
        curr: Point = Point(0, 0),
        acc: HashMap<Point, MazeEntity> = hashMapOf()
    ): HashMap<Point, MazeEntity> {
        curr.adjacentSidesInf().filter { it !in acc }.forEach { adj ->
            val dir = curr.deltaDir(adj)
            val res = getResponse(dir)
            if (res == 0L) acc[adj] = WALL
            else {
                acc[adj] = if (res == 2L) OXYGEN else NOTHING
                generate(adj, acc)
                getResponse(-dir)
            }
        }

        return acc
    }

    val graph = generate()
    val target = graph.entries.first { (_, v) -> v == OXYGEN }.key
    val neighs: (Point) -> Iterable<Point> = { it.adjacentSidesInf().filter { p -> graph[p] != WALL } }

    partOne = bfsDistance(Point(0, 0), { it == target }, neighs).dist.toString()
    partTwo = fillDistance(target, neighs).values.max().toString()
}

enum class MazeEntity {
    WALL, OXYGEN, NOTHING
}