package com.grappenmaker.aoc.year16

class VM(initialProgram: List<Insn>) {
    val insns = initialProgram.toMutableList()
    val registers = mutableMapOf<Char, Long>()
    var ptr = 0
    var cycles = 0
    var halted = false
    var output = ArrayDeque<Long>()

    fun InsnArgument.eval() = when (this) {
        is LiteralArgument -> value
        is Register -> registers.getOrPut(name) { 0 }
    }

    fun InsnArgument.reg() = (this as? Register)?.name

    inline fun step(onStep: () -> Unit = {}) {
        if (halted) return

        val (mnemonic, args) = insns[ptr]
        try {
            when (mnemonic) {
                "cpy" -> registers[args[1].reg() ?: return] = args[0].eval()
                "inc" -> registers[args[0].reg() ?: return] = args[0].eval() + 1
                "dec" -> registers[args[0].reg() ?: return] = args[0].eval() - 1
                "jnz" -> if (args[0].eval() != 0L) ptr += (args[1].eval() - 1).toInt()
                "tgl" -> {
                    val target = ptr + args[0].eval().toInt()
                    val targetInsn = insns.getOrNull(target) ?: return
                    insns[target] = targetInsn.copy(mnemonic = when (val tm = targetInsn.mnemonic) {
                        "inc" -> "dec"
                        "dec", "tgl", "out" -> "inc"
                        "jnz" -> "cpy"
                        "cpy" -> "jnz"
                        else -> error("Unexpected opcode: $tm")
                    })
                }
                "out" -> output.addLast(args[0].eval())
                else -> error("Impossible")
            }
        } finally {
            cycles++
            if (++ptr !in insns.indices) halted = true
            else onStep()
        }
    }

    inline fun stepUntilHalted(onStep: () -> Unit = {}) {
        while (!halted) step(onStep)
    }
}

data class Insn(val mnemonic: String, val arguments: List<InsnArgument>)

sealed interface InsnArgument
data class LiteralArgument(val value: Long) : InsnArgument
data class Register(val name: Char) : InsnArgument

fun List<String>.parseProgram() = map { l ->
    val parts = l.split(" ")
    Insn(parts.first(), parts.drop(1).map { a ->
        a.singleOrNull { it in 'a'..'z' }?.let { Register(it) } ?: LiteralArgument(a.toLong())
    })
}