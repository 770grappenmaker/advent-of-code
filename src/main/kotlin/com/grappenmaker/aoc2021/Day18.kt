package com.grappenmaker.aoc2021

fun Solution.solveDay18(): Answer {
    // Parse all numbers (AST like)
    val numbers = inputLines.map { SnailfishNumber.parse(it) }

    // Part one
    val partOne = numbers.map { it.clone() }.reduce { acc, cur -> (acc + cur).reduceNumber() }

    // Part two
    // Get permutations and calculate
    val partTwo = numbers.permutationPairs().maxOf { (a, b) -> (a.clone() + b.clone()).reduceNumber().magnitude() }
    return partOne.magnitude() to partTwo
}

private sealed class SnailfishNumber(var parent: NumberPair?) {
    companion object {
        fun parse(str: String): SnailfishNumber =
            if (str.startsWith("[")) {
                // Parse as a pair
                // Find the top level comma index
                var opening = 0
                var idx = -1

                str.forEachIndexed { i, c ->
                    when (c) {
                        '[' -> opening++
                        ']' -> opening--
                        ',' -> if (opening == 1) {
                            // If there is only one opening bracket (without closing yet),
                            // this index is the index of a top-level pair
                            idx = i
                            return@forEachIndexed
                        }
                    }
                }

                NumberPair(parse(str.take(idx).drop(1)), parse(str.drop(idx + 1).dropLast(1)))
            } else NormalInt(str.toInt())
    }

    fun getAllParents(): List<NumberPair> {
        val parents = mutableListOf<NumberPair>()
        var next: NumberPair? = parent

        while (next != null) {
            parents.add(next)
            next = next.parent
        }

        return parents
    }

    operator fun plus(other: SnailfishNumber) = NumberPair(this, other, parent)
    abstract fun magnitude(): Int

    // Deep clone
    fun clone(): SnailfishNumber = when (this) {
        is NormalInt -> NormalInt(value)
        is NumberPair -> NumberPair(left.clone(), right.clone())
    }
}

// Debugging is included for convenience
private class NumberPair(
    var left: SnailfishNumber,
    var right: SnailfishNumber,
    parent: NumberPair? = null
) : SnailfishNumber(parent), Iterable<SnailfishNumber> {
    init {
        // Set parents
        left.parent = this
        right.parent = this
    }

    fun reduceNumber(): NumberPair {
        val result = this

        while (true) {
            val explodingPair = getExplodingPair(this)
            if (explodingPair != null) {
                explode(explodingPair, explodingPair.left, explodingPair.right)
                continue
            }

            val splittingNumber = getSplittingNumber(this)
            if (splittingNumber != null) {
                splittingNumber.split()
                continue
            }

            break
        }

        return result
    }

    fun goLeft(num: SnailfishNumber): SnailfishNumber? =
        if (num == right) left else parent?.goLeft(this)

    fun goRight(num: SnailfishNumber): SnailfishNumber? =
        if (num == left) right else parent?.goRight(this)

    fun leftNormalNumber(): NormalInt? = when (left) {
        is NormalInt -> left as NormalInt
        is NumberPair -> (left as NumberPair).leftNormalNumber()
    }

    fun rightNormalNumber(): NormalInt? = when (right) {
        is NormalInt -> right as NormalInt
        is NumberPair -> (right as NumberPair).rightNormalNumber()
    }

    fun replace(from: SnailfishNumber, to: SnailfishNumber) {
        when (from) {
            left -> left = to
            right -> right = to
            else -> error("from element was not found")
        }

        to.parent = this
    }

    override fun magnitude(): Int = left.magnitude() * 3 + right.magnitude() * 2

    override fun iterator() = iterator {
        yield(left)
        yield(right)
    }

    override fun toString() = "${if (parent == null) "root" else ""}[$left,$right]"
}

private class NormalInt(val value: Int, parent: NumberPair? = null) : SnailfishNumber(parent) {
    fun split(): NumberPair {
        val newPair = NumberPair(NormalInt(value / 2), NormalInt(value - (value / 2)))
        parent?.replace(this, newPair)

        return newPair
    }

    override fun magnitude() = value
    override fun toString() = value.toString()
}

private fun getExplodingPair(num: SnailfishNumber): NumberPair? = when(num) {
    is NormalInt -> null
    is NumberPair -> {
        if (num.getAllParents().size == 4) num
        else getExplodingPair(num.left) ?: getExplodingPair(num.right)
    }
}

private fun getSplittingNumber(num: SnailfishNumber): NormalInt? = when (num) {
    is NormalInt -> if (num.value >= 10) num else null
    is NumberPair -> getSplittingNumber(num.left) ?: getSplittingNumber(num.right)
}

private fun explode(pair: NumberPair, left: SnailfishNumber, right: SnailfishNumber) {
    val parent = pair.parent ?: error("This pair must have a parent, in order to explode")

    if (left is NormalInt) {
        when (val goLeft = parent.goLeft(pair)) {
            is NormalInt -> goLeft.parent?.replace(goLeft, NormalInt(goLeft.value + left.value))
            is NumberPair -> goLeft.rightNormalNumber()?.let { it.parent?.replace(it, NormalInt(it.value + left.value)) }
            else -> Unit
        }
    }

    if (right is NormalInt) {
        when (val goRight = parent.goRight(pair)) {
            is NormalInt -> goRight.parent?.replace(goRight, NormalInt(goRight.value + right.value))
            is NumberPair -> goRight.leftNormalNumber()?.let { it.parent?.replace(it, NormalInt(it.value + right.value)) }
            else -> Unit
        }
    }

    parent.replace(pair, NormalInt(0))
}