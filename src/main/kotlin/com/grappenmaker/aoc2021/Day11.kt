package com.grappenmaker.aoc2021

fun Solution.solveDay11(): Pair<Int, Int> {
    // Part one
    // Welp the parsing is the same as day 9
    val width = inputLines.first().length
    val height = inputLines.size

    // 48 isn't magic: it is my wacky way to parse numbers
    // (48 is '0'.code)
    val octos = inputLines.flatMap { line -> line.map { Octopus(it.code - 48) } }

    var totalFlashes = 0
    var steps = 0

    fun flash(idx: Int) {
        totalFlashes++
        octos[idx].power = -1
        getAllAdjacents(asXY(idx, width), width, height).forEach {
            val octo = octos[it]
            if (octo.power != -1) octo.power += 1
            if (octo.power >= 10) flash(it)
        }
    }

    val resetFlashed = { octos.forEach { o -> if (o.power == -1) o.power = 0 } }
    val stepOctos = { reset: Boolean ->
        octos.forEach { o -> o.power += 1 }
        octos.forEachIndexed { idx, octo -> if (octo.power == 10) flash(idx) }
        if (reset) resetFlashed()

        steps++
    }

    repeat(100) { stepOctos(true) }
    val partOne = totalFlashes

    // Part two
    while (true) {
        stepOctos(false)
        if (octos.all { (power) -> power == -1 }) break
        resetFlashed()
    }

    return partOne to steps
}

data class Octopus(var power: Int)