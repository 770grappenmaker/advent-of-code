package com.grappenmaker.aoc2021

import kotlin.math.floor

// 5x5
const val BOARD_SIZE = 5
val spacePattern = """\s+""".toRegex()

fun Solution.solveDay4() {
    // Part one
    val lines = inputLines

    // Read numbers
    val numbers = lines.first().split(",").map { it.toInt() }

    // Read boards
    val boardsText = lines.joinToString("\n").substringAfter("\n\n").split("\n\n")
    val boards = boardsText.asSequence()
        .map { b ->
            b.trim().lines()
                .flatMap { it.trim().split(spacePattern) }
                .mapIndexed { i, s -> Cell(s.toInt(), i) }
        }
        .map { Board(it.toTypedArray()) }.toSet()

    // Simulate game
    val game = Game(numbers, boards)
    game.getEventualWinner()?.let { println("Part one: ${it.score * game.lastNumber}") }

    // Part two
    game.reset()
    val lastWinner = game.getRanking().last()
    println("Part two: ${game.lastNumber * lastWinner.score}")
}

class Game(private val numbers: List<Int>, private val boards: Set<Board>) {
    var lastNumber = 0
        private set
    private var index = 0

    private fun step() {
        if (index >= numbers.size) return

        this.lastNumber = numbers[index]
        boards.forEach { it.mark(lastNumber) }
        index++
    }

    fun getEventualWinner(): Board? {
        for (i in numbers) {
            step()

            val winner = getWinners().firstOrNull()
            if (winner != null) return winner
        }

        return null
    }

    fun getRanking(): List<Board> {
        val boardsLeft = boards.toMutableSet()
        val result = mutableListOf<Board>()

        for (i in numbers) {
            step()

            val newWinners = boardsLeft.filter { it.hasWon() }
            result.addAll(newWinners)
            boardsLeft.removeAll(newWinners.toSet())

            if (boardsLeft.isEmpty()) break
        }

        return result
    }

    fun reset() {
        this.index = 0
        boards.forEach { it.reset() }
    }

    private fun getWinners() = boards.filter { it.hasWon() }
}

class Board(private val cells: Array<Cell>) {
    private val rows get() = (0 until BOARD_SIZE).map { i -> cells.filter { it.getX() == i } }
    private val cols get() = (0 until BOARD_SIZE).map { i -> cells.filter { it.getY() == i } }
    val score get() = cells.filter { !it.marked }.sumOf { it.number }

    fun hasWon(): Boolean {
        val checkAll: (List<Cell>) -> Boolean = { it.all { c -> c.marked } }
        return rows.any(checkAll) || cols.any(checkAll)
    }

    fun mark(num: Int) = cells.filter { it.number == num }.forEach { it.marked = true }
    fun reset() = cells.forEach { it.marked = false }
}

data class Cell(val number: Int, val index: Int, var marked: Boolean = false) {
    fun getX() = index % BOARD_SIZE
    fun getY() = floor(index / BOARD_SIZE.toDouble()).toInt()
}