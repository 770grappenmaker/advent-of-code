package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day21() = puzzle(day = 21) {
    // Note to self: helpful to debug these kinds of things instead of writing a full "decompilation"
    // Just printing out the instructions and finding the single reference to the reg0 would have been sufficient
    // There is not a lot of programming involved, except that we could maybe add optimizations to the code
    // to make this run faster than 77 seconds...
    // instructions.forEach { println(it) }

    with(inputLines.parseVM()) {
        val checkInsn = instructions.indexOfFirst { (it.args[1] as? RegisterValue)?.index == 0 }
        val resultReg = (instructions[checkInsn].args.first() as RegisterValue).index
        val result = mutableSetOf<Int>()

        // The idea is that the program does not halt if it never tries a new value again,
        // so the last time the value is checked (= the eqrr is reached) uniquely, thats the endpoint
        // A set does exactly that
        stepUntilHalt { if (ip == checkInsn && !result.add(registers[resultReg])) halt() }

        partOne = result.first().toString()
        partTwo = result.last().toString()
    }
}