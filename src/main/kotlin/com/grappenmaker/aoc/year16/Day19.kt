package com.grappenmaker.aoc.year16

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.deepen
import com.grappenmaker.aoc.queueOf
import com.grappenmaker.aoc.rotate

fun PuzzleSet.day19() = puzzle(day = 19) {
    val amount = input.toInt()

    // Naive, really bad solution...
    // Wikipedia suggests this: put the first bit last
    // partOne = amount.toString(2).deepen().rotate(-1).joinToString("").toInt(2).s()
    val elvesLeft = (0 until amount).toMutableSet()
    var iter = 0

    fun findNext(): Int {
        for (r in iter + 1 until amount) if (r in elvesLeft) return r
        for (l in 0 until iter) if (l in elvesLeft) return l
        error("Done? What?")
    }

    while (elvesLeft.size > 1) {
        val next = findNext()
        if (iter in elvesLeft) elvesLeft -= next
        iter = next
    }

    partOne = (elvesLeft.single() + 1).s()

    val halfCircle = amount / 2
    val queue = queueOf((1..halfCircle).toList())
    val stack = queueOf((amount downTo halfCircle + 1).toList())

    while (stack.isNotEmpty()) {
        // The idea is that we keep track of the elves that
        // are in front of everyone. By kicking people out of the queues
        // and not looking at the result, that is how we are "stealing"
        // Therefore, eventually the queue has a single element left, that
        // then wants to steal, but cannot
        (if (queue.size > stack.size) queue else stack).removeLast()
        stack.addFirst(queue.removeFirst())
        queue.addLast(stack.removeLast())
    }

    partTwo = queue.single().s()
}