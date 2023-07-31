package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.countTrue
import com.grappenmaker.aoc.doubleLines
import com.grappenmaker.aoc.splitInts

fun PuzzleSet.day25() = puzzle(day = 25) {
    // should be a linked list
    val tape = mutableMapOf<Int, Boolean>()
    var cursor = 0
    var state = inputLines.first().substringBeforeLast('.').last()

    data class Step(val toWrite: Boolean, val left: Boolean, val nextState: Char)
    data class State(val whenFalse: Step, val whenTrue: Step)

    fun List<String>.parseStep() = Step(
        this[0].splitInts().single() == 1,
        "left" in this[1],
        this[2].substringBeforeLast('.').last()
    )

    fun Step.perform() {
        tape[cursor] = toWrite
        if (left) cursor-- else cursor++
        state = nextState
    }

    val states = input.doubleLines().drop(1).map(String::lines).associate {
        it.first().substringBeforeLast(':').last() to State(it.slice(2..4).parseStep(), it.slice(6..8).parseStep())
    }

    repeat(inputLines[1].splitInts().single()) {
        val (whenFalse, whenTrue) = states.getValue(state)
        (if (tape[cursor] == true) whenTrue else whenFalse).perform()
    }

    partOne = tape.values.countTrue().s()
}