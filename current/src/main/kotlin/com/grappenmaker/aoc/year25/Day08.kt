package com.grappenmaker.aoc.year25

import com.grappenmaker.aoc.*

fun main() = simplePuzzle(8, 2025) {
    val pts = inputLines.map { l ->
        val (x, y, z) = l.split(",").map(String::toInt)
        Point3D(x, y, z)
    }

    infix fun Point3D.euclideanDistanceTo(other: Point3D) =
        (x - other.x).toLong().let { it * it } +
                (y - other.y).toLong().let { it * it } +
                (z - other.z).toLong().let { it * it }

    val sorted = pts.permPairsExclusiveSeq().sortedBy { (a, b) -> a euclideanDistanceTo b }
    val uf = UnionFind<Point3D>()
    val direct = hashSetOf<UnorderedPair<Point3D>>()

    var done = 0
    for ((a, b) in sorted) {
        if (!direct.add(UnorderedPair(a, b))) continue
        if (++done == 1000) partOne = uf.islands.values.map { it.size }.sortedDescending().take(3).product()

        uf.add(a, b)

        if (uf.roots.size == 1 && done > 1000) {
            partTwo = a.x * b.x
            break
        }
    }
}

class UnorderedPair<T>(val a: T, val b: T) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UnorderedPair<*>) return false

        if (a == other.a && b == other.b) return true
        if (a == other.b && b == other.a) return true

        return false
    }

    override fun hashCode(): Int {
        val ah = a?.hashCode() ?: 0
        val bh = b?.hashCode() ?: 0
        return ah xor bh
    }

    override fun toString(): String = "UnorderedPair{$a, $b}"
}

class UnionFind<T : Any> {
    val parents = hashMapOf<T, Entry>()

    fun addValue(v: T): Boolean {
        if (v in parents) return false
        parents[v] = Entry(0, null, v)
        return true
    }

    fun add(a: T, b: T): Boolean {
        val rootA = ensureRootOf(a)
        val rootB = ensureRootOf(b)
        if (rootA == rootB) return false

        when {
            rootA.rank < rootB.rank -> rootA.parent = rootB
            rootA.rank > rootB.rank -> rootB.parent = rootA
            else -> {
                rootA.parent = rootB
                rootB.rank++
            }
        }

        return true
    }

    fun contains(a: T, b: T): Boolean {
        val rootA = rootOf(a) ?: return false
        val rootB = rootOf(b) ?: return false
        return rootA == rootB
    }

    operator fun contains(element: UnorderedPair<T>) = contains(element.a, element.b)

    fun add(element: UnorderedPair<T>): Boolean {
        if (contains(element.a, element.b)) return false
        add(element.a, element.b)
        return true
    }

    val size get() = roots.size

    fun rootOf(v: T) = parents[v]?.root
    fun ensureRootOf(v: T) = parents.getOrPut(v) { Entry(0, null, v) }.root

    val roots get() = parents.keys.filterToSet { parents.getValue(it).parent == null }
    val islands get() = parents.keys.groupBy { parents.getValue(it).root }

    inner class Entry(var rank: Int, var parent: Entry?, val value: T) {
        val root: Entry get() = parent?.root?.also { parent = it } ?: this
    }
}