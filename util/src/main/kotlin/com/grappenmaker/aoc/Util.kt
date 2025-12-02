@file:OptIn(ExperimentalTypeInference::class)
@file:Suppress("unused")

package com.grappenmaker.aoc

import com.grappenmaker.aoc.Direction.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.math.BigInteger
import java.util.*
import kotlin.concurrent.thread
import kotlin.experimental.ExperimentalTypeInference
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun Iterable<Int>.diff() = reduce { acc, curr -> acc - curr }

fun <T> List<T>.asPair() = this[0] to this[1]
fun <T> Pair<T, T>.asList() = listOf(first, second)
inline fun <F, T> Pair<F, F>.mapBoth(block: (F) -> T) = block(first) to block(second)
inline fun <F, S, T> Pair<F, S>.mapFirst(block: (F) -> T) = block(first) to second
inline fun <F, S, T> Pair<F, S>.mapSecond(block: (S) -> T) = first to block(second)

fun <T> MutableList<T>.removeFirstN(n: Int) = (0..<n).map { removeFirst() }
fun <T> MutableList<T>.removeLastN(n: Int) = (0..<n).map { removeLast() }.asReversed()
fun <T> MutableList<T>.removeNAt(n: Int, atIdx: Int) = (0..<n).map { removeAt(atIdx) }
fun <T> MutableList<T>.remove(range: IntRange) = range.map { removeAt(range.first) }

// compat
fun <T> Iterable<Iterable<T>>.swapOrder(forceDrain: Boolean = true) = transpose(forceDrain)
fun <T> Iterable<Iterable<T>>.transpose(forceDrain: Boolean = true) = buildList {
    val iterators = this@transpose.map { it.iterator() }
    while (iterators.all { it.hasNext() }) add(iterators.map { it.next() })
    if (forceDrain && iterators.any { it.hasNext() }) error("Iterators were not drained while swapping")
}

fun <T> Sequence<Iterable<T>>.transpose(forceDrain: Boolean = true) = sequence {
    val iterators = this@transpose.map { it.iterator() }.toList()
    while (iterators.all { it.hasNext() }) yield(iterators.map { it.next() })
    if (forceDrain && iterators.any { it.hasNext() }) error("Iterators were not drained while swapping")
}

@JvmName("swapOrderSequences")
fun <T> Sequence<Sequence<T>>.transpose(forceDrain: Boolean = true) =
    map { it.asIterable() }.transpose(forceDrain).map { it.asSequence() }

fun Iterable<Int>.product() = reduce { acc, curr -> acc * curr }

@JvmName("productLongs")
fun Iterable<Long>.product() = reduce { acc, curr -> acc * curr }

@JvmName("productBigs")
fun Iterable<BigInteger>.product() = reduce { acc, curr -> acc * curr }

fun <T> Collection<T>.splitHalf() = chunked(size / 2).asPair()
fun <T> List<T>.splitAt(index: Int) = subList(0, index) to subList(index, size)
fun <T> List<T>.splitAtExcluding(index: Int) = subList(0, index) to subList(index, size).drop(1)

fun String.splitHalf() = chunked(length / 2).asPair()
fun String.splitAt(index: Int) = substring(0, index) to substring(index, length)
fun String.splitAtExcluding(index: Int) = substring(0, index) to substring(index, length).drop(1)

fun <A, B> Pair<A, B>.swap() = second to first

inline fun <T> MutableList<T>.mapInPlace(transform: (T) -> T) = forEachIndexed { idx, t -> this[idx] = transform(t) }
inline fun <T> MutableList<T>.mapInPlaceIndexed(transform: (idx: Int, T) -> T) =
    forEachIndexed { idx, t -> this[idx] = transform(idx, t) }

inline fun <T> Array<T>.mapInPlace(transform: (T) -> T) = forEachIndexed { idx, t -> this[idx] = transform(t) }
fun MutableList<Int>.incrementAll() = mapInPlace { it + 1 }
fun Array<Int>.incrementAll() = mapInPlace { it + 1 }

@JvmName("debugStrings")
fun List<List<String>>.debug() = joinToString("\n") { it.joinToString("") }

@JvmName("debugChars")
fun List<List<Char>>.debug() = joinToString("\n") { it.joinToString("") }

fun <T> Iterable<T>.allDistinct(): Boolean {
    val set = hashSetOf<T>()
    iterator().drain { if (!set.add(it)) return false }
    return true
}

fun <T> Sequence<T>.allDistinct() = asIterable().allDistinct()

fun <T, M> Iterable<T>.allDistinctBy(map: (T) -> M): Boolean {
    val set = hashSetOf<M>()
    iterator().drain { if (!set.add(map(it))) return false }
    return true
}

