@file:Suppress("UnusedImport")

package com.grappenmaker.aoc.practice24.year20

import com.grappenmaker.aoc.*

fun PuzzleSet.day04() = puzzle(day = 4) {
    val req = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    val possECL = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

    var p1 = 0
    var p2 = 0

    for (dll in input.doubleLines()) {
        val ll = dll.lines().flatMap { l ->
            l.split(" ").map { p ->
                val (k, v) = p.split(":")
                k to v
            }
        }.toMap()

        if (!req.all { it in ll }) continue
        p1++

        if (ll.getValue("byr").toInt() !in 1920..2002) continue
        if (ll.getValue("iyr").toInt() !in 2010..2020) continue
        if (ll.getValue("eyr").toInt() !in 2020..2030) continue

        val hgt = ll.getValue("hgt")
        if (hgt.length < 3) continue

        if (
            hgt.dropLast(2).toInt() !in (when (hgt.takeLast(2)) {
                "cm" -> 150..193
                "in" -> 59..76
                else -> error("impossible")
            })
        ) continue

        if (ll.getValue("ecl") !in possECL) continue

        val hcl = ll.getValue("hcl")
        if (hcl[0] != '#') continue
        if (hcl.drop(1).any { c -> !c.isDigit() && c !in 'a'..'f' }) continue

        val pid = ll.getValue("pid")
        if (pid.length != 9 || !pid.all { it.isDigit() }) continue

        p2++
    }

    partOne = p1
    partTwo = p2
}