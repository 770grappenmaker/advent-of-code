package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.year22.Direction.*
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val dAdjacentSides = enumValues<Direction>().map { it.toPoint() }
val dAdjacentDiagonals = listOf(UP + RIGHT, DOWN + RIGHT, UP + LEFT, DOWN + LEFT)
val dAllAdjacent = dAdjacentSides + dAdjacentDiagonals

data class Point(val x: Int, val y: Int)

fun Pair<Int, Int>.toPoint() = Point(first, second)
fun Point.toIndex(width: Int) = x + y * width
fun asPointIndex(x: Int, y: Int, width: Int) = x + y * width
fun pointFromIndex(index: Int, width: Int) = Point(index % width, index / width)

private fun Point.getAdjacent(of: List<Point>, width: Int, height: Int) =
    of.map { this + it }.filter { it.x in 0 until width && it.y in 0 until height }

fun Point.adjacentSides(width: Int, height: Int) = getAdjacent(dAdjacentSides, width, height)
fun Point.adjacentDiagonals(width: Int, height: Int) = getAdjacent(dAdjacentDiagonals, width, height)
fun Point.allAdjacent(width: Int, height: Int) = getAdjacent(dAllAdjacent, width, height)

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)
operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)
operator fun Point.times(tim: Int) = Point(x * tim, y * tim)
operator fun Point.div(by: Int) = Point(x / by, y / by)
operator fun Point.rem(with: Int) = Point(x % with, y % with)
operator fun Point.unaryMinus() = Point(-x, -y)
operator fun Point.rangeTo(other: Point) = Line(this, other)

val Point.manhattanDistance get() = abs(x) + abs(y)
infix fun Point.manhattanDistanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)

enum class Direction(val dx: Int, val dy: Int) {
    UP(0, 1), RIGHT(1, 0), DOWN(0, -1), LEFT(-1, 0);

    fun next(by: Int = 1) = values()[(ordinal + by).mod(4)]
}

operator fun Point.plus(other: Direction) = Point(x + other.dx, y + other.dy)
operator fun Point.minus(other: Direction) = Point(x - other.dx, y - other.dy)
fun Direction.toPoint() = Point(dx, dy)

operator fun Direction.plus(other: Direction) = Point(dx + other.dx, dy + other.dy)
operator fun Direction.minus(other: Direction) = Point(dx - other.dx, dy - other.dy)
operator fun Direction.times(n: Int) = Point(dx * n, dy * n)

data class Line(val a: Point, val b: Point) {
    val minX get() = min(a.x, b.x)
    val minY get() = min(a.y, b.y)
    val maxX get() = max(a.x, b.x)
    val maxY get() = max(a.y, b.y)
}

fun Pair<Point, Point>.toLine() = Line(first, second)

operator fun Line.contains(some: Point) = some.x in minX..maxX && some.y in minY..maxY

interface Plane {
    val width: Int
    val height: Int
    val allPoints get() = (0 until width * height).map { pointFromIndex(it) }

    fun Point.adjacentSides() = getAdjacent(dAdjacentSides, width, height)
    fun Point.adjacentSideIndices() = adjacentSides().toIndices()
    fun Point.adjacentDiagonals() = getAdjacent(dAdjacentDiagonals, width, height)
    fun Point.adjacentDiagonalIndices() = adjacentDiagonals().toIndices()
    fun Point.allAdjacent() = getAdjacent(dAllAdjacent, width, height)
    fun Point.allAdjacentIndices() = allAdjacent().toIndices()

    fun Point.toIndex() = x + y * width
    fun toPointIndex(x: Int, y: Int) = x + y * width
    fun pointFromIndex(index: Int) = Point(index % width, index / width)
    fun List<Point>.toIndices() = map { it.toIndex() }
}

val Plane.topLeftCorner get() = Point(0, 0)
val Plane.topRightCorner get() = Point(width - 1, 0)
val Plane.bottomLeftCorner get() = Point(0, height - 1)
val Plane.bottomRightCorner get() = Point(width - 1, height - 1)

