package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.mostFrequent
import com.grappenmaker.aoc.splitInts
import java.text.SimpleDateFormat
import java.time.ZoneOffset

@PuzzleEntry
fun PuzzleSet.day4() = puzzle {
    // Wacky way to do this stuff
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val events = inputLines.sorted().map { l ->
        val timePart = l.substringAfter('[').substringBefore(']')
        val minute = format.parse(timePart).toInstant().atZone(ZoneOffset.UTC).minute
        when {
            "begins shift" in l -> GuardStartShift(minute, l.splitInts().last())
            "falls asleep" in l -> GuardSleep(minute)
            "wakes up" in l -> GuardWakeUp(minute)
            else -> error("Invalid guard message $l")
        }
    }

    // Double parsing, prob should've avoided
    val shifts = buildList {
        var current = events.first() as GuardStartShift
        var sleepTime = current.minute

        for (event in events) {
            when (event) {
                is GuardStartShift -> current = event
                is GuardSleep -> sleepTime = event.minute
                is GuardWakeUp -> add(GuardShift(current.id, current.minute, sleepTime, event.minute))
            }
        }
    }.groupBy { it.id }

    // Actual solution, lol
    val sleepIndex = shifts.mapValues { (_, t) -> t.flatMap { (it.sleep..<it.wake).toList() } }
    val (p1ID, targetMinutes) = sleepIndex.maxBy { (_, v) -> v.size }
    val bestMinute = targetMinutes.mostFrequent()
    partOne = (p1ID * bestMinute).s()

    val (p2ID, globalBestMinute) = sleepIndex.flatMap { (k, v) -> v.map { t -> k to t } }.mostFrequent()
    partTwo = (p2ID * globalBestMinute).s()
}

sealed interface GuardEvent {
    val minute: Int
}

data class GuardStartShift(override val minute: Int, val id: Int) : GuardEvent
data class GuardWakeUp(override val minute: Int) : GuardEvent
data class GuardSleep(override val minute: Int) : GuardEvent
data class GuardShift(val id: Int, val start: Int, val sleep: Int, val wake: Int)