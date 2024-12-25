package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.doubleLines
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.splitInts
import com.grappenmaker.aoc.takeUntil

@PuzzleEntry
fun PuzzleSet.day24() = puzzle {
    data class Group(
        val units: Int,
        val hp: Int,
        val immunities: Set<String>,
        val weaknesses: Set<String>,
        val damage: Int,
        val damageType: String,
        val initiative: Int
    )

    fun Group.effectivePower() = units * damage
    fun Group.damageTo(other: Group) = when (damageType) {
        in other.immunities -> 0
        in other.weaknesses -> effectivePower() * 2
        else -> effectivePower()
    }

    fun String.parseGroup(): Group {
        val words = split(' ')
        val (units, hp, damage, initiative) = splitInts()
        val detailParts = if ('(' !in this) emptyMap() else {
            val details = substringAfter('(').substringBefore(')')
            details.split("; ").associate {
                val (property, types) = it.split(" to ")
                property to types.split(", ").toSet()
            }
        }

        return Group(
            units = units,
            hp = hp,
            immunities = detailParts["immune"] ?: emptySet(),
            weaknesses = detailParts["weak"] ?: emptySet(),
            damage = damage,
            damageType = words[words.indexOf("damage") - 1],
            initiative = initiative
        )
    }

    val ordering = compareByDescending<Group> { it.effectivePower() }.thenByDescending { it.initiative }

    data class GameState(val immuneSystem: List<Group>, val infection: List<Group>)

    fun GameState.isOver() = immuneSystem.isEmpty() || infection.isEmpty()
    fun GameState.remainingUnits() = (immuneSystem + infection).sumOf { it.units }
    fun GameState.advance(): GameState {
        fun List<Group>.selectTargets(enemy: List<Group>): Map<Group, Group?> {
            val enemiesLeft = enemy.sortedWith(ordering).toMutableList()

            return sortedWith(ordering).associateWith { ourGroup ->
                enemiesLeft
                    .maxByOrNull { ourGroup.damageTo(it) }
                    ?.takeIf { ourGroup.damageTo(it) > 0 }
                    ?.also { enemiesLeft -= it }
            }
        }

        val newImmuneSystem = immuneSystem.toMutableList()
        val newInfection = infection.toMutableList()

        fun IntRange.asIDs(hostile: Boolean) = map { it to hostile }
        fun Pair<Int, Boolean>.army() = if (second) newInfection else newImmuneSystem
        fun Pair<Int, Boolean>.currentGroup() = army()[first]
        fun Pair<Int, Boolean>.replace(new: Group) {
            val army = army()
            army.removeAt(first)
            army.add(first, new)
        }

        fun Group.asID(hostile: Boolean) = (if (hostile) newInfection else newImmuneSystem).indexOf(this) to hostile
        fun Map<Group, Group?>.asIDTargets(hostile: Boolean) = toList().associate { (attacker, target) ->
            attacker.asID(hostile) to target?.asID(!hostile)
        }

        val immuneTargets = immuneSystem.selectTargets(infection).asIDTargets(false)
        val infectionTargets = infection.selectTargets(immuneSystem).asIDTargets(true)

        val ids = (newImmuneSystem.indices.asIDs(false) + newInfection.indices.asIDs(true))
            .sortedByDescending { it.currentGroup().initiative }

        for (id in ids) {
            val group = id.currentGroup()
            if (group.units <= 0) continue

            val target = (if (id.second) infectionTargets else immuneTargets)[id] ?: continue
            val targetGroup = target.currentGroup()
            val unitsLost = group.damageTo(targetGroup) / targetGroup.hp
            target.replace(targetGroup.copy(units = targetGroup.units - unitsLost))
        }

        fun List<Group>.filterAlive() = filter { it.units > 0 }
        return GameState(newImmuneSystem.filterAlive(), newInfection.filterAlive())
    }

    fun GameState.evaluate() = generateSequence(this) { it.advance() }
        .takeUntil { !it.isOver() }.windowed(2)
        .takeWhile { (a, b) -> a != b }
        .map { (_, a) -> a }.last()

    fun GameState.won() = immuneSystem.isNotEmpty() && infection.isEmpty()

    val (initialImmune, initialInfection) = input.doubleLines().map { it.lines().drop(1).map(String::parseGroup) }
    partOne = GameState(initialImmune, initialInfection).evaluate().remainingUnits().toString()

    fun List<Group>.boost(amount: Int) = map { it.copy(damage = it.damage + amount) }
    fun evaluateWithBoost(amount: Int) = GameState(initialImmune.boost(amount), initialInfection).evaluate()

    var min = 0
    var max = 50

    while (min + 1 < max) {
        val pivot = (min + max) / 2
        if (evaluateWithBoost(pivot).won()) max = pivot else min = pivot
    }

    partTwo = evaluateWithBoost(max).remainingUnits().toString()
}