package com.grappenmaker.aoc.practice25.everybodycodes

import com.grappenmaker.aoc.ints
import com.grappenmaker.aoc.sumOfIndexed

fun ECSolveContext.y25day05() {
    data class Node(var left: Int? = null, var middle: Int, var right: Int? = null)
    data class Sword(val id: Int, val nodes: List<Node>) {
        val quality by lazy {
            var res = 0L

            for (n in nodes) {
                res *= 10
                res += n.middle
            }

            res
        }
    }

    fun Node.concat(): Int {
        var ans = if (left != null) left!! * 10 + middle else middle
        if (right != null) {
            ans *= 10
            ans += right!!
        }

        return ans
    }

    fun calc(nums: List<Int>): List<Node> {
        val res = mutableListOf(Node(middle = nums.first()))

        outer@ for (n in nums.drop(1)) {
            for (node in res) {
                if (n < node.middle && node.left == null) {
                    node.left = n
                    continue@outer
                }

                if (n > node.middle && node.right == null) {
                    node.right = n
                    continue@outer
                }
            }

            res += Node(middle = n)
        }

        return res
    }

    fun ECInput.parse(): MutableList<Sword> {
        val res = mutableListOf<Sword>()
        for ((idx, l) in inputLines.withIndex()) {
            res += Sword(idx + 1, calc(l.substringAfter(":").ints()))
        }

        return res
    }

    partOne = partOneInput.parse().single().quality

    val partTwoSwords = partTwoInput.parse()
    partTwoSwords.sortBy { it.quality }
    partTwo = partTwoSwords.last().quality - partTwoSwords.first().quality

    val partThreeSwords = partThreeInput.parse()
    partThreeSwords.sortWith(
        compareBy<Sword> { it.quality }.thenComparator { a, b ->
            a.nodes.zip(b.nodes).asSequence()
                .map { it.first.concat() - it.second.concat() }
                .firstOrNull { it != 0 } ?: 0
        }.thenBy { it.id }.reversed()
    )

    partThree = partThreeSwords.sumOfIndexed { idx, sword -> (idx + 1) * sword.id }
}