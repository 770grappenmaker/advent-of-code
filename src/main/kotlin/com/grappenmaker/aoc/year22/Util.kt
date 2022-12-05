package com.grappenmaker.aoc.year22

fun <T> List<T>.asPair() = this[0] to this[1]

fun <T> MutableList<T>.removeLastN(n: Int) = (0 until n).map { removeLast() }.asReversed()

fun <T> Iterable<Iterable<T>>.swapOrder() = buildList {
    val iterators = this@swapOrder.map { it.iterator() }
    while (iterators.all { it.hasNext() }) {
        add(iterators.map { it.next() })
    }
}

fun List<Int>.product() = reduce { acc, curr -> acc * curr }