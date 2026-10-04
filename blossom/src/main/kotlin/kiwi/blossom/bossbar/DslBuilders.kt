package me.znotchill.kiwi.blossom.bossbar

fun bossBar(id: String, block: me.znotchill.kiwi.blossom.bossbar.BossBarBuilder.() -> Unit): me.znotchill.kiwi.blossom.bossbar.TrackedBossBar {
    val builder = _root_ide_package_.me.znotchill.kiwi.blossom.bossbar.BossBarBuilder().apply(block)
    val bar = builder.build()
    return _root_ide_package_.me.znotchill.kiwi.blossom.bossbar.TrackedBossBar(id, bar)
}
