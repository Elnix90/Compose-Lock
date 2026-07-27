package com.mrhwsn.composelock

public interface ComposeLockCallback {
    public fun onStart(dot: Dot)
    public fun onDotConnected(dot: Dot)
    public fun onResult(result:List<Dot>)
}