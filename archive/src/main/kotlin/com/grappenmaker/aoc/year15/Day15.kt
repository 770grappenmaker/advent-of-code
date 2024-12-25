package com.grappenmaker.aoc.year15

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry

@PuzzleEntry
fun PuzzleSet.day15() = puzzle {
    val ingredients = inputLines.map { l ->
        val (_, propertyList) = l.split(": ")
        val (capacity, durability, flavor, texture, calories) =
            propertyList.split(", ").map { it.split(" ")[1].toInt() }

        Ingredient(capacity, durability, flavor, texture, calories)
    }

    val cookieSize = 100

    // Idea: Start with a cookie with equal proportions per ingredient
    // (this is sort of the average cookie and statistically a good starter?)
    // Then "BFS through neighbours" until we find the best one
    // I assume this can be done a lot better, since it took 5001ms the first time
    // I tried to run part 2. Fun puzzle though.g
    fun solve(requireCalories: Boolean, starterCookie: Cookie): Cookie {
        val seen = hashSetOf(starterCookie.hash)
        val queue = ArrayDeque(listOf(starterCookie))

        var highestScore = Cookie(ingredients.associateWith { 0 })
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()

            if (
                next.score > highestScore.score &&
                (next.calories == 500 || !requireCalories)
            ) highestScore = next

            queue.addAll(next.adjacent().filter { seen.add(it.hash) })
        }

        return highestScore
    }

    val cookie = Cookie(ingredients.associateWith { cookieSize / ingredients.size })
    val partOneCookie = solve(false, cookie)

    partOne = partOneCookie.score.toString()
    partTwo = solve(true, partOneCookie).score.toString()
}

data class Cookie(val ingredients: Map<Ingredient, Int>) {
    // Utility to keep track of what cookies we have had
    // Since every ingredient has a smaller size than 100,
    // we can store every ingredient as 7 bits of a long
    // (assuming we have no more than 64/7=9 ingredients)
    val hash = ingredients.values.reduceIndexed { idx, acc, curr -> acc or (curr and 0x7F shl (idx * 7)) }

    val singleIngredient by lazy {
        ingredients.toList().fold(emptyCookie) { acc, curr ->
            val (ingredient, amount) = curr
            acc.copy(
                capacity = acc.capacity + ingredient.capacity * amount,
                durability = acc.durability + ingredient.durability * amount,
                flavor = acc.flavor + ingredient.flavor * amount,
                texture = acc.texture + ingredient.texture * amount,
                calories = acc.calories + ingredient.calories * amount
            )
        }
    }

    val calories get() = singleIngredient.calories

    // Basically selects every combo of 2 ingredients and varies them by one
    fun adjacent() = ingredients.flatMap { (ingredientA, amountA) ->
        (ingredients - ingredientA).flatMap { (ingredientB, amountB) ->
            val aPlus = ingredientA to amountA + 1
            val aMinus = ingredientA to amountA - 1
            val bPlus = ingredientB to amountB + 1
            val bMinus = ingredientB to amountB - 1

            listOf(
                ingredients + aPlus + bMinus,
                ingredients + aMinus + bPlus
            ).filter { it.isValid() }.map { Cookie(it) }
        }
    }

    val score get() = singleIngredient.score

    operator fun compareTo(other: Cookie) = singleIngredient.compareTo(other.singleIngredient)
}

fun Map<Ingredient, Int>.isValid() = values.none { it <= 0 }

val emptyCookie = Ingredient(0, 0, 0, 0, 0)

// This can also represent a whole cookie
data class Ingredient(
    val capacity: Int,
    val durability: Int,
    val flavor: Int,
    val texture: Int,
    val calories: Int
)

operator fun Ingredient.compareTo(other: Ingredient) = score - other.score

val Ingredient.score: Int
    get() {
        val allProps = listOf(capacity, durability, flavor, texture)
        return if (allProps.any { it <= 0 }) 0
        else allProps.reduce { acc, curr -> acc * curr }
    }