fun <T, M> Sequence<T>.allDistinctBy(map: (T) -> M) = asIterable().allDistinctBy(map)

fun <T> Iterable<T>.allIdentical(): Boolean {
    val iter = iterator()
    if (!iter.hasNext()) return true

    val first = iter.next()
    iter.drain { if (it != first) return false }
    return true
}

fun <T> Sequence<T>.allIdentical() = asIterable().allIdentical()

fun <T, M> Iterable<T>.allIdenticalBy(map: (T) -> M): Boolean {
    val iter = iterator()
    if (!iter.hasNext()) return true

    val first = map(iter.next())
    iter.drain { if (map(it) != first) return false }
    return true
}

fun <T, M> Sequence<T>.allIdenticalBy(map: (T) -> M) = asIterable().allIdenticalBy(map)

fun <T> List<T>.rotate(amount: Int): List<T> {
    val actualShift = amount.mod(size)
    if (actualShift == 0) return this

    val (l, r) = splitAt(size - actualShift)
    return r + l
}

// Optimization
fun <T> MutableList<T>.rotateInPlace(amount: Int) {
    val actualAmount = amount % size
    return when {
        actualAmount == 0 -> Unit
        actualAmount < 0 -> repeat(-actualAmount) { add(removeFirst()) }
        else -> repeat(actualAmount) { add(0, removeLast()) }
    }
}

@JvmName("deepenIterable")
fun <T> Iterable<Iterable<T>>.deepen() = map { it.toList() }

@JvmName("deepenString")
fun Iterable<String>.deepen() = map { it.toList() }

fun String.deepen() = toList()

inline fun <T> Iterator<T>.drain(use: (T) -> Unit) {
    while (hasNext()) use(next())
}

// Heap's algorithm
// See https://en.wikipedia.org/wiki/Heap%27s_algorithm
fun <T> Collection<T>.permutationsHeaps(): List<List<T>> = buildList {
    fun recurse(list: MutableList<T>, k: Int) {
        if (k == 1) {
            add(list.toList())
            return
        }

        for (i in 0..<k) {
            recurse(list, k - 1)
            Collections.swap(list, if (k % 2 == 0) i else 0, k - 1)
        }
    }

    recurse(this@permutationsHeaps.toMutableList(), this@permutationsHeaps.size)
}

fun <T> Iterable<T>.permutations() = toList().permutations()
fun <T> Iterable<T>.permutations(r: Int) = toList().permutations(r)

// Inspired by:
// https://docs.python.org/3/library/itertools.html#itertools.permutations
fun <T> List<T>.permutations(r: Int = size) = sequence {
    if (r > size || isEmpty()) return@sequence

    val ind = indices.toMutableList()
    val cyc = (size downTo size - r).toMutableList()
    yield(take(r))

    while (true) {
        for (i in r - 1 downTo 0) {
            if (--cyc[i] == 0) {
                ind.add(ind.removeAt(i))
                cyc[i] = size - i
            } else {
                Collections.swap(ind, i, size - cyc[i])
                yield(slice(ind.take(r)))
                break
            }

            if (i == 0) return@sequence
        }
    }
}

//fun <T> List<T>.permutations(r: Int = size): List<List<T>> = when {
//    isEmpty() || r == 0 -> emptyList()
//    r == 1 -> map { listOf(it) }
//    else -> flatMap { c -> (this - c).permutations(r - 1).map { it + c } }
//}

fun <T> Iterable<T>.combinations() = toList().combinations()
fun <T> Iterable<T>.combinations(r: Int) = toList().combinations(r)
fun <T> List<T>.combinations(r: Int = size) =
    indices.permutations(r).filter { it.sorted() == it }.map { p -> p.map { this[it] } }

fun <T> Iterable<T>.powerSet(filter: (Set<T>) -> Boolean = { true }): Set<Set<T>> {
    val iter = iterator()
    if (!iter.hasNext()) return setOf(emptySet())

    val next = iter.next()
    val recurse = iter.asSequence().asIterable().powerSet(filter)
    return (recurse + recurse.map { it + next }).filterTo(hashSetOf(), filter)
}

@Deprecated("Includes (x, x) for every x in iterable", ReplaceWith("permPairsExclusive()"))
fun <T> Iterable<T>.permPairs(): List<Pair<T, T>> {
    val result = mutableListOf<Pair<T, T>>()
    forEach { a -> mapTo(result) { a to it } }
    return result
}

fun <T> Iterable<T>.permPairsExclusive(): List<Pair<T, T>> {
    val result = mutableListOf<Pair<T, T>>()
    forEachIndexed { idx, v -> filterIndexed { idx2, _ -> idx != idx2 }.mapTo(result) { v to it } }
    return result
}

