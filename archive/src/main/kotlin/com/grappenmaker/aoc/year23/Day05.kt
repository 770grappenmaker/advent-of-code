package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day5() = puzzle(day = 5) {
    val blocks = input.doubleLines()
    val seeds = blocks.first().substringAfter(": ").split(" ").map(String::toLong)

    val maps = blocks.drop(1).map { it.lines() }.map { p ->
        p.drop(1).map { it.split(" ").map(String::toLong) }.map { (a, b, c) -> b..b + c to a..a + c }
    }

    partOne = seeds.minOf { v ->
        maps.fold(v) { a, c -> c.find { a in it.first }?.let { it.second.first + a - it.first.first } ?: a }
    }.s()

    partTwo = maps.fold(seeds.chunked(2).map { (a, b) -> a..a + b }) { curr, m ->
        buildList outer@{
            addAll(m.fold(curr) { c, ra ->
                buildList {
                    for (r in c) {
                        val overlap = ra.first.overlap(r)
                        if (overlap == null) {
                            add(r)
                            continue
                        }

                        val delta = ra.second.first - ra.first.first
                        this@outer.add((delta + overlap.first)..(delta + overlap.last))

                        if (r.last > ra.first.last) add(ra.first.last..r.last)
                        if (r.first < ra.first.first) add(r.first..ra.first.first)
                    }
                }
            })
        }.simplify()
    }.minOf { it.first }.s()
}