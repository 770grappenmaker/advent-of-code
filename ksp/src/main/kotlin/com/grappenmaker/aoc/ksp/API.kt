package com.grappenmaker.aoc.ksp

/**
 * Adds the annotated member to the puzzle set with the given [year]. If [year] < 2015, the year is deemed invalid
 * or not set, and the symbol processor will attempt to infer the year by the last few digits of the package name
 */
@Retention
@Target(AnnotationTarget.FUNCTION)
annotation class PuzzleEntry(val year: Int = -1)

/**
 * Adds the annotated member to a set of (generated) PuzzleSets that the runner can reference
 */
@Retention
@Target(AnnotationTarget.PROPERTY)
annotation class GeneratedPuzzleSet