fun <T> Iterable<T>.permPairsExclusiveSeq() = sequence {
    for ((idxa, va) in this@permPairsExclusiveSeq.withIndex()) {
        for ((idxb, vb) in this@permPairsExclusiveSeq.withIndex()) {
            if (idxa == idxb) continue
            yield(va to vb)
        }
    }
}

inline fun <T, N> Iterable<T>.permPairsExclusive(transform: (T, T) -> N): List<N> {
    val result = mutableListOf<N>()
    forEachIndexed { idx, v -> filterIndexed { idx2, _ -> idx != idx2 }.mapTo(result) { transform(v, it) } }
    return result
}

fun String.parseRange(inclusive: Boolean = true) = split("-").map(String::toInt).asPair().toRange(inclusive)
fun Pair<Int, Int>.toRange(inclusive: Boolean = true) = IntRange(first, if (inclusive) second else second - 1)
fun Pair<Long, Long>.toRange(inclusive: Boolean = true) = LongRange(first, if (inclusive) second else second - 1)

fun <T> Iterable<T>.countContains(value: T) = count { it == value }
fun String.countContains(value: Char) = count { it == value }
fun String.countContains(value: String) = windowed(value.length).count { it == value }

fun <T> Iterable<T>.notDistinct() = buildList {
    val seen = hashSetOf<T>()
    this@notDistinct.forEach { if (!seen.add(it)) add(it) }
}

fun <T> Iterable<T>.firstNotDistinct(): T {
    val seen = hashSetOf<T>()
    return first { !seen.add(it) }
}

fun <T> Iterable<T>.findNotDistinct(): T? {
    val seen = hashSetOf<T>()
    return find { !seen.add(it) }
}

fun <T> Iterable<T>.untilNotDistinct(): List<T> {
    val seen = hashSetOf<T>()
    return takeWhile { seen.add(it) }
}

fun <T> Iterable<T>.hasDuplicate(): Boolean {
    val seen = hashSetOf<T>()
    return any { !seen.add(it) }
}

inline fun <T, V> Iterable<T>.firstNotDistinctBy(block: (T) -> V): T {
    val seen = hashSetOf<V>()
    return first { !seen.add(block(it)) }
}

inline fun <T, V> Iterable<T>.findNotDistinctBy(block: (T) -> V): T? {
    val seen = hashSetOf<V>()
    return find { !seen.add(block(it)) }
}

inline fun <T, V> Iterable<T>.untilNotDistinctBy(block: (T) -> V): List<T> {
    val seen = hashSetOf<V>()
    return takeWhile { seen.add(block(it)) }
}

inline fun <T, V> Iterable<T>.hasDuplicateBy(block: (T) -> V): Boolean {
    val seen = hashSetOf<V>()
    return any { !seen.add(block(it)) }
}

fun <T> Sequence<T>.firstNotDistinct() = asIterable().firstNotDistinct()
fun <T> Sequence<T>.untilNotDistinct() = asIterable().untilNotDistinct()
fun <T> Sequence<T>.hasDuplicate() = asIterable().hasDuplicate()
inline fun <T, V> Sequence<T>.firstNotDistinctBy(block: (T) -> V) = asIterable().firstNotDistinctBy(block)
inline fun <T, V> Sequence<T>.untilNotDistinctBy(block: (T) -> V) = asIterable().untilNotDistinctBy(block)
inline fun <T, V> Sequence<T>.hasDuplicateBy(block: (T) -> V) = asIterable().hasDuplicateBy(block)

inline fun <T> Iterable<T>.findIndexOf(cond: (T) -> Boolean): Int? {
    for ((i, e) in iterator().withIndex()) if (cond(e)) return i
    return null
}

inline fun <T> Iterable<T>.partitionIndexed(block: (idx: Int, T) -> Boolean): Pair<List<T>, List<T>> {
    val l = mutableListOf<T>()
    val r = mutableListOf<T>()
    forEachIndexed { idx, el -> (if (block(idx, el)) l else r).add(el) }

    return l to r
}

fun <T> Iterable<T>.deinterlace() = partitionIndexed { idx, _ -> idx % 2 == 0 }
fun <T> Sequence<T>.repeatInfinitely() = sequence { while (true) yieldAll(this@repeatInfinitely) }

fun gcd(a: Int, b: Int): Int = if (a == 0) b else gcd(b % a, a)
fun gcd(a: Long, b: Long): Long = if (a == 0L) b else gcd(b % a, a)
fun gcd(a: BigInteger, b: BigInteger): BigInteger = if (a == BigInteger.ZERO) b else gcd(b % a, a)
fun lcm(a: Int, b: Int): Int = (a * b) / gcd(a, b)
fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)
fun lcm(a: BigInteger, b: BigInteger): BigInteger = (a * b) / gcd(a, b)

