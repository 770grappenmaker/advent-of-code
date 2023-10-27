package com.grappenmaker.aoc

import com.grappenmaker.jvmutil.generateMethod
import com.grappenmaker.jvmutil.instance
import com.grappenmaker.jvmutil.internalNameOf
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.ClassNode

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
    generateMethod("generate", "()L${internalNameOf<PuzzleSet>()};") {
        // Init puzzleset
        visitTypeInsn(NEW, internalNameOf<PuzzleSet>())
        visitInsn(DUP)
        visitIntInsn(SIPUSH, year)
        visitMethodInsn(INVOKESPECIAL, internalNameOf<PuzzleSet>(), "<init>", "(I)V", false)

        // On stack: PuzzleSet (keep on stack)
        // TODO: clean up this horrible code
        val actualPackage = packag.removeSuffix("/")
        (1..25).mapNotNull { loadAsNode("$actualPackage/${numToName(it)}") }.forEach { node ->
            node.methods.singleOrNull {
                it.desc == "(L${internalNameOf<PuzzleSet>()};)V" && it.access and ACC_STATIC != 0
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
    fun createClass(bytes: ByteArray, name: String): Class<*> =
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
    loader: (bytes: ByteArray, binaryName: String) -> Class<*> = GenericClassDefiner::createClass,
    block: ClassVisitor.() -> Unit
) = com.grappenmaker.jvmutil.generateClass(
    name, extends, implements, generateConstructor, access,
    writerOptions, version, loader, debug, block
)