package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.permPairs

fun PuzzleSet.day2() = puzzle {
    fun eval(noun: Long, verb: Long) = with(startComputer(input)) {
        this[1] = noun
        this[2] = verb
        runUntilHalt()
        this[0]
    }

    partOne = eval(12, 2).s()

    val (noun, verb) = (0L..99L).toList().permPairs().first { (noun, verb) -> eval(noun, verb) == 19690720L }
    partTwo = (100 * noun + verb).s()
}