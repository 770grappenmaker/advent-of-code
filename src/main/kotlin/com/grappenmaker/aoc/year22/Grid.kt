package com.grappenmaker.aoc.year22

import com.grappenmaker.aoc.year22.Direction.*
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val dAdjacentSides = enumValues<Direction>().map { it.toPoint() }
val dAdjacentDiagonals = listOf(DOWN + RIGHT, UP + RIGHT, DOWN + LEFT, UP + LEFT)
val dAllAdjacent = dAdjacentSides + dAdjacentDiagonals

data class Point(val x: Int, val y: Int)

fun Pair<Int, Int>.toPoint() = Point(first, second)
fun Point.toIndex(width: Int) = x + y * width
fun asPointIndex(x: Int, y: Int, width: Int) = x + y * width
fun pointFromIndex(index: Int, width: Int) = Point(index % width, index / width)

private fun Point.getAdjacent(of: List<Point>, width: Int, height: Int) =
    of.map { this + it }.filter { it.x in 0 until width && it.y in 0 until height }

fun Point.adjacentSides(width: Int, height: Int) = getAdjacent(dAdjacentSides, width, height)
fun Point.adjacentSidesInf() = dAdjacentSides.map { it + this }
fun Point.adjacentDiagonals(width: Int, height: Int) = getAdjacent(dAdjacentDiagonals, width, height)
fun Point.adjacentDiagonalsInf() = dAdjacentDiagonals.map { it + this }
fun Point.allAdjacent(width: Int, height: Int) = getAdjacent(dAllAdjacent, width, height)
fun Point.allAdjacentInf() = dAllAdjacent.map { it + this }

fun Point.adjacentTo(other: Point) = abs(x - other.x) <= 1 && abs(y - other.y) <= 1
fun Point.clamp(bx: Int, by: Int) = Point(min(max(x, -bx), bx), min(max(y, -by), by))
fun Point.clamp(minX: Int, maxX: Int, minY: Int, maxY: Int) = Point(min(max(x, minX), maxX), min(max(y, minY), maxY))
fun Point.clampUnit() = clamp(1, 1)

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)
operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)
operator fun Point.times(tim: Int) = Point(x * tim, y * tim)
operator fun Point.div(by: Int) = Point(x / by, y / by)
operator fun Point.rem(with: Int) = Point(x % with, y % with)
operator fun Point.unaryMinus() = Point(-x, -y)
operator fun Point.rangeTo(other: Point) = Line(this, other)

fun Point.map(block: (x: Int, y: Int) -> Pair<Int, Int>) = block(x, y).toPoint()
fun Point.mapX(block: (x: Int) -> Int) = copy(x = block(x))
fun Point.mapY(block: (y: Int) -> Int) = copy(y = block(y))

val Point.manhattanDistance get() = abs(x) + abs(y)
infix fun Point.manhattanDistanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)

val Point.chebyshevDistance get() = max(abs(x), abs(y))
infix fun Point.chebyshevDistanceTo(other: Point) = max(abs(x - other.x), abs(y - other.y))

enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

    fun next(by: Int = 1) = values()[(ordinal + by).mod(4)]
}

operator fun Point.plus(other: Direction) = Point(x + other.dx, y + other.dy)
operator fun Point.minus(other: Direction) = Point(x - other.dx, y - other.dy)
fun Direction.toPoint() = Point(dx, dy)

operator fun Direction.plus(other: Direction) = Point(dx + other.dx, dy + other.dy)
operator fun Direction.minus(other: Direction) = Point(dx - other.dx, dy - other.dy)
operator fun Direction.times(n: Int) = Point(dx * n, dy * n)
operator fun Direction.unaryMinus() = (-toPoint()).asDirection()

fun Point.asDirection() = enumValues<Direction>().first { it.dx == x && it.dy == y }
fun Point.deltaDir(other: Point) = (other - this).asDirection()

data class Line(val a: Point, val b: Point) {
    val minX get() = min(a.x, b.x)
    val minY get() = min(a.y, b.y)
    val maxX get() = max(a.x, b.x)
    val maxY get() = max(a.y, b.y)
    val xRange get() = rangeDirection(a.x, b.x)
    val yRange get() = rangeDirection(a.y, b.y)
}

