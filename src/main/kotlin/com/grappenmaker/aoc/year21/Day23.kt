package com.grappenmaker.aoc.year21

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.Direction.*
import java.util.*

fun PuzzleSet.day23() = puzzle(day = 23) {
    fun List<String>.parse() = map { it.padEnd(inputLines.first().length, ' ') }.asCharGrid()

    fun solve(grid: Grid<Char>): String {
        fun Point.bucket() = if (x in 3..9 && y >= 2) (x - 3) / 2 else null

        data class Amphipod(
            val position: Point,
            val type: Int,
            val cost: Int = 10.pow(type),
        )

        fun Amphipod.atBucket(bucket: Int) = position.bucket() == bucket

        // First time ever overwriting equals and hashcode in kotlin
        data class State(
            val pods: Set<Amphipod>,
            val lastCost: Int = 0,
            val lastMoved: Amphipod = pods.first(),
            val done: Set<Int> = emptySet()
        ) {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as State

                if (pods != other.pods) return false
                return lastMoved == other.lastMoved
            }

            override fun hashCode(): Int {
                var result = pods.hashCode()
                result = 31 * result + lastMoved.hashCode()
                return result
            }
        }

        val initial = grid.findPoints { it in 'A'..'D' }.map { Amphipod(it, grid[it] - 'A') }
        val needsInBucket = initial.count { it.type == 0 }

        fun adaptedDijkstra(): DijkstraPath<State> = with(grid) {
            val start = State(initial.toSet())
            val seen = hashSetOf(start)
            val queue = PriorityQueue(compareBy<Pair<State, Int>> { (_, c) -> c })
                .also { it.add(start to 0) }

            queue.drain { (state, currentCost) ->
                if (state.done.size == 4) return DijkstraPath(
                    end = state,
                    path = emptyList(),
                    cost = currentCost
                )

                fun findAtBucket(bucket: Int) = state.pods.find { it.atBucket(bucket) }
                fun findAllAtBucket(bucket: Int) = state.pods.filter { it.atBucket(bucket) }

                fun Point.valid() = grid[this] != '#' && state.pods.none { it.position == this }
                fun Point.poss() = adjacentSides().filter { it.valid() }

                state.pods.flatMap { moving ->
                    val bucket = moving.position.bucket()
                    if (bucket in state.done) return@flatMap emptyList()

                    val update = { new: Point, steps: Int ->
                        val updated = moving.copy(position = new)
                        State(
                            pods = state.pods - moving + updated,
                            lastCost = moving.cost * steps,
                            lastMoved = updated,
                            done = state.done,
                        )
                    }

                    val otherPod = findAtBucket(moving.type)
                    val approves = otherPod == null || otherPod.type == moving.type

                    val pathToEnd = bfsDistance(
                        start = moving.position,
                        isEnd = { it.bucket() == moving.type },
                        neighbors = { if (approves) it.poss() else emptyList() }
                    )

                    val originalEnd = pathToEnd.original.end
                    val pathEnd = originalEnd?.let {
                        var curr = it
                        while (curr.valid()) curr += DOWN
                        curr - DOWN
                    }

                    when {
                        pathEnd != null && pathToEnd.dist != 0 -> listOf(
                            update(pathEnd, pathToEnd.dist + (pathEnd manhattanDistanceTo originalEnd)).let {
                                val thereBefore = findAllAtBucket(moving.type).size
                                if (thereBefore == needsInBucket - 1) it.copy(done = it.done + moving.type) else it
                            }
                        )

                        bucket != null && grid.column(moving.position.x)
                            .none { it.y < moving.position.y && state.pods.any { p -> p.position == it } } -> {
                            var curr = moving.position
                            while (grid[curr] != '#') curr += UP

                            val endedAt = curr - UP
                            listOf(LEFT, RIGHT)
                                .map { endedAt + it }
                                .filter { it.valid() }
                                .map { update(it, curr manhattanDistanceTo moving.position) }
                        }

                        moving == state.lastMoved -> {
                            listOf(LEFT, RIGHT).mapNotNull { d ->
                                val nextIntended = moving.position + d
                                if (!nextIntended.valid()) return@mapNotNull null
                                nextIntended
                                    .let { if ((it + DOWN).bucket() != null) it + d else it }
                                    .takeIf { it.valid() }
                            }.map { update(it, moving.position manhattanDistanceTo it) }
                        }

                        else -> emptyList()
                    }
                }.forEach { if (seen.add(it)) queue.offer(it to currentCost + it.lastCost) }
            }

            error("Should never happen")
        }

        return adaptedDijkstra().cost.s()
    }

    partOne = solve(inputLines.parse())

    val transformed = inputLines.toMutableList()
    transformed.addAll(3, """
        |  #D#C#B#A#
        |  #D#B#A#C#
    """.trimMargin().lines())

    partTwo = solve(transformed.parse())
}