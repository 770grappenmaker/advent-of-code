package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.Point3D
import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.rangeTo
import com.grappenmaker.aoc.volumeLong

@PuzzleEntry
fun PuzzleSet.day22() = puzzle(day = 22) {
    // This took me way too long to come up with
    // My initial idea of cutting ended up too slow, since after you cut a region once,
    // you need to cut all of those new regions, and so on, for all the OFF instructions...
    // Yeah, that is slow... it grows exponentially
    data class Step(val positive: Boolean, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange)

    val originalSteps = inputLines.map { l ->
        val (on, coords) = l.split(" ")
        val (xr, yr, zr) = coords.split(",").map {
            val (s, e) = it.drop(2).split("..").map(String::toInt)
            s..e
        }

        Step(on == "on", xr, yr, zr)
    }

    fun IntRange.overlap(other: IntRange) =
        (maxOf(first, other.first)..minOf(last, other.last)).takeIf { !it.isEmpty() }

    fun Step.overlap(other: Step) = xRange.overlap(other.xRange)?.let { x ->
        yRange.overlap(other.yRange)?.let { y ->
            zRange.overlap(other.zRange)?.let { Step(!positive, x, y, it) }
        }
    }

    fun solve(steps: List<Step>): String {
        val known = mutableListOf<Step>()
        var score = 0L
        val update = { (pos, x, y, z): Step ->
            score += (Point3D(x.first, y.first, z.first)..Point3D(x.last, y.last, z.last))
                .volumeLong.let { if (pos) it else -it }
        }

        steps.forEach { curr ->
            known += known.mapNotNull { e -> e.overlap(curr)?.also(update) }
            if (curr.positive) known += curr.also(update)
        }

        return score.toString()
    }

    val partialRange = -50..50
    partOne = solve(originalSteps.mapNotNull {
        it.copy(
            xRange = it.xRange.overlap(partialRange) ?: return@mapNotNull null,
            yRange = it.yRange.overlap(partialRange) ?: return@mapNotNull null,
            zRange = it.zRange.overlap(partialRange) ?: return@mapNotNull null
        )
    })

    partTwo = solve(originalSteps)
}