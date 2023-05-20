package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.*
import org.objectweb.asm.Opcodes.*
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import kotlin.random.Random

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

    fun generate(humnValue: Double? = null): Day21Eval {
        val cName = "com/grappenmaker/aoc/year22/Day21JVMGenerated${Random.nextInt()}"
        return generateClass(name = cName, implements = listOf(internalNameOf<Day21Eval>())) {
            parsed.forEach { (name, info) ->
                generateMethod(name, "()D") {
                    when {
                        name == "humn" && humnValue != null -> visitLdcInsn(humnValue)
                        info is NumberInfo -> visitLdcInsn(info.number)
                        info is InvokeInfo -> {
                            visitVarInsn(ALOAD, 0)
                            visitMethodInsn(INVOKEVIRTUAL, cName, info.lhs, "()D", false)
                            visitVarInsn(ALOAD, 0)
                            visitMethodInsn(INVOKEVIRTUAL, cName, info.rhs, "()D", false)
                            visitInsn(info.opcode)
                        }
                    }

                    visitInsn(DRETURN)
                }
            }
        }.instance()
    }

    val original = generate()
    partOne = original.root().toLong().s()
    val rootInfo = parsed.getValue("root") as InvokeInfo
    val randomCase = generate(1000.0)
    val changes = original.eval(rootInfo.lhs) != randomCase.eval(rootInfo.lhs)
    val target = if (changes) original.eval(rootInfo.rhs) else original.eval(rootInfo.lhs)

    val variable = if (changes) rootInfo.lhs else rootInfo.rhs

    var min = 0L
    var max = 1L shl 43

    while (true) {
        val pivot = (min + max) / 2
        val found = generate(pivot.toDouble()).eval(variable)
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
    fun root(): Double
}

private val handlesLookup = MethodHandles.lookup()
private val evalMethodType = MethodType.methodType(Double::class.javaPrimitiveType)

fun <T : Day21Eval> T.eval(name: String) =
    handlesLookup.findVirtual(this::class.java, name, evalMethodType).bindTo(this).invokeExact() as Double

sealed interface Info
data class NumberInfo(val number: Double) : Info
data class InvokeInfo(val opcode: Int, val lhs: String, val rhs: String) : Info