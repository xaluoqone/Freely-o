package com.xaluoqone.widget

import android.view.View
import kotlin.reflect.KProperty

interface ViewProperty<T> {
    var value: T
    operator fun component1(): T
    operator fun component2(): (T) -> Unit
}

operator fun <T> ViewProperty<T>.setValue(view: View, property: KProperty<*>, value: T) {
    if (this.value != value) {
        this.value = value
        view.invalidate()
    }
}

operator fun <T> ViewProperty<T>.getValue(view: View, property: KProperty<*>): T = value

class ViewPropertyImpl<T>(override var value: T) : ViewProperty<T> {
    override fun component1(): T = value
    override fun component2(): (T) -> Unit = { value = it }
}

fun <T> viewPropertyOf(t: T): ViewPropertyImpl<T> {
    return ViewPropertyImpl(t)
}