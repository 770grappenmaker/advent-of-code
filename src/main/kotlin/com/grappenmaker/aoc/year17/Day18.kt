package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.queueOf

fun PuzzleSet.day18() = puzzle(day = 18) {
    val insns = inputLines.map { l ->
        val parts = l.split(" ")
        Computer.Insn(parts.first(), parts.drop(1).map { a ->
            a.singleOrNull { it in 'a'..'z' }?.let { Computer.Register(it) } ?: Computer.LiteralArgument(a.toLong())
        })
    }

    partOne = Computer(insns).also { while (it.running) it.step() }.recovered.s()

    val p0 = Computer(insns, true, 0L)
    val p1 = Computer(insns, true, 1L)

    p0.otherComputer = p1
    p1.otherComputer = p0

    while (p0.continues || p1.continues) {
        p0.step()
        p1.step()
    }

    partTwo = p1.sends.s()
}

class Computer(
    private val insns: List<Insn>,
    private val partTwo: Boolean = false,
    pid: Long = 0L,
) {
    var running = true
    var ptr = 0
    val registers = mutableMapOf<Char, Long>()

    // Part one details
    var lastSound = 0L
    var recovered = 0L

    // Part two details
    var sends = 0L
    val messageBus = queueOf<Long>()
    var waiting = false
    var otherComputer: Computer? = null

    val continues get() = running && !waiting

    init {
        if (partTwo) registers['p'] = pid
    }

    private fun InsnArgument.eval() = when (this) {
        is LiteralArgument -> value
        is Register -> registers.getOrPut(name) { 0 }
    }

    private fun InsnArgument.reg() = (this as Register).name

    private fun tryReceive(): Boolean {
        val curr = messageBus.removeLastOrNull()
        if (curr == null) {
            waiting = true
            return false
        }

        registers[insns[ptr].arguments[0].reg()] = curr
        waiting = false
        return true
    }

    fun step() {
        if (waiting && tryReceive()) ptr++
        if (!running) return

        val (mnemonic, arguments) = insns[ptr]
        when (mnemonic) {
            "set" -> registers[arguments[0].reg()] = arguments[1].eval()
            "add" -> registers[arguments[0].reg()] = arguments[0].eval() + arguments[1].eval()
            "mul" -> registers[arguments[0].reg()] = arguments[0].eval() * arguments[1].eval()
            "mod" -> registers[arguments[0].reg()] = arguments[0].eval() % arguments[1].eval()
            "snd" -> {
                if (partTwo) {
                    sends++
                    otherComputer?.messageBus?.addFirst(arguments[0].eval())
                } else {
                    lastSound = arguments[0].eval()
                }
            }
            "rcv" -> {
                if (partTwo) {
                    if (!tryReceive()) return
                } else if (arguments[0].eval() != 0L) {
                    recovered = lastSound
                    running = false
                }
            }

            "jgz" -> if (arguments[0].eval() > 0) ptr += (arguments[1].eval() - 1).toInt()
            else -> error("Impossible")
        }

        ptr++
    }

    data class Insn(val mnemonic: String, val arguments: List<InsnArgument>)

    sealed interface InsnArgument
    data class LiteralArgument(val value: Long) : InsnArgument
    data class Register(val name: Char) : InsnArgument
}