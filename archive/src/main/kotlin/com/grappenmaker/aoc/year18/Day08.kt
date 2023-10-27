package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet

data class LicenseNode(val children: List<LicenseNode>, val meta: List<Int>)
val LicenseNode.metaSum: Int get() = meta.sum() + children.sumOf { it.metaSum }
val LicenseNode.value: Int get() = when {
    children.isNotEmpty() -> meta.sumOf { children.getOrNull(it - 1)?.value ?: 0 }
    else -> meta.sum()
}

fun PuzzleSet.day8() = puzzle(8) {
    fun List<Int>.parse(): Pair<LicenseNode, List<Int>> {
        val (childCount, metaCount) = take(2)
        val children = mutableListOf<LicenseNode>()
        val left = generateSequence(drop(2)) {
            val (n, left) = it.parse()
            children += n
            left
        }.take(childCount + 1).last()

        return LicenseNode(children, left.take(metaCount)) to left.drop(metaCount)
    }

    val (parsed) = input.split(" ").map(String::toInt).parse()
    partOne = parsed.metaSum.s()
    partTwo = parsed.value.s()
}