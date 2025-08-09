package com.grappenmaker.aoc.practice25.everybodycodes

fun ECSolveContext.day09() {
    fun createRecursive(stamps: List<Int>, dp: MutableMap<Int, Int>, shortcut: Boolean): DeepRecursiveFunction<Int, Int> {
        val func = DeepRecursiveFunction<Int, Int> { left ->
            if (left == 0) return@DeepRecursiveFunction 0
            if (left in dp) return@DeepRecursiveFunction dp.getValue(left)

            var best = 100000

            for (stamp in stamps) {
                if (stamp > left) break

                val next = if (shortcut) {
                    callRecursive(left % stamp) + left / stamp
                } else {
                    callRecursive(left - stamp) + 1
                }

                best = minOf(best, next)
            }

            dp[left] = best
            best
        }

        for (i in 0..1000) func(i) // initialize dp table (optimization because it reduces recursive depth

        return func
    }

//    fun recurse(left: Int, stamps: List<Int>, dp: MutableMap<Int, Int>, shortcut: Boolean): Int {
//        if (left == 0) return 0
//        if (left in dp) return dp.getValue(left)
//
//        var best = 100000
//
//        for (stamp in stamps) {
//            if (stamp > left) break
//
//            val next = if (shortcut) {
//                recurse(left % stamp, stamps, dp, true) + left / stamp
//            } else {
//                recurse(left - stamp, stamps, dp, false) + 1
//            }
//
//            best = minOf(best, next)
//        }
//
//        dp[left] = best
//        return best
//    }

    fun ECInput.solve(stamps: List<Int>, shortcut: Boolean): Int {
        var ans = 0
        val dp = hashMapOf<Int, Int>()
        val recurse = createRecursive(stamps, dp, shortcut)

        for (l in inputLines) {
            val num = l.toInt()
            ans += recurse(num)
        }

        return ans
    }

    partOne = partOneInput.solve(listOf(1, 3, 5, 10), true)
    partTwo = partTwoInput.solve(listOf(1, 3, 5, 10, 15, 16, 20, 24, 25, 30), false)

    var ans = 0

    val nums = partThreeInput.inputLines.map { it.toInt() }
    val stamps = listOf(1, 3, 5, 10, 15, 16, 20, 24, 25, 30, 37, 38, 49, 50, 74, 75, 100, 101)
    val recurse = createRecursive(stamps, hashMapOf(), false)

    for (n in nums) {
        var left = n / 2
        var right = n - left
        var optimal = 100000000

        while (right - left <= 100) {
            optimal = minOf(optimal, recurse(left) + recurse(right))
            left--
            right++
        }

        ans += optimal
    }

    partThree = ans
}