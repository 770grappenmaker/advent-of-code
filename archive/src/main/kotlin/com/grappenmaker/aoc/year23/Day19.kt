package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day19() = puzzle(day = 19) {
    val (rules, parts) = input.doubleLines()
    val ps = parts.lines().map { l ->
        l.drop(1).dropLast(1).split(",").associate { p ->
            val (c, d) = p.split("=")
            c to d.toLong()
        }
    }

    data class Rule(val p: String, val range: LongRange, val inverseRange: LongRange, val to: String)
    data class Workflow(val name: String, val rules: List<Rule>, val to: String)

    val wf = rules.lines().associate { l ->
        val name = l.substringBefore('{')
        val rls = l.drop(name.length + 1).dropLast(1).split(",")

        name to Workflow(name, rls.dropLast(1).map { rr ->
            val (rest, to) = rr.split(":")
            val p = rest.first().toString()
            val op = rest[1]
            val comp = rest.drop(2).toLong()
            Rule(
                p, when (op) {
                    '>' -> comp + 1..4000
                    '<' -> 1..<comp
                    else -> error("IMP $op")
                }, when (op) {
                    '>' -> 1..comp // <=
                    '<' -> comp..4000 // >=
                    else -> error("IMP $op")
                }, to
            )
        }, rls.last())
    }

    val start = wf.getValue("in")

    fun find(e: Map<String, Long>): Boolean {
        var curr = start
        while (true) when (val to = curr.rules.find { e.getValue(it.p) in it.range }?.to ?: curr.to) {
            "A" -> return true
            "R" -> return false
            else -> curr = wf.getValue(to)
        }
    }

    partOne = ps.sumOf { m -> if (find(m)) m.values.sum() else 0 }.toString()
    val default = "xmas".associate { it.toString() to 1..4000L }

    fun Map<String, LongRange>.merge(p: String, nr: LongRange): Map<String, LongRange>? {
        val new = toMutableMap()
        new[p] = nr.overlap(new.getValue(p)) ?: return null
        return new
    }

    fun solve(cw: Workflow, r: Map<String, LongRange>): List<Map<String, LongRange>> = when (cw.to) {
        "A" -> listOf(r)
        "R" -> emptyList()
        else -> {
            fun solve(cw: String, r: Map<String, LongRange>): List<Map<String, LongRange>> = when (cw) {
                "A" -> listOf(r)
                "R" -> emptyList()
                else -> solve(wf.getValue(cw), r)
            }

            var curr = r
            val res = mutableListOf<Map<String, LongRange>>()

            for (rule in cw.rules) {
                curr.merge(rule.p, rule.range)?.let { res += solve(rule.to, it) }
                curr = curr.merge(rule.p, rule.inverseRange) ?: curr
            }

            res + solve(cw.to, curr)
        }
    }

    partTwo = (solve(start, default).sumOf { m -> m.values.map { it.width().toBigInteger() }.product() }).toString()
}