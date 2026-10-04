package me.znotchill.kiwi.blossom.bossbar

import net.minestom.server.entity.Player

object BossBarManager {
    private val playerBars = mutableMapOf<Player, MutableMap<String, me.znotchill.kiwi.blossom.bossbar.TrackedBossBar>>()

    fun get(player: Player, id: String): me.znotchill.kiwi.blossom.bossbar.TrackedBossBar? =
        playerBars[player]?.get(id)

    fun set(player: Player, bar: me.znotchill.kiwi.blossom.bossbar.TrackedBossBar) {
        val bars = playerBars.computeIfAbsent(player) { mutableMapOf() }
        bars[bar.id] = bar
        player.showBossBar(bar.bar)
    }

    fun update(player: Player, id: String, block: me.znotchill.kiwi.blossom.bossbar.BossBarBuilder.() -> Unit): me.znotchill.kiwi.blossom.bossbar.TrackedBossBar? {
        val existing = get(player, id) ?: return null

        val builder = _root_ide_package_.me.znotchill.kiwi.blossom.bossbar.BossBarBuilder.from(existing.bar).apply(block)
        val updatedBar = builder.build()

        // update the existing bar to avoid flickering
        with(existing.bar) {
            name(updatedBar.name())
            progress(updatedBar.progress())
            color(updatedBar.color())
            overlay(updatedBar.overlay())
            flags().forEach { removeFlag(it) }
            updatedBar.flags().forEach { addFlag(it) }
        }

        return existing
    }


    fun hide(player: Player, id: String) {
        val tracked = get(player, id) ?: return
        player.hideBossBar(tracked.bar)
        playerBars[player]?.remove(id)
        if (playerBars[player]?.isEmpty() == true) {
            playerBars.remove(player)
        }
    }

    fun hideAll(player: Player) {
        playerBars[player]?.values?.forEach { player.hideBossBar(it.bar) }
        playerBars.remove(player)
    }
}
