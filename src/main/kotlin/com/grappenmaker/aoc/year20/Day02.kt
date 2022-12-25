package com.grappenmaker.aoc.year20

import com.grappenmaker.aoc.PuzzleSet
import com.grappenmaker.aoc.asPair
import com.grappenmaker.aoc.countContains
import com.grappenmaker.aoc.toRange

fun PuzzleSet.day2() = puzzle {
    val passwords = inputLines.map {
        val (range, letter, password) = it.split(" ")
        PasswordData(range.split("-").map(String::toInt).asPair(), letter.first(), password)
    }

    partOne = passwords.count { (range, letter, pwd) -> pwd.countContains(letter) in range.toRange() }.s()
    partTwo = passwords.count { (nums, letter, pwd) ->
        (pwd[nums.first - 1] == letter) xor (pwd[nums.second - 1] == letter)
    }.s()
}

data class PasswordData(val nums: Pair<Int, Int>, val letter: Char, val password: String)