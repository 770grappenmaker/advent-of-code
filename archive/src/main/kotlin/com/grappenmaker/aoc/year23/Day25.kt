package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day25() = puzzle(day = 25) {
    // I am quite happy I solved it with a viz, this algorithm is... not great
    val total = buildMap<String, List<String>> {
        inputLines.forEach { l ->
            val (n, rest) = l.split(": ")
            val o = rest.split(" ")
            put(n, o)
            o.forEach { put(it, (get(it) ?: emptyList()) + n) }
        }
    }

    fun eval(map: Map<String, List<String>>): List<Set<String>> {
        val left = map.keys.toHashSet()
        val groups = mutableListOf<Set<String>>()

        while (left.isNotEmpty()) {
            val ff = floodFill(left.first(), neighbors = { curr -> map.getValue(curr).filter { it in left } })
            left -= ff
            groups += ff
        }

        return groups
    }

    val comb = total.keys.combinations(2).toSet()
    a@ while (true) {
        val removed = hashSetOf<Set<String>>()

        repeat(3) {
            val m = total.mapValues { it.value.toMutableList() }
            removed.map { it.toList() }.forEach { (f, t) ->
                m.getValue(f) -= t
                m.getValue(t) -= f
            }

            val freq = hashMapOf<Set<String>, Int>().withDefault { 0 }

            comb.randomSamples(10) { (f, t) ->
                val res = dijkstra(f, isEnd = { it == t }, neighbors = { m.getValue(it) }, findCost = { 1 })
                res?.path?.zipWithNext()?.forEach { (f, t) ->
                    val k = setOf(f, t)
                    freq[k] = freq.getValue(k) + res.path.size
                }
            }

            removed += freq.toList().filter { (k) ->
                k.none { f -> m.getValue(f).any { t -> m.getValue(t).singleOrNull() == f } }
            }.maxBy { it.second }.first
        }

        val m = total.mapValues { it.value.toMutableList() }
        removed.map { it.toList() }.forEach { (f, t) ->
            m.getValue(f) -= t
            m.getValue(t) -= f
        }

        val (big, small) = eval(m).partition { it.size > 100 }
        if (big.size != 2) continue@a

        val (a, b) = big.map { it.toHashSet() }
        for (s in small.flatten()) {
            val seen = hashSetOf(s)
            val queue = queueOf(s)

            while (queue.isNotEmpty()) when (val next = queue.removeLast()) {
                in a -> {
                    a += s
                    continue
                }
                in b -> {
                    b += s
                    continue
                }
                else -> total.getValue(next).forEach { if (seen.add(it)) queue.addFirst(it) }
            }
        }

        partOne = (a.size * b.size).s()
        break
    }
}