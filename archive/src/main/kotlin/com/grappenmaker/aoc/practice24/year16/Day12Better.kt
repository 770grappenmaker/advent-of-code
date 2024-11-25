@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.practice24.year16

import com.grappenmaker.aoc.*

fun PuzzleSet.day12() = puzzle(day = 12) {
    with(launchVM(inputLines)) {
        run()
        partOne = registers[0]

        reset()
        registers[2] = 1
        run()

        partTwo = registers[0]
    }
}

data class Insn(val opcode: String, val params: List<Parameter>)
private val Insn.a get() = params[0]
private val Insn.b get() = params[1]

sealed interface Parameter {
    operator fun get(vm: VM): Int
    operator fun set(vm: VM, value: Int)
}

data class RegisterParameter(val register: Int) : Parameter {
    override fun get(vm: VM) = vm.registers[register]
    override fun set(vm: VM, value: Int) {
        vm.registers[register] = value
    }
}

data class LiteralParameter(val value: Int) : Parameter {
    override fun get(vm: VM) = value
    override fun set(vm: VM, value: Int) = error("Cannot set a literal value!")
}

fun launchVM(input: List<String>) = VM(input.map {
    val s = it.split(" ")
    Insn(s.first(), s.drop(1).map { p ->
        if (p.first().isLetter()) RegisterParameter(p.single() - 'a') else LiteralParameter(p.toInt())
    })
})

class VM(private val instructions: List<Insn>) {
    var ptr = 0
    var halted = false
        private set

    val registers = MutableList(4) { 0 }
    var cycles = 0
        private set

    fun step() {
        if (halted) return

        val curr = instructions[ptr]
        interpret(curr)

        ptr++
        cycles++
        if (ptr !in instructions.indices) halt()
    }

    fun interpret(insn: Insn) {
        // Implementation
        when (insn.opcode) {
            "cpy" -> insn.b[this] = insn.a[this]
            "inc" -> insn.a[this]++
            "dec" -> insn.a[this]--
            "jnz" -> if (insn.a[this] != 0) jump(insn.b[this])
            else -> error("Invalid/unexpected opcode ${insn.opcode}")
        }
    }

    fun halt() {
        halted = true
    }

    fun jump(offset: Int) {
        ptr += offset - 1
    }

    fun run(onStep: VM.() -> Unit = {}) {
        while (!halted) {
            step()
            onStep()
        }
    }

    fun reset() {
        ptr = 0
        registers.mapInPlace { 0 }
        halted = false
        cycles = 0
    }
}