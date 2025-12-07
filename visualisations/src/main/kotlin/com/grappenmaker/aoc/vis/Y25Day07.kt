package com.grappenmaker.aoc.vis

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.grappenmaker.aoc.*
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.graphics.use
import java.util.zip.Deflater
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.floor

fun main() {
    Lwjgl3Application(Y25Day07Visualisation, Lwjgl3ApplicationConfiguration().apply {
        useVsync(false)
    })
}

private val activeColor = Color(0f, .9f, 0f, 1f)
private val caretColor = Color(.9f, .9f, .9f, 1f)
private val startColor = Color(.9f, 0f, 0f, 1f)
private val headColor = Color(.9f, .9f, 0f, 1f)
private val background2 = Color(.15f, .15f, .2f, 1f)
private val markedColor2 = Color(.5f, .93f, .5f, 1f)

object Y25Day07Visualisation : KtxGame<KtxScreen>(clearScreen = true) {
    override fun create() {
        addScreen(Y25Day07Screen)
        setScreen<Y25Day07Screen>()
    }
}

object Y25Day07Screen : DisposableScreen() {
    val shapes by disposable { ShapeRenderer() }
    val batch by disposable { SpriteBatch() }
    val font by disposable { BitmapFont(Gdx.files.local("font.fnt")) }

    val originalGrid = Path("..").resolve(defaultInput(2025, 7)).readLines().asCharGrid()
    var grid = originalGrid.asMutableGrid()

    const val gridPadding = 0f
    val graphicalGridWidth get() = Gdx.graphics.width.toFloat() - gridPadding - gridPadding
    val graphicalGridHeight get() = Gdx.graphics.height - font.lineHeight - gridPadding - gridPadding

    private val cellSize
        get() = floor(minOf(graphicalGridWidth, graphicalGridHeight) / maxOf(grid.width, grid.height))

    fun ShapeRenderer.draw(point: Point) {
        rect(
            /* x = */ (point.x * cellSize + (graphicalGridWidth - grid.width * cellSize) / 2 + gridPadding),
            /* y = */ graphicalGridHeight / 2 - (point.y + 1) * cellSize + grid.height / 2 * cellSize + gridPadding,
            /* width = */ cellSize,
            /* height = */ cellSize
        )
    }

    fun ShapeRenderer.draw(g: GridLike<Char>) {
        for (p in g.pointsSequence) {
            color = when (p) {
                in marked -> markedColor2
                in heads -> headColor
                else -> when (g[p]) {
                    '^' -> caretColor
                    'S' -> startColor
                    '|' -> activeColor
                    else -> continue
                }
            }

            draw(p)
        }
    }

    fun ShapeRenderer.draw() {
        color = background2
        rect(0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        draw(grid)
    }

    fun updateUp() {
        partTwo = heads.sumOf { memo.getValue(it) }
        if (heads.singleOrNull() == start) return

        marked.addAll(heads)
        heads = buildSet { for (head in heads) addAll(from.getOrDefault(head, emptySet())) }
    }

    fun update() {
        if (doneDown) {
            updateUp()
            return
        }

        if (frameCounter == waitFrames) {
            heads = setOf(start)
            return
        }

        if (heads.isEmpty()) {
            heads = doneHeads
            doneDown = true
            return
        }

        heads = buildSet {
            for (head in heads) {
                val d = head + Direction.DOWN
                if (d !in grid) {
                    doneHeads += head
                    continue
                }

                fun Point.use() {
                    add(this)
                    grid[this] = '|'
                    from.getOrPut(this) { hashSetOf() } += head
                }

                if (grid[d] != '^') {
                    d.use()
                    continue
                }

                (d + Direction.LEFT).use()
                (d + Direction.RIGHT).use()

                partOne++
            }
        }
    }

    var paused by disposable { true }
    var timeSinceUpdate by disposable { 0f }
    const val updateRate = 1f / 100
    var frameCounter = 0
    const val waitFrames = 10

    var partOne = 0
    var partTwo = 0L
    var heads = emptySet<Point>()
    val start get() = grid.findPointsValued('S').single()
    var from = hashMapOf<Point, MutableSet<Point>>()
    var doneDown = false
    var doneHeads = hashSetOf<Point>()
    var marked = hashSetOf<Point>()

    val memo = buildMap {
        fun recur(head: Point): Long = getOrPut(head) {
            val d = head + Direction.DOWN
            if (d !in originalGrid) return@getOrPut 1

            if (originalGrid[d] == '^') recur(d + Direction.LEFT) + recur(d + Direction.RIGHT) else recur(d)
        }

        recur(start)
    }

    fun updateAndSave() {
        if (frameCounter >= waitFrames) update()
        frameCounter++

        val pixmap = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        PixmapIO.writePNG(
            /* file = */ Gdx.files.local("frames/frame-${"%04d".format(frameCounter)}.png"),
            /* pixmap = */ pixmap,
            /* compression = */ Deflater.DEFAULT_COMPRESSION,
            /* flipY = */ true
        )

        pixmap.dispose()
    }

    override fun show() {
        val headerStr = "Part 1: 0000   Part 2: 00000000000000"
        val layout = GlyphLayout(font, headerStr)

        val width = (layout.width + 20) * 1.3
        val height = width + font.lineHeight

        Gdx.graphics.setWindowedMode(width.toInt(), height.toInt())
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            reset()
            grid = originalGrid.asMutableGrid()
            frameCounter = 0
            heads = emptySet()
            partOne = 0
            partTwo = 0
            doneDown = false
            from = hashMapOf()
            doneHeads = hashSetOf()
            marked = hashSetOf()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) paused = !paused

        shapes.use(ShapeRenderer.ShapeType.Filled) { renderer -> renderer.draw() }
        batch.use { batch ->
            val headerStr = "Part 1: $partOne   Part 2: $partTwo"
            val layout = GlyphLayout(font, headerStr)
            font.draw(
                /* batch = */ batch,
                /* str = */ headerStr,
                /* x = */ (Gdx.graphics.width - layout.width) / 2,
                /* y = */ Gdx.graphics.height.toFloat() - (font.lineHeight + gridPadding - layout.height) / 2
            )
        }

        timeSinceUpdate += Gdx.graphics.deltaTime
        if (timeSinceUpdate > updateRate) {
            timeSinceUpdate -= updateRate
            if (!paused) updateAndSave()
        }
    }
}