fun gcdOf(vararg i: Int) = i.reduce { acc, curr -> gcd(acc, curr) }
fun gcdOf(vararg i: Long) = i.reduce { acc, curr -> gcd(acc, curr) }
fun gcdOf(vararg i: BigInteger) = i.reduce { acc, curr -> gcd(acc, curr) }
fun lcmOf(vararg i: Int) = i.reduce { acc, curr -> lcm(acc, curr) }
fun lcmOf(vararg i: Long) = i.reduce { acc, curr -> lcm(acc, curr) }
fun lcmOf(vararg i: BigInteger) = i.reduce { acc, curr -> lcm(acc, curr) }

fun Iterable<Int>.gcd() = reduce { a, b -> gcd(a, b) }

@JvmName("gcdLongs")
fun Iterable<Long>.gcd() = reduce { a, b -> gcd(a, b) }

@JvmName("gcdBigs")
fun Iterable<BigInteger>.gcd() = reduce { a, b -> gcd(a, b) }

fun Iterable<Int>.lcm() = reduce { a, b -> lcm(a, b) }

@JvmName("lcmLongs")
fun Iterable<Long>.lcm() = reduce { a, b -> lcm(a, b) }

@JvmName("lcmBigs")
fun Iterable<BigInteger>.lcm() = reduce { a, b -> lcm(a, b) }

fun Int.isCoprimeWith(other: Int) = gcd(this, other) == 1
fun Long.isCoprimeWith(other: Long) = gcd(this, other) == 1L
fun BigInteger.isCoprimeWith(other: BigInteger) = gcd(this, other) == BigInteger.ONE

fun rangeDirection(a: Int, b: Int) = if (b < a) a downTo b else a..b

fun Char.parseDirection() = when (this) {
    'U' -> UP
    'D' -> DOWN
    'L' -> LEFT
    'R' -> RIGHT
    else -> error("Invalid direction marker $this")
}

inline fun <T> Iterable<T>.takeUntil(cond: (T) -> Boolean): MutableList<T> {
    val list = mutableListOf<T>()
    forEach { el ->
        list.add(el)
        if (cond(el)) return list
    }

    return list
}

// same as takeUntilGraceful except the last item might be iterated over, even if it will never be yielded
// this is a result of an old silly implementation
fun <T> Sequence<T>.takeUntil(cond: (T) -> Boolean): Sequence<T> {
    var can = true
    return takeWhile {
        val old = can
        can = cond(it)
        old
    }
}

// takes all elements until cond becomes false
// this means that whenever cond becomes false, the sequence will no longer be iterated over
// this also means that the first item, if it exists, will always be yielded, because we do not evaluate the predicate
// for it
fun <T> Sequence<T>.takeUntilGraceful(cond: (T) -> Boolean): Sequence<T> {
    val iter = iterator()
    if (!iter.hasNext()) return emptySequence()

    return sequence {
        var last: T
        do {
            last = iter.next()
            yield(last)
        } while (cond(last) && iter.hasNext())
    }
}

fun String.splitInts() = "-?\\d+".toRegex().findAll(this).map { it.value.toInt() }.toList()
fun String.splitLongs() = "-?\\d+".toRegex().findAll(this).map { it.value.toLong() }.toList()
fun String.splitIntsPos() = "\\d+".toRegex().findAll(this).map { it.value.toInt() }.toList()
fun String.ints() = splitInts() // shortcut
fun String.longs() = splitLongs() // shortcut
fun String.intsPos() = splitIntsPos() // shortcut

fun String.onceSplit(at: String, default: String = this) = substringBefore(at, default) to substringAfter(at, default)

inline fun <T> T.applyN(n: Int, block: (T) -> T): T {
    var curr = this
    repeat(n) { curr = block(curr) }
    return curr
}

fun String.doubleLineSequence() = splitToSequence("\r\r\n\n", "\n\n", "\r\r")
fun String.doubleLines() = doubleLineSequence().toList()

fun IntRange.sum() = if (start > endInclusive) 0 else endInclusive * (endInclusive + 1) / 2 - start * (start -  1) / 2

fun <T> Iterable<T>.frequencies() = groupingBy { it }.eachCount()
fun <T> Iterable<T>.mostFrequent() = frequencies().maxBy { it.value }.key
fun <T> Iterable<T>.leastFrequent() = frequencies().minBy { it.value }.key

inline fun <T> Iterable<T>.allIndexed(block: (idx: Int, T) -> Boolean): Boolean {
    forEachIndexed { idx, v -> if (!block(idx, v)) return false }
    return true
}

