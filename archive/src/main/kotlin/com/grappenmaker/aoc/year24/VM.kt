package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import kotlin.random.Random

// simplified vm code
//fun run(startA: Long = defaultA, skipBranch: Boolean = false): List<Int> {
//    var a = startA
//    var b = 0L
//    var c = 0L
//    var ip = 0
//    val outs = mutableListOf<Int>()
//
//    while (ip in program.indices) {
//        val curr = program[ip]
//        val op = program[ip + 1]
//
//        val literal = op.toLong()
//        val combo = when(op) {
//            4 -> a
//            5 -> b
//            6 -> c
//            else -> op
//        }.toLong()
//
//        when(curr) {
//            0 -> a = a ushr combo.toInt()
//            1 -> b = b xor literal
//            2 -> b = combo % 8
//            3 -> if (a != 0L && !skipBranch) {
//                ip = literal.toInt()
//                continue
//            }
//
//            4 -> b = b xor c
//            5 -> outs += (combo % 8).toInt()
//            6 -> b = a ushr combo.toInt()
//            7 -> c = a ushr combo.toInt()
//        }
//
//        ip += 2
//    }
//
//    return outs
//}

data class Insn(val opcode: Int, val literal: Long, val combo: Parameter)

sealed interface Parameter {
    operator fun get(vm: VM): Long
    operator fun set(vm: VM, value: Long)
}

data class RegisterParameter(val register: Int) : Parameter {
    override fun get(vm: VM) = vm.registers[register]
    override fun set(vm: VM, value: Long) {
        vm.registers[register] = value
    }
}

data class LiteralParameter(val value: Long) : Parameter {
    override fun get(vm: VM) = value
    override fun set(vm: VM, value: Long) = error("Cannot set a literal value!")
}

fun List<Int>.parseInsns() = chunked(2) { (insn, op) ->
    Insn(insn, op.toLong(), if (op in 4..6) RegisterParameter(op - 4) else LiteralParameter(op.toLong()))
}

fun launchVM(input: List<Int>) = VM(input.parseInsns())

class VM(private val instructions: List<Insn>, private val haltOnBranch: Boolean = false) {
    var ptr = 0
    var halted = false
        private set

    val registers = MutableList(3) { 0L }
    var a by registers.delegate(0)
    var b by registers.delegate(1)
    var c by registers.delegate(2)

    val outputs = mutableListOf<Int>()

    var cycles = 0
        private set

    fun step() {
        if (halted) return

        val curr = instructions[ptr]
        if (interpret(curr)) ptr += 2

        cycles++
        if (ptr !in instructions.indices) halt()
    }

    fun interpret(insn: Insn): Boolean {
        when (insn.opcode) {
            0 -> a = a ushr insn.combo[this].toInt()
            1 -> b = b xor insn.literal
            2 -> b = insn.combo[this] % 8
            3 -> if (a != 0L) {
                val newPtr = insn.literal.toInt()
                if (newPtr % 2 != 0) error("Tried to jump to non-instruction position")
                ptr = newPtr / 2 // TODO
                return false
            }

            4 -> b = b xor c
            5 -> outputs += (insn.combo[this] % 8).toInt()
            6 -> b = a ushr insn.combo[this].toInt()
            7 -> c = a ushr insn.combo[this].toInt()
            else -> error("Invalid/unexpected opcode ${insn.opcode}")
        }

        return true
    }

    fun halt() {
        halted = true
    }

    fun jump(to: Int) {
        ptr = to
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

object SillyLoader : ClassLoader() {
    fun createClass(name: String?, bytes: ByteArray): Class<*> = defineClass(name, bytes, 0, bytes.size)
}

fun List<Insn>.createCompiled() =
    SillyLoader.createClass(null, compile()).getConstructor().newInstance() as CompiledVMProgram

interface CompiledVMProgram {
    fun run(a: Long, b: Long, c: Long): List<Int>
    fun runSingle(a: Long, b: Long, c: Long): List<Int>
}

fun List<Insn>.compile(): ByteArray {
    val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)

    writer.visit(
        /* version = */ V9,
        /* access = */ ACC_PUBLIC,
        /* name = */ "Compiled${Random.nextInt(0, 1000)}",
        /* signature = */ null,
        /* superName = */ "java/lang/Object",
        /* interfaces = */ arrayOf("com/grappenmaker/aoc/year24/CompiledVMProgram")
    )

    with (writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)) {
        visitCode()
        visitVarInsn(ALOAD, 0)
        visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        visitInsn(RETURN)
        visitMaxs(-1, -1)
        visitEnd()
    }

    fun MethodVisitor.implement(skipBranches: Boolean) {
        val labels = if (skipBranches) emptyMap()
        else filter { it.opcode == 3 }.associate { it.literal.toInt() to Label() }

        visitCode()

        visitTypeInsn(NEW, "java/util/ArrayList")
        visitInsn(DUP)
        visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V", false)
        visitVarInsn(ASTORE, 8)

        fun reg(opcode: Int, idx: Int) = visitVarInsn(opcode, 2 * idx + 1)

        fun Parameter.compile() = when (this) {
            is LiteralParameter -> visitLdcInsn(value)
            is RegisterParameter -> reg(LLOAD, register)
        }

        for ((idx, insn) in withIndex()) {
            if (idx in labels && !skipBranches) visitLabel(labels[idx])

            when (insn.opcode) {
                0, 6, 7 -> {
                    reg(LLOAD, 0)
                    insn.combo.compile()
                    visitInsn(L2I)
                    visitInsn(LUSHR)
                    reg(LSTORE, when (insn.opcode) {
                        0 -> 0
                        6 -> 1
                        7 -> 2
                        else -> error("Impossible")
                    })
                }

                1 -> {
                    reg(LLOAD, 1)
                    visitLdcInsn(insn.literal)
                    visitInsn(LXOR)
                    reg(LSTORE, 1)
                }

                2 -> {
                    insn.combo.compile()
                    visitLdcInsn(8L)
                    visitInsn(LREM)
                    reg(LSTORE, 1)
                }

                3 -> {
                    if (skipBranches) continue
                    reg(LLOAD, 0)
                    visitInsn(LCONST_0)
                    visitInsn(LCMP)
                    visitJumpInsn(IFNE, labels[insn.literal.toInt()])
                }

                4 -> {
                    reg(LLOAD, 1)
                    reg(LLOAD, 2)
                    visitInsn(LXOR)
                    reg(LSTORE, 1)
                }

                5 -> {
                    visitVarInsn(ALOAD, 8)
                    insn.combo.compile()
                    visitLdcInsn(8L)
                    visitInsn(LREM)
                    visitInsn(L2I)
                    visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false)
                    visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true)
                    visitInsn(POP)
                }

                else -> error("Invalid/unexpected opcode ${insn.opcode}")
            }
        }

        visitVarInsn(ALOAD, 8)
        visitInsn(ARETURN)

        visitMaxs(-1, -1)
        visitEnd()
    }

    writer.visitMethod(ACC_PUBLIC, "run", "(JJJ)Ljava/util/List;", null, null).implement(false)
    writer.visitMethod(ACC_PUBLIC, "runSingle", "(JJJ)Ljava/util/List;", null, null).implement(true)

    writer.visitEnd()
    return writer.toByteArray()
}