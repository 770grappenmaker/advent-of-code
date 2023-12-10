package com.grappenmaker.aoc

import com.grappenmaker.aoc.Direction.*
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.*

val dAdjacentSides = enumValues<Direction>().map { it.toPoint() }
val dAdjacentDiagonals = listOf(DOWN + RIGHT, UP + RIGHT, DOWN + LEFT, UP + LEFT)
val dAllAdjacent = dAdjacentSides + dAdjacentDiagonals

data class Point(val x: Int, val y: Int)
data class PointL(val x: Long, val y: Long)

fun Point.toL() = PointL(x.toLong(), y.toLong())

fun Pair<Int, Int>.toPoint() = Point(first, second)
fun Point.toIndex(width: Int) = x + y * width
fun asPointIndex(x: Int, y: Int, width: Int) = x + y * width
fun pointFromIndex(index: Int, width: Int) = Point(index % width, index / width)

private fun Point.getAdjacent(of: List<Point>, width: Int, height: Int) =
    of.map { this + it }.filter { it.x in 0..<width && it.y in 0..<height }

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
operator fun PointL.plus(other: PointL) = PointL(x + other.x, y + other.y)
operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)
operator fun Point.times(tim: Int) = Point(x * tim, y * tim)
operator fun Point.times(other: Point) = Point(x * other.x, y * other.y)
operator fun Point.div(by: Int) = Point(x / by, y / by)
operator fun Point.rem(with: Int) = Point(x % with, y % with)
operator fun Point.unaryMinus() = Point(-x, -y)
operator fun Point.rangeTo(other: Point) = Line(this, other)

fun Point.map(block: (x: Int, y: Int) -> Pair<Int, Int>) = block(x, y).toPoint()
fun Point.mapX(block: (x: Int) -> Int) = copy(x = block(x))
fun Point.mapY(block: (y: Int) -> Int) = copy(y = block(y))

@JvmName("mapAllCoords")
fun Point.map(block: (coord: Int) -> Int) = copy(x = block(x), y = block(y))

val Point.manhattanDistance get() = abs(x) + abs(y)
infix fun Point.manhattanDistanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)

val Point.chebyshevDistance get() = max(abs(x), abs(y))
infix fun Point.chebyshevDistanceTo(other: Point) = max(abs(x - other.x), abs(y - other.y))

val Point.euclideanDistanceSq get() = x * x + y * y
val Point.euclideanDistance get() = sqrt(euclideanDistanceSq.toDouble())
infix fun Point.euclideanDistanceSqTo(other: Point) = (other.x - x).let { it * it } + (other.y - y).let { it * it }
infix fun Point.euclideanDistanceTo(other: Point) = sqrt(euclideanDistanceSqTo(other).toDouble())

enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

    fun next(by: Int = 1) = entries[(ordinal + by).mod(4)]
}

val Direction.isHorizontal get() = this == RIGHT || this == LEFT
val Direction.isVertical get() = this == DOWN || this == UP

operator fun Point.plus(other: Direction) = Point(x + other.dx, y + other.dy)
operator fun Point.minus(other: Direction) = Point(x - other.dx, y - other.dy)
fun Direction.toPoint() = Point(dx, dy)

operator fun PointL.plus(other: Direction) = PointL(x + other.dx.toLong(), y + other.dy.toLong())

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
fun Line.isStraight() = isHorizontal() || isVertical()
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

val Plane.points get() = xRange.flatMap { x -> yRange.map { Point(x, it) } }
val Plane.pointsSequence get() = xRange.asSequence().flatMap { x -> yRange.map { Point(x, it) } }
val Plane.pointsFlipped get() = yRange.flatMap { y -> xRange.map { Point(it, y) } }
val Plane.pointsSequenceFlipped get() = yRange.asSequence().flatMap { y -> xRange.map { Point(it, y) } }
//val Plane.pointsSequence get() = sequence { for (x in xRange) for (y in yRange) yield(Point(x, y)) }

val Plane.xRange get() = 0..<width
val Plane.yRange get() = 0..<height

val Plane.rows get() = yRange.map { y -> xRange.map { Point(it, y) } }
val Plane.columns get() = xRange.map { x -> yRange.map { Point(x, it) } }

