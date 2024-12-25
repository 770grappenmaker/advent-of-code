package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.drain
import com.grappenmaker.aoc.hasDuplicateBy
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.splitInts
import java.util.*

@PuzzleEntry
fun PuzzleSet.day22() = puzzle(day = 22) {
    val (initialBossHP, bossDMG) = input.splitInts()

    // Horrible code but it was the first idea I had please forgive meee
    val spells = listOf(
        Spell(53) { it.copy(bossHP = it.bossHP - 4) },
        Spell(73) { it.copy(bossHP = it.bossHP - 2, playerHP = it.playerHP + 2) },
        Spell(113) {
            it.copy(
                effects = it.effects + Effect(6, 0, remove = { s -> s.copy(playerDef = s.playerDef - 7) }),
                playerDef = it.playerDef + 7
            )
        },
        Spell(173) { it.copy(effects = it.effects + Effect(6, 1, step = { s -> s.copy(bossHP = s.bossHP - 3) })) },
        Spell(229) { it.copy(effects = it.effects + Effect(5, 2, step = { s -> s.copy(mana = s.mana + 101) })) }
    )

    val initialState = GameState(initialBossHP)

    fun solve(hardMode: Boolean): Int {
        val seen = hashSetOf(initialState)
        val queue = PriorityQueue<Pair<GameState, Int>>(compareBy { (_, c) -> c }).also { it.add(initialState to 0) }

        queue.drain { (current, currentCost) ->
            if (current.bossHP <= 0) return currentCost
            if (current.playerHP <= 0) return@drain
            val step1 = current.effects.fold(current) { acc, curr ->
                curr.step(acc).let { if (curr.timer == 1) curr.remove(it) else it }
            }

            val step2 = step1.copy(effects = step1.effects.mapNotNull {
                val newTimer = it.timer - 1
                if (newTimer == 0) null else it.copy(timer = newTimer)
            })

            if (!current.playerTurn) {
                val actualDamage = (bossDMG - step2.playerDef).coerceAtLeast(1)
                val bossUpdated = step2.copy(
                    playerHP = step2.playerHP - actualDamage - if (hardMode) 1 else 0,
                    playerTurn = true
                )

                if (seen.add(bossUpdated)) queue.offer(bossUpdated to currentCost)
            } else {
                spells.filter { it.mana <= step2.mana }.forEach { cast ->
                    val appliedSpell = cast.apply(step2)
                    if (appliedSpell.effects.hasDuplicateBy { it.type }) return@forEach

                    val new = appliedSpell.copy(mana = step2.mana - cast.mana, playerTurn = false)
                    if (seen.add(new)) queue.offer(new to currentCost + cast.mana)
                }
            }
        }

        error("Not found")
    }

    partOne = solve(false).toString()
    partTwo = solve(true).toString()
}

data class Spell(val mana: Int, val apply: (GameState) -> GameState)

data class Effect(
    val timer: Int,
    val type: Int,
    val step: (GameState) -> GameState = { it },
    val remove: (GameState) -> GameState = { it }
)

data class GameState(
    val bossHP: Int,
    val effects: List<Effect> = emptyList(),
    val playerHP: Int = 50,
    val mana: Int = 500,
    val playerDef: Int = 0,
    val playerTurn: Boolean = true
)