package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.year19.Opcode.*
import com.grappenmaker.aoc.year19.ParameterMode.*

sealed interface Opcode {
    val width: Int get() = 0
    val shouldStep: Boolean get() = true
    fun IntCode.execute()

    data object Add : Opcode {
        override val width = 3
        override fun IntCode.execute() {
            setParameter(3, getParameter(1) + getParameter(2))
        }
    }

    data object Multiply : Opcode {
        override val width = 3
        override fun IntCode.execute() {
            setParameter(3, getParameter(1) * getParameter(2))
        }
    }

    data object Input : Opcode {
        override val width = 1
        override fun IntCode.execute() {
            setParameter(1, getInput())
        }
    }

    data object Output : Opcode {
        override val width = 1
        override fun IntCode.execute() {
            output(getParameter(1))
        }
    }

    interface JumpInstruction : Opcode {
        override val shouldStep get() = false
        fun shouldJump(operand: Long): Boolean

        override fun IntCode.execute() {
            pc = if (shouldJump(getParameter(1))) getParameter(2) else pc + 3
        }
    }

    data object JumpTrue : JumpInstruction {
        override fun shouldJump(operand: Long) = operand != 0L
    }

    data object JumpFalse : JumpInstruction {
        override fun shouldJump(operand: Long) = operand == 0L
    }

    interface CompareInstruction : Opcode {
        override val width get() = 3
        fun compare(lhs: Long, rhs: Long): Boolean

        override fun IntCode.execute() {
            setParameter(3, if (compare(getParameter(1), getParameter(2))) 1 else 0)
        }
    }

    data object LessThan : CompareInstruction {
        override fun compare(lhs: Long, rhs: Long) = lhs < rhs
    }

    data object Equals : CompareInstruction {
        override fun compare(lhs: Long, rhs: Long) = lhs == rhs
    }

    data object AdjustRelative : Opcode {
        override val width = 1
        override fun IntCode.execute() {
            relative += getParameter(1)
        }
    }

    data object Halt : Opcode {
        override fun IntCode.execute() = halt()
    }
}

sealed interface ParameterMode {
    fun IntCode.getValue(offset: Long): Long
    fun IntCode.setValue(offset: Long, value: Long) {
        error("Setting is not supported for mode $this!")
    }

    data object PositionMode : ParameterMode {
        override fun IntCode.getValue(offset: Long) = getAtPC(offset)()
        override fun IntCode.setValue(offset: Long, value: Long) {
            set(getAtPC(offset), value)
        }
    }

    data object ImmediateMode : ParameterMode {
        override fun IntCode.getValue(offset: Long) = getAtPC(offset)
    }

    data object RelativeMode : ParameterMode {
        override fun IntCode.getValue(offset: Long) = (getAtPC(offset) + relative)()
        override fun IntCode.setValue(offset: Long, value: Long) {
            set(getAtPC(offset) + relative, value)
        }
    }
}

class IntCode(
    val memory: MutableMap<Long, Long> = hashMapOf(),
    private var input: Iterator<Long> = emptyList<Long>().iterator()
) : MutableMap<Long, Long> by memory {
    private val supportedOpcodes = mapOf(
        1L to Add,
        2L to Multiply,
        3L to Input,
        4L to Output,
        5L to JumpTrue,
        6L to JumpFalse,
        7L to LessThan,
        8L to Equals,
        9L to AdjustRelative,
        99L to Halt
    )

    private val supportedModes = mapOf(
        0L to PositionMode,
        1L to ImmediateMode,
        2L to RelativeMode
    )

    val output = mutableListOf<Long>()
    private val outputHandlers = mutableListOf<(Long) -> Unit>()

    var pc = 0L
    var relative = 0L
    private var halted = false

    override fun get(key: Long) = memory[key] ?: 0

    fun getAtPC(offset: Long = 0) = get(pc + offset)
    fun advancePC(amount: Int = 1) {
        pc += amount
    }

    fun halt() {
        halted = true
    }

    fun isHalted() = halted

    fun output(value: Long) {
        output.add(value)
        outputHandlers.forEach { it(value) }
    }

    fun getInput() = input.next()
    fun input(block: () -> Long) = input(iterator { while (true) yield(block()) })

    fun addInput(inp: Iterable<Long>) {
        val old = input
        input(iterator { yieldAll(old); yieldAll(inp.iterator()) })
    }

    fun addInput(inp: Long) {
        val old = input
        input(iterator { yieldAll(old) ; yield(inp) })
    }

    fun input(seq: Sequence<Long>) = input(seq.asIterable().iterator())
    fun input(iter: Iterator<Long>) {
        input = iter
    }

    operator fun Long.invoke() = get(this)

    private fun getMode(idx: Int): ParameterMode {
        val mode = ((getAtPC() / 10) / (pow10(idx))) % 10
        return supportedModes[mode] ?: error("Invalid mode $mode for ${getAtPC()} for param $idx")
    }

    fun getParameter(idx: Int) = with(getMode(idx)) { getValue(idx.toLong()) }
    fun setParameter(idx: Int, value: Long) = with(getMode(idx)) { setValue(idx.toLong(), value) }

    fun step(debug: Boolean = false) {
        if (halted) return
        val instruction = getAtPC()
        val opcode = instruction % 100

        with((supportedOpcodes[opcode] ?: error("Invalid instruction $instruction at $pc"))) {
            if (debug) println("Executing instruction $this (pc=$pc)")

            execute()
            if (shouldStep) advancePC(width + 1)
        }
    }

    fun stepUntilOutput(debug: Boolean = false): Long {
        if (halted) error("Already halted before output")

        val start = output.size
        while (output.size == start && !halted) step(debug)
        return output.last()
    }

    fun runUntilHalt(debug: Boolean = false) {
        while (!halted) {
            step(debug)
            if (debug) println(debugMemory())
        }
    }

    fun debugMemory() = (0L..memory.keys.max()).map(this::get).joinToString(",")

    fun onOutput(block: (Long) -> Unit) {
        outputHandlers.add(block)
    }

    fun outputSequence() = sequence { while (!halted) yield(stepUntilOutput()) }
}

fun startComputer(program: List<Long>, input: Iterator<Long>) =
    IntCode(memory = program.withIndex().associate { (idx, v) -> idx.toLong() to v }.toMutableMap(), input)

fun startComputer(program: List<Long>, input: List<Long> = listOf()) = startComputer(program, input.iterator())

fun startComputer(program: String, input: List<Long> = listOf()) =
    startComputer(program.split(",").map(String::toLong), input)

fun String.evalProgram(vararg args: Long) = with(startComputer(this, args.toList())) {
    runUntilHalt()
    output.last()
}

fun List<Long>.evalProgram(args: List<Long>) = with(startComputer(this, args)) {
    runUntilHalt()
    output.last()
}

fun List<Long>.evalProgram(vararg args: Long) = evalProgram(args.toList())

fun String.programResults(vararg args: Long) = with(startComputer(this, args.toList())) {
    runUntilHalt()
    output
}

fun List<Long>.programResults(args: List<Long>) = with(startComputer(this, args)) {
    runUntilHalt()
    output
}

fun List<Long>.programResults(vararg args: Long) = programResults(args.toList())

private fun pow10(n: Int): Int {
    var num = 1
    repeat(n) { num *= 10 }
    return num
}