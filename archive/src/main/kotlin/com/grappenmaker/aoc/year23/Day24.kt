package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day24() = puzzle(day = 24) {
    data class Stone(val pos: List<Double>, val vel: List<Double>)
    val stones = inputLines.map { l ->
        val (a, b) = l.split(" @ ").map {
            "-?\\d+".toRegex().findAll(it).map { p -> p.value.toDouble() }.toList()
        }

        Stone(a, b)
    }

    data class Line(val d: Double, val inter: Double, val stone: Stone)
    fun Line.eval(x: Double) = d * x + inter

    val ls = stones.map { (pos, vel) ->
        val (x0, y0) = pos
        val (vx0, vy0) = vel
        val d = (vy0) / (vx0)
        val inter = y0 - d * x0
        Line(d, inter, Stone(pos, vel))
    }

    val r = 200000000000000.0..400000000000000.0
    partOne = ls.combinations(2).count { (a, b) ->
        if (a.d == b.d) return@count false
        val xi = (a.d * a.stone.pos[0] - a.stone.pos[1] + b.stone.pos[1] - b.d * b.stone.pos[0]) / ((a.d - b.d))
        val yi = a.eval(xi)

        ((xi in r && yi in r) &&
                (if (a.stone.vel[0] < 0) xi < a.stone.pos[0] else xi > a.stone.pos[0]) &&
                (if (a.stone.vel[1] < 0) yi < a.stone.pos[1] else yi > a.stone.pos[1]) &&
                (if (b.stone.vel[0] < 0) xi < b.stone.pos[0] else xi > b.stone.pos[0]) &&
                (if (b.stone.vel[1] < 0) yi < b.stone.pos[1] else yi > b.stone.pos[1]))
    }.toString()

    // Line visualization for desmos
//    partTwo = (stones.map { (pos, vel) ->
//        val (a) = listOf(pos).map { l -> l.map { it / 100000000000000.0 } }
//        Stone(a, vel)
//    }.joinToString("\n") { (pos, vel) ->
//        "(" + (pos.zip(vel).joinToString(",") { (p, t) -> "${p.toBigDecimal()} + (t(100))(${t.toBigDecimal()})" }) + ")"
//    })

    // Generating the python script
//    println(stones.joinToString("\n") { (pos, vel) ->
//        "stones.append(((${pos.joinToString(", ") { it.toLong().toString() }}), (${vel.joinToString(", ") { it.toLong().toString() }})))"
//    })

    partTwo = "No part two solution in Kotlin. Ended up using z3 in a small Python script."
}