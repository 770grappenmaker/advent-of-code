package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.deepen
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day4() = puzzle {
    val rooms = inputLines.map { l ->
        val (name, info) = l.substringBeforeLast('-') to l.substringAfterLast('-')
        val (id, check) = info.substringBeforeLast('[') to info.substringAfterLast('[')
        RoomCheck(name, id.toInt(), check.removeSuffix("]").deepen().sorted())
    }

    val filtered = rooms.filter { (name, _, check) ->
        val chars = name.filter { it.isLetter() }.groupingBy { it }.eachCount().toList()
            .sortedWith(compareByDescending<Pair<Char, Int>> { it.second }.then(compareBy { it.first }))

        chars.take(5).map { it.first }.sorted() == check
    }

    partOne = filtered.sumOf { it.id }.toString()
    partTwo = filtered.first { (text, shift) ->
        text.deepen().joinToString("") { c ->
            when {
                c.isLetter() -> ((c.code - 97 + shift) % 26 + 97).toChar()
                c == '-' -> ' '
                else -> c
            }.toString()
        } == "northpole object storage"
    }.id.toString()
}

data class RoomCheck(val name: String, val id: Int, val checksum: List<Char>)