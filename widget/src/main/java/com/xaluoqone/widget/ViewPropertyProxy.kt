package com.xaluoqone.widget

import android.view.View
import kotlin.reflect.KProperty

class ViewPropertyProxy<T>(private var value: T) {
    operator fun getValue(thisRef: View, property: KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: View, property: KProperty<*>, value: T) {
        this.value = value
        thisRef.invalidate()
    }
}