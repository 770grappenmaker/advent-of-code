package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*
import com.grappenmaker.jvmutil.*
import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*
import kotlin.random.Random
import kotlin.random.nextUInt

// Would win first place in a code bowl competition
// (huge)
fun PuzzleSet.day24() = puzzle(day = 24) {
    val evalDesc = "(L${internalNameOf<MonadState>()};)L${internalNameOf<MonadResult>()};"
    fun solve(leftBound: Int, rightBound: Int, inc: Int): String {
        val cName = "Y21D24Solution${Random.nextUInt()}"
        return generateClass(
            name = cName,
            generateConstructor = false,
            implements = listOf(internalNameOf<EvaluateDay24>())
        ) {
            visitField(0, "memo", "Ljava/util/Map;", null, null)
            generateMethod("<init>", "()V") {
                loadThis()
                visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
                loadThis()
                construct(java.util.HashMap::class.java.getConstructor())
                visitFieldInsn(PUTFIELD, cName, "memo", "Ljava/util/Map;")

                visitInsn(RETURN)
            }

            val correctDesc = "(L${internalNameOf<MonadState>()};)Z"
            generateMethod("correct", correctDesc) {
                val label = Label()
                visitVarInsn(ALOAD, 1)
                getProperty(MonadState::z)
                visitJumpInsn(IFEQ, label)
                visitInsn(ICONST_0)
                visitInsn(IRETURN)
                visitLabel(label)
                visitInsn(ICONST_1)
                visitInsn(IRETURN)
            }

            generateMethod("eval${inputLines.size}", evalDesc) {
                construct(::MonadResult) {
                    loadThis()
                    visitVarInsn(ALOAD, 1)
                    visitMethodInsn(INVOKEVIRTUAL, cName, "correct", correctDesc, false)
                    visitMethodInsn(
                        INVOKESTATIC,
                        "kotlin/collections/CollectionsKt",
                        "emptyList",
                        "()Ljava/util/List;",
                        false
                    )
                }

                visitInsn(ARETURN)
            }

            inputLines.forEachIndexed { idx, l ->
                generateMethod("eval$idx", evalDesc) {
                    fun memoize() {
                        visitInsn(DUP)
                        loadThis()
                        visitFieldInsn(GETFIELD, cName, "memo", "Ljava/util/Map;")
                        visitInsn(SWAP)
                        visitVarInsn(ALOAD, 1)
                        visitInsn(SWAP)
                        visitMethodInsn(
                            INVOKEINTERFACE,
                            "java/util/Map",
                            "put",
                            "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                            true
                        )

                        visitInsn(POP)
                    }

                    fun returnInvalid() {
                        construct(::MonadResult) {
                            visitInsn(ICONST_0)
                            visitMethodInsn(
                                INVOKESTATIC,
                                "kotlin/collections/CollectionsKt",
                                "emptyList",
                                "()Ljava/util/List;",
                                false
                            )
                        }

                        memoize()
                        visitInsn(ARETURN)
                    }

                    visitVarInsn(ALOAD, 1)
                    visitMethodInsn(INVOKEVIRTUAL, internalNameOf<MonadState>(), "getZ", "()I", false)

                    // Don't know if this works on all inputs...
                    loadConstant(0x500000)

                    val notTooBig = Label()
                    visitJumpInsn(IF_ICMPLT, notTooBig)
                    returnInvalid()

                    visitLabel(notTooBig)
                    loadThis()
                    visitFieldInsn(GETFIELD, cName, "memo", "Ljava/util/Map;")
                    visitVarInsn(ALOAD, 1)
                    invokeMethod(java.util.Map<*, *>::containsKey)

                    val memoLabel = Label()
                    visitJumpInsn(IFEQ, memoLabel)
                    loadThis()
                    visitFieldInsn(GETFIELD, cName, "memo", "Ljava/util/Map;")
                    visitVarInsn(ALOAD, 1)
                    invokeMethod(java.util.Map<*, *>::get)
                    visitTypeInsn(CHECKCAST, internalNameOf<MonadResult>())
                    visitInsn(ARETURN)

                    visitLabel(memoLabel)
                    fun init(index: Int, name: String) {
                        visitVarInsn(ALOAD, 1)
                        visitMethodInsn(
                            INVOKEVIRTUAL,
                            internalNameOf<MonadState>(),
                            "get${name.uppercase()}",
                            "()I",
                            false
                        )
                        visitVarInsn(ISTORE, index)
                    }

                    init(2, "w")
                    init(3, "x")
                    init(4, "y")
                    init(5, "z")

                    fun handle(reg: Int, opcode: Int) = visitVarInsn(opcode, reg + 2)
                    fun String.getValue() =
                        singleOrNull()?.let { if (it in 'w'..'z') handle(it - 'w', ILOAD) else null }
                            ?: loadConstant(toInt())

                    fun evalNext() {
                        loadThis()
                        construct(::MonadState) {
                            visitVarInsn(ALOAD, 1)
                            visitMethodInsn(INVOKEVIRTUAL, internalNameOf<MonadState>(), "getIdx", "()I", false)
                            visitInsn(ICONST_1)
                            visitInsn(IADD)
                            repeat(4) { visitVarInsn(ILOAD, it + 2) }
                        }

                        visitMethodInsn(INVOKEVIRTUAL, cName, "eval${idx + 1}", evalDesc, false)
                    }

                    val parts = l.split(" ")
                    val reg = parts[1].single() - 'w'

                    fun simpleInsn(handle: () -> Unit) {
                        handle(reg, ILOAD)
                        parts[2].getValue()
                        handle()
                        handle(reg, ISTORE)
                    }

                    when (parts.first()) {
                        "inp" -> {
                            loadConstant(leftBound)
                            visitVarInsn(ISTORE, 6)

                            val label = Label().also { visitLabel(it) }
                            visitVarInsn(ILOAD, 6)
                            handle(reg, ISTORE)
                            evalNext()
                            visitVarInsn(ASTORE, 7)

                            visitVarInsn(ALOAD, 7)
                            getProperty(MonadResult::valid)
                            val end = Label()
                            visitJumpInsn(IFEQ, end)
                            construct(::MonadResult) {
                                visitInsn(ICONST_1)
                                visitVarInsn(ALOAD, 7)
                                getProperty(MonadResult::soFar)

                                visitVarInsn(ILOAD, 6)
                                box(Type.INT_TYPE)
                                visitMethodInsn(
                                    INVOKESTATIC,
                                    "kotlin/collections/CollectionsKt",
                                    "plus",
                                    "(Ljava/util/Collection;Ljava/lang/Object;)Ljava/util/List;",
                                    false
                                )
                            }

                            memoize()
                            visitInsn(ARETURN)

                            visitLabel(end)
                            visitIincInsn(6, inc)
                            visitVarInsn(ILOAD, 6)
                            loadConstant(rightBound)
                            visitJumpInsn(IF_ICMPNE, label)

                            returnInvalid()
                        }

                        "add" -> simpleInsn { visitInsn(IADD) }
                        "mul" -> simpleInsn { visitInsn(IMUL) }
                        "div" -> simpleInsn { visitInsn(IDIV) }
                        "mod" -> simpleInsn { visitInsn(IREM) }
                        "eql" -> simpleInsn {
                            val a = Label()
                            val b = Label()
                            visitJumpInsn(IF_ICMPNE, a)
                            loadConstant(1)
                            visitJumpInsn(GOTO, b)
                            visitLabel(a)
                            loadConstant(0)
                            visitLabel(b)
                        }

                        else -> error("Impossible")
                    }

                    evalNext()
                    memoize()
                    visitInsn(ARETURN)
                }
            }
        }.instance<EvaluateDay24>().eval0(MonadState(0, 0, 0, 0, 0)).soFar.asReversed().joinToString("")
    }

    partOne = solve(9, 0, -1)
    partTwo = solve(1, 10, 1)
}

data class MonadState(val idx: Int, val w: Int, val x: Int, val y: Int, val z: Int)
data class MonadResult(val valid: Boolean, val soFar: List<Int>)

interface EvaluateDay24 {
    fun eval0(state: MonadState): MonadResult
}