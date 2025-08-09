package com.grappenmaker.aoc.practice25.everybodycodes

import com.grappenmaker.aoc.*

fun ECSolveContext.day07() {
    fun computeOps(g: CharGrid): List<Char> {
        var curr = Point(0, 0)
        var dir = Direction.RIGHT
        val res = StringBuilder()

        do {
            res.append(g[curr])

            val tn = curr + dir
            if (tn in g && g[tn] != ' ') {
                curr = tn
                continue
            }

            val dl = dir.next(-1)
            val dr = dir.next(1)

            if (curr + dl in g && g[curr + dl] != ' ') {
                curr += dl
                dir = dl
                continue
            }

            if (curr + dr in g && g[curr + dr] != ' ') {
                curr += dr
                dir = dr
                continue
            }

            break
        } while ((curr.x != 0 || curr.y != 0) && curr in g)

        return res.toString().toList().rotate(-1)
    }

    fun eval(ops: List<Char>, alsoOps: List<Char>, rounds: Int): Long {
        var curr = 10
        var ans = 0L

        for (i in 0..<rounds) {
            for ((j, o) in ops.withIndex()) {
                val idx = i * ops.size + j

                when (o) {
                    '+' -> curr++
                    '-' -> curr--
                    else -> when (alsoOps[idx % alsoOps.size]) {
                        '+' -> curr++
                        '-' -> curr--
                    }
                }

                ans += curr
            }
        }

        return ans
    }

    fun ECInput.solveEarly(g: String, rounds: Int): String {
        val ops = computeOps(g.lines().asCharGrid())
        val order = mutableListOf<Pair<Char, Long>>()

        for (l in inputLines) {
            val (f, s) = l.split(":")
            order += f.single() to eval(ops, s.split(",").map { it.single() }, rounds)
        }

        order.sortByDescending { it.second }
        return order.joinToString("") { it.first.toString() }
    }

    partOne = partOneInput.solveEarly("==========", 1)
    partTwo = partTwoInput.solveEarly("""
        S-=++=-==++=++=-=+=-=+=+=--=-=++=-==++=-+=-=+=-=+=+=++=-+==++=++=-=-=--
        -                                                                     -
        =                                                                     =
        +                                                                     +
        =                                                                     +
        +                                                                     =
        =                                                                     =
        -                                                                     -
        --==++++==+=+++-=+=-=+=-+-=+-=+-=+=-=+=--=+++=++=+++==++==--=+=++==+++-
    """.trimIndent(), 10)

    val partThreeOps = computeOps("""
        S+= +=-== +=++=     =+=+=--=    =-= ++=     +=-  =+=++=-+==+ =++=-=-=--
        - + +   + =   =     =      =   == = - -     - =  =         =-=        -
        = + + +-- =-= ==-==-= --++ +  == == = +     - =  =    ==++=    =++=-=++
        + + + =     +         =  + + == == ++ =     = =  ==   =   = =++=       
        = = + + +== +==     =++ == =+=  =  +  +==-=++ =   =++ --= + =          
        + ==- = + =   = =+= =   =       ++--          +     =   = = =--= ==++==
        =     ==- ==+-- = = = ++= +=--      ==+ ==--= +--+=-= ==- ==   =+=    =
        -               = = = =   +  +  ==+ = = +   =        ++    =          -
        -               = + + =   +  -  = + = = +   =        +     =          -
        --==++++==+=+++-= =-= =-+-=  =+-= =-= =--   +=++=+++==     -=+=++==+++-
    """.trimIndent().lines().asCharGrid())

    val toBeat = eval(partThreeOps, partThreeInput.input.drop(2).split(",").map { it.single() }, 11)

    fun count(curr: String, a: Int, b: Int, c: Int): Int {
        if (curr.length == 11) return if (eval(partThreeOps, curr.toList(), 11) > toBeat) 1 else 0

        var ans = 0
        if (a < 5) ans += count("$curr+", a + 1, b, c)
        if (b < 3) ans += count("$curr-", a, b + 1, c)
        if (c < 3) ans += count("$curr=", a, b, c + 1)

        return ans
    }

    partThree = count("", 0, 0, 0)
}