@JvmName("patternRepeatingInts")
inline fun <T : Any> T.patternRepeating(totalIterations: Int, returnAfterFirst: Boolean = true, next: (T) -> T) =
    patternRepeating(totalIterations.toLong(), returnAfterFirst, next)

inline fun <T : Any> T.patternRepeating(totalIterations: Long, returnAfterFirst: Boolean = true, next: (T) -> T): T {
    val seen = mutableMapOf<T, Long>()

    var iter = 0L
    var left = totalIterations
    var curr = this

    while (left > 0) {
        if (curr in seen) {
            val steps = iter - seen.getValue(curr)
            if (steps != 0L) {
                iter += left / steps * steps
                left %= steps

                if (returnAfterFirst) {
                    val possible = seen.toList().find { (_, i) -> i == (totalIterations % steps) + steps }?.first
                    if (possible != null) return possible
                }
            }
        }

        seen[curr] = iter
        curr = next(curr)

        iter++
        left--
    }

    return curr
}

fun Char.toDirectionOrNull() = when (this) {
    '^' -> UP
    'v' -> DOWN
    '>' -> RIGHT
    '<' -> LEFT
    else -> null
}

fun Char.toDirection() = toDirectionOrNull() ?: error("Impossible")

fun <T> MutableList<T>.delegate(index: Int) = object : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = get(index)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        set(index, value)
    }
}

fun <T> List<T>.delegate(index: Int) = ReadOnlyProperty<Any?, T> { _, _ -> get(index) }

fun Iterable<Boolean>.countTrue() = count { it }
fun Iterable<Boolean>.countFalse() = count { !it }

fun Int.toDigits(base: Int = 10) = buildList {
    var curr = this@toDigits
    while (curr != 0) {
        add(0, curr % base)
        curr /= base
    }
}

fun Long.toDigits(base: Long = 10L) = buildList {
    var curr = this@toDigits
    while (curr != 0L) {
        add(0, curr % base)
        curr /= base
    }
}

fun IntRange.overlaps(other: IntRange) = first <= other.last && other.first >= last
fun LongRange.overlaps(other: LongRange) = first <= other.last && other.first >= last

fun LongRange.overlap(other: LongRange) =
    (maxOf(first, other.first)..minOf(last, other.last)).takeIf { !it.isEmpty() }

// returns everything in this range but not in other
operator fun LongRange.minus(other: LongRange): List<LongRange> = buildList {
    (first..minOf(last, maxOf(first, other.first) - 1)).let { if (!it.isEmpty()) add(it) }
    (maxOf(minOf(last, other.last) + 1, first)..last).let { if (!it.isEmpty()) add(it) }
}

// returns everything in this range but not in other
operator fun IntRange.minus(other: IntRange): List<IntRange> = buildList {
    (first..minOf(last, maxOf(first, other.first) - 1)).let { if (!it.isEmpty()) add(it) }
    (maxOf(minOf(last, other.last) + 1, first)..last).let { if (!it.isEmpty()) add(it) }
}

fun IntRange.overlap(other: IntRange) =
    (maxOf(first, other.first)..minOf(last, other.last)).takeIf { !it.isEmpty() }

fun IntRange.width() = last - first + 1
fun LongRange.width() = last - first + 1

fun Int.pow(n: Int): Int = when {
    n == 0 -> 1
    n % 2 == 0 -> (this * this).pow(n / 2)
    else -> this * pow(n - 1)
}

fun Int.powMod(n: Int, mod: Int): Int = when {
    n == 0 -> 1
    n % 2 == 0 -> (this % mod * this % mod).pow(n / 2) % mod
    else -> this % mod * pow(n - 1) % mod
}

fun Long.pow(n: Long): Long = when {
    n == 0L -> 1L
    n % 2L == 0L -> (this * this).pow(n / 2L)
    else -> this * pow(n - 1L)
}

fun Long.powMod(n: Long, mod: Long): Long = when {
    n == 0L -> 1L
    n % 2L == 0L -> (this % mod * this % mod).pow(n / 2L) % mod
    else -> this % mod * pow(n - 1L) % mod
}

fun <T> Iterable<T>.repeat(n: Int): List<T> {
    val result = mutableListOf<T>()
    repeat(n) { result += this }
    return result
}

fun <T> Sequence<T>.nth(n: Int) = take(n + 1).last()

fun Iterable<IntRange>.simplify() = simplifyRanges(
    first = IntRange::first,
    last = IntRange::last,
    builder = Int::rangeTo
)

@JvmName("simplifyLongs")
fun Iterable<LongRange>.simplify() = simplifyRanges(
    first = LongRange::first,
    last = LongRange::last,
    builder = Long::rangeTo
)