val Plane.area get() = width * height
operator fun Plane.contains(point: Point) = point.x in 0 until width && point.y in 0 until height

data class Rectangle(val a: Point, val b: Point) : Plane {
    override val width get() = abs(a.x - b.x)
    override val height get() = abs(a.y - b.y)
}

val Rectangle.points get() = (a.x..b.x).flatMap { x -> (a.y..b.y).map { Point(x, it) } }

fun sizedRect(width: Int, height: Int) = Rectangle(Point(0, 0), Point(width, height))
fun centeredRect(width: Int, height: Int) = Rectangle(Point(-width / 2, -height / 2), Point(width / 2, height / 2))

interface GridLike<T> : Plane {
    val elements: List<T>

    fun Point.adjacentSideElements() = adjacentSides().map { elements[it.toIndex()] }
    fun Point.adjacentDiagonalElements() = adjacentDiagonals().map { elements[it.toIndex()] }
    fun Point.allAdjacentElements() = allAdjacent().map { elements[it.toIndex()] }

    operator fun get(by: Point) = elements[by.toIndex()]
}

class MutableGrid<T>(
    override val width: Int,
    override val height: Int,
    override val elements: MutableList<T>
) : MutableList<T> by elements, GridLike<T> {
    init {
        assertDimensions(width, height)
    }

    operator fun set(by: Point, value: T) = set(by.toIndex(), value)
}

fun <T> MutableGrid<T>.asGrid() = Grid(width, height, elements.toList())

class Grid<T>(
    override val width: Int,
    override val height: Int,
    override val elements: List<T>
) : List<T> by elements, GridLike<T> {
    init {
        assertDimensions(width, height)
    }
}

fun <T> Grid<T>.asMutableGrid() = MutableGrid(width, height, elements.toMutableList())
fun <T> Grid<T>.asPseudoGrid(scale: Int) = PseudoGrid(width, height, elements, scale)
fun <T> Grid<T>.asPseudoGridMut(scale: Int) = PseudoGrid(width, height, elements.toMutableList(), scale)

inline fun <T> List<String>.asGrid(transform: (Char) -> T) = Grid(
    width = this[0].length,
    height = size,
    elements = flatMap { it.map(transform) }.toMutableList()
)

fun List<String>.asDigitGrid() = asGrid { it.code - 48 }
fun List<String>.asMutableDigitGrid() = asDigitGrid().asMutableGrid()

class PseudoGrid<T>(
    val actualWidth: Int,
    val actualHeight: Int,
    override val elements: List<T>,
    scalingFactor: Int
) : List<T> by elements, GridLike<T> {
    init {
        assertDimensions(actualWidth, actualHeight)
    }

    override val width = actualWidth * scalingFactor
    override val height = actualHeight * scalingFactor

    override fun get(index: Int): T {
        require(index in 0..area) { "Index out of bounds" }
        val (x, y) = pointFromIndex(index)
        return elements[(x % actualWidth) + (y % actualHeight) * actualWidth]
    }

    override fun get(by: Point) = get(by.toIndex())
}

private fun <T> List<T>.assertDimensions(width: Int, height: Int) =
    require(size == width * height) { "Dimensions of list does not match ($width x $height)" }

fun intGrid(width: Int, height: Int) = Grid(width, height, IntArray(width * height).toList())
fun mutableIntGrid(width: Int, height: Int) = MutableGrid(width, height, IntArray(width * height).toMutableList())
fun booleanGrid(width: Int, height: Int) = Grid(width, height, BooleanArray(width * height).toList())
fun mutableBooleanGrid(width: Int, height: Int) =
    MutableGrid(width, height, BooleanArray(width * height).toMutableList())

typealias BooleanGrid = GridLike<Boolean>
typealias IntGrid = GridLike<Int>

fun BooleanGrid.countTrue() = elements.count { it }
fun BooleanGrid.countFalse() = elements.count { !it }