@Suppress("UnusedReceiverParameter")
val Plane.topLeftCorner get() = Point(0, 0)
val Plane.topRightCorner get() = Point(width - 1, 0)
val Plane.bottomLeftCorner get() = Point(0, height - 1)
val Plane.bottomRightCorner get() = Point(width - 1, height - 1)
val Plane.corners get() = listOf(topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner)

val Plane.area get() = width * height
val Plane.areaLong get() = width.toLong() * height.toLong()
operator fun Plane.contains(point: Point) = point.x in 0..<width && point.y in 0..<height

data class SimplePlane(override val width: Int, override val height: Int) : Plane

data class Rectangle(val a: Point, val b: Point) {
    val width get() = abs(a.x - b.x) + 1
    val height get() = abs(a.y - b.y) + 1
    val minX get() = min(a.x, b.x)
    val maxX get() = max(a.x, b.x)
    val minY get() = min(a.y, b.y)
    val maxY get() = max(a.y, b.y)
    val xRange get() = minX..maxX
    val yRange get() = minY..maxY
}

val Rectangle.area get() = width * height
val Rectangle.areaLong get() = width.toLong() * height.toLong()

val Rectangle.topLeftCorner get() = Point(minX, minY)
val Rectangle.topRightCorner get() = Point(maxX, minY)
val Rectangle.bottomLeftCorner get() = Point(minX, maxY)
val Rectangle.bottomRightCorner get() = Point(maxX, maxY)
val Rectangle.corners get() = listOf(topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner)

fun Rectangle.asPlane() = SimplePlane(width, height)
val Rectangle.points get() = (a.x..b.x).flatMap { x -> (a.y..b.y).map { Point(x, it) } }
val Rectangle.pointsSequence
    get() = sequence {
        for (x in a.x..b.x) for (y in a.y..b.y) yield(Point(x, y))
    }

operator fun Rectangle.contains(point: Point) = point.x in xRange && point.y in yRange
fun Rectangle.overlaps(other: Rectangle) =
    minX < other.maxX && maxX > other.minX && minY < other.maxY && maxY > other.minY

fun Rectangle.overlapsInclusive(other: Rectangle) =
    minX <= other.maxX && maxX >= other.minX && minY <= other.maxY && maxY >= other.minY

fun Rectangle.intersect(other: Rectangle) = points.intersect(other.points.toSet())
fun Rectangle.onEdge(other: Point) = other.x == minX || other.x == maxX || other.y == minY || other.y == maxY

fun sizedRect(width: Int, height: Int, atX: Int = 0, atY: Int = 0) =
    Rectangle(Point(atX, atY), Point(atX + width - 1, atY + height - 1))

fun centeredRect(width: Int, height: Int) = Rectangle(Point(-width / 2, -height / 2), Point(width / 2, height / 2))

fun Rectangle.movedTo(x: Int, y: Int) = sizedRect(width, height, x, y)

interface GridLike<T> : Plane, Iterable<T> {
    val elements: List<T>

    fun Point.adjacentSideElements() = adjacentSides().map { get(it) }
    fun Point.adjacentSideIndexed() = adjacentSides().map { it to get(it) }
    fun Point.adjacentDiagonalElements() = adjacentDiagonals().map { get(it) }
    fun Point.adjacentDiagonalIndexed() = adjacentDiagonals().map { it to get(it) }
    fun Point.allAdjacentElements() = allAdjacent().map { get(it) }
    fun Point.allAdjacentIndexed() = allAdjacent().map { it to get(it) }

    fun rowValues(index: Int) = row(index).map { this[it] }
    fun columnValues(index: Int) = column(index).map { this[it] }

    operator fun get(key: Point) = if (key !in this) error("Invalid key $key") else elements[key.toIndex()]
    fun getOrNull(by: Point) = if (by !in this) null else get(by)
}

val <T> GridLike<T>.rowsValues get() = yRange.map { rowValues(it) }
val <T> GridLike<T>.columnsValues get() = xRange.map { columnValues(it) }

data class MutableGrid<T>(
    override val width: Int,
    override val height: Int,
    override val elements: MutableList<T>
) : MutableList<T> by elements, GridLike<T> {
    init {
        assertDimensions(width, height)
    }

    operator fun set(by: Point, value: T) = set(by.toIndex(), value)
}

inline fun <T> MutableGrid<T>.mapInPlaceIndexedElements(transform: (Point, T) -> T) =
    elements.mapInPlaceIndexed { idx, t -> transform(pointFromIndex(idx), t) }

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

