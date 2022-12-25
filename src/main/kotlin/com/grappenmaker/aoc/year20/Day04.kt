package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.asPair

fun PuzzleSet.day4() = puzzle {
    val colorRegex = """^#([0-9]|[a-f]){6}$""".toRegex()
    val eyeColors = "amb blu brn gry grn hzl oth".split(" ")
    val pidRegex = """^\d\d\d\d\d\d\d\d\d$""".toRegex()
    val passports = input.split("\n\n").map { l ->
        l.lines().joinToString(" ").split(" ")
            .associate { it.split(":").asPair() }
    }

    val filtered = passports.filter { listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid").all { k -> k in it } }
    partOne = filtered.size.s()
    partTwo = filtered.count { p ->
        val hgt by p
        val parsedHeight = hgt.dropLast(2).toIntOrNull()
        val validHeight = when {
            hgt.endsWith("in") -> parsedHeight in 59..76
            hgt.endsWith("cm") -> parsedHeight in 150..193
            else -> false
        }

        p.getValue("byr").toIntOrNull() in 1920..2002 && p.getValue("iyr").toIntOrNull() in 2010..2020 &&
                p.getValue("eyr").toIntOrNull() in 2020..2030 && validHeight && p.getValue("hcl").matches(colorRegex) &&
                p.getValue("ecl") in eyeColors && p.getValue("pid").matches(pidRegex)
    }.s()
}