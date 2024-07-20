package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.onceSplit

@PuzzleEntry
// Overengineering sometimes is also good
fun PuzzleSet.day23() = puzzle(23) {
    val insns = inputLines.map { l ->
        val (insn, rest) = l.onceSplit(" ")
        val split = rest.split(", ")
        when (insn) {
            "hlf" -> HalfInsn(rest.asRegister())
            "tpl" -> TripleInsn(rest.asRegister())
            "inc" -> IncrementInsn(rest.asRegister())
            "jmp" -> JumpInsn(rest.toInt())
            "jie" -> JumpEven(split[1].toInt(), split[0].asRegister())
            "jio" -> JumpOne(split[1].toInt(), split[0].asRegister())
            else -> error("Invalid insn $insn")
        }
    }

    fun List<Instruction>.execute(a: Int = 0, b: Int = 0): Int {
        val regs = mutableMapOf(Register.A to a, Register.B to b)
        var idx = 0
        while (idx in indices) {
            when (val insn = this[idx]) {
                is JumpInsn -> idx += insn.offset - 1
                is CondJump -> if (insn.check(regs.getValue(insn.reg))) idx += insn.offset - 1
                is MutInsn -> regs[insn.reg] = insn.update(regs.getValue(insn.reg))
            }

            idx++
        }

        return regs.getValue(Register.B)
    }

    partOne = insns.execute().s()
    partTwo = insns.execute(a = 1).s()
}

enum class Register {
    A, B
}

fun String.asRegister() = when (this) {
    "a" -> Register.A
    "b" -> Register.B
    else -> error("Invalid register $this")
}

sealed interface Instruction
sealed interface RegInsn : Instruction {
    val reg: Register
}

sealed interface MutInsn : RegInsn {
    fun update(v: Int): Int
}

data class HalfInsn(override val reg: Register) : MutInsn {
    override fun update(v: Int) = v / 2
}

data class TripleInsn(override val reg: Register) : MutInsn {
    override fun update(v: Int) = v * 3
}

data class IncrementInsn(override val reg: Register) : MutInsn {
    override fun update(v: Int) = v + 1
}

data class JumpInsn(val offset: Int) : Instruction

sealed interface CondJump : RegInsn {
    fun check(v: Int): Boolean
    val offset: Int
}

data class JumpEven(override val offset: Int, override val reg: Register) : CondJump {
    override fun check(v: Int) = v % 2 == 0
}

data class JumpOne(override val offset: Int, override val reg: Register) : CondJump {
    override fun check(v: Int) = v == 1
}