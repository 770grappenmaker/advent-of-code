package com.grappenmaker.aoc.practice25.everybodycodes

import com.grappenmaker.aoc.*

fun ECSolveContext.y25day07() {
    fun check(n: String, g: Map<Char, Set<Char>>) =
        n.windowed(2).all { (f, s) -> s in (g[f] ?: emptySet()) }

    fun ECInput.parse(): Pair<List<String>, Map<Char, Set<Char>>> {
        val names = inputLines[0].split(",")
        val graph = hashMapOf<Char, MutableSet<Char>>()

        for (l in inputLines.drop(2)) {
            val (b, a) = l.split(" > ")
            graph.getOrPut(b.single()) { hashSetOf() } += a.split(",").map { it.single() }
        }

        return names to graph
    }

    val (p1n, p1g) = partOneInput.parse()
    partOne = p1n.single { check(it, p1g) }

    val (p2n, p2g) = partTwoInput.parse()
    partTwo = p2n.sumOfIndexed { idx, n -> (idx + 1) * if (check(n, p2g)) 1 else 0 }

    val (names, graph) = partThreeInput.parse()
    val ans = hashSetOf<String>()

    fun recur(soFar: String) {
        if (soFar.length in 7..11) ans += soFar
        if (soFar.length == 11) return

        (graph[soFar.last()] ?: emptySet()).forEach { recur(soFar + it) }
    }

    for (n in names) if (check(n, graph)) recur(n)
    partThree = ans.size
}