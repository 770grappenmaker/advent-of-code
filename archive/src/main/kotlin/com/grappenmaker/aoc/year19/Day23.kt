package com.grappenmaker.aoc.year19

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.queueOf

@PuzzleEntry
fun PuzzleSet.day23() = puzzle(day = 23) {
    fun seq(partTwo: Boolean) = sequence {
        var lastNATX: Long? = null
        var lastNATY: Long? = null
        
        val inputBuffers = List(50) { queueOf(it.toLong()) }
        val network = List(50) {
            startComputer(input).apply { input { inputBuffers[it].removeFirstOrNull() ?: -1L } }
        }
        
        fun send(a: Int, x: Long, y: Long) = inputBuffers[a].run {
            addLast(x)
            addLast(y)
        }

        while (true) {
            for (pc in network) {
                pc.step()
                if (pc.output.size >= 3) {
                    val a = pc.output.removeFirst()
                    val x = pc.output.removeFirst()
                    val y = pc.output.removeFirst()
                    if (a == 255L) {
                        lastNATX = x
                        lastNATY = y
                        if (!partTwo) yield(y)
                    } else send(a.toInt(), x, y)
                }
            }
            
            if (partTwo && inputBuffers.all { it.isEmpty() } && lastNATX != null && lastNATY != null) {
                send(0, lastNATX, lastNATY)
                yield(lastNATY)
            }
        }
    }
    
    partOne = seq(false).first().s()
    
    // I cannot figure out why i have to remove every second element.. apparently i yield too many elements? odd
    partTwo = seq(true).chunked(2) { (a) -> a }.zipWithNext().first { (a, b) -> a == b }.first.s()
}