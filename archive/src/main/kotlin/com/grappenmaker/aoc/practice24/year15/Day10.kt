@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.practice24.year15

import com.grappenmaker.aoc.*

fun PuzzleSet.day10() = puzzle(day = 10) {
    var curr = input.map { it.digitToInt() }

    fun perform() {
        curr = buildList {
            val iter = curr.iterator()
            var c = 1
            var last = iter.next()

            fun push() {
                add(c)
                add(last)
            }

            for (num in iter) {
                if (last != num) {
                    push()
                    last = num
                    c = 1
                    continue
                }

                c++
            }

            push()
        }
    }

    repeat(40) { perform() }
    partOne = curr.size

    repeat(10) { perform() }
    partTwo = curr.size
}