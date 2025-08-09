package com.grappenmaker.aoc.practice25.everybodycodes

fun ECSolveContext.day06() {
    fun ECInput.solve(p1: Boolean = false): String {
        val g = mutableMapOf<String, MutableSet<String>>()

        for (l in inputLines) {
            val (f, s) = l.split(":")
            val vs = s.split(",")

            for (v in vs) g.getOrPut(v) { hashSetOf() } += f
        }

        val paths = mutableListOf<Pair<Int, String>>()

        data class Search(
            val path: String,
            val steps: Int,
            val curr: String,
            val seen: Set<String> = emptySet()
        )

        val q = ArrayDeque<Search>()
        q.addLast(Search("@", 0, "@"))

        var ans: String? = null
        var ansSteps = -1
        val seen = hashSetOf<Int>()

        while (q.isNotEmpty()) {
            val (path, steps, curr, ss) = q.removeLast()
            if (curr == "RR") {
                paths += steps to path

                if (steps == ansSteps) {
                    ansSteps = -1
                    ans = null
                }

                if (!seen.add(steps)) continue
                if (ans == null) {
                    ans = path
                    ansSteps = steps
                }

                continue
            }

            for (next in g.getOrDefault(curr, emptySet())) {
                if (next !in ss) q.addFirst(
                    Search(
                        path = (if (p1) next else next.first().toString()) + path,
                        steps = steps + 1,
                        curr = next,
                        seen = ss + next
                    )
                )
            }
        }

        return ans ?: "no sol"
    }

    partOne = partOneInput.solve(true)
    partTwo = partTwoInput.solve()
    partThree = partThreeInput.solve()
}