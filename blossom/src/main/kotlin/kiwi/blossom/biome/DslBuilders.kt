package me.znotchill.kiwi.blossom.biome

import net.minestom.server.world.biome.Biome

fun biome(block: me.znotchill.kiwi.blossom.biome.BiomeBuilder.() -> Unit): Biome {
    return _root_ide_package_.me.znotchill.kiwi.blossom.biome.BiomeBuilder()
        .apply(block)
        .build()
}