package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import kotlin.math.sqrt

fun PuzzleSet.day23() = puzzle(day = 23) {
    var partOneAns = 0
    val program = inputLines.parseProgram()
    VM(program).stepUntilHalt { if (program[ptr].mnemonic == "mul") partOneAns++ }
    partOne = partOneAns.s()

    val truthy = VM.LiteralArgument(1)
    val (idx, insn) = program.withIndex().first { (_, v) -> v.mnemonic == "jnz" && v.arguments.first() == truthy }
    val target = idx + (insn.arguments[1] as VM.LiteralArgument).value.toInt()
    val testVM = VM(program).apply {
        registers['a'] = 1
        stepUntilHalt { if (ptr == target) running = false }
    }

    val from = testVM.registers.getValue('b')
    val to = testVM.registers.getValue('c')
    val step = -(program.last { it.mnemonic == "sub" }.arguments[1] as VM.LiteralArgument).value
    partTwo = (from..to step step).count { t -> (2 until sqrt(t.toFloat()).toLong()).any { t % it == 0L } }.s()
}