private inline fun <T, V : Comparable<V>> Iterable<T>.simplifyRanges(
    crossinline first: T.() -> V,
    last: T.() -> V,
    builder: (V, V) -> T
): List<T> {
    val iter = sortedBy(first).iterator()
    if (!iter.hasNext()) return emptyList()

    val result = mutableListOf(iter.next())
    iter.drain { curr ->
        val l = result.last()
        val ll = l.last()
        when {
            ll < curr.first() -> result += curr
            else -> result[result.size - 1] = builder(l.first(), maxOf(ll, curr.last()))
        }
    }

    return result
}

fun Int.factorial(): Int {
    var n = 1
    for (i in 2..this) n *= i
    return n
}

fun String.digits() = mapNotNull { it.digitToIntOrNull() }

fun <K : Any, V> Map<K?, V>.filterKeysNotNull(): Map<K, V> {
    val result = hashMapOf<K, V>()
    forEach { (k, v) -> if (k != null) result[k] = v }
    return result
}

fun <K, V : Any> Map<K, V?>.filterValuesNotNull(): Map<K, V> {
    val result = hashMapOf<K, V>()
    forEach { (k, v) -> if (v != null) result[k] = v }
    return result
}

inline fun <T, V> Iterable<T>.singleNotNullOf(block: (T) -> V?): V {
    var result: V? = null

    for (el in this) {
        val curr = block(el)
        if (curr != null) {
            if (result != null) error("Collection contains more than one matching element")
            result = curr
        }
    }

    return result ?: error("Collection contains no element matching the predicate")
}

inline fun <T, V> Iterable<T>.singleNotNullOfOrNull(block: (T) -> V?): V? {
    var result: V? = null

    for (el in this) {
        val curr = block(el)
        if (curr != null) {
            if (result != null) return null
            result = curr
        }
    }

    return result
}

operator fun <T> List<T>.component6() = this[5]
operator fun <T> List<T>.component7() = this[6]
operator fun <T> List<T>.component8() = this[7]
operator fun <T> List<T>.component9() = this[8]
operator fun <T> List<T>.component10() = this[9]

operator fun String.component1() = this[0]
operator fun String.component2() = this[1]
operator fun String.component3() = this[2]
operator fun String.component4() = this[3]
operator fun String.component5() = this[4]
operator fun String.component6() = this[5]
operator fun String.component7() = this[6]
operator fun String.component8() = this[7]
operator fun String.component9() = this[8]
operator fun String.component10() = this[9]

data class EuclideanResult(val d: Int, val s: Int = 0, val t: Int = 1)
data class EuclideanResultL(val d: Long, val s: Long = 0, val t: Long = 1)

fun euclideanAlgo(a: Int, b: Int): EuclideanResult = if (a == 0) EuclideanResult(b) else {
    val (d, s, t) = euclideanAlgo(b % a, a)
    EuclideanResult(d, t - (b / a) * s, s)
}

fun fullEuclideanAlgo(a: Int, b: Int): List<EuclideanResult> =
    generateSequence(EuclideanResult(a, 1, 0) to EuclideanResult(b)) { (u, v) ->
        val (r1, s1, t1) = u
        val (r2, s2, t2) = v
        if (r2 == 0) return@generateSequence v to v

        val d = r1 / r2
        v to EuclideanResult(r1 % r2, s1 - d * s2, t1 - d * t2)
    }.takeUntil { (_, b) -> b.d != 0 }.map { (a) -> a }.toList()

fun euclideanAlgo(a: Long, b: Long): EuclideanResultL = if (a == 0L) EuclideanResultL(b) else {
    val (d, s, t) = euclideanAlgo(b % a, a)
    EuclideanResultL(d, t - (b / a) * s, s)
}

fun fullEuclideanAlgo(a: Long, b: Long): List<EuclideanResultL> =
    generateSequence(EuclideanResultL(a, 1L, 0L) to EuclideanResultL(b)) { (u, v) ->
        val (r1, s1, t1) = u
        val (r2, s2, t2) = v
        if (r2 == 0L) return@generateSequence v to v

        val d = r1 / r2
        v to EuclideanResultL(r1 % r2, s1 - d * s2, t1 - d * t2)
    }.takeUntil { (_, b) -> b.d != 0L }.map { (a) -> a }.toList()

fun Int.modInverse(mod: Int): Int {
    val (d, s, _) = euclideanAlgo(this, mod)
    if (d != 1) error("No (real?) mod inverse for x $this^-1 % $mod = 1")
    return s.mod(mod)
}

fun Long.modInverse(mod: Long): Long {
    val (d, s, _) = euclideanAlgo(this, mod)
    if (d != 1L) error("No (real?) mod inverse for x $this^-1 % $mod = 1")
    return s.mod(mod)
}

