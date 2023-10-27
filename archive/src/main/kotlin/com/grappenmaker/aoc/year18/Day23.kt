package com.grappenmaker.aoc.year18

import com.grappenmaker.aoc.*

fun PuzzleSet.day23() = puzzle(day = 23) {
    data class Bot(val pos: Point3D, val r: Int)
    val bots = inputLines.map { l ->
        val (x, y, z, r) = l.splitInts()
        Bot(Point3D(x, y, z), r)
    }

    fun Point3D.inRange(bot: Bot) = this manhattanDistanceTo bot.pos <= bot.r

    val target = bots.maxBy { it.r }
    partOne = bots.count { it.pos.inRange(target) }.s()

    // You will have to know, above this single line of code were about 80 lines of ideas written out
    // to solve the problem. Initial idea was to... do a bunch of spatial math to hope to decrease the search area
    // to a reasonable size, then bruteforce...
    // Yeah this problem was really fucking hard to me
    fun Bot.potential(other: Bot) = pos manhattanDistanceTo other.pos <= r + other.r

    // Set because we do `in` checks later
    val graph = bots.associateWith { b -> (bots - b).filter { b.potential(it) }.toSet() }

//    // Idea: recursive algorithm to do it
//    // For each point, connect it to another, recursive down the chain
//    // Then each iteration, if there is no connection, discard it
//    fun recurse(curr: Set<Bot>): Set<Bot> {
//        if (curr.size > 2 && curr.any { p -> (curr - p).any { o -> o !in graph.getValue(p) } }) return curr
//        return curr.flatMap { p -> graph.getValue(p).filter { it !in curr }.map { o -> recurse(curr + o) } }.maxByOrNull { it.size } ?: curr
//    }

//    // That did not work, now we can be a little bit smarter about it.
//    // We can keep track of all visitable points in this iteration
//    // We can always look at the node with the most neighbours, that makes intuitive sense
//    // We can also immediately discard stuff that isn't neighbour with the next added node!
//    // That way we don't have to check anything else if that makes sense
//    fun recurse(todo: Set<Bot> = bots.toSet(), soFar: Set<Bot> = emptySet()): Set<Bot> {
//        if (todo.isEmpty()) return soFar
//
//        return todo.map { n ->
//            val neigh = graph.getValue(n)
//            recurse(todo.filterTo(hashSetOf()) { it in neigh }, soFar + n)
//        }.maxByOrNull { it.size } ?: soFar
//    }

    // That did not work either, guess I am not smart enough
    // Wikipedia with the rescue, teaching me about cliques
    /*
            algorithm BronKerbosch2(R, P, X) is
            if P and X are both empty then
                report R as a maximal clique
            choose a pivot vertex u in P ⋃ X
            for each vertex v in P \ N(u) do
                BronKerbosch2(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
                P := P \ {v}
                X := X ⋃ {v}
     */
    fun algo(r: Set<Bot> = emptySet(), p: Set<Bot> = bots.toSet(), x: Set<Bot> = emptySet()): Set<Bot> {
        if (p.isEmpty() && x.isEmpty()) return r

        // How to choose? Well, we know that we end when we find the best solution
        // We find that solution the quickest if u has the highest potential, which is of course ITS neighbour count
        // This apparently makes a huge difference, I tried the plain algorithm first which did not yield a
        // solution in a reasonable amount of time.
        val u = (p + x).maxBy { graph.getValue(it).size }
        return (p - graph.getValue(u)).map { v ->
//        return p.asSequence().map { v ->
            val neighs = graph.getValue(v)
            algo(r + v, p.intersect(neighs), x.intersect(neighs))
        }.maxBy { it.size }
    }

    // Now at the set of points of most overlap, we know that, since manhattan distance, the answer is equal to
    // the distance function (distance to center - radius, as for all circles) since any rotation to that is just
    // the same if that makes sense (it does not)
    // Check for max because farthest away on circle distance is closest
    // (counterintuitive, did this wrong the first time, math is hard)
    partTwo = algo().maxOf { it.pos.manhattanDistance - it.r }.s()

    // Hard puzzle, part one was really easy though... how would you come up with this algorithm yourself?
    // It makes sense where it comes from but I would have never thought of doing that...
}