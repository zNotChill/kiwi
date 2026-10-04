package me.znotchill.kiwi.blossom.test

import me.znotchill.kiwi.blossom.instances.InstanceTemplate
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.generator.GenerationUnit

internal object BaseGenerator : InstanceTemplate {
    override fun generate(unit: GenerationUnit) {
        unit.modifier().fill(
            unit.absoluteStart(),
            unit.absoluteEnd().withY(1.0),
            Block.BEDROCK
        )
    }
}