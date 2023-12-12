package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day12() = puzzle(day = 12) {
    data class Entry(val d: List<Boolean?>, val sz: List<Int>)

    val s = inputLines.map { l ->
        val (a, b) = l.split(" ")
        Entry(a.map {
            when (it) {
                '#' -> true
                '.' -> false
                '?' -> null
                else -> error("Impossible")
            }
        }, b.split(",").map(String::toInt))
    }

    data class Memo(val left: List<Boolean?>, val sizes: List<Int>)

    val memo = hashMapOf<Memo, Long>()

    fun recurse(left: List<Boolean?>, sizes: List<Int>): Long {
        fun count(): Long = memo.getOrPut(Memo(left, sizes)) {
            if (sizes.isEmpty()) return@getOrPut 0
            val ss = sizes.first()

            when {
                !left.take(ss).all { it == null || it } -> 0
                left.size < ss -> 0
                left.size == ss -> if (sizes.size == 1) 1 else 0
                left[ss] == true -> 0
                else -> recurse(left.drop(ss + 1), sizes.drop(1))
            }
        }

        return when {
            left.isEmpty() -> if (sizes.isEmpty()) 1 else 0
            else -> when (left.first()) {
                false -> recurse(left.drop(1), sizes)
                true -> count()
                null -> count() + recurse(left.drop(1), sizes)
            }
        }
    }

//    val recursion = object {
//        val recurse: DeepRecursiveFunction<Memo, Long> = DeepRecursiveFunction {
//            when {
//                it.left.isEmpty() -> if (it.sizes.isEmpty()) 1 else 0
//                else -> when (it.left.first()) {
//                    false -> callRecursive(Memo(it.left.drop(1), it.sizes))
//                    true -> count.callRecursive(it)
//                    null -> count.callRecursive(it) + callRecursive(Memo(it.left.drop(1), it.sizes))
//                }
//            }
//        }
//
//        val count: DeepRecursiveFunction<Memo, Long> = DeepRecursiveFunction {
//            memo.getOrPut(it) {
//                if (it.sizes.isEmpty()) return@getOrPut 0
//                val ss = it.sizes.first()
//
//                when {
//                    it.left.size < ss -> 0
//                    !it.left.take(ss).all { v -> v == null || v } -> 0
//                    it.left.size == ss -> if (it.sizes.size == 1) 1 else 0
//                    it.left[ss] == true -> 0
//                    else -> recurse.callRecursive(Memo(it.left.drop(ss + 1), it.sizes.drop(1)))
//                }
//            }
//        }
//    }

    fun List<Entry>.solve() = sumOf { (d, sz) -> recurse(d, sz) }.s()

    partOne = s.solve()
    memo.clear()
    partTwo = s.map { (a, b) -> Entry(listOf(a, listOf(null)).repeat(5).flatten().dropLast(1), b.repeat(5)) }.solve()
}