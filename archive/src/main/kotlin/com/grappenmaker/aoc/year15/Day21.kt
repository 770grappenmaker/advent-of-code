package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.permPairsExclusive
import com.grappenmaker.aoc.splitInts

@PuzzleEntry
fun PuzzleSet.day21() = puzzle(21) {
    data class SpecialItem(val cost: Int, val dmg: Int, val arm: Int)
    operator fun SpecialItem.plus(other: SpecialItem) = SpecialItem(cost + other.cost, dmg + other.dmg, arm + other.arm)

    val weapons = listOf(
        SpecialItem(8, 4, 0),
        SpecialItem(10, 5, 0),
        SpecialItem(25, 6, 0),
        SpecialItem(40, 7, 0),
        SpecialItem(74, 8, 0),
    )

    val armor = listOf(
        SpecialItem(13, 0, 1),
        SpecialItem(31, 0, 2),
        SpecialItem(53, 0, 3),
        SpecialItem(72, 0, 4),
        SpecialItem(102, 0, 5),
        SpecialItem(0, 0, 0)
    )

    val rings = listOf(
        SpecialItem(25, 1, 0),
        SpecialItem(50, 2, 0),
        SpecialItem(100, 3, 0),
        SpecialItem(20, 0, 1),
        SpecialItem(40, 0, 2),
        SpecialItem(80, 0, 3),
        SpecialItem(0, 0, 0),
        SpecialItem(0, 0, 0),
    )

    val (initialBossHP, bossDMG, bossARM) = input.splitInts()
    fun SpecialItem.sim(): Boolean {
        var ourHP = 100
        var bossHP = initialBossHP

        while (true) {
            bossHP -= (dmg - bossARM).coerceAtLeast(1)
            if (bossHP <= 0) return true
            ourHP -= (bossDMG - arm).coerceAtLeast(1)
            if (ourHP <= 0) return false
        }
    }

    val ringPairs = rings.permPairsExclusive()
    val poss = weapons.flatMap { w -> armor.flatMap { a -> ringPairs.map { (r1, r2) -> w + a + r1 + r2 } } }.toSet()
    val (wins, loses) = poss.partition { it.sim() }
    partOne = wins.minOf { it.cost }.toString()
    partTwo = loses.maxOf { it.cost }.toString()
}