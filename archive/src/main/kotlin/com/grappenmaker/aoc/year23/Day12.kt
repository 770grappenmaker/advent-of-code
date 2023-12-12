package com.grappenmaker.aoc.year23

import com.grappenmaker.aoc.*

fun PuzzleSet.day12() = puzzle(day = 12) {
    //    data class Memo(val left: List<Boolean?>, val sizes: List<Int>)
    data class Memo(val configIdx: Int, val sizesIdx: Int)
    data class Entry(val config: List<Boolean?>, val sizes: List<Int>)

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

    fun recurse(state: Memo, data: Entry, memo: MutableMap<Memo, Long> = hashMapOf()): Long {
        // naive/list based solution
//        fun count() = if (sizes.isEmpty()) 0 else {
//            val ss = sizes.first()
//            if ((ss in left.indices && left[ss] == true) || left.size < ss) 0 else memo.getOrPut(this) {
//                when {
//                    !left.take(ss).all { it != false } -> 0
//                    left.size == ss -> if (sizes.size == 1) 1 else 0
//                    else -> Memo(left.drop(ss + 1), sizes.drop(1)).recurse(memo)
//                }
//            }
//        }
//
//        return when {
//            left.isEmpty() -> if (sizes.isEmpty()) 1 else 0
//            else -> when (left.first()) {
//                false -> Memo(left.drop(1), sizes).recurse(memo)
//                true -> count()
//                null -> count() + Memo(left.drop(1), sizes).recurse(memo)
//            }
//        }

        // Uses list tricks to not excessively use RAM
        fun count() = if (state.sizesIdx !in data.sizes.indices) 0 else {
            val ss = data.sizes[state.sizesIdx] + state.configIdx
            if ((ss in data.config.indices && data.config[ss] == true) || data.config.size < ss) 0
            else memo.getOrPut(state) {
                when {
                    !(state.configIdx..<ss).all { data.config[it] != false } -> 0
                    state.configIdx == ss -> if (data.sizes.size - state.sizesIdx == 1) 1 else 0
                    else -> recurse(Memo(ss + 1, state.sizesIdx + 1), data, memo)
                }
            }
        }

        return when {
            state.configIdx !in data.config.indices -> if (state.sizesIdx !in data.sizes.indices) 1 else 0
            else -> when (data.config[state.configIdx]) {
                false -> recurse(Memo(state.configIdx + 1, state.sizesIdx), data, memo)
                true -> count()
                null -> count() + recurse(Memo(state.configIdx + 1, state.sizesIdx), data, memo)
            }
        }
    }

    fun List<Entry>.solve() = sumOf { recurse(Memo(0, 0), it) }.s()

    partOne = s.solve()
    partTwo = s.map { (a, b) -> Entry(listOf(a).asPseudoList(5).joinToList(null), b.asPseudoList(5)) }.solve()
//    partTwo = s.map { (a, b) -> Entry(a.repeatWithSeparatorExp(5, null), b.asPseudoList(5)) }.solve()
}