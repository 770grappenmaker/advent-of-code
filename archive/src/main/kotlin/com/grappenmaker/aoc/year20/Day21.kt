package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.onceSplit

@PuzzleEntry
fun PuzzleSet.day21() = puzzle(day = 21) {
    data class Food(val ingredients: Set<String>, val allergens: List<String>)

    val foods = inputLines.map { l ->
        val (ingredientsParts, allergensPart) = l.onceSplit(" (contains ")
        Food(ingredientsParts.split(" ").toSet(), allergensPart.substringBefore(')').split(", "))
    }

    // TODO: improve this ugly code (probably wontfix lol)
    val assoc = foods.flatMap { f -> f.allergens.map { it to f.ingredients } }
        .groupBy { (a) -> a }.mapValues { (_, b) -> b.map { (_, c) -> c } }

    val allIngredients = assoc.values.flatten().flatten().toSet()
    val canBe = assoc.mapValues { it.value.reduce { acc, curr -> acc intersect curr }.toSet() }
    val yesAllergen = canBe.values.reduce { acc, curr -> acc + curr }.toSet()
    partOne = foods.sumOf { f -> (allIngredients - yesAllergen).count { it in f.ingredients } }.s()
    partTwo = canBe.findUnambiguousSolution().entries.sortedBy { (a) -> a }.joinToString(",") { (_, b) -> b }
}

// Keys mapped to values that are "possible", find the only solution
// TODO: maybe util
fun <K, V> Map<K, Set<V>>.findUnambiguousSolution(): Map<K, V> {
    val toLookAt = mapValues { it.value.toMutableSet() }.toMutableMap()
    val result = mutableMapOf<K, V>()

    while (toLookAt.isNotEmpty()) {
        val (key, onlyPoss) = toLookAt.entries.first { it.value.size == 1 }
        toLookAt -= key

        val poss = onlyPoss.single()
        toLookAt.forEach { it.value -= poss }
        result[key] = poss
    }

    return result
}