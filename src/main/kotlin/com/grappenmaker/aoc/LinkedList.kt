@file:Suppress("DuplicatedCode")

package com.grappenmaker.aoc

fun <T> List<T>.toDLL() = toDLLS().first()

fun <T> List<T>.toDLLS() = if (isEmpty()) error("Cannot turn empty list into DLL") else map { DLL(it) }.also {
    (it.plusElement(it.first())).zipWithNext { a, b ->
        a.next = b
        b.prev = a
    }
}

data class DLL<T>(
    val value: T,
    var prev: DLL<T>? = null,
    var next: DLL<T>? = null,
) : Iterable<T> {
    override fun iterator() = DLLIterator()
    override fun toString() = "DLL(value=$value)"

    fun debug() = "DLL(value=$value,prev=$prev,next=$next)"

    inner class DLLIterator : Iterator<T> {
        var curr: DLL<T>? = this@DLL

        override fun hasNext() = curr != null
        override fun next(): T {
            val result = curr?.value
            curr = curr?.next
            return result ?: error("hasNext = false")
        }
    }
}

fun <T> Iterable<T>.takeUntilFirst(): List<T> {
    val iter = iterator()
    if (!iter.hasNext()) return emptyList()

    val first = iter.next()
    return listOf(first) + iter.asSequence().takeWhile { it != first }
}

fun <T> DLL<T>.advance(n: Int = 1) = when {
    n < 0 -> (0..<-n).fold(this) { acc, _ -> acc.prev!! }
    else -> (0..<n).fold(this) { acc, _ -> acc.next!! }
}

fun <T> DLL<T>.take(n: Int) = when {
    n < 0 -> (1..<-n).scan(this) { acc, _ -> acc.prev!! }
    else -> (1..<n).runningFold(this) { acc, _ -> acc.next!! }
}

fun <T> DLL<T>.insertAfter(other: DLL<T>) {
    other.next = next
    next!!.prev = other
    next = other
    other.prev = this
}

fun <T> DLL<T>.insertAfter(other: List<DLL<T>>) {
    if (other.isEmpty()) return
    if (other.size == 1) return insertAfter(other.single())

    val first = other.first()
    val last = other.last()

    first.prev!!.next = last.next
    last.next!!.prev = first.prev

    last.next = next
    next!!.prev = last
    next = first
    first.prev = this
}

fun <T> DLL<T>.insertBefore(other: DLL<T>) {
    other.prev!!.next = other.next
    other.next!!.prev = other.prev

    other.prev = prev
    prev!!.next = other
    prev = other
    other.next = this
}

fun <T> DLL<T>.insertBefore(other: List<DLL<T>>) {
    if (other.isEmpty()) return
    if (other.size == 1) return insertAfter(other.single())

    val first = other.first()
    val last = other.last()

    first.prev!!.next = last.next
    last.next!!.prev = first.prev

    first.prev = prev
    prev!!.next = first
    prev = last
    first.next = this
}

fun <T> DLL<T>.unlink() {
    val old = next
    prev!!.next = old
    old!!.prev = prev
}