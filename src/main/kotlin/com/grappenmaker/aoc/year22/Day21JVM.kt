package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.*
import org.objectweb.asm.Opcodes.*

fun main() = simplePuzzle(21, 2022) {
    val cName = "com/grappenmaker/aoc/year22/Day21JVMGenerated"
    partOne = generateClass(name = cName, implements = listOf(internalNameOf<Day21Eval>())) {
        inputLines.forEach { line ->
            val (name, job) = line.split(": ")
            val sp = job.split(" ")
            generateMethod(name, "()D") {
                val number = job.toDoubleOrNull()
                if (number != null) {
                    visitLdcInsn(number)
                } else {
                    visitVarInsn(ALOAD, 0)
                    visitMethodInsn(INVOKEVIRTUAL, cName, sp[0], "()D", false)
                    visitVarInsn(ALOAD, 0)
                    visitMethodInsn(INVOKEVIRTUAL, cName, sp[2], "()D", false)
                    visitInsn(
                        when (sp[1]) {
                            "+" -> DADD
                            "-" -> DSUB
                            "/" -> DDIV
                            "*" -> DMUL
                            else -> error("Invalid operator $sp")
                        }
                    )
                }

                visitInsn(DRETURN)
            }
        }
    }.instance<Day21Eval>().root().toLong().s()
}

interface Day21Eval {
    fun root(): Double
}