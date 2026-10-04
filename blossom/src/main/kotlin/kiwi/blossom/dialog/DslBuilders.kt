package me.znotchill.kiwi.blossom.dialog

fun dialog(block: DialogBuilder.() -> Unit): Dialog {
    return DialogBuilder().apply(block).build()
}