fun chineseRemainder(nums: List<Int>, rem: List<Int>): Int =
    chineseRemainder(nums.map { it.toLong() }, rem.map { it.toLong() }).toInt()

fun chineseRemainder(nums: List<Long>, rem: List<Long>): Long {
    require(nums.size == rem.size) { "sizes of nums and rem do not match!" }
    require(nums.zipWithNext().all { (a, b) -> a.isCoprimeWith(b) }) { "pairwise coprimality violated!" }

    val prod = rem.product()
    return rem.indices.sumOf { i ->
        val pm1 = prod / rem[i]
        nums[i] * pm1 * pm1.modInverse(rem[i])
    }.mod(prod)
}

suspend fun <T, R> Iterable<T>.parallelize(block: (T) -> R) = coroutineScope { map { async { block(it) } }.awaitAll() }

fun <T, R> List<T>.parallelizeThreads(
    n: Int = Runtime.getRuntime().availableProcessors(),
    block: (T) -> R
): List<R> {
    val division = indices.chunked(size / n + size % n)
    val results = mutableListOf<Pair<Int, List<R>>>()

    val threads = (0..<n).map { idx ->
        thread(isDaemon = true) { results += idx to division[idx].map { block(this[it]) } }
    }

    threads.forEach { it.join() }

    return results.sortedBy { (a) -> a }.flatMap { (_, b) -> b }
}

fun <T> Iterable<Iterable<T>>.joinToList(el: T) = joinToList(el) { it }
inline fun <T, R> Iterable<Iterable<T>>.joinToList(el: R, transform: (T) -> R): List<R> {
    val result = mutableListOf<R>()
    var seen = false

    for (e in this) {
        if (seen) result += el
        result += e.map(transform)
        seen = true
    }

    return result
}

// repeats = 1 -> normal list
// repeats = 0 -> empty list
// repeats = -1 -> circular list (even negative indices)
// repeats = n (n > 1) -> repeated list
class PseudoList<T>(
    private val repeats: Int = 1,
    private val backing: List<T>,
    maxSize: Int = if (repeats == -1) Int.MAX_VALUE else backing.size * repeats
) : List<T> by backing {
    init {
        require(repeats > -1) { "repeats value $repeats invalid!" }
    }

    override val size = maxSize
    override fun get(index: Int) = when {
        repeats == -1 -> backing[index.mod(backing.size)]
        index >= size -> throw IndexOutOfBoundsException("$index for size $size")
        else -> backing[index % backing.size]
    }

    override fun isEmpty() = repeats == 0 || backing.isEmpty()
    override fun iterator() = listIterator()

    private inner class Iter(private var idx: Int = 0) : ListIterator<T> {
        override fun hasNext() = repeats == -1 || idx < size
        override fun hasPrevious() = repeats == -1 || idx > 0
        override fun next() = get(idx++)
        override fun nextIndex() = if (repeats == -1) idx + 1 else (idx + 1).coerceAtMost(size)
        override fun previous() = get(idx--)
        override fun previousIndex() = if (repeats == -1) idx - 1 else (idx - 1).coerceAtLeast(-1)
    }

    override fun listIterator(): ListIterator<T> = Iter()
    override fun listIterator(index: Int): ListIterator<T> = Iter(index)

    override fun subList(fromIndex: Int, toIndex: Int): List<T> = when {
        repeats != -1 && fromIndex < 0 -> throw IndexOutOfBoundsException("$fromIndex < 0")
        repeats != -1 && toIndex > size -> throw IndexOutOfBoundsException("$toIndex > $size")
        else -> {
            println("Warning: subLists in PseudoList are clones!")
            (fromIndex..<toIndex).map(this::get)
        }
    }

    override fun lastIndexOf(element: T) = if (repeats == -1) error("Cannot find last index of object on infinite list")
    else backing.lastIndexOf(element).let { if (it < 0) it else (repeats - 1) * backing.size + it }
}

fun <T> Iterable<T>.asPseudoList(n: Int) = PseudoList(n, toList())
fun <T> Iterable<T>.repeatWithSeparatorExp(n: Int, sep: T): PseudoList<T> {
    val operated = this + sep
    return PseudoList(n, operated, operated.size * n - 1)
}

@OverloadResolutionByLambdaReturnType
inline fun <T> Iterable<T>.sumOfNotNull(block: (T) -> Int?): Int {
    var res = 0
    for (el in this) res += block(el) ?: 0
    return res
}

@JvmName("sumOfNotNullLongs")
@OverloadResolutionByLambdaReturnType
inline fun <T> Iterable<T>.sumOfNotNull(block: (T) -> Long?): Long {
    var res = 0L
    for (el in this) res += block(el) ?: 0L
    return res
}

@OverloadResolutionByLambdaReturnType
inline fun <T> Iterable<T>.sumOfIndexed(block: (idx: Int, T) -> Int): Int {
    var res = 0
    for ((idx, el) in withIndex()) res += block(idx, el)
    return res
}

@JvmName("sumOfIndexedLongs")
@OverloadResolutionByLambdaReturnType
inline fun <T> Iterable<T>.sumOfIndexed(block: (idx: Int, T) -> Long): Long {
    var res = 0L
    for ((idx, el) in withIndex()) res += block(idx, el)
    return res
}

inline fun <T, R> Iterable<T>.mapToSet(block: (T) -> R): Set<R> = mapTo(hashSetOf(), block)
inline fun <T> Iterable<T>.filterToSet(block: (T) -> Boolean): Set<T> = filterTo(hashSetOf(), block)
inline fun <T, R> Iterable<T>.flatMapToSet(block: (T) -> Iterable<R>): Set<R> = flatMapTo(hashSetOf(), block)

data class Edge<T>(val to: T, val weight: Int = 1)

fun <T> Set<T>.asWeightedGraph(dist: (from: T, to: T) -> Int = { _, _ -> 1 }, neighbors: (T) -> List<T>) = buildMap {
    this@asWeightedGraph.forEach { p ->
        this[p] = buildList {
            val queue = queueOf(p to 0)
            val seen = hashSetOf(p)

            while (queue.isNotEmpty()) {
                val (curr, d) = queue.removeFirst()
                if (curr in this@asWeightedGraph && curr != p) {
                    add(Edge(curr, d))
                    continue
                }

                neighbors(curr).filter { seen.add(it) }.map { queue.addLast(it to d + dist(curr, it)) }
            }
        }
    }
}

fun <T> Map<T, List<Edge<T>>>.weights() = mapValues { (_, v) -> v.associate { it.to to it.weight } }

inline fun <T> Iterable<T>.randomSamples(n: Int, block: (T) -> Unit) = toList().randomSamples(n, block)

inline fun <T> Set<T>.randomSamples(n: Int, block: (T) -> Unit) {
    require(n <= size) { "$n samples greater than max (size=$size)" }

    val curr = toHashSet()
    repeat(n) { curr.random().also(block).also { curr -= it } }
}

inline fun <T> List<T>.randomSamples(n: Int, block: (T) -> Unit) {
    require(n <= size) { "$n samples greater than max (size=$size)" }

    val seenIndices = hashSetOf<Int>()
    repeat(n) {
        var idx: Int
        do idx = indices.random() while (idx in seenIndices)
        seenIndices += idx
        block(this[idx])
    }
}

fun <T> Iterator<T>.toList() = asSequence().toList()

inline fun findZero(
    guess: Double,
    dx: Double = .001,
    rounds: Int = 20,
    noinline derivative: ((Double) -> Double)? = null,
    func: (Double) -> Double
): Double {
    var curr = guess

    repeat(rounds) {
        val eval = func(curr)
        if (eval == 0.0 || eval.isNaN() || curr.isNaN()) return curr
        curr -= if (derivative != null) eval / derivative(curr) else eval * dx / (func(curr + dx) - eval)
    }

    return curr
}

fun Iterable<Boolean>.all() = all { it }
fun Sequence<Boolean>.all() = all { it }
fun Iterable<Boolean>.none() = none { it }
fun Sequence<Boolean>.none() = none { it }

fun Iterable<BigInteger>.sum(): BigInteger = fold(BigInteger.ZERO) { acc, curr -> acc + curr }
fun Sequence<BigInteger>.sum(): BigInteger = fold(BigInteger.ZERO) { acc, curr -> acc + curr }

infix fun <T> Set<T>.subseteq(other: Set<T>) = other.containsAll(this)
infix fun <T> Set<T>.supseteq(other: Set<T>) = other.subseteq(this)

fun counter(start: Int = 1) = iterator {
    var i = start
    while (true) yield(i++)
}

fun counterSequence(start: Int = 1) = sequence {
    var i = start
    while (true) yield(i++)
}

fun <K, V> Map<K, V>.gv(key: K): V = getValue(key)

class NonDataBox<T>(val value: T)

fun Iterable<String>.padSameLength(): List<String> {
    val len = maxOf { it.length }
    return map { it.padEnd(len, ' ') }
}

fun <T> MutableList<T>.addN(n: Int, el: T) {
    repeat(n) { add(el) }
}

inline fun <E> buildMutableList(@BuilderInference builderAction: MutableList<E>.() -> Unit) =
    mutableListOf<E>().apply(builderAction)