package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet

fun PuzzleSet.day19() = puzzle(day = 19) {
    val (rulesPart, messagesPart) = input.split("\n\n")
    fun parseRule(rest: String) = when {
        rest.startsWith("\"") -> CharRule(rest.removeSurrounding("\"").single())
        else -> rest.split(" | ").map { CompoundRule(it.split(" ").map(String::toInt)) }
            .let { it.singleOrNull() ?: OrRule(it) }
    }

    val initialRules = rulesPart.lines().associate { l ->
        val (numPart, rest) = l.split(": ")
        numPart.toInt() to parseRule(rest)
    }

    fun solve(rules: Map<Int, RuleDesc>): String {
        val toMatch = rules.getValue(0)
        return messagesPart.lines().count { toMatch.match(it, rules).any(String::isEmpty) }.s()
    }

    partOne = solve(initialRules)
    partTwo = solve(
        initialRules.toMutableMap().also {
            it[8] = parseRule("42 | 42 8")
            it[11] = parseRule("42 31 | 42 11 31")
        }
    )
}

sealed interface RuleDesc {
    fun match(on: String, rules: Map<Int, RuleDesc>): List<String>
}

data class CharRule(val char: Char) : RuleDesc {
    override fun match(on: String, rules: Map<Int, RuleDesc>) =
        listOfNotNull(if (on.firstOrNull() == char) on.drop(1) else null)
}

data class CompoundRule(val references: List<Int>) : RuleDesc {
    override fun match(on: String, rules: Map<Int, RuleDesc>) =
        references.fold(listOf(on)) { acc, idx -> acc.flatMap { rules.getValue(idx).match(it, rules) } }
}

data class OrRule(val references: List<RuleDesc>) : RuleDesc {
    override fun match(on: String, rules: Map<Int, RuleDesc>) = references.flatMap { it.match(on, rules) }
}