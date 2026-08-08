package io.github.elnix90.lock.patttern

import io.github.elnix90.lock.Dot

public interface ComposeLockCallback {
    public fun onStart(dot: Dot)
    public fun onDotConnected(dot: Dot)
    public fun onResult(result:List<Dot>)
}