fun <T> MutableGrid<T>.insert(other: GridLike<T>, at: Point) {
    var currIdx = at.toIndex()
    other.rowsValues.forEach { row ->
        row.forEachIndexed { c, cv -> elements[currIdx + c] = cv }
        currIdx += width
    }
}

fun <T> GridLike<T>.asGrid() = Grid(width, height, elements.toList())

data class Grid<T>(
    override val width: Int,
    override val height: Int,
    override val elements: List<T>
) : List<T> by elements, GridLike<T> {
    init {
        assertDimensions(width, height)
    }
}

fun <T> GridLike<T>.asMutableGrid() = MutableGrid(width, height, elements.toMutableList())
fun <T> GridLike<T>.asPseudoGrid(scale: Int) = PseudoGrid(width, height, elements, scale)
fun <T> GridLike<T>.asPseudoGridMut(scale: Int) = PseudoGrid(width, height, elements.toMutableList(), scale)

inline fun <T> List<String>.asGrid(transform: (Char) -> T) = Grid(
    width = this[0].length,
    height = size,
    elements = flatMap { it.map(transform) }.toMutableList()
)

fun List<String>.asCharGrid() = asGrid { it }

inline fun <T> List<String>.asMutableGrid(transform: (Char) -> T) = asGrid(transform).asMutableGrid()

fun <T> List<List<T>>.asGrid() = flatten().asGrid(this[0].size)
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

    override fun get(key: Point) = get(key.toIndex())
}

private fun <T> List<T>.assertDimensions(width: Int, height: Int) = require(size == width * height) {
    "Dimensions of list does not match ($width x $height = ${width * height} != $size)"
}

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
    Grid(width, height, (0..<width * height).map { init(pointFromIndex(it, width)) })

inline fun <T> mutableGrid(width: Int, height: Int, init: (Point) -> T) =
    MutableGrid(width, height, (0..<width * height).map { init(pointFromIndex(it, width)) }.toMutableList())

typealias BooleanGrid = GridLike<Boolean>
typealias MutableBooleanGrid = MutableGrid<Boolean>
typealias IntGrid = GridLike<Int>
typealias MutableIntGrid = MutableGrid<Int>
typealias CharGrid = GridLike<Char>
typealias MutableCharGrid = MutableGrid<Char>

fun MutableBooleanGrid.toggle(point: Point) {
    this[point] = !this[point]
}

fun MutableBooleanGrid.enable(point: Point) {
    this[point] = true
}

fun MutableBooleanGrid.disable(point: Point) {
    this[point] = false
}

fun BooleanGrid.filterTrue() = points.filter { this[it] }

fun MutableIntGrid.increment(point: Point) {
    this[point]++
}

fun MutableIntGrid.decrement(point: Point) {
    this[point]--
}

inline fun <T, N> GridLike<T>.mapElements(transform: (T) -> N) = Grid(width, height, elements.map(transform))
inline fun <T, N> GridLike<T>.mapIndexedElements(transform: (Point, T) -> N) =
    Grid(width, height, elements.mapIndexed { idx, t -> transform(pointFromIndex(idx), t) })

inline fun <T> GridLike<T>.findPoints(check: (T) -> Boolean) = points.filter { check(this[it]) }
fun <T> GridLike<T>.findPointsValued(value: T): List<Point> = findPoints { it == value }

fun <T> queueOf() = ArrayDeque<T>()
fun <T> queueOf(list: Iterable<T>) = ArrayDeque<T>().also { it.addAll(list) }
fun <T> Iterable<T>.toQueue() = ArrayDeque<T>().also { it.addAll(this) }
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
        else neighbors(next).forEach { if (seen.add(it)) queue.addFirst(it) }
    }

    return BFSResult(null, seen)
}

inline fun <T> floodFill(initial: T, neighbors: (T) -> Iterable<T>) = bfs(initial, { false }, neighbors).seen

fun <T> GridLike<T>.bfs(start: Point, end: Point, diagonals: Boolean = false) = bfs(start, { it == end }, diagonals)
fun <T> GridLike<T>.bfsDistance(start: Point, end: Point, diagonals: Boolean = false) =
    bfsDistance(start, { it == end }, diagonals)

inline fun <T> GridLike<T>.bfs(start: Point, isEnd: (Point) -> Boolean, diagonals: Boolean = false) =
    bfs(start, isEnd) { if (diagonals) it.allAdjacent() else it.adjacentSides() }

