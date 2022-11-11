package com.grappenmaker.aoc.year15

import kotlin.math.max
import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.year15.LightingCommand.*

val lightBoardSize = 1000 // square board

fun PuzzleSet.day6() = puzzle {
    val insns = inputLines.map { l ->
        val (a, b) = l.trim().split(" ").filter { "," in it }.map { point ->
            val (x, y) = point.split(",").map(String::toInt)
            Light(x, y)
        }

        when {
            l.startsWith("turn on") -> On(a, b)
            l.startsWith("turn off") -> Off(a, b)
            l.startsWith("toggle") -> Toggle(a, b)
            else -> error("Invalid command")
        }
    }

    val toggled = BooleanArray(lightBoardSize * lightBoardSize)
    val brightnessMap = IntArray(lightBoardSize * lightBoardSize)
    insns.forEach { cmd ->
        cmd.forEachIndex { index ->
            val (toggleValue, deltaBrightness) = when (cmd) {
                is On -> true to 1
                is Off -> false to -1
                is Toggle -> !toggled[index] to 2
            }

            toggled[index] = toggleValue
            brightnessMap[index] = max(0, brightnessMap[index] + deltaBrightness)
        }
    }

    partOne = toggled.count { it }.s()
    partTwo = brightnessMap.sum().s()
}

sealed interface LightingCommand {
    val a: Light
    val b: Light

    data class On(override val a: Light, override val b: Light) : LightingCommand
    data class Off(override val a: Light, override val b: Light) : LightingCommand
    data class Toggle(override val a: Light, override val b: Light) : LightingCommand
}

inline fun LightingCommand.forEachIndex(block: (Int) -> Unit) {
    for (x in a.x..b.x) {
        for (y in a.y..b.y) {
            block(x + y * lightBoardSize)
        }
    }
}

// These represent corners, not actual lights
data class Light(val x: Int, val y: Int)