package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day20() = puzzle(day = 20) {
    data class Module(val op: Char, val name: String, val to: List<String>)

    val m = inputLines.associate { l ->
        val (a, b) = l.split(" -> ")
        val n = if (a == "broadcaster") a else a.drop(1)
        n to Module(a.first(), n, b.split(", "))
    }

    val allModules = m.values.toList()
    val parents = buildMap<String, List<String>> {
        allModules.forEach { m -> m.to.forEach { put(it, (get(it) ?: emptyList()) + m.name) } }
    }

    val (mFlop, mConf) = m.toList().partition { (_, b) -> b.op == '%' || b.name == "broadcaster" }
    val flops = mFlop.associate { it.first to false }.toMutableMap()
    val conj = mConf.associate { (a) -> a to parents.getValue(a).associateWith { false }.toMutableMap() }.toMutableMap()

    var lo = 0L
    var hi = 0L
    var n = 0L
    val lastSeen = hashMapOf<String, Long>()
    val seenBefore = hashMapOf<String, Int>()
    val search = parents.getValue(parents.getValue("rx").single()).toSet()
    val cycles = mutableListOf<Long>()

    data class Pulse(val s: String, val high: Boolean, val from: String = "")
    a@ while (true) {
        n++
        val queue = queueOf(Pulse("broadcaster", false))

        while (queue.isNotEmpty()) {
            val (s, high, from) = queue.removeFirst()
            if (s == "rx" && !high) break@a
            if (high) hi++ else lo++

            if (!high) {
                if (s in search && s in lastSeen && seenBefore.getValue(s) > 1) cycles += n - lastSeen.getValue(s)
                seenBefore[s] = (seenBefore[s] ?: 0) + 1
                lastSeen[s] = n
            }

            if (search.size == cycles.size) {
                partTwo = cycles.lcm().s()
                break@a
            }

            val curr = m[s] ?: continue
            if (s == "broadcaster") {
                curr.to.forEach { queue.addLast(Pulse(it, high, s)) }
                continue
            }

            when (curr.op) {
                '%' -> if (!high) {
                    val cv = flops.getValue(s)
                    flops[s] = !cv
                    curr.to.forEach { queue.addLast(Pulse(it, !cv, s)) }
                }

                '&' -> {
                    conj.getValue(s)[from] = high
                    val cv = !conj.getValue(s).all { it.value }
                    curr.to.forEach { queue.addLast(Pulse(it, cv, s)) }
                }

                else -> error("Invalid ${curr.op}")
            }
        }

        if (n == 1000L) partOne = (lo * hi).s()
    }
}