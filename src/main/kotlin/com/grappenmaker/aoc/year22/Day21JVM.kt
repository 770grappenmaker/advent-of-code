package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.*
import com.grappenmaker.jvmutil.generateMethod
import com.grappenmaker.jvmutil.instance
import com.grappenmaker.jvmutil.internalNameOf
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes.*
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType

fun main() = simplePuzzle(21, 2022) {
    val parsed = inputLines.associate { l ->
        val (name, job) = l.split(": ")
        val sp = job.split(" ")
        name to when (val n = job.toDoubleOrNull()) {
            null -> InvokeInfo(
                when (sp[1]) {
                    "+" -> DADD
                    "-" -> DSUB
                    "/" -> DDIV
                    "*" -> DMUL
                    else -> error("Invalid operator $sp")
                }, sp[0], sp[2]
            )

            else -> NumberInfo(n)
        }
    }

    val cName = "com/grappenmaker/aoc/year22/Day21JVMGenerated"
    val eval = generateClass(name = cName, implements = listOf(internalNameOf<Day21Eval>())) {
        parsed.forEach { (name, info) ->
            generateMethod(name, "(D)D") {
                if (name == "humn") {
                    visitVarInsn(DLOAD, 1)
                    visitInsn(DCONST_0)
                    visitInsn(DCMPG)

                    val label = Label()
                    visitJumpInsn(IFEQ, label)

                    visitVarInsn(DLOAD, 1)
                    visitInsn(DRETURN)

                    visitLabel(label)
                }

                when (info) {
                    is NumberInfo -> visitLdcInsn(info.number)
                    is InvokeInfo -> {
                        visitVarInsn(ALOAD, 0)
                        visitVarInsn(DLOAD, 1)
                        visitMethodInsn(INVOKEVIRTUAL, cName, info.lhs, "(D)D", false)
                        visitVarInsn(ALOAD, 0)
                        visitVarInsn(DLOAD, 1)
                        visitMethodInsn(INVOKEVIRTUAL, cName, info.rhs, "(D)D", false)
                        visitInsn(info.opcode)
                    }
                }

                visitInsn(DRETURN)
            }
        }
    }.instance<Day21Eval>()

    partOne = eval.root(0.0).toLong().s()
    val rootInfo = parsed.getValue("root") as InvokeInfo
    val changes = eval.eval(rootInfo.lhs, 0.0) != eval.eval(rootInfo.lhs, 1000.0)
    val target = if (changes) eval.eval(rootInfo.rhs) else eval.eval(rootInfo.lhs)
    val variable = if (changes) rootInfo.lhs else rootInfo.rhs

    var min = 0L
    var max = 1L shl 43

    while (true) {
        val pivot = (min + max) / 2
        val found = eval.eval(variable, pivot.toDouble())
        val s = target - found

        when {
            s < 0L -> min = pivot
            s > 0L -> max = pivot
            else -> {
                partTwo = pivot.s()
                break
            }
        }
    }
}

interface Day21Eval {
    fun root(humn: Double): Double
}

private val handlesLookup = MethodHandles.lookup()
private val evalMethodType = MethodType.methodType(Double::class.javaPrimitiveType, Double::class.javaPrimitiveType)

fun <T : Day21Eval> T.eval(name: String, humn: Double = 0.0) =
    handlesLookup.findVirtual(this::class.java, name, evalMethodType).bindTo(this).invokeExact(humn) as Double

sealed interface Info
data class NumberInfo(val number: Double) : Info
data class InvokeInfo(val opcode: Int, val lhs: String, val rhs: String) : Info