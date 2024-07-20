package com.grappenmaker.aoc.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.FunctionKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo
import kotlin.properties.ReadOnlyProperty

private const val puzzleAnnotationName = "com.grappenmaker.aoc.ksp.PuzzleEntry"
private const val puzzleSetName = "com.grappenmaker.aoc.PuzzleSet"
private const val puzzleName = "com.grappenmaker.aoc.Puzzle"
private const val generatedPackage = "com.grappenmaker.aoc.years"

@OptIn(KspExperimental::class)
class Processor(
    private val generator: CodeGenerator,
    private val options: Map<String, String>,
) : SymbolProcessor {
    private var seen = false
    private fun booleanProperty(default: Boolean) =
        ReadOnlyProperty<Any, Boolean> { _, p -> (options[p.name] ?: default.toString()) == "true" }

    private val generatePuzzleSets by booleanProperty(true)
    private val generateYearSet by booleanProperty(false)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (seen) return emptyList()
        seen = true
        return processEntries(resolver) + processSets(resolver)
    }

    private fun processSets(resolver: Resolver): List<KSAnnotated> {
        if (!generateYearSet) return emptyList()

        val toRef = resolver
            .getDeclarationsFromPackage(generatedPackage)
            .filter { it.isAnnotationPresent(GeneratedPuzzleSet::class) }
            .filterIsInstance<KSPropertyDeclaration>()
            .map { MemberName(it.packageName.asString(), it.simpleName.asString()) }.toList()

        val puzzleSetType = ClassName.bestGuess(puzzleSetName)
        val puzzleType = ClassName.bestGuess(puzzleName)
        FileSpec.builder(generatedPackage, "Years")
            .indent("    ")
            .addFileComment("This is an auto-generated collection of years. Do not modify!")
            .addProperty(
                PropertySpec.builder("years", List::class.asTypeName().parameterizedBy(puzzleSetType))
                    .initializer("listOf(${toRef.joinToString { "%M" }})", *toRef.toTypedArray())
                    .build()
            )
            .addProperty(
                PropertySpec.builder("puzzles", List::class.asTypeName().parameterizedBy(puzzleType))
                    .initializer("years.flatMap { it.puzzles }")
                    .build()
            )
            .build().writeTo(generator, false, emptyList())

        return emptyList()
    }

    private fun processEntries(resolver: Resolver): List<KSAnnotated> {
        if (!generatePuzzleSets) return emptyList()
        val toGenerate = mutableMapOf<Int, MutableList<KSFunctionDeclaration>>()
        val seen = hashSetOf<Pair<String, String>>()

        fun Sequence<KSAnnotated>.store() {
            for (puzzle in this) {
                val annotation = puzzle.getAnnotationsByType(PuzzleEntry::class).singleOrNull()

                if (
                    annotation == null ||
                    puzzle !is KSFunctionDeclaration ||
                    puzzle.functionKind != FunctionKind.TOP_LEVEL ||
                    puzzle.extensionReceiver?.resolve()?.declaration?.qualifiedName?.asString() != puzzleSetName
                ) continue

                if (!seen.add(puzzle.packageName.asString() to puzzle.simpleName.asString())) continue

                val packYear = puzzle.packageName.asString().takeLastWhile { it.isDigit() }
                    .toIntOrNull()?.let { it + 2000 }

                val annYear = annotation.year.takeIf { it >= 2015 }
                val targetYear = annYear ?: packYear
                if (targetYear == null) continue

                toGenerate.getOrPut(targetYear) { mutableListOf() } += puzzle
            }
        }

        resolver.getSymbolsWithAnnotation(puzzleAnnotationName).store()

        // we now know which years have changed, so we can add its siblings as well
        // TODO: rethink if this might be inefficient
        toGenerate.values.asSequence()
            .flatMap { m -> m.map { it.packageName.asString() } }.distinct()
            .forEach { resolver.getDeclarationsFromPackage(it).store() }

        if (toGenerate.isEmpty()) return emptyList()

        val puzzleSetType = ClassName.bestGuess(puzzleSetName)
        for ((year, refs) in toGenerate) {
            // failsafe just in case the declaration doesn't specify day
            refs.sortBy { m -> m.simpleName.asString().takeLastWhile { it.isDigit() }.toIntOrNull() ?: 0 }
            FileSpec.builder(generatedPackage, "Year$year")
                .indent("    ")
                .addFileComment("This is an auto-generated puzzle set, add puzzles with @PuzzleEntry. Do not modify!")
                .addProperty(
                    PropertySpec.builder("year$year", puzzleSetType)
                        .addAnnotation(GeneratedPuzzleSet::class)
                        .initializer(
                            CodeBlock.builder()
                                .beginControlFlow("%T($year).apply {", puzzleSetType)
                                .apply {
                                    for (decl in refs) addStatement(
                                        "%M()", MemberName(
                                            decl.packageName.asString(),
                                            decl.simpleName.asString(),
                                            true
                                        )
                                    )
                                }
                                .endControlFlow()
                                .build()
                        ).build()
                ).build().writeTo(generator, false, refs.mapNotNull { it.containingFile })
        }

        // TODO: figure out how to do this incrementally
//        val module = resolver.getModuleName().asString().replaceFirstChar { it.uppercaseChar() }
//        val generatedMembers = toGenerate.keys.map { MemberName(generatedPackage, "year$it") }
//        FileSpec.builder(generatedPackage, "${module}Years")
//            .indent("    ")
//            .addFileComment("This is an auto-generated collection of years in this module. Do not modify!")
//            .addProperty(
//                PropertySpec.builder("yearsIn$module", List::class.asTypeName().parameterizedBy(puzzleSetType))
//                    .initializer("listOf(${generatedMembers.joinToString { "%M" }})", *generatedMembers.toTypedArray())
//                    .build()
//            ).build().writeTo(generator, false, emptyList())

        return emptyList()
    }
}