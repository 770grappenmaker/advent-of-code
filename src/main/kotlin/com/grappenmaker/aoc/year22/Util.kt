package com.grappenmaker.aoc.year22

import java.util.*

fun <T> List<T>.asPair() = this[0] to this[1]
fun <A, B> Pair<A, B>.asList() = listOf(first, second)

fun <T> MutableList<T>.removeFirstN(n: Int) = (0 until n).map { removeFirst() }.asReversed()
fun <T> MutableList<T>.removeLastN(n: Int) = (0 until n).map { removeLast() }.asReversed()

fun <T> Iterable<Iterable<T>>.swapOrder(forceDrain: Boolean = true) = buildList {
    val iterators = this@swapOrder.map { it.iterator() }
    while (iterators.all { it.hasNext() }) {
        add(iterators.map { it.next() })
    }

    if (forceDrain && iterators.any { it.hasNext() }) error("Iterators were not drained while swapping")
}

fun List<Int>.product() = reduce { acc, curr -> acc * curr }

fun <T> List<T>.splitHalf() = chunked(size / 2).asPair()
fun <T> List<T>.splitAt(index: Int) = subList(0, index) to subList(index, size)

fun <A, B> Pair<A, B>.swap() = second to first

inline fun <T> MutableList<T>.mapInPlace(transform: (T) -> T) = forEachIndexed { idx, t -> this[idx] = transform(t) }
inline fun <T> Array<T>.mapInPlace(transform: (T) -> T) = forEachIndexed { idx, t -> this[idx] = transform(t) }
fun MutableList<Int>.incrementAll() = mapInPlace { it + 1 }

@JvmName("debugStrings")
fun List<List<String>>.debug() = joinToString("\n") { it.joinToString("") }

@JvmName("debugChars")
fun List<List<Char>>.debug() = joinToString("\n") { it.joinToString("") }

fun <T> Iterable<T>.allDistinct(): Boolean {
    val set = hashSetOf<T>()
    iterator().drain { if (!set.add(it)) return false }
    return true
}

fun <T> List<T>.rotate(amount: Int): List<T> {
    val actualShift = if (amount < 1) size + (amount % size) else amount % size
    if (actualShift == 0) return this

    val (l, r) = splitAt(size - actualShift)
    return r + l
}

@JvmName("deepenIterable")
fun <T> List<Iterable<T>>.deepen() = map { it.toList() }

@JvmName("deepenString")
fun List<String>.deepen() = map { it.toList() }

fun String.deepen() = toList()

inline fun <T> Iterator<T>.drain(use: (T) -> Unit) {
    while (hasNext()) use(next())
}

// Heap's algorithm
// See https://en.wikipedia.org/wiki/Heap%27s_algorithm
fun <T> List<T>.permutations(): List<List<T>> = buildList {
    fun recurse(list: List<T>, k: Int) {
        if (k == 1) {
            add(list.toList())
            return
        }

        for (i in 0 until k) {
            recurse(list, k - 1)
            Collections.swap(list, if (k % 2 == 0) i else 0, k - 1)
        }
    }

    recurse(this@permutations.toList(), this@permutations.size)
}

fun <T> List<T>.permutationPairs(): List<Pair<T, T>> {
    val result = mutableListOf<Pair<T, T>>()
    forEach { a -> mapTo(result) { a to it } }
    return result
}

fun String.parseRange(inclusive: Boolean = true) = split("-").map(String::toInt).asPair().toRange(inclusive)
fun Pair<Int, Int>.toRange(inclusive: Boolean = true) = IntRange(first, if (inclusive) second else second - 1)

fun <T> Iterable<T>.countContains(value: T) = count { it == value }
fun String.countContains(value: Char) = count { it == value }
fun String.countContains(value: String) = windowed(value.length).count { it == value }

fun <T> Iterable<T>.notDistinct() = buildList {
    val seen = hashSetOf<T>()
    this@notDistinct.forEach { if (!seen.add(it)) add(it) }
}

fun <T> Iterable<T>.firstNotDistinct(): T? {
    val seen = hashSetOf<T>()
    return firstOrNull { !seen.add(it) }
}