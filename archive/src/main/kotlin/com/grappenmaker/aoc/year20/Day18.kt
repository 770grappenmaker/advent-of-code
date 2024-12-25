package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.ksp.PuzzleEntry
import com.grappenmaker.aoc.queueOf
import com.grappenmaker.aoc.removeLastN

@PuzzleEntry
fun PuzzleSet.day18() = puzzle(day = 18) {
    fun lexer(l: String): List<Token> {
        var ptr = 0
        val partials = mutableListOf<Token>()
        val groupStack = queueOf<Int>()

        while (ptr < l.length) {
            when (l[ptr]) {
                in '0'..'9' -> {
                    val startPtr = ptr
                    while (ptr < l.length && l[ptr] in '0'..'9') ptr++
                    partials += PartialLiteral(l.substring(startPtr, ptr).trim().toLong())
                    ptr--
                }

                '*' -> partials += MultiplyOperator
                '+' -> partials += AdditionOperator
                '(' -> groupStack += partials.size
                ')' -> partials += Group(partials.removeLastN(partials.size - groupStack.removeLast()))
                ' ' -> {}
                else -> error("Impossible")
            }

            ptr++
        }

        return partials
    }

    fun parser(tokens: List<Token>): ExprElement {
        var idx = 0
        var currentExpression: ExprElement? = null

        fun value(i: Int) = when (val v = tokens[i]) {
            AdditionOperator, MultiplyOperator -> error("Cannot have non value token in operator")
            is Group -> parser(v.children)
            is PartialLiteral -> Literal(v.value)
        }

        while (idx < tokens.size) {
            // DRY folks
            currentExpression = when (val curr = tokens[idx]) {
                AdditionOperator -> Addition(currentExpression ?: value(idx - 1), value(idx + 1))
                MultiplyOperator -> Multiplication(currentExpression ?: value(idx - 1), value(idx + 1))
                is Group -> currentExpression ?: parser(curr.children)
                else -> currentExpression
            }

            idx++
        }

        return currentExpression ?: error("Expression was never parsed")
    }

    fun partTwoTokens(tokens: List<Token>): List<Token> {
        val new = mutableListOf<Token>()
        var idx = 0
        while (idx in tokens.indices) {
            new += when (val v = tokens[idx]) {
                AdditionOperator -> {
                    val oldRHS = tokens[idx + 1]
                    val newRHS = if (oldRHS is Group) Group(partTwoTokens(oldRHS.children)) else oldRHS
                    Group(listOf(new.removeLast(), AdditionOperator, newRHS)).also { idx++ }
                }
                is Group -> Group(partTwoTokens(v.children))
                else -> v
            }

            idx++
        }

        return new
    }

    val lexed = inputLines.map { lexer(it) }
    fun solve(parse: (List<Token>) -> ExprElement) = lexed.sumOf { parse(it).eval() }.toString()

    partOne = solve { parser(it) }
    partTwo = solve { parser(partTwoTokens(it)) }
}

sealed interface Token
data object AdditionOperator : Token
data object MultiplyOperator : Token

@JvmInline
value class Group(val children: List<Token>) : Token

@JvmInline
value class PartialLiteral(val value: Long) : Token

sealed interface ExprElement {
    fun eval(): Long
}

data class Addition(val lhs: ExprElement, val rhs: ExprElement) : ExprElement {
    override fun eval() = lhs.eval() + rhs.eval()
}

data class Multiplication(val lhs: ExprElement, val rhs: ExprElement) : ExprElement {
    override fun eval() = lhs.eval() * rhs.eval()
}

@JvmInline
value class Literal(val value: Long) : ExprElement {
    override fun eval() = value
}