fun Line.isHorizontal() = a.y == b.y
fun Line.isVertical() = a.x == b.x
fun Line.isDiagonal() = abs(a.x - b.x) == abs(a.y - b.y)
fun Line.allPoints() = when {
    isHorizontal() -> xRange.map { Point(it, a.y) }
    isVertical() -> yRange.map { Point(a.x, it) }
    isDiagonal() -> xRange.zip(yRange).map(Pair<Int, Int>::toPoint)
    else -> error("Line must be horizontal, vertical or diagonal (from $a to $b)")
}

fun Pair<Point, Point>.toLine() = Line(first, second)

operator fun Line.contains(some: Point) = some.x in minX..maxX && some.y in minY..maxY
fun Line.intersects(other: Line) = intersections(other).isNotEmpty()
fun Line.intersections(other: Line) = allPoints().intersect(other.allPoints().toSet())

fun List<Line>.connect() = flatMap { it.allPoints().dropLast(1) } + last().b

interface Plane {
    val width: Int
    val height: Int

    fun Point.adjacentSides() = getAdjacent(dAdjacentSides, width, height)
    fun Point.adjacentSideIndices() = adjacentSides().toIndices()

    fun Point.adjacentDiagonals() = getAdjacent(dAdjacentDiagonals, width, height)
    fun Point.adjacentDiagonalIndices() = adjacentDiagonals().toIndices()

    fun Point.allAdjacent() = getAdjacent(dAllAdjacent, width, height)
    fun Point.allAdjacentIndices() = allAdjacent().toIndices()
    fun Point.clamp() = Point(x.coerceIn(xRange), y.coerceIn(yRange))

    // up->down
    fun column(index: Int): List<Point> {
        require(index in xRange) { "column out of bounds: $index in width $width" }
        return yRange.map { Point(index, it) }
    }

    // left->right
    fun row(index: Int): List<Point> {
        require(index in yRange) { "row out of bounds: $index in height $height" }
        return xRange.map { Point(it, index) }
    }

    fun Point.toIndex() = x + y * width
    fun toPointIndex(x: Int, y: Int) = x + y * width
    fun pointFromIndex(index: Int) = Point(index % width, index / width)
    fun List<Point>.toIndices() = map { it.toIndex() }
}

val Plane.points get() = (0 until width * height).map { pointFromIndex(it) }
val Plane.pointsSequence get() = sequence { (0 until width * height).forEach { yield(pointFromIndex(it)) } }
val Plane.xRange get() = 0 until width
val Plane.yRange get() = 0 until height

val Plane.rows get() = yRange.map { y -> xRange.map { Point(it, y) } }
val Plane.columns get() = xRange.map { x -> yRange.map { Point(x, it) } }

val Plane.topLeftCorner get() = Point(0, 0)
val Plane.topRightCorner get() = Point(width - 1, 0)
val Plane.bottomLeftCorner get() = Point(0, height - 1)
val Plane.bottomRightCorner get() = Point(width - 1, height - 1)
val Plane.corners get() = listOf(topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner)

val Plane.area get() = width * height
operator fun Plane.contains(point: Point) = point.x in 0 until width && point.y in 0 until height

data class SimplePlane(override val width: Int, override val height: Int) : Plane

data class Rectangle(val a: Point, val b: Point) {
    val width get() = abs(a.x - b.x)
    val height get() = abs(a.y - b.y)
    val minX get() = min(a.x, b.x)
    val maxX get() = max(a.x, b.x)
    val minY get() = min(a.y, b.y)
    val maxY get() = max(a.y, b.y)
    val xRange get() = minX..maxX
    val yRange get() = minY..maxY
}

val Rectangle.topLeftCorner get() = Point(minX, minY)
val Rectangle.topRightCorner get() = Point(maxX, minY)
val Rectangle.bottomLeftCorner get() = Point(minX, maxY)
val Rectangle.bottomRightCorner get() = Point(maxX, maxY)
val Rectangle.corners get() = listOf(topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner)

fun Rectangle.asPlane() = SimplePlane(width, height)
val Rectangle.points get() = (a.x..b.x).flatMap { x -> (a.y..b.y).map { Point(x, it) } }

operator fun Rectangle.contains(point: Point) = point.x in xRange && point.y in yRange
fun Rectangle.overlaps(other: Rectangle) =
    minX < other.maxX && maxX > other.minX && minY < other.maxY && maxY > other.minY

fun Rectangle.overlapsInclusive(other: Rectangle) =
    minX <= other.maxX && maxX >= other.minX && minY <= other.maxY && maxY >= other.minY

fun Rectangle.intersect(other: Rectangle) = points.intersect(other.points.toSet())

fun sizedRect(width: Int, height: Int, atX: Int = 0, atY: Int = 0) =
    Rectangle(Point(atX, atY), Point(atX + width - 1, atY + height - 1))

fun centeredRect(width: Int, height: Int) = Rectangle(Point(-width / 2, -height / 2), Point(width / 2, height / 2))

fun Rectangle.movedTo(x: Int, y: Int) = sizedRect(width, height, x, y)

interface GridLike<T> : Plane {
    val elements: List<T>

    fun Point.adjacentSideElements() = adjacentSides().map { get(it) }
    fun Point.adjacentSideIndexed() = adjacentSides().map { it to get(it) }
    fun Point.adjacentDiagonalElements() = adjacentDiagonals().map { get(it) }
    fun Point.adjacentDiagonalIndexed() = adjacentDiagonals().map { it to get(it) }
    fun Point.allAdjacentElements() = allAdjacent().map { get(it) }
    fun Point.allAdjacentIndexed() = allAdjacent().map { it to get(it) }

    fun rowValues(index: Int) = row(index).map { this[it] }
    fun columnValues(index: Int) = column(index).map { this[it] }

    operator fun get(by: Point) = elements[by.toIndex()]
    fun getOrNull(by: Point) = elements.getOrNull(by.toIndex())
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

fun <T> MutableGrid<T>.setColumn(index: Int, values: List<T>) {
    require(values.size == height) { "Invalid length ${values.size} for height $height" }
    column(index).zip(values).forEach { (point, value) -> this[point] = value }
}

fun <T> MutableGrid<T>.setRow(index: Int, values: List<T>) {
    require(values.size == width) { "Invalid length ${values.size} for width $width" }
    row(index).zip(values).forEach { (point, value) -> this[point] = value }
}

fun <T> MutableGrid<T>.rotateRow(row: Int, amount: Int) = setRow(row, rowValues(row).rotate(amount))
fun <T> MutableGrid<T>.rotateColumn(column: Int, amount: Int) = setColumn(column, columnValues(column).rotate(amount))

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

fun List<String>.asCharGrid() = asGrid { it }

inline fun <T> List<String>.asMutableGrid(transform: (Char) -> T) = asGrid(transform).asMutableGrid()

fun <T> List<List<T>>.asGrid() = Grid(size, this[0].size, flatten())
fun <T> List<T>.asGrid(width: Int) = Grid(width, size / width, this)

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

inline fun <T> buildGrid(
    width: Int,
    height: Int,
    default: (Point) -> T,
    block: MutableGrid<T>.() -> Unit
): Grid<T> {
    val tempGrid = mutableGrid(width, height, default)
    tempGrid.block()
    return tempGrid.asGrid()
}

inline fun <T> buildGridDefault(width: Int, height: Int, default: T, block: MutableGrid<T>.() -> Unit) =
    buildGrid(width, height, { default }, block)

inline fun buildBooleanGrid(
    width: Int,
    height: Int,
    default: Boolean = false,
    block: MutableBooleanGrid.() -> Unit
) = buildGrid(width, height, { default }, block)

inline fun buildIntGrid(
    width: Int,
    height: Int,
    default: Int = 0,
    block: MutableIntGrid.() -> Unit
) = buildGrid(width, height, { default }, block)

inline fun <T> grid(width: Int, height: Int, init: (Point) -> T) =
    Grid(width, height, (0 until width * height).map { init(pointFromIndex(it, width)) })

inline fun <T> mutableGrid(width: Int, height: Int, init: (Point) -> T) =
    MutableGrid(width, height, (0 until width * height).map { init(pointFromIndex(it, width)) }.toMutableList())

typealias BooleanGrid = GridLike<Boolean>
typealias MutableBooleanGrid = MutableGrid<Boolean>
typealias IntGrid = GridLike<Int>
typealias MutableIntGrid = MutableGrid<Int>

fun MutableBooleanGrid.toggle(point: Point) {
    this[point] = !this[point]
}

fun MutableBooleanGrid.enable(point: Point) {
    this[point] = true
}

fun MutableBooleanGrid.disable(point: Point) {
    this[point] = false
}

fun BooleanGrid.countTrue() = elements.count { it }
fun BooleanGrid.countFalse() = elements.count { !it }

fun MutableIntGrid.increment(point: Point) {
    this[point]++
}

fun MutableIntGrid.decrement(point: Point) {
    this[point]--
}

inline fun <T, N> GridLike<T>.mapElements(transform: (T) -> N) = Grid(width, height, elements.map(transform))
inline fun <T, N> GridLike<T>.mapIndexedElements(transform: (Point, T) -> N) =
    Grid(width, height, elements.mapIndexed { idx, t -> transform(pointFromIndex(idx), t) })

fun <T> queueOf() = ArrayDeque<T>()
fun <T> queueOf(initial: T) = ArrayDeque<T>().also { it.add(initial) }
fun <T> queueOf(vararg elements: T) = ArrayDeque<T>().also { it.addAll(elements) }

inline fun <T> ArrayDeque<T>.drain(use: (T) -> Unit) {
    while (isNotEmpty()) use(removeLast())
}

inline fun <T> Queue<T>.drain(use: (T) -> Unit) {
    while (isNotEmpty()) use(remove()!!)
}

data class BFSResult<T>(val end: T?, val seen: Set<T>)

inline fun <T> bfs(
    initial: T,
    isEnd: (T) -> Boolean,
    neighbors: (T) -> Iterable<T>
): BFSResult<T> {
    val seen = hashSetOf(initial)
    val queue = queueOf(initial)

    queue.drain { next ->
        if (isEnd(next)) return BFSResult(next, seen)
        else neighbors(next).forEach { if (seen.add(it)) queue.add(it) }
    }

    return BFSResult(null, seen)
}

inline fun <T> floodFill(initial: T, neighbors: (T) -> Iterable<T>) = bfs(initial, { false }, neighbors).seen

inline fun <T> GridLike<T>.bfs(start: Point, isEnd: (Point) -> Boolean, diagonals: Boolean = false) =
    bfsPoint(start, isEnd, diagonals).let { p -> BFSResult(p.end?.let { get(it) }, p.seen) }

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

fun <T> searchQueue(initial: T) = PriorityQueue<SearchNode<T>>(compareBy { (_, c) -> c })
    .also { it.add(initial to 0) }

inline fun <T> dijkstra(
    initial: T, // assuming start is cost zero
    isEnd: (T) -> Boolean,
    neighbors: (T) -> Iterable<T>,
    crossinline findCost: (T) -> Int
): DijkstraPath<T>? {
    val seen = hashSetOf(initial)
    val cameFrom = hashMapOf<T, T>()
    val queue = searchQueue(initial)

    queue.drain { (current, currentCost) ->
        if (isEnd(current)) return DijkstraPath(
            end = current,
            path = generateSequence(current) { cameFrom[it] }.toList().asReversed(),
            cost = currentCost
        )

        neighbors(current).forEach { new ->
            if (seen.add(new)) {
                cameFrom[new] = current
                queue.offer(new to currentCost + findCost(new))
            }
        }
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

@JvmName("debugAny")
fun <T> Grid<T>.debug() = rows.joinToString("\n") { row -> row.joinToString("") { this[it].toString() } }

@JvmName("debugBooleans")
fun BooleanGrid.debug(
    on: String = "#",
    off: String = "."
) = rows.joinToString("\n") { row -> row.joinToString("") { if (this[it]) on else off } }

typealias Graph<T> = Map<T, List<T>>

fun <T> Graph<T>.dijkstra(start: T, end: T): DijkstraPath<T>? =
    dijkstra(start, { it == end }, { this[it] ?: listOf() }, { 1 })

fun <T> Graph<T>.bfs(start: T) = bfs(start, { false }, { this[it] ?: listOf() })

fun <T> fillDistance(start: T, neighbors: (T) -> Iterable<T>): Map<T, Int> {
    val queue = searchQueue(start)
    val seen = hashSetOf(start)
    val result = hashMapOf<T, Int>()

    queue.drain { (p, dist) ->
        if (p !in result) result[p] = dist
        neighbors(p).forEach { if (seen.add(it)) queue.add(it to dist + 1) }
    }

    return result
}

fun <T> bfsDistance(start: T, isEnd: (T) -> Boolean, neighbors: (T) -> Iterable<T>): Int {
    val queue = searchQueue(start)
    val seen = hashSetOf(start)

    queue.drain { (p, dist) ->
        if (isEnd(p)) return dist
        neighbors(p).forEach { if (seen.add(it)) queue.add(it to dist + 1) }
    }

    error("No path found")
}

fun Iterable<Point>.shiftPositive(): List<Point> {
    val minX = min(minX(), 0)
    val minY = min(minY(), 0)
    if (minX == 0 && minY == 0) return toList()

    val shift = Point(abs(minX), abs(minY))
    return map { it + shift }
}

fun Iterable<Point>.minimizeEmpty(): List<Point> {
    val minX = minX()
    val minY = minY()
    if (minX == 0 && minY == 0) return toList()

    return map { Point(it.x - minX, it.y - minY) }
}

fun Iterable<Point>.minimizeEmptyX(): List<Point> {
    val minX = minX()
    if (minX == 0) return toList()
    return map { Point(it.x - minX, it.y) }
}

fun Iterable<Point>.minimizeEmptyY(): List<Point> {
    val minY = minY()
    if (minY == 0) return toList()
    return map { Point(it.x, it.y - minY) }
}

fun Iterable<Point>.asBooleanGrid() = grid(maxX() + 1, maxY() + 1) { it in this }

fun Iterable<Point>.minX() = minOf { it.x }
fun Iterable<Point>.maxX() = maxOf { it.x }
fun Iterable<Point>.minY() = minOf { it.y }
fun Iterable<Point>.maxY() = maxOf { it.y }
fun Iterable<Point>.shiftDelta() = Point(-minX(), -minY())

fun <T> GridLike<T>.extend(width: Int = this.width, height: Int = this.height, default: (Point) -> T) =
    grid(width, height) { getOrNull(it) ?: default(it) }

fun <T> GridLike<T>.asInfiniteGrid() = InfiniteGrid(points.associateWith { this[it] }.toMutableMap())
fun <T> Map<Point, T>.asInfiniteGrid() = InfiniteGrid(toMutableMap())

// Experimental, do not use D:
class InfiniteGrid<T>(val map: MutableMap<Point, T> = hashMapOf()) : GridLike<T>, MutableMap<Point, T> by map {
    override val width = Int.MAX_VALUE
    override val height = Int.MAX_VALUE
    override val elements get() = error("Infinite grids cannot be indexed")

    override fun get(by: Point) = map[by] ?: error("No value for $by")
    override fun getOrNull(by: Point) = map[by]
    operator fun set(point: Point, value: T) {
        map[point] = value
    }

    private fun infiniError(): Nothing = error("Grid is infinite")

    override fun column(index: Int) = infiniError()
    override fun row(index: Int) = infiniError()
    override fun Point.toIndex() = infiniError()
    override fun toPointIndex(x: Int, y: Int) = infiniError()
    override fun pointFromIndex(index: Int) = infiniError()
    override fun List<Point>.toIndices() = infiniError()
}