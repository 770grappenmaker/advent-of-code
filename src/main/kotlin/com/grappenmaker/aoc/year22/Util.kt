package com.grappenmaker.aoc.year22

import kotlin.random.Random
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

fun <T> List<T>.asPair() = this[0] to this[1]

fun <T> MutableList<T>.removeLastN(n: Int) = (0 until n).map { removeLast() }.asReversed()

fun <T> Iterable<Iterable<T>>.swapOrder() = buildList {
    val iterators = this@swapOrder.map { it.iterator() }
    while (iterators.all { it.hasNext() }) {
        add(iterators.map { it.next() })
    }
}

fun List<Int>.product() = reduce { acc, curr -> acc * curr }

fun <T> List<T>.splitHalf() = chunked(size / 2).asPair()
fun <T> List<T>.splitAt(index: Int) = subList(0, index) to subList(index, size)

fun <A, B> Pair<A, B>.swap() = second to first

inline fun <T> MutableList<T>.mapInPlace(transform: (T) -> T) = forEachIndexed { idx, t -> this[idx] = transform(t) }
inline fun <T> Array<T>.mapInPlace(transform: (T) -> T) = forEachIndexed { idx, t -> this[idx] = transform(t) }
fun MutableList<Int>.incrementAll() = mapInPlace { it + 1 }