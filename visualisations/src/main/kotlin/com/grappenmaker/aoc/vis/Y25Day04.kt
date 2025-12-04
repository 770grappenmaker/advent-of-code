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
import ktx.graphics.copy
import ktx.graphics.use
import java.util.zip.Deflater
import kotlin.random.Random

fun main() {
    Lwjgl3Application(Y25Day04Visualisation, Lwjgl3ApplicationConfiguration().apply {
        useVsync(false)
//        setWindowedMode(138, 138)
    })
}

val activeColor = Color(.9f, .9f, .9f, 1f)
val historyColor = Color(.9f, 0f, 0f, 1f)
val background = Color(.15f, .15f, .2f, 1f)
val markedColor = Color(.5f, .93f, .5f, 1f)

fun Color.coerceAtLeast(other: Color) = Color(
    maxOf(r, other.r),
    maxOf(g, other.g),
    maxOf(b, other.b),
    maxOf(a, other.a),
)

object Y25Day04Visualisation : KtxGame<KtxScreen>(clearScreen = true) {
    override fun create() {
        addScreen(Y25Day04Screen)
        setScreen<Y25Day04Screen>()
    }
}

object Y25Day04Screen : DisposableScreen() {
    val shapes by disposable { ShapeRenderer() }
    val batch by disposable { SpriteBatch() }
    val font by disposable { BitmapFont(Gdx.files.local("font.fnt")) }

    val originalGrid: GridLike<Boolean> = grid(138, 138) { Random.nextFloat() < .65 }
//    val originalGrid = Path("..").resolve(defaultInput(2025, 4)).readLines().asGrid { it == '@' }

    val history by disposable { mutableListOf<BooleanGrid>() }
    var grid = originalGrid.asMutableGrid()

    const val gridPadding = 10f
    val graphicalGridWidth get() = Gdx.graphics.width.toFloat() - gridPadding - gridPadding
    val graphicalGridHeight get() = Gdx.graphics.height - font.lineHeight - gridPadding - gridPadding

    private val cellSize
        get() = minOf(graphicalGridWidth, graphicalGridHeight) / maxOf(grid.width, grid.height)

    fun GridLike<Boolean>.iter() = pointsSequence.filter { this[it] && it.allAdjacent().count { p -> this[p] } < 4 }

    fun ShapeRenderer.draw(points: Sequence<Point>) = points.forEach { (x, y) ->
        rect(
            /* x = */ x * cellSize + (graphicalGridWidth - grid.width * cellSize) / 2 + gridPadding,
            /* y = */ graphicalGridHeight / 2 - (y + 1) * cellSize + grid.height / 2 * cellSize + gridPadding,
            /* width = */ cellSize,
            /* height = */ cellSize
        )
    }

    fun ShapeRenderer.draw(g: GridLike<Boolean>) = draw(g.pointsSequence.filter { g[it] })

    fun ShapeRenderer.draw() {
        color = background
        rect(0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        for ((idx, g) in history.withIndex()) {
            val c = (2f / (history.lastIndex - idx + 1)).coerceAtMost(.5f)
            color = historyColor.copy().mul(c).coerceAtLeast(background)
            draw(g)
        }

        color = activeColor
        draw(grid)

        if (frameCounter < waitFrames) {
            color = markedColor
            draw(originalGrid.iter())
        }
    }

    fun update() {
        history += grid.asGrid()
        if (history.size > 20) history.removeFirstN(history.size - 20)
        removed += grid.iter().toList().onEach { grid[it] = false }.size
    }

    var paused by disposable { true }
    var timeSinceUpdate by disposable { 0f }
    const val updateRate = 1f / 10
    var frameCounter = 0
    var removed = 0
    const val waitFrames = 10

    val partOne get() = originalGrid.iter().count()

    fun updateAndSave() {
        if (frameCounter >= waitFrames) update()
        frameCounter++

        val pixmap = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        PixmapIO.writePNG(
            /* file = */ Gdx.files.local("frames/frame-${"%03d".format(frameCounter)}.png"),
            /* pixmap = */ pixmap,
            /* compression = */ Deflater.DEFAULT_COMPRESSION,
            /* flipY = */ true
        )

        pixmap.dispose()
    }

    override fun show() {
        val headerStr = "Part 1: 0000   Part 2: 0000"
        val layout = GlyphLayout(font, headerStr)

        val width = (layout.width + 20) * 2
        val height = width + font.lineHeight

        Gdx.graphics.setWindowedMode(width.toInt(), height.toInt())
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            reset()
            grid = originalGrid.asMutableGrid()
            frameCounter = 0
            removed = 0
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) paused = !paused

        shapes.use(ShapeRenderer.ShapeType.Filled) { renderer -> renderer.draw() }
        batch.use { batch ->
            val headerStr = "Part 1: $partOne   Part 2: $removed"
            val layout = GlyphLayout(font, headerStr)
            font.draw(
                /* batch = */ batch,
                /* str = */ headerStr,
                /* x = */ (Gdx.graphics.width - layout.width) / 2,
                /* y = */ Gdx.graphics.height.toFloat() - (font.lineHeight + gridPadding - layout.height) / 2
            )
//            font.draw(batch, "Part 2: $removed", 0f, Gdx.graphics.height - font.lineHeight - 5f)
        }

        timeSinceUpdate += Gdx.graphics.deltaTime
        if (timeSinceUpdate > updateRate) {
            timeSinceUpdate -= updateRate
            if (!paused) updateAndSave()
        }
    }
}