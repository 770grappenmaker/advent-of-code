package com.grappenmaker.aoc.year17

import com.grappenmaker.aoc.queueOf

class VM(
    val insns: List<Insn>,
    private val duetSetting: Boolean = false,
    pid: Long = 0L,
) {
    var running = true
    var ptr = 0
    val registers = mutableMapOf<Char, Long>()

    // Day 18 details
    // Part one details
    var lastSound = 0L
    var recovered = 0L

    // Part two details
    var sends = 0L
    val messageBus = queueOf<Long>()
    var waiting = false
    var otherComputer: VM? = null

    val continues get() = running && !waiting

    init {
        if (duetSetting) registers['p'] = pid
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

    fun step(onStep: VM.() -> Unit = {}) {
        if (waiting && tryReceive()) ptr++
        if (ptr !in insns.indices) running = false
        if (!running) return

        onStep()

        val (mnemonic, arguments) = insns[ptr]
        when (mnemonic) {
            "set" -> registers[arguments[0].reg()] = arguments[1].eval()
            "add" -> registers[arguments[0].reg()] = arguments[0].eval() + arguments[1].eval()
            "mul" -> registers[arguments[0].reg()] = arguments[0].eval() * arguments[1].eval()
            "mod" -> registers[arguments[0].reg()] = arguments[0].eval() % arguments[1].eval()
            "snd" -> {
                if (duetSetting) {
                    sends++
                    otherComputer?.messageBus?.addFirst(arguments[0].eval())
                } else {
                    lastSound = arguments[0].eval()
                }
            }
            "rcv" -> {
                if (duetSetting) {
                    if (!tryReceive()) return
                } else if (arguments[0].eval() != 0L) {
                    recovered = lastSound
                    running = false
                }
            }

            "jgz" -> if (arguments[0].eval() > 0) ptr += (arguments[1].eval() - 1).toInt()

            "sub" -> registers[arguments[0].reg()] = arguments[0].eval() - arguments[1].eval()
            "jnz" -> if (arguments[0].eval() != 0L) ptr += (arguments[1].eval() - 1).toInt()
            else -> error("Impossible")
        }

        ptr++
    }

    fun stepUntilHalt(onStep: VM.() -> Unit = {}) {
        while (running) step(onStep)
    }

    data class Insn(val mnemonic: String, val arguments: List<InsnArgument>)

    sealed interface InsnArgument
    data class LiteralArgument(val value: Long) : InsnArgument
    data class Register(val name: Char) : InsnArgument
}

fun List<String>.parseProgram() = map { l ->
    val parts = l.split(" ")
    VM.Insn(parts.first(), parts.drop(1).map { a ->
        a.singleOrNull { it in 'a'..'z' }?.let { VM.Register(it) } ?: VM.LiteralArgument(a.toLong())
    })
}