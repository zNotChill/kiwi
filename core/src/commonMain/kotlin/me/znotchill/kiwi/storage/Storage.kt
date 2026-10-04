package me.znotchill.kiwi.storage

open class Storage<K, V> {
    private val values: MutableMap<K, V> = mutableMapOf()

    fun get(k: K) = values[k]

    fun new(k: K, v: V) {
        values[k] = v
    }

    fun remove(k: K) {
        values.remove(k)
    }

    fun clear() {
        values.clear()
    }
}