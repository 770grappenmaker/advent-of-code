@file:JvmName("AOC2021")

package com.grappenmaker.aoc2021

import com.grappenmaker.aoc2021.Color.Codes.*
import java.io.File
import java.io.Serializable
import kotlin.math.max
import kotlin.system.measureTimeMillis

const val TREE = """
         * 
        /|\                  
       /*|O\
      /*/|\*\
     /X/O|*\X\
    /*/X/|\X\*\
   /O/*/X|*\O\X\             
  /*/O/X/|\X\O\*\
 /X/O/*/X|O\X\*\O\
/O/X/*/O/|\X\*\O\X\
        |X|      
        |X|    
"""

fun main(args: Array<String>) {
    // Inform user
    if (args.isEmpty()) {
        println("Usage: AOC2021 <day> [inputFile]")
        return
    }

    // Get puzzle index
    val index = args.first().runCatching { toInt() }.getOrNull() ?: run {
        println("Invalid day number/index \"${args.first()}\"")
        return
    }

    // Formatting
    val colors = arrayOf(GREEN, RED, WHITE, GREEN, WHITE)

    // Give it some color
    val colorize = { s: String ->
        s.lines().joinToString(System.lineSeparator()) {
            it.mapIndexed { i, c -> "${colors[i % colors.size]}$c$RESET" }.joinToString("")
        }
    }

    // Define text color
    val textColor = BRIGHT_BLUE

    // Print header
    println("${textColor}Advent of Code 2021")
    println("Running Kotlin version $RED${KotlinVersion.CURRENT}$RESET")

    // Print tree
    println(colorize(TREE))

    // Print some "Christmas lights"
    val light = "o"
    val lightCount = 36
    val lights = colorize(light.repeat(lightCount)).split(light).joinToString("$light ")
    println(lights)

    // Print the date
    val dayText = "Day $index (https://adventofcode.com/2021/day/$index)"

    // Don't mind the formatting magic
    println(" ".repeat(max(0, (lightCount - dayText.length / 2) - 1)) + textColor + dayText)
    println(lights)

    // Actually run the solution
    val solution = Solution(index, overrideFile = args.getOrNull(1)?.let { File(it) })
    measureTimeMillisResult { solution.run() }.also {
        val (time, result) = it
        val (partOne, partTwo) = result

        println()
        println("${GREEN}Part one: ${BRIGHT_BLUE}$partOne")
        println("${GREEN}Part two: ${BRIGHT_BLUE}$partTwo")

        println()
        println(lights)
        println()
        println("${textColor}Took $RED${time}ms$RESET ${textColor}to run solution.")
    }
}

typealias Answer = Pair<Serializable, Serializable>

// Util to get input and run the solution
class Solution(private val day: Int, private val overrideFile: File? = null) {
    private val inputFile get() = overrideFile ?: File("inputs", "day-${day.toString().padStart(2, '0')}.txt")
    val inputLines = inputFile.readLines()
    val input get() = inputLines.joinToString("\n")

    fun run(): Answer = when (day) {
        1 -> solveDay1()
        2 -> solveDay2()
        3 -> solveDay3()
        4 -> solveDay4()
        5 -> solveDay5()
        6 -> solveDay6()
        7 -> solveDay7()
        8 -> solveDay8()
        9 -> solveDay9()
        10 -> solveDay10()
        11 -> solveDay11()
        12 -> solveDay12()
        13 -> solveDay13()
        14 -> solveDay14()
        15 -> solveDay15()
        16 -> solveDay16()
        17 -> solveDay17()
        else -> error("Couldn't find solution for day $day")
    }
}

inline fun <T> measureTimeMillisResult(block: () -> T): Pair<Long, T> {
    var result: T
    val time = measureTimeMillis { result = block() }
    return time to result
}

// Utility for color, idea from u/microhod_96
object Color {
    const val ESCAPE = "\u001B"

    enum class Codes(val code: Int) {
        // Not exhaustive, I don't need more
        RESET(0),
        RED(31),
        GREEN(32),
        WHITE(37),
        BRIGHT_BLUE(94);

        override fun toString() = "$ESCAPE[${code}m"
    }
}