fun <T> queueOf() = ArrayDeque<T>()
fun <T> queueOf(initial: T) = ArrayDeque<T>().also { it.add(initial) }
fun <T> queueOf(vararg elements: T) = ArrayDeque<T>().also { it.addAll(elements) }

inline fun <T> ArrayDeque<T>.drain(use: (T) -> Unit) {
    while (isNotEmpty()) use(removeLast())
}

inline fun <T> Queue<T>.drain(use: (T) -> Unit) {
    while (isNotEmpty()) use(remove()!!)
}

data class BFSResult<T>(val end: T?, val distance: Int, val seen: Set<T>)

inline fun <T> bfs(initial: T, isEnd: (T) -> Boolean, neighbors: (T) -> Iterable<T>): BFSResult<T> {
    val seen = hashSetOf(initial)
    val queue = queueOf(initial)
    var distance = -1 // -1 because for first element 1 gets added as well

    queue.drain { next ->
        distance++
        if (isEnd(next)) return BFSResult(next, distance, seen)
        else neighbors(next).forEach { if (seen.add(it)) queue.add(it) }
    }

    return BFSResult(null, distance, seen)
}

inline fun <T> floodFill(initial: T, neighbors: (T) -> Iterable<T>) = bfs(initial, { false }, neighbors).seen

inline fun <T> GridLike<T>.bfs(start: Point, isEnd: (Point) -> Boolean, diagonals: Boolean = false) =
    bfsPoint(start, isEnd, diagonals).let { p -> BFSResult(p.end?.let { get(it) }, p.distance, p.seen) }

fun <T> GridLike<T>.bfs(start: Point, end: Point, diagonals: Boolean = false) = bfs(start, { it == end }, diagonals)

inline fun <T> GridLike<T>.bfsPoint(start: Point, isEnd: (Point) -> Boolean, diagonals: Boolean = false) =
    bfs(start, isEnd) { if (diagonals) it.allAdjacent() else it.adjacentSides() }

fun <T> GridLike<T>.bfsPoint(start: Point, end: Point, diagonals: Boolean = false) =
    bfsPoint(start, { it == end }, diagonals)

inline fun <T> GridLike<T>.floodFill(start: Point, condition: (Point) -> Boolean, diagonals: Boolean = false) =
    floodFill(start) { (if (diagonals) it.allAdjacent() else it.adjacentSides()).filter(condition) }

data class DijkstraPath<T>(val end: T, val path: List<T>, val cost: Int)

// element - cost
typealias SearchNode<T> = Pair<T, Int>

inline fun <T> dijkstra(
    initial: T, // assuming start is cost zero
    isEnd: (T) -> Boolean,
    neighbors: (T) -> Iterable<T>,
    crossinline findCost: (T) -> Int
): DijkstraPath<T>? {
    val seen = hashSetOf<T>()
    val cameFrom = hashMapOf<T, T>()
    val queue = PriorityQueue<SearchNode<T>>(compareBy { (_, c1) -> c1 })
    queue.add(initial to 0)
    seen.add(initial)

    queue.drain { (current, currentCost) ->
        if (isEnd(current)) return DijkstraPath(
            end = current,
            path = generateSequence(current) { cameFrom[it] }.toList().asReversed(),
            cost = currentCost
        )

        neighbors(current).forEach { new -> if (seen.add(new)) queue.offer(new to currentCost + findCost(new)) }
    }

    return null
}

fun <T> GridLike<T>.dijkstra(
    initial: Point,
    isEnd: (Point) -> Boolean,
    findCost: (Point) -> Int,
    diagonals: Boolean = false
) = dijkstra(initial, isEnd, { if (diagonals) it.allAdjacent() else it.adjacentSides() }, findCost)

fun <T> GridLike<T>.dijkstra(
    initial: Point,
    end: Point,
    findCost: (Point) -> Int,
    diagonals: Boolean = false
) = dijkstra(initial, { it == end }, findCost, diagonals)

fun IntGrid.dijkstra(initial: Point, end: Point, diagonals: Boolean = false) =
    dijkstra(initial, end, { this[it] }, diagonals)