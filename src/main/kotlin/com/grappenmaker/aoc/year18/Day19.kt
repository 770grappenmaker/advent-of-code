package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.delegate
import com.grappenmaker.aoc.splitInts

fun PuzzleSet.day19() = puzzle(day = 19) {
    val boundReg = inputLines.first().splitInts().single()
    data class Instruction(val opcode: Opcode, val args: List<OpcodeValue>)

    val instructions = inputLines.drop(1).map { l ->
        val parts = l.split(" ")
        val opPart = parts.first()
        val op = when (opPart) {
            "addr", "addi" -> Addition
            "mulr", "muli" -> Multiplication
            "banr", "bani" -> BitwiseAND
            "borr", "bori" -> BitwiseOR
            "setr", "seti" -> Assignment
            "gtir", "gtri", "gtrr" -> GTTesting
            "eqir", "eqri", "eqrr" -> EQTesting
            else -> error("Impossible")
        }

        val mode = when (op) {
            GTTesting, EQTesting -> when (opPart.drop(2)) {
                "ir" -> listOf(true, false)
                "ri" -> listOf(false, true)
                "rr" -> listOf(false, false)
                else -> error("Impossible")
            }

            Assignment -> when (opPart.last()) {
                'r' -> listOf(false, false)
                'i' -> listOf(true, false)
                else -> error("Impossible")
            }

            else -> when (opPart.last()) {
                'r' -> listOf(false, false)
                'i' -> listOf(false, true)
                else -> error("Impossible")
            }
        }

        Instruction(op, parts.drop(1).map(String::toInt).decodeArguments(mode))
    }

    fun run(partTwo: Boolean): Int {
        val registers = MutableList(6) { 0 }
        if (partTwo) registers[0] = 1

        var ip by registers.delegate(boundReg)
        var cycles = 0

        while (ip in instructions.indices) {
            if (partTwo && cycles == 100) break

            val insn = instructions[ip]
            val (a, b, c) = insn.args
            insn.opcode.exec(registers, a, b, c)

            if (ip !in instructions.indices) break
            ip++
            cycles++
        }

        return if (partTwo) registers.max() else registers[0]
    }

    partOne = run(false).s()

    val toFactorize = run(true)
    partTwo = (1..toFactorize).filter { toFactorize % it == 0 }.sum().s()
}