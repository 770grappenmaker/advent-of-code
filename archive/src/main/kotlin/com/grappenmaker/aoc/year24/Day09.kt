@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.year24

import com.grappenmaker.aoc.*

fun PuzzleSet.day09() = puzzle(day = 9) {
    // do not feel like refactoring nicely today, so two different solutions you shall receive
    val data = input.map { it.digitToInt() }
    fun p1(): Long {
        val usedData = data.slice(0..data.size step 2)
        val used = usedData.withIndex().flatMapTo(mutableListOf()) { (idx, v) -> List(v) { idx } }
        val free = data.slice(1..<data.size step 2).toQueue()

        var usedPtr = 1
        var ptr = usedData.first()
        while (ptr in used.indices) {
            while (free[0]-- > 0) {
                used.add(ptr, used.removeLast())
                ptr++
            }

            free.removeFirst()
            ptr += usedData[usedPtr++]
        }

        return used.sumOfIndexed { idx, v -> idx * v.toLong() }
    }

    data class PosLen<T>(val pos: Int, val len: Int, val v: T? = null)
    fun <T> PosLen<T>.next() = pos + len
    fun p2(): Long {
        val ans = mutableListOf<Int?>()
        val usedData = ArrayDeque<PosLen<Int>>()
        val freeData = mutableListOf<PosLen<Nothing?>>()

        var isSpace = false
        for (v in data) {
            val toAdd = if (isSpace) {
                freeData.add(PosLen(usedData.first().next(), v))
                null
            } else {
                val fid = (usedData.firstOrNull()?.v ?: -1) + 1
                usedData.addFirst(PosLen(freeData.lastOrNull()?.next() ?: 0, v, fid))
                fid
            }

            repeat(v) { ans.add(toAdd) }
            isSpace = !isSpace
        }

        for ((idx, size, fid) in usedData) {
            val firstPos = freeData.findIndexOf { it.pos < idx && size <= it.len } ?: continue
            val (freeLoc, freeStride) = freeData[firstPos]
            freeData[firstPos] = PosLen(freeLoc + size, freeStride - size)
            ans.subList(idx, idx + size).mapInPlace { null }
            ans.subList(freeLoc, freeLoc + size).mapInPlace { fid }
        }

        return ans.sumOfIndexed { idx, v -> if (v == null) 0 else idx * v.toLong() }
    }

    partOne = p1()
    partTwo = p2()
}