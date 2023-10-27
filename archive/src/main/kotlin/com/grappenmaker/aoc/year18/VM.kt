package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.delegate
import com.grappenmaker.aoc.mapInPlace
import com.grappenmaker.aoc.splitInts

typealias RegisterContainer = MutableList<Int>

sealed interface OpcodeValue {
    operator fun get(registers: RegisterContainer): Int
    operator fun set(registers: RegisterContainer, value: Int)
}

data class RegisterValue(val index: Int) : OpcodeValue {
    override fun get(registers: RegisterContainer) = registers[index]
    override fun set(registers: RegisterContainer, value: Int) {
        registers[index] = value
    }
}

data class ImmediateValue(val value: Int) : OpcodeValue {
    override fun get(registers: RegisterContainer) = value
    override fun set(registers: RegisterContainer, value: Int) = error("Cannot set immediate value ($value)")
}

data object Ignored : OpcodeValue {
    override fun get(registers: RegisterContainer) = error("Should never happen")
    override fun set(registers: RegisterContainer, value: Int) = error("Should never happen")
}

val allOpcodes = listOf(Addition, Multiplication, BitwiseAND, BitwiseOR, Assignment, GTTesting, EQTesting)

sealed interface Opcode {
    // Boolean represents if immediate
    val argumentArrangements: List<List<Boolean>> get() = listOf(listOf(false, false), listOf(false, true))
    fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue)
}

fun List<Int>.decodeArguments(arrangement: List<Boolean?>) = (arrangement + false).zip(this).map { (i, v) ->
    when(i) {
        true -> ImmediateValue(v)
        false -> RegisterValue(v)
        null -> Ignored
    }
}

fun Opcode.exec(registers: RegisterContainer, nums: List<Int>, arrangement: List<Boolean>) {
    val (a, b, c) = nums.decodeArguments(arrangement)
    exec(registers, a, b, c)
}

data object Addition : Opcode {
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = a[registers] + b[registers]
    }
}

data object Multiplication : Opcode {
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = a[registers] * b[registers]
    }
}

data object BitwiseAND : Opcode {
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = a[registers] and b[registers]
    }
}

data object BitwiseOR : Opcode {
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = a[registers] or b[registers]
    }
}

data object Assignment : Opcode {
    override val argumentArrangements = listOf(listOf(false, false), listOf(true, false))
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = a[registers]
    }
}

data object GTTesting : Opcode {
    override val argumentArrangements = listOf(listOf(true, false), listOf(false, true), listOf(false, false))
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = if (a[registers] > b[registers]) 1 else 0
    }
}

data object EQTesting : Opcode {
    override val argumentArrangements = listOf(listOf(true, false), listOf(false, true), listOf(false, false))
    override fun exec(registers: RegisterContainer, a: OpcodeValue, b: OpcodeValue, c: OpcodeValue) {
        c[registers] = if (a[registers] == b[registers]) 1 else 0
    }
}

data class Instruction(val opcode: Opcode, val args: List<OpcodeValue>)

fun List<String>.parseVM(): Computer {
    val pointer = first().splitInts().first()
    val instructions = drop(1).map { l ->
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
                'r' -> listOf(false, null)
                'i' -> listOf(true, null)
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

    return Computer(pointer, instructions)
}

class Computer(boundReg: Int, val instructions: List<Instruction>) {
    val registers = MutableList(6) { 0 }

    var ip by registers.delegate(boundReg)
    var cycles = 0
    var halted = false

    fun halt() {
        halted = true
    }

    fun reset() {
        registers.mapInPlace { 0 }
        cycles = 0
        halted = false
    }

    fun step() {
        if (halted) return

        val insn = instructions[ip]
        val (a, b, c) = insn.args
        insn.opcode.exec(registers, a, b, c)

        if (ip !in instructions.indices) return halt()
        ip++
        cycles++
    }

    inline fun stepUntilHalt(onStep: () -> Unit = {}) {
        while (!halted) {
            step()
            onStep()
        }
    }
}