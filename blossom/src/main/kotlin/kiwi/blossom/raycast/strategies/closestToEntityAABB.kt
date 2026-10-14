package me.znotchill.kiwi.blossom.raycast.strategies

import me.znotchill.kiwi.blossom.raycast.EntityHit
import me.znotchill.kiwi.blossom.raycast.EntityHitStrategy
import net.minestom.server.collision.BoundingBox
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.LivingEntity
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun aabbStrategy(inflate: Double = 0.0, livingOnly: Boolean = true): EntityHitStrategy =
    { rayPos, dir, maxDistance, entities ->
        var best: EntityHit? = null
        for (entity in entities) {
            if (livingOnly && entity !is LivingEntity) continue
            val t = rayAabb(rayPos, dir, maxDistance, entity.position, entity.boundingBox, inflate)
                ?: continue
            if (best == null || t < best.distance) best = EntityHit(entity, t)
        }
        best
    }

val boundingBoxStrategy: EntityHitStrategy = aabbStrategy()
val closestToEntityAABB: EntityHitStrategy = aabbStrategy(inflate = 0.4)

private fun rayAabb(
    origin: Pos, dir: Vec, maxT: Double,
    pos: Pos, bb: BoundingBox, inflate: Double
): Double? {
    val range = doubleArrayOf(0.0, maxT)
    if (!slab(
            origin.x, dir.x, pos.x + bb.minX() - inflate,
            pos.x + bb.maxX() + inflate, range)
        ) return null
    if (!slab(
            origin.y, dir.y, pos.y + bb.minY() - inflate,
            pos.y + bb.maxY() + inflate, range)
        ) return null
    if (!slab(
            origin.z, dir.z, pos.z + bb.minZ() - inflate,
            pos.z + bb.maxZ() + inflate, range)
        ) return null
    return range[0]
}

private fun slab(origin: Double, dir: Double, min: Double, max: Double, range: DoubleArray): Boolean {
    if (abs(dir) < 1e-9)
        return origin in min..max
    var t1 = (min - origin) / dir
    var t2 = (max - origin) / dir
    if (t1 > t2) {
        val tmp = t1
        t1 = t2
        t2 = tmp
    }
    range[0] = max(range[0], t1)
    range[1] = min(range[1], t2)
    return range[0] <= range[1]
}