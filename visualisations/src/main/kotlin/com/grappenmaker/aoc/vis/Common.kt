package com.grappenmaker.aoc.vis

import com.badlogic.gdx.utils.Disposable
import ktx.app.KtxScreen
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class DisposableScreen : KtxScreen {
    protected val disposables = mutableListOf<DisposableDelegate<*>>()

    protected class DisposableDelegate<T : Any>(private val resetFunc: () -> T) : ReadWriteProperty<Any, T> {
        var current = resetFunc()
        override fun getValue(thisRef: Any, property: KProperty<*>) = current
        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            current = value
        }

        fun reset() {
            current = resetFunc()
        }
    }

    protected fun <T : Any> disposable(reset: () -> T) = DisposableDelegate(reset).also { disposables += it }
    fun reset() {
        disposables.forEach { it.reset() }
    }

    override fun resize(width: Int, height: Int) = reset()
    override fun dispose() = disposables.forEach { (it.current as? Disposable)?.dispose() }
}