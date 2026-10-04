package me.znotchill.kiwi.blossom.bossbar

import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player

class PlayerBossBar(
    private val player: Player,
    private val tracked: me.znotchill.kiwi.blossom.bossbar.TrackedBossBar
) {
    init {
        player.showBossBar(tracked.bar)
    }

    fun update(block: me.znotchill.kiwi.blossom.bossbar.BossBarBuilder.() -> Unit) {
        val updated = _root_ide_package_.me.znotchill.kiwi.blossom.bossbar.BossBarManager.update(player, tracked.id, block) ?: return

        val previousBar = _root_ide_package_.me.znotchill.kiwi.blossom.bossbar.BossBarManager.get(player, tracked.id)
        if (previousBar != null) {
            player.hideBossBar(previousBar.bar)
            MinecraftServer.getBossBarManager().destroyBossBar(previousBar.bar)
        }
        updated.show(player)
    }

    fun hide() {
        val trackedBar = BossBarManager.get(player, tracked.id) ?: return

        player.hideBossBar(trackedBar.bar)
        BossBarManager.hide(player, tracked.id)
    }
}