package com.grappenmaker.aoc.vis

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.grappenmaker.aoc.*
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.graphics.use
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun main() {
    Lwjgl3Application(Visualisation, Lwjgl3ApplicationConfiguration().also { it.useVsync(false) })
}

object Visualisation : KtxGame<KtxScreen>(clearScreen = true) {
    override fun create() {
        addScreen(TestScreen)
        setScreen<TestScreen>()
    }
}

object TestScreen : KtxScreen {
    private val disposables = mutableListOf<DisposableDelegate<*>>()

    class DisposableDelegate<T : Any>(private val resetFunc: () -> T) : ReadWriteProperty<Any, T> {
        var current = resetFunc()
        override fun getValue(thisRef: Any, property: KProperty<*>) = current
        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            current = value
        }

        fun reset() {
            current = resetFunc()
        }
    }

    private fun <T : Any> disposable(reset: () -> T) = DisposableDelegate(reset).also { disposables += it }
    fun reset() = disposables.forEach { it.reset() }

    private const val cellSize = 5

    val grid by disposable {
        mutableBooleanGrid(
            Gdx.graphics.width / cellSize,
            Gdx.graphics.height / cellSize
        ).apply {
            val (tl, br) = centeredRect(width / 4, height / 4)
            val offset = Point(width / 2, height / 2)
            Rectangle(tl + offset, br + offset).points.forEach { enable(it) }
        }
    }

    var antPos by disposable { Point(grid.width / 2, grid.height / 2) }
    var antDir by disposable { Direction.UP }

    fun update() {
        if (antPos !in grid) return

        antDir = antDir.next(if (grid[antPos]) -1 else 1)
        grid[antPos] = !grid[antPos]
        antPos += antDir
    }

    val shapes by disposable { ShapeRenderer() }
    fun ShapeRenderer.draw(points: List<Point>) = points.forEach { (x, y) ->
        val sx = (x * cellSize).toFloat()
        val sy = (y * cellSize).toFloat()
        rect(sx, Gdx.graphics.height - sy, cellSize.toFloat(), cellSize.toFloat())
    }

    val batch by disposable { SpriteBatch() }
    val font by disposable { BitmapFont() }

    override fun render(delta: Float) {
        update()

        shapes.use(ShapeRenderer.ShapeType.Filled) {
            it.setColor(1f, 1f, 1f, 1f)
            it.draw(grid.filterTrue())
        }

        // will maybe do something else with this later
        batch.use { font.draw(it, "This is just a test", 10f, Gdx.graphics.height - 10f) }
    }

    override fun resize(width: Int, height: Int) = reset()
    override fun dispose() = disposables.forEach { (it.current as? Disposable)?.dispose() }
}