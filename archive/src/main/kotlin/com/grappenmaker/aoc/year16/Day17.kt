package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day17() = puzzle(day = 17) {
    val grid = """
        #########
        #S| | | #
        #-#-#-#-#
        # | | | #
        #-#-#-#-#
        # | | | #
        #-#-#-#-#
        # | | |V#
        #########
    """.trimIndent().lines().asCharGrid()

    val start = grid.findPointsValued('S').single()
    data class PathData(val path: String = "", val curr: Point = start)

    fun Direction.format() = when (this) {
        Direction.UP -> "U"
        Direction.RIGHT -> "R"
        Direction.DOWN -> "D"
        Direction.LEFT -> "L"
    }

    fun PathData.neighs(): List<PathData> {
        val doors = (input + path).opens().map { curr + it }
        return enumValues<Direction>().filter { dir ->
            val target = curr + dir
            target in grid && when (grid[target]) {
                ' ', 'V' -> true
                '#' -> false
                '|', '-' -> target in doors
                else -> error("Impossible")
            }
        }.map {
            val newTarget = curr + it
            val gridValue = grid[newTarget]
            PathData(path + it.format(), if (gridValue == '-' || gridValue == '|') newTarget + it else newTarget)
        }
    }

    partOne = bfs(PathData(), isEnd = { grid[it.curr] == 'V' }, neighbors = { it.neighs() }).end!!.path

    fun PathData.reverseOptimize(): Int {
        if (grid[curr] == 'V') return path.length
        return neighs().maxOfOrNull { it.reverseOptimize() } ?: Int.MIN_VALUE
    }

    partTwo = PathData().reverseOptimize().toString()
}

fun String.md5() = md5.digest(encodeToByteArray()).take(2).map { it.toInt() and 0xFF }
fun String.opens(): List<Direction> {
    val (lo, hi) = md5()
    return buildList {
        if (lo shr 4 and 0xF in 0xb..0xf) add(Direction.UP)
        if (lo and 0xF in 0xb..0xf) add(Direction.DOWN)
        if (hi shr 4 and 0xF in 0xb..0xf) add(Direction.LEFT)
        if (hi and 0xF in 0xb..0xf) add(Direction.RIGHT)
    }
}