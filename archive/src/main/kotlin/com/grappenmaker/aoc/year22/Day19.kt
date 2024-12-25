package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.product
import com.grappenmaker.aoc.splitInts
import kotlin.math.max
import kotlin.math.min

@PuzzleEntry
fun PuzzleSet.day19() = puzzle {
    val costs = inputLines.map { l -> l.splitInts().drop(1).parseCosts() }

    fun RobotsCost.geodes(state: RobotState, seen: MutableMap<RobotState, Int> = hashMapOf()): Int {
        if (state.time == 0) return state.geode

        var result = state.geode
        val capped = state.cap(this)
        seen[capped]?.let { return it }

        nextBuilds(state).forEach { result = max(geodes(it, seen), result) }

        seen[capped] = result
        return result
    }

    fun RobotsCost.geodes(time: Int) = geodes(RobotState(time = time))
    partOne = costs.withIndex().sumOf { (idx, v) -> (idx + 1) * v.geodes(24) }.toString()
    partTwo = costs.take(3).map { it.geodes(32) }.product().toString()
}

fun RobotState.cap(bp: RobotsCost): RobotState {
    val tm1 = time - 1

    fun capResource(amount: Int, cost: Int, robots: Int) = min(cost * time - tm1 * robots, amount)
    val newOreRobots = minOf(oreRobots, bp.maxOreCost)
    val newClayRobots = minOf(clayRobots, bp.obbyClayCost)
    val newObbyRobots = minOf(obbyRobots, bp.geodeObbyCost)

    return copy(
        oreRobots = newOreRobots,
        clayRobots = newClayRobots,
        obbyRobots = newObbyRobots,
        ore = capResource(ore, bp.maxOreCost, newOreRobots),
        clay = capResource(clay, bp.obbyClayCost, newClayRobots),
        obby = capResource(obby, bp.geodeObbyCost, newObbyRobots),
    )
}

data class RobotsCost(
    val oreOreCost: Int,
    val clayOreCost: Int,
    val obbyOreCost: Int,
    val obbyClayCost: Int,
    val geodeOreCost: Int,
    val geodeObbyCost: Int
) {
    val maxOreCost = maxOf(oreOreCost, clayOreCost, obbyOreCost, geodeOreCost)
}

fun List<Int>.parseCosts() = RobotsCost(this[0], this[1], this[2], this[3], this[4], this[5])

fun RobotsCost.nextBuilds(state: RobotState) = buildList {
    add(state.advance())

    if (state.ore >= oreOreCost) add(
        state.advance(
            oreRobots = state.oreRobots + 1,
            ore = state.ore - oreOreCost + state.oreRobots
        )
    )

    if (state.ore >= clayOreCost) add(
        state.advance(
            clayRobots = state.clayRobots + 1,
            ore = state.ore - clayOreCost + state.oreRobots
        )
    )

    if (state.ore >= obbyOreCost && state.clay >= obbyClayCost) add(
        state.advance(
            obbyRobots = state.obbyRobots + 1,
            ore = state.ore - obbyOreCost + state.oreRobots,
            clay = state.clay - obbyClayCost + state.clayRobots
        )
    )

    if (state.ore >= geodeOreCost && state.obby >= geodeObbyCost) add(
        state.advance(
            geodeRobots = state.geodeRobots + 1,
            ore = state.ore - geodeOreCost + state.oreRobots,
            obby = state.obby - geodeObbyCost + state.obbyRobots
        )
    )
}

data class RobotState(
    val oreRobots: Int = 1,
    val clayRobots: Int = 0,
    val obbyRobots: Int = 0,
    val geodeRobots: Int = 0,
    val ore: Int = 0,
    val clay: Int = 0,
    val obby: Int = 0,
    val geode: Int = 0,
    val time: Int
)

fun RobotState.advance(
    oreRobots: Int = this.oreRobots,
    clayRobots: Int = this.clayRobots,
    obbyRobots: Int = this.obbyRobots,
    geodeRobots: Int = this.geodeRobots,
    ore: Int = this.ore + this.oreRobots,
    clay: Int = this.clay + this.clayRobots,
    obby: Int = this.obby + this.obbyRobots,
    geode: Int = this.geode + this.geodeRobots,
) = RobotState(oreRobots, clayRobots, obbyRobots, geodeRobots, ore, clay, obby, geode, time - 1)