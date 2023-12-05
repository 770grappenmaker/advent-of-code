package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day5() = puzzle(day = 5) {
    val blocks = input.doubleLines()
    val seeds = blocks.first().substringAfter(": ").split(" ").map(String::toLong)

    data class Entry(val sourceRange: LongRange, val destRange: LongRange)

    val maps = blocks.drop(1).map { it.lines() }.associate { b ->
        val v = b.drop(1).map { it.split(" ").map(String::toLong) }.map { (a, b, c) -> Entry(b..b + c, a..a + c) }
        b.first().substringBeforeLast(" map:") to v
    }

    data class Value(val type: String, val value: Long)

    fun Value.convert(t: String) = Value(t, maps.getValue("$type-to-$t").find { value in it.sourceRange }
        ?.let { r -> r.destRange.first + value - r.sourceRange.first } ?: value)

    partOne = seeds.minOf {
        Value("seed", it).convert("soil").convert("fertilizer").convert("water").convert("light")
            .convert("temperature").convert("humidity").convert("location").value
    }.s()

    partTwo = listOf(
        "seed", "soil", "fertilizer", "water", "light", "temperature", "humidity", "location"
        // location was missing, this worked during solving, but I have no idea why
    ).windowed(2).fold(seeds.chunked(2).map { (a, b) -> a..a + b }) { curr, (f, t) ->
        buildList outer@ {
            addAll(maps.getValue("$f-to-$t").fold(curr) { c, ra ->
                buildList {
                    for (r in c) {
                        val overlap = ra.sourceRange.overlap(r)
                        if (overlap == null) {
                            add(r)
                            continue
                        }

                        val delta = ra.destRange.first - ra.sourceRange.first
                        this@outer.add((delta + overlap.first)..(delta + overlap.last))

                        if (r.last > ra.sourceRange.last) add(ra.sourceRange.last..r.last)
                        if (r.first < ra.sourceRange.first) add(r.first..ra.sourceRange.first)
                    }
                }
            })
        }
    }.minOf { it.first }.s()
}