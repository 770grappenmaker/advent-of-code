package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.*
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day8() = puzzle(8) {
    fun run(program: List<String>, returnAnyways: Boolean): Int? {
        var acc = 0
        var pos = 0
        val seen = hashSetOf<Int>()

        outer@ while (seen.add(pos) && pos < program.size) {
            val l = program[pos]
            val p = l.split(' ')
            val n = p[1].toInt()

            when (p[0]) {
                "nop" -> {}
                "acc" -> acc += n
                "jmp" -> pos += (n - 1)
            }

            pos++
        }

        if (pos >= program.size || returnAnyways) return acc
        return null
    }

    partOne = run(inputLines, true).toString()

    for ((idx, l) in inputLines.withIndex()) {
        val cmd = l.substringBefore(' ')
        if (cmd == "acc") continue

        val program = buildList {
            addAll(inputLines.take(idx))
            add((if (cmd == "nop") "jmp " else "nop ") + l.substringAfter(' '))
            addAll(inputLines.drop(idx + 1))
        }

        val res = run(program, false)
        if (res != null) {
            partTwo = res
            break
        }
    }
}