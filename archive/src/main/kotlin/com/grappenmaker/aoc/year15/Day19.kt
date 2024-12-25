package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.asPair
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.splitAt

@PuzzleEntry
fun PuzzleSet.day19() = puzzle(day = 19) {
    val (rulesPart, molecule) = input.split("\n\n")
    val rules = rulesPart.lines().map { it.split(" => ").asPair() }

    fun Pair<String, String>.replacements(on: String) = on.indices.mapNotNull { idx ->
        if (on.regionMatches(idx, first, 0, first.length)) {
            val (before, after) = on.toList().splitAt(idx)
            (before + second + after.drop(first.length)).joinToString("")
        } else null
    }

    partOne = rules.fold(setOf<String>()) { acc, curr -> acc + curr.replacements(molecule) }.size.toString()

    fun Pair<String, String>.replaceOnce(on: String) = if (first !in on) null else on.replaceFirst(first, second)
    val reverseRules = rules.map { (a, b) -> b to a }

    // When the random "hillclimbing" works :facepalm:
    // This is probably not garaunteed to work all the time, but it worked for my input
    // (and it is blazingly fast)
    // Other attempts include:
    // - naive BFS
    // - dijkstra with levenshtein distance
    // - dijkstra with memoized levenshtein distance
    // - greedy algorithm trying the biggest replacement (invalid)
    // - greedy backtracking
    // If this were to check all permutations, since there are 43 rules in my case, 43! = big (panic)
    // But since this is random, we could technically say this runs O(1)... lol
    // Proper solution: CYK, but no experience with it

    fun solvePartTwo(): String {
        val r = reverseRules.toMutableList()

        while (true) {
            r.shuffle()

            var curr = molecule
            var result = 0
            while (true) {
                var updated = curr
                r.forEach {
                    while (true) {
                        updated = it.replaceOnce(updated) ?: break
                        result++
                    }
                }

                if (curr == updated) break
                curr = updated
            }

            if (curr == "e") return result.toString()
        }
    }

    partTwo = solvePartTwo()
}