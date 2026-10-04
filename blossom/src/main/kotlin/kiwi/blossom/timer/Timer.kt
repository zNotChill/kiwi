package me.znotchill.kiwi.blossom.timer

class Timer {
    var started: Boolean = false
    var start: Long? = null
    val splits: MutableMap<Long, String> = mutableMapOf()

    fun start() {
        if (started) return
        started = true
        start = System.nanoTime()
        splits.clear()
    }

    fun split(name: String) {
        splits[System.nanoTime()] = name
    }

    fun render(): String {
        if (start == null) return "timer[]"
        return splits.map {
            val elapsed = (it.key - start!!) / 1000
            "${it.value}=${elapsed}μs"
        }.joinToString(", ")
    }
}