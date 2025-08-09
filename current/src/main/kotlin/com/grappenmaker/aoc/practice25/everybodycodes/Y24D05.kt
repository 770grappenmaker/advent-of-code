package com.grappenmaker.aoc.practice25.everybodycodes

import com.grappenmaker.aoc.*

fun ECSolveContext.day05() {
    data class State(val i: Int, val columns: List<List<Int>>)
    fun ECInput.parse() = State(0, inputLines.map { l -> l.split(" ").map(String::toInt) }.transpose())

    fun State.update(): Pair<State, Long> {
        val fromColIdx = i % columns.size
        val toColIdx = (i + 1) % columns.size

        val col = columns[fromColIdx]
        val toCol = columns[toColIdx]
        val clapper = col.first()

        var left = clapper - 1
        var at = 0
        var side = false

        while (left > 0) {
            left--

            if ((!side && at == toCol.lastIndex) || (side && at == 0)) {
                side = !side
                continue
            }

            if (side) at-- else at++
        }

        val newColumns = List(columns.size) { idx ->
            when (idx) {
                fromColIdx -> col.drop(1)
                toColIdx -> {
                    val copy = toCol.toMutableList()
                    copy.add(if (side) at + 1 else at, clapper)
                    copy
                }

                else -> columns[idx]
            }
        }

        val shouted = newColumns.joinToString("") { it.first().toString() }.toLong()

        return State(toColIdx, newColumns) to shouted
    }

    partOne = generateSequence(partOneInput.parse() to 0L) { (a) -> a.update() }.map { it.second }.nth(10)

    val freq = hashMapOf<Long, Int>()
    var p2s = partTwoInput.parse()
    var i = 0

    while (true) {
        val (state, shouted) = p2s.update()
        val newFreq = freq.getOrDefault(shouted, 0) + 1
        i++

        if (newFreq == 2024) {
            partTwo = i * shouted
            break
        }

        freq[shouted] = newFreq
        p2s = state
    }

    var max = 0L

    partThreeInput.parse().patternRepeating(1000000) {
        val (newState, shouted) = it.update()
        max = maxOf(max, shouted)
        newState
    }

    partThree = max
}