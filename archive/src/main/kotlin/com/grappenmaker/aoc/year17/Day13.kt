package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.asPair
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.queueOf
import com.grappenmaker.aoc.splitInts

@PuzzleEntry
fun PuzzleSet.day13() = puzzle(day = 13) {
    // depth -> range
    val layers = inputLines.associate { it.splitInts().asPair() }
    val lastLayer = layers.keys.max()

    // Dumb solution, there is probably a nice solution using modular arithmetic
    fun Map<Int, Pair<Int, Int>>.update() = mapValues { (depth, state) ->
        val (pos, dir) = state
        val nextPos = pos + dir
        val nextDir = if (nextPos == 0 || nextPos == layers.getValue(depth) - 1) -dir else dir
        nextPos to nextDir
    }

    fun seq() = generateSequence(layers.mapValues { 0 to 1 }) { it.update() }

    var severity = 0
    val iter = seq().iterator()
    for (idx in 0..lastLayer) {
        val (oldPos) = iter.next()[idx] ?: continue
        if (oldPos == 0) severity += idx * layers.getValue(idx)
    }

    partOne = severity.toString()

    var target = 0
    val ring = queueOf(seq().take(lastLayer + 1).toList())

    label@ while (true) {
        try {
            for (idx in 0..lastLayer) {
                val (oldPos) = ring[idx][idx] ?: continue
                if (oldPos == 0) {
                    ring.removeFirst()
                    ring.addLast(ring.last().update())
                    continue@label
                }
            }

            break@label
        } finally {
            target++
        }
    }

    partTwo = (target - 1).toString()
}