inline fun <T> GridLike<T>.bfsDistance(start: Point, isEnd: (Point) -> Boolean, diagonals: Boolean = false) =
    bfsDistance(start, isEnd) { if (diagonals) it.allAdjacent() else it.adjacentSides() }

inline fun <T> GridLike<T>.floodFill(start: Point, condition: (Point) -> Boolean, diagonals: Boolean = false) =
    floodFill(start) { (if (diagonals) it.allAdjacent() else it.adjacentSides()).filter(condition) }

data class DijkstraPath<T>(val end: T, val path: List<T>, val cost: Int)

inline fun <T> dijkstra(
    initial: T, // assuming start is cost zero
    isEnd: (T) -> Boolean,
    neighbors: (T) -> Iterable<T>,
    crossinline findCost: (T) -> Int
): DijkstraPath<T>? {
    val seen = hashSetOf(initial)
    val cameFrom = hashMapOf<T, T>()
    val queue = PriorityQueue<Pair<T, Int>>(compareBy { (_, c) -> c }).also { it.add(initial to 0) }

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
fun <T> GridLike<T>.debug() = rows.joinToString("\n") { row -> row.joinToString("") { this[it].toString() } }

@JvmName("debugBooleans")
fun BooleanGrid.debug(
    on: String = "#",
    off: String = ".",
) = rows.joinToString("\n") { row -> row.joinToString("") { if (this[it]) on else off } }

typealias Graph<T> = Map<T, List<T>>

fun <T> Graph<T>.dijkstra(start: T, end: T): DijkstraPath<T>? =
    dijkstra(start, { it == end }, { this[it] ?: listOf() }, { 1 })

fun <T> Graph<T>.bfs(start: T) = bfs(start, { false }, { this[it] ?: listOf() })

inline fun <T> fillDistance(initial: T, neighbors: (T) -> Iterable<T>): Map<T, Int> {
    val queue = queueOf(initial to 0)
    val seen = hashSetOf(initial)
    val result = hashMapOf<T, Int>()

    queue.drain { (p, dist) ->
        if (p !in result) result[p] = dist
        neighbors(p).forEach { if (seen.add(it)) queue.addFirst(it to dist + 1) }
    }

    return result
}

data class BFSDistanceResult<T>(val original: BFSResult<T>, val dist: Int)

inline fun <T> bfsDistance(initial: T, isEnd: (T) -> Boolean, neighbors: (T) -> Iterable<T>): BFSDistanceResult<T> {
    val queue = queueOf(initial to 0)
    val seen = hashSetOf(initial)

    queue.drain { (p, dist) ->
        if (isEnd(p)) return BFSDistanceResult(BFSResult(p, seen), dist)
        neighbors(p).forEach { if (seen.add(it)) queue.addFirst(it to dist + 1) }
    }

    return BFSDistanceResult(BFSResult(null, seen), -1)
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
fun Iterable<Point>.minBound() = Point(minX(), minY())
fun Iterable<Point>.maxBound() = Point(maxX(), maxY())
fun Iterable<Point>.boundary() = Rectangle(minBound(), maxBound())
fun Iterable<Point>.shiftDelta() = Point(-minX(), -minY())

// not sure about these
inline fun <T> GridLike<T>.extend(width: Int = this.width, height: Int = this.height, default: (Point) -> T) =
    grid(width, height) { getOrNull(it) ?: default(it) }

inline fun <T> GridLike<T>.extendDir(x: Int = 0, y: Int = 0, default: (Point) -> T) =
    extend(width + x, height + y, default)

// "in all directions"
fun <T> GridLike<T>.expand(x: Int = 1, y: Int = 1, default: T): Grid<T> {
    val newW = width + x * 2
    val newH = height + y * 2
    val emptyX = List(newW * y) { default }
    val emptyY = List(x) { default }
    return Grid(newW, newH, emptyX + rows.flatMap { emptyY + it.map(this::get) + emptyY } + emptyX)
}

fun <T> GridLike<T>.move(dx: Int, dy: Int) = grid(width + dx, height + dy) { (x, y) -> this[Point(x - dx, y - dy)] }

fun BooleanGrid.expandEmpty(x: Int = 1, y: Int = 1) = expand(x, y, false)

fun <T> GridLike<T>.asInfiniteGrid() = InfiniteGrid(points.associateWith { this[it] }.toMutableMap())
fun <T> Map<Point, T>.asInfiniteGrid() = InfiniteGrid(toMutableMap())
fun <T> Map<Point, T>.asGrid() = grid(keys.maxX() + 1, keys.maxY() + 1) { getValue(it) }

// Experimental, do not use D:
class InfiniteGrid<T>(val map: MutableMap<Point, T> = hashMapOf()) : GridLike<T>, MutableMap<Point, T> by map {
    override val width = Int.MAX_VALUE
    override val height = Int.MAX_VALUE
    override val elements get() = infiniError()

    override fun get(key: Point) = map[key] ?: error("No value for $key")
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
    override fun iterator() = infiniError()
}

// Extra 3d stuffs
data class Point3D(val x: Int, val y: Int, val z: Int)

operator fun Point3D.plus(other: Point3D) = Point3D(x + other.x, y + other.y, z + other.z)
operator fun Point3D.minus(other: Point3D) = Point3D(x - other.x, y - other.y, z - other.z)
operator fun Point3D.times(tim: Int) = Point3D(x * tim, y * tim, z * tim)
operator fun Point3D.times(other: Point3D) = Point3D(x * other.x, y * other.y, z * other.z)
operator fun Point3D.div(by: Int) = Point3D(x / by, y / by, z / by)
operator fun Point3D.rem(with: Int) = Point3D(x % with, y % with, z % with)
operator fun Point3D.unaryMinus() = Point3D(-x, -y, -z)

fun Point3D.mapX(block: (x: Int) -> Int) = copy(x = block(x))
fun Point3D.mapY(block: (y: Int) -> Int) = copy(y = block(y))
fun Point3D.mapZ(block: (z: Int) -> Int) = copy(z = block(z))
fun Point3D.map(block: (coord: Int) -> Int) = copy(x = block(x), y = block(y), z = block(z))

val Point3D.manhattanDistance get() = abs(x) + abs(y) + abs(z)
infix fun Point3D.manhattanDistanceTo(other: Point3D) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

val d3AdjacentSides = listOf(
    Point3D(0, 1, 0), Point3D(0, -1, 0), Point3D(-1, 0, 0),
    Point3D(1, 0, 0), Point3D(0, 0, 1), Point3D(0, 0, -1)
)

fun Point3D.adjacentSides() = d3AdjacentSides.map { this + it }

@JvmName("minX3D")
fun Iterable<Point3D>.minX() = minOf { it.x }

@JvmName("maxX3D")
fun Iterable<Point3D>.maxX() = maxOf { it.x }

@JvmName("minY3D")
fun Iterable<Point3D>.minY() = minOf { it.y }

@JvmName("maxY3D")
fun Iterable<Point3D>.maxY() = maxOf { it.y }

@JvmName("minZ3D")
fun Iterable<Point3D>.minZ() = minOf { it.z }

@JvmName("maxZ3D")
fun Iterable<Point3D>.maxZ() = maxOf { it.z }

fun Iterable<Point3D>.minBound() = Point3D(minX(), minY(), minZ())
fun Iterable<Point3D>.maxBound() = Point3D(maxX(), maxY(), maxZ())
fun Iterable<Point3D>.shiftDelta() = Point3D(-minX(), -minY(), -maxZ())
fun Iterable<Point3D>.boundary() = Cube(minBound(), maxBound())

data class Cube(val a: Point3D, val b: Point3D) {
    val width get() = abs(a.x - b.x) + 1
    val height get() = abs(a.y - b.y) + 1
    val depth get() = abs(a.z - b.z) + 1
    val minX get() = min(a.x, b.x)
    val maxX get() = max(a.x, b.x)
    val minY get() = min(a.y, b.y)
    val maxY get() = max(a.y, b.y)
    val minZ get() = min(a.z, b.z)
    val maxZ get() = max(a.z, b.z)
    val xRange get() = minX..maxX
    val yRange get() = minY..maxY
    val zRange get() = minZ..maxZ
}

val Cube.volume get() = width * height * depth
val Cube.volumeLong get() = width.toLong() * height.toLong() * depth.toLong()
val Cube.points get() = (a.x..b.x).flatMap { x -> (a.y..b.y).flatMap { y -> (a.z..b.z).map { Point3D(x, y, it) } } }

operator fun Cube.contains(point: Point3D) = point.x in xRange && point.y in yRange && point.z in zRange

// Sorry, point2d makes a line cause that makes sense,
// but here it makes more sense to make it a cube
// This is totally not going to hunt me
operator fun Point3D.rangeTo(other: Point3D) = Cube(this, other)

// Utility to work with n-dimensional points
@JvmInline
value class PointND(val coords: List<Int>)

val PointND.dimensions get() = coords.size

operator fun PointND.plus(other: PointND) = PointND(coords.zip(other.coords) { a, b -> a + b })
operator fun PointND.plus(amount: Int) = PointND(coords.map { it + amount })
operator fun PointND.minus(amount: Int) = PointND(coords.map { it - amount })
operator fun PointND.minus(other: PointND) = PointND(coords.zip(other.coords) { a, b -> a - b })
operator fun PointND.times(tim: Int) = PointND(coords.map { it * tim })
operator fun PointND.times(other: PointND) = PointND(coords.zip(other.coords) { a, b -> a * b })
operator fun PointND.div(by: Int) = PointND(coords.map { it / by })
operator fun PointND.rem(with: Int) = PointND(coords.map { it % with })
operator fun PointND.unaryMinus() = PointND(coords.map { -it })

val PointND.manhattanDistance get() = coords.sumOf { it.absoluteValue }
infix fun PointND.manhattanDistanceTo(other: PointND) = (this - other).manhattanDistance

fun Iterable<PointND>.minBound() = PointND(List(first().dimensions) { dim -> minOf { it.coords[dim] } })
fun Iterable<PointND>.maxBound() = PointND(List(first().dimensions) { dim -> maxOf { it.coords[dim] } })
fun Iterable<PointND>.boundary() = NDVolume(minBound(), maxBound())

private inline fun ndRecursion(dimensions: Int, selector: (Int) -> List<Int>): List<PointND> {
    val result = mutableListOf<PointND>()
    val queue = queueOf(selector(0).map { PointND(listOf(it)) })

    while (queue.isNotEmpty()) {
        val curr = queue.removeFirst()
        if (dimensions == curr.dimensions) {
            result += curr
            continue
        }

        selector(curr.dimensions).mapTo(queue) { PointND(curr.coords + it) }
    }

    return result
}

private val dimensionalDCache = hashMapOf<Int, List<PointND>>()

fun PointND.adjacent() = dimensionalDCache.getOrPut(dimensions) {
    val d = listOf(-1, 0, 1)
    ndRecursion(dimensions) { d }.filter { p -> p.coords.any { it != 0 } }
}.map { this + it }

data class NDVolume(val a: PointND, val b: PointND)

val NDVolume.dimensions get() = a.dimensions

operator fun NDVolume.contains(point: PointND) = point.coords.allIndexed { idx, c -> c in a.coords[idx]..b.coords[idx] }
val NDVolume.points get() = ndRecursion(dimensions) { (a.coords[it]..b.coords[it]).toList() }

operator fun PointND.rangeTo(other: PointND) = NDVolume(this, other)

fun <T> GridLike<T>.automaton(step: GridLike<T>.(point: Point, curr: T) -> T) =
    generateSequence(this) { it.mapIndexedElements { point, v -> it.step(point, v) } }

inline fun <T> floydWarshall(
    vertices: Iterable<T>,
    neighbors: (T) -> Iterable<T>,
    distance: (T, T) -> Int = { _, _ -> 1 }
): Map<T, Map<T, Int>> {
    val dist = mutableMapOf<T, MutableMap<T, Int>>()
    val collected = vertices.toList()

    for (vertex in collected) {
        val trivial = mutableMapOf(vertex to 0)
        neighbors(vertex).forEach { trivial[it] = distance(vertex, it) }
        trivial[vertex] = 0
        dist[vertex] = trivial
    }

    for (k in collected) for (i in collected) for (j in collected) {
        val id = dist.getValue(i)
        val ij = id[j]
        val ik = id[k]
        val kj = dist.getValue(k)[j]
        if (ik != null && kj != null && (ij == null || ij > ik + kj)) id[j] = ik + kj
    }

    return dist
}

fun <T> GridLike<T>.flip() = rowsValues.asReversed().asGrid()
fun <T> GridLike<T>.rotate() = columnsValues.map { it.asReversed() }.asGrid()
fun <T> GridLike<T>.orientations() =
    generateSequence(this) { it.rotate() }.take(4).flatMap { listOf(it, it.flip()) }.toSet()