package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day11() = puzzle(day = 11) {
    val g = hashMapOf<String, MutableSet<String>>()

    for (l in inputLines) {
        val (f, t) = l.split(": ")
        for (tt in t.split(" ")) g.getOrPut(f) { hashSetOf() } += tt
    }

    data class Search(val curr: String, val seen: Set<String>)

    fun solve(start: String, goal: Set<String>): Long {
        val memo = hashMapOf<Search, Long>()
        fun recur(search: Search): Long = memo.getOrPut(search) {
            if (search.curr == "out") return if (search.seen == goal) 1L else 0L
            g.getValue(search.curr).sumOf { recur(Search(it, (search.seen + it) intersect goal)) }
        }

        return recur(Search(start, emptySet()))
    }

    partOne = solve("you", setOf())
    partTwo = solve("svr", setOf("dac", "fft"))
}