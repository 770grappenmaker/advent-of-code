package com.grappenmaker.aoc.practice25.everybodycodes

fun ECSolveContext.day11() {
    fun ECInput.parse(): Map<String, Map<String, Int>> {
        val rules = hashMapOf<String, Map<String, Int>>()

        for (l in inputLines) {
            val (f, s) = l.split(":")
            rules[f] = s.split(",").groupingBy { it }.eachCount()
        }

        return rules
    }

    fun simul(rules: Map<String, Map<String, Int>>, start: String, rounds: Int): Long {
        var pop = hashMapOf(start to 1L)

        fun update() {
            val next = hashMapOf<String, Long>()

            for ((k, v) in rules) {
                val have = pop.getOrDefault(k, 0L)

                for ((nk, nv) in v) {
                    next[nk] = next.getOrDefault(nk, 0L) + have * nv
                }
            }

            pop = next
        }

        repeat(rounds) { update() }
        return pop.values.sum()
    }

    fun ECInput.solveEarly(start: String, rounds: Int) = simul(parse(), start, rounds)

    partOne = partOneInput.solveEarly("A", 4)
    partTwo = partTwoInput.solveEarly("Z", 10)

    val rules = partThreeInput.parse()
    val nums = rules.keys.map { simul(rules, it, 20) }.sorted()
    partThree = nums.last() - nums.first()
}