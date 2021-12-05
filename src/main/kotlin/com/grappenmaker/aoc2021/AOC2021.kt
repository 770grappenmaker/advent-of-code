@file:JvmName("AOC2021")

package com.grappenmaker.aoc2021

import com.grappenmaker.aoc2021.Color.Codes.*
import java.io.File
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
    // By the way, the colors are messed up because of the spaces
    // so the order is weird
    val colors = arrayOf(RED, BRIGHT_YELLOW, GREEN)

    // Give it some color
    val colorize = { s: String ->
        s.lines().joinToString(System.lineSeparator()) {
            it.mapIndexed { i, c -> "${colors[i % colors.size]}$c$RESET" }.joinToString("")
        }
    }

    // Print header
    println("${BRIGHT_BLUE}Advent of Code 2021$RESET")

    // Print tree
    println(colorize(TREE))

    // Print some "christmas lights"
    val light = "o "
    val lightCount = 36
    val lights = colorize(light.repeat(lightCount))
    println(lights)

    // Print the date
    val dayText = "Day $index (https://adventofcode.com/2021/day/$index)"

    // Don't mind the formatting magic
    println(" ".repeat(max(0, (lightCount - dayText.length / 2) - 1)) + BRIGHT_BLUE + dayText)

    // Set result to blue
    println(lights)
    println(BRIGHT_BLUE)

    // Actually run the solution
    val context = Context(index, args.getOrNull(1)?.let { File(it) })
    measureTimeMillis {
        context.run {
            when (index) {
                1 -> solveDay1()
                2 -> solveDay2()
                3 -> solveDay3()
                4 -> solveDay4()
                5 -> solveDay5()
                else -> println("Couldn't find solution for day $index")
            }
        }
    }.also {
        println()
        println(lights)
        println("${BRIGHT_BLUE}Took $RED${it}ms$RESET ${BRIGHT_BLUE}to run solution.")
    }
}

// Util to get input
class Context(private val day: Int, private val overrideFile: File? = null) {
    fun getInputFile() = overrideFile ?: File("inputs", "day-${day.toString().padStart(2, '0')}.txt")
    fun getInput(): String = getInputFile().readText()
    fun getInputLines(): List<String> = getInputFile().readLines()
}

// Utility for color, idea from u/microhod_96
object Color {
    const val ESCAPE = "\u001B"

    enum class Codes(val code: Int) {
        // Not exhaustive, i don't need more
        RESET(0), RED(31), GREEN(32), BRIGHT_YELLOW(93), BRIGHT_BLUE(94);

        override fun toString() = "$ESCAPE[${code}m"
    }
}