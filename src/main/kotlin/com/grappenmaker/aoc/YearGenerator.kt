package com.grappenmaker.aoc

import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter

// Generates a PuzzleSet for a given year.
// Package is an internal name (`.` -> `/`)
// numToName convert a day index to a classname
// (which means you have to use a consistent filename scheme)

// This thing is completely redundant, not elegant,
// can cause bugs etc. but I don't care, I like writing more code to write less code.
fun generateYear(
    year: Int,
    packag: String,
    numToName: (Int) -> String = { "Day${it.toString().padStart(2, '0')}Kt" }
) = generateClass(implements = listOf(internalNameOf<PuzzleSetGenerator>())) {
    val internalPSet = internalNameOf<PuzzleSet>()
    generateMethod("generate", "()L$internalPSet;") {
        // Init puzzleset
        visitTypeInsn(NEW, internalPSet)
        visitInsn(DUP)
        visitIntInsn(SIPUSH, year)
        visitMethodInsn(INVOKESPECIAL, internalPSet, "<init>", "(I)V", false)

        // On stack: PuzzleSet (keep on stack)
        // TODO: clean up this horrible code
        val actualPackage = packag.removeSuffix("/")
        (1..25).mapNotNull { loadAsNode("$actualPackage/${numToName(it)}") }.forEach { node ->
            node.methods.singleOrNull {
                it.desc == "(L$internalPSet;)V" && it.access and ACC_STATIC != 0
            }?.let { method ->
                visitInsn(DUP)
                visitMethodInsn(INVOKESTATIC, node.name, method.name, method.desc, false)
            } ?: error("No single puzzle method has been found in ${node.name}!")
        }

        // Still on stack: PuzzleSet
        visitInsn(ARETURN)
    }
}.instance<PuzzleSetGenerator>().generate()

fun loadAsNode(internalName: String, loader: ClassLoader = PuzzleSetGenerator::class.java.classLoader): ClassNode? =
    ClassNode().also {
        ClassReader((loader.getResourceAsStream("$internalName.class") ?: return null))
            .accept(it, ClassReader.SKIP_DEBUG)
    }

interface PuzzleSetGenerator {
    fun generate(): PuzzleSet
}

var unnamedClassCounter = 0
    get() = field++

object GenericClassDefiner : ClassLoader(PuzzleSetGenerator::class.java.classLoader) {
    fun createClass(name: String, bytes: ByteArray): Class<*> =
        defineClass(name, bytes, 0, bytes.size).also { resolveClass(it) }
}

inline fun generateClass(
    name: String = "Unnamed$unnamedClassCounter",
    extends: String = "java/lang/Object",
    implements: List<String> = listOf(),
    access: Int = ACC_PUBLIC,
    generateConstructor: Boolean = true,
    writerOptions: Int = ClassWriter.COMPUTE_FRAMES,
    version: Int = V1_8,
    debug: Boolean = false,
    loader: (binaryName: String, bytes: ByteArray) -> Class<*> = GenericClassDefiner::createClass,
    block: ClassVisitor.() -> Unit
): Class<*> {
    val writer = ClassWriter(writerOptions)
    return with(if (debug) TraceClassVisitor(writer, PrintWriter(System.out)) else writer) {
        visit(version, access, name, null, extends, implements.toTypedArray())

        if (generateConstructor) {
            generateMethod("<init>", "()V") {
                visitVarInsn(ALOAD, 0)
                visitMethodInsn(INVOKESPECIAL, extends, "<init>", "()V", false)
                visitInsn(RETURN)
            }
        }

        block()
        visitEnd()

        loader(name.replace('/', '.'), writer.toByteArray())
    }
}

inline fun ClassVisitor.generateMethod(
    name: String,
    desc: String,
    access: Int = ACC_PUBLIC,
    maxStack: Int = -1,
    maxLocal: Int = -1,
    block: MethodVisitor.() -> Unit
) {
    val mv = visitMethod(access, name, desc, null, null)
    mv.visitCode()
    mv.block()
    mv.visitMaxs(maxStack, maxLocal)
    mv.visitEnd()
}

inline fun <reified T : Any> Class<*>.instance() = getConstructor().newInstance() as T
inline fun <reified T> internalNameOf(): String = Type.getInternalName(T::class.java)