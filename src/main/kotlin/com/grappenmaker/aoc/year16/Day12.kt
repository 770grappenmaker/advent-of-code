package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day12() = puzzle(day = 12) {
    // Should maybe make some sort of assembly handler
    val insns = inputLines.map { l ->
        val parts = l.split(" ")
        Insn(parts.first(), parts.drop(1).map { a ->
            a.singleOrNull { it in 'a'..'z' }?.let { Register(it) } ?: LiteralArgument(a.toLong())
        })
    }

    fun solve(partTwo: Boolean): String {
        val registers = mutableMapOf<Char, Long>()
        if (partTwo) registers['c'] = 1

        fun InsnArgument.eval() = when (this) {
            is LiteralArgument -> value
            is Register -> registers.getOrPut(name) { 0 }
        }

        fun InsnArgument.reg() = (this as Register).name

        var ptr = 0
        while (ptr in insns.indices) {
            val (mnemonic, args) = insns[ptr]
            when (mnemonic) {
                "cpy" -> registers[args[1].reg()] = args[0].eval()
                "inc" -> registers[args[0].reg()] = args[0].eval() + 1
                "dec" -> registers[args[0].reg()] = args[0].eval() - 1
                "jnz" -> if (args[0].eval() != 0L) ptr += (args[1].eval() - 1).toInt()
                else -> error("Impossible")
            }

            ptr++
        }

        return registers.getValue('a').s()
    }

    partOne = solve(false)
    partTwo = solve(true)
}

data class Insn(val mnemonic: String, val arguments: List<InsnArgument>)

sealed interface InsnArgument
data class LiteralArgument(val value: Long) : InsnArgument
data class Register(val name: Char) : InsnArgument