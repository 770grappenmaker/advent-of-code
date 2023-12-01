package com.grappenmaker.aoc

import com.grappenmaker.aoc.Direction.*
import java.util.*
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

fun <T> Iterable<Iterable<T>>.swapOrder(forceDrain: Boolean = true) = buildList {
    val iterators = this@swapOrder.map { it.iterator() }
    while (iterators.all { it.hasNext() }) {
        add(iterators.map { it.next() })
    }

    if (forceDrain && iterators.any { it.hasNext() }) error("Iterators were not drained while swapping")
}

fun Iterable<Int>.product() = reduce { acc, curr -> acc * curr }

@JvmName("productLongs")
fun Iterable<Long>.product() = reduce { acc, curr -> acc * curr }

fun <T> List<T>.splitHalf() = chunked(size / 2).asPair()
fun <T> List<T>.splitAt(index: Int) = subList(0, index) to subList(index, size)
fun <T> List<T>.splitAtExcluding(index: Int) = subList(0, index) to subList(index, size).drop(1)

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

fun <T, M> Iterable<T>.allDistinctBy(map: (T) -> M): Boolean {
    val set = hashSetOf<M>()
    iterator().drain { if (!set.add(map(it))) return false }
    return true
}

fun <T> Iterable<T>.allIdentical(): Boolean {
    val iter = iterator()
    if (!iter.hasNext()) return true

    val first = iter.next()
    iter.drain { if (it != first) return false }
    return true
}

fun <T, M> Iterable<T>.allIdenticalBy(map: (T) -> M): Boolean {
    val iter = iterator()
    if (!iter.hasNext()) return true

    val first = map(iter.next())
    iter.drain { if (map(it) != first) return false }
    return true
}

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
fun <T> List<Iterable<T>>.deepen() = map { it.toList() }

@JvmName("deepenString")
fun List<String>.deepen() = map { it.toList() }

fun String.deepen() = toList()

inline fun <T> Iterator<T>.drain(use: (T) -> Unit) {
    while (hasNext()) use(next())
}

// Heap's algorithm
// See https://en.wikipedia.org/wiki/Heap%27s_algorithm
fun <T> List<T>.permutationsHeaps(): List<List<T>> = buildList {
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
    yield(take(r + 1))

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

inline fun <T, N> Iterable<T>.permPairsExclusive(transform: (T, T) -> N): List<N> {
    val result = mutableListOf<N>()
    forEachIndexed { idx, v -> filterIndexed { idx2, _ -> idx != idx2 }.mapTo(result) { transform(v, it) } }
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
fun lcm(a: Int, b: Int): Int = (a * b) / gcd(a, b)
fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)

fun gcdOf(vararg i: Int) = i.reduce { acc, curr -> gcd(acc, curr) }
fun gcdOf(vararg i: Long) = i.reduce { acc, curr -> gcd(acc, curr) }
fun lcmOf(vararg i: Int) = i.reduce { acc, curr -> lcm(acc, curr) }
fun lcmOf(vararg i: Long) = i.reduce { acc, curr -> lcm(acc, curr) }

fun List<Int>.gcd() = reduce { a, b -> gcd(a, b) }

@JvmName("gcdLongs")
fun List<Long>.gcd() = reduce { a, b -> gcd(a, b) }

fun List<Int>.lcm() = reduce { a, b -> lcm(a, b) }

@JvmName("lcmLongs")
fun List<Long>.lcm() = reduce { a, b -> lcm(a, b) }

fun rangeDirection(a: Int, b: Int) = if (b < a) a downTo b else a..b

fun Char.parseDirection() = when (this) {
    'U' -> UP
    'D' -> DOWN
    'L' -> LEFT
    'R' -> RIGHT
    else -> error("Invalid direction marker $this")
}

inline fun <T> List<T>.takeUntil(cond: (T) -> Boolean): MutableList<T> {
    val list = mutableListOf<T>()
    forEach { el ->
        list.add(el)
        if (cond(el)) return list
    }

    return list
}

fun <T> Sequence<T>.takeUntil(cond: (T) -> Boolean): Sequence<T> {
    var can = true
    return takeWhile {
        val old = can
        can = cond(it)
        old
    }
}

fun String.splitInts() = "-?\\d+".toRegex().findAll(this).map { it.value.toInt() }.toList()

fun String.onceSplit(at: String, default: String = this) = substringBefore(at, default) to substringAfter(at, default)

inline fun <T> T.applyN(n: Int, block: (T) -> T): T {
    var curr = this
    repeat(n) { curr = block(curr) }
    return curr
}

fun String.doubleLineSequence() = splitToSequence("\r\r\n\n", "\n\n", "\r\r")
fun String.doubleLines() = doubleLineSequence().toList()

fun IntRange.sum() = (start - endInclusive + 1) * (start + endInclusive) / 2

fun <T> Iterable<T>.frequencies() = groupingBy { it }.eachCount()
fun <T> Iterable<T>.mostFrequent() = frequencies().maxBy { it.value }.key
fun <T> Iterable<T>.leastFrequent() = frequencies().minBy { it.value }.key

inline fun <T> Iterable<T>.allIndexed(block: (idx: Int, T) -> Boolean): Boolean {
    forEachIndexed { idx, v -> if (!block(idx, v)) return false }
    return true
}

inline fun <T : Any> T.patternRepeating(totalIterations: Int, next: (T) -> T): T {
    val seen = mutableMapOf<T, Int>()

    var iter = 0
    var left = totalIterations
    var curr = this

    while (left > 0) {
        if (curr in seen && iter != seen.getValue(curr)) {
            val steps = iter - seen.getValue(curr)
            if (steps != 0) {
                iter += left / steps
                left %= steps
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

fun Int.toDigits() = buildList {
    var curr = this@toDigits
    while (curr != 0) {
        add(0, curr % 10)
        curr /= 10
    }
}

fun Long.toDigits() = buildList {
    var curr = this@toDigits
    while (curr != 0L) {
        add(0, curr % 10L)
        curr /= 10L
    }
}

fun IntRange.overlaps(other: IntRange) = first <= other.last && other.first >= last
fun LongRange.overlaps(other: LongRange) = first <= other.last && other.first >= last

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