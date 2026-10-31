package me.znotchill.kiwi.blossom.util

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.attribute.Attribute

val LivingEntity.actualEyeHeight: Double
    get() {
        val entityBoundingBox = this.boundingBox
        val entityScale = this.getAttribute(Attribute.SCALE)
        val entityTop = (entityBoundingBox.maxY() * entityScale.value)

        return entityTop
    }

val LivingEntity.headPosition: Pos
    get() {
        return Pos(
            this.position.x,
            this.position.y + actualEyeHeight,
            this.position.z,
        )
    }

/**
 * the player's actual eye height is 1.62 blocks, and their
 * height is 1.8 blocks, so 0.18
 */
val LivingEntity.crosshairPosition: Pos
    get() {
        return Pos(
            this.position.x,
            (this.position.y + actualEyeHeight) - 0.18,
            this.position.z,
        )
    }

fun LivingEntity.getHeightOnBoundingBox(pos: Pos): Double {
    val feetY = position.y + boundingBox.minY()
    return (pos.y - feetY) / boundingBox.height()
}