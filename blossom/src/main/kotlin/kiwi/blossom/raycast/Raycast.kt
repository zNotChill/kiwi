package me.znotchill.kiwi.blossom.raycast

import me.znotchill.kiwi.blossom.extensions.toSmall
import me.znotchill.kiwi.blossom.pos.SmallPos
import me.znotchill.kiwi.blossom.raycast.strategies.boundingBoxStrategy
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockFace
import kotlin.math.absoluteValue
import kotlin.math.floor

data class RaycastResult(
    val hitBlock: Boolean,
    val hitBlockPosition: Pos,
    val hitBlockType: Block,
    val hitEntity: Boolean,
    val entityHit: Entity?,
    val hitUnloadedChunk: Boolean,
    val hitMaxDistance: Boolean,
    val hitPosition: Pos,
    val relativeHitPosition: Pos,
    val hitFace: BlockFace,
    var journey: List<SmallPos> = emptyList(),
)

data class RayTemplate(
    val included: List<EntityType>?,
    val excluded: List<EntityType>?,
)

data class EntityHit(val entity: Entity, val distance: Double)

typealias EntityHitStrategy = (
    rayPos: Pos,
    rayDir: Vec,
    maxDistance: Double,
    candidates: List<Entity>
) -> EntityHit?

class SimulatedRaycast(
    var instance: Instance,
    var position: Pos = Pos(0.0, 0.0, 0.0),
    var direction: Vec = Vec.ZERO,

    /**
     * The maximum distance (in blocks) that this raycast can reach.
     */
    var maxDistance: Double = 200.0,

    /**
     * The distance (in blocks) to travel each raycast step.
     */
    var travelStep: Double = 0.05,

    /**
     * How many steps in-between storing a [SmallPos] in the [me.znotchill.kiwi.blossom.raycast.SimulatedRaycast.journey].
     * Helps conserve memory if maximum accuracy is unnecessary.
     */
    var journeyStep: Int = 5,

    /**
     * The radius (in blocks) to find a list of [Entity].
     * Used for finding candidates, which has an [EntityHitStrategy] check performed on each.
     */
    var nearbyEntityRadius: Double = 3.0,

    /**
     * The logic to perform upon all candidates within the [nearbyEntityRadius] of the raycast.
     * Defaults to [boundingBoxStrategy], which checks which entities collided with the ray.
     */
    var entityHitStrategy: EntityHitStrategy = boundingBoxStrategy,

    var epsilon: Double = 0.005,

    var entityFilter: (Entity) -> Boolean = { true },

    var onUnloadedChunkHit: (RaycastResult, SimulatedRaycast) -> RaycastOutput? = {_,_->RaycastOutput.KILL},
    var onMaxDistanceHit: (RaycastResult, SimulatedRaycast) -> RaycastOutput? = {_,_->RaycastOutput.KILL},
    var onBlockHit: (RaycastResult, SimulatedRaycast) -> RaycastOutput? = {_,_->RaycastOutput.KILL},
    var onEntityHit: (RaycastResult, SimulatedRaycast) -> RaycastOutput? = {_,_->RaycastOutput.KILL},
    var onLivingEntityHit: (RaycastResult, SimulatedRaycast) -> RaycastOutput? = {_,_->RaycastOutput.KILL},
    var onPlayerHit: (RaycastResult, SimulatedRaycast) ->RaycastOutput? = {_,_->RaycastOutput.KILL}
) {
    /**
     * How many steps this raycast will (most likely) take in its lifetime.
     */
    val maxSteps: Int = (maxDistance / travelStep).toInt()
    val journey: ArrayList<SmallPos> = ArrayList(maxSteps / journeyStep + 1)

    lateinit var result: RaycastResult

    var isFinished: Boolean = false

    fun execute() {
        if (direction.isZero || direction.lengthSquared() == 0.0) {
            val failRes = RaycastResult(
                hitBlock = false,
                hitBlockPosition = Pos.ZERO,
                hitBlockType = Block.AIR,
                hitEntity = false,
                entityHit = null,
                hitUnloadedChunk = false,
                hitMaxDistance = false,
                hitPosition = position,
                relativeHitPosition = Pos.ZERO,
                hitFace = BlockFace.TOP,
                journey = journey
            )
            result = failRes
            isFinished = true
            return
        }

        val dir = direction.normalize()

        for (step in 0..maxSteps) {
            if (isFinished) break
            step(dir, step)
        }

        if (!::result.isInitialized) {
            val finalRes = RaycastResult(
                hitBlock = false,
                hitBlockPosition = Pos.ZERO,
                hitBlockType = Block.AIR,
                hitEntity = false,
                entityHit = null,
                hitUnloadedChunk = false,
                hitMaxDistance = true,
                hitPosition = position.add(dir.mul(maxDistance)),
                relativeHitPosition = Pos.ZERO,
                hitFace = BlockFace.TOP,
                journey = journey
            )
            result = finalRes
            onMaxDistanceHit(finalRes, this)
        }
    }

    fun step(dir: Vec, index: Int) {
        if (isFinished) return
        val rayPos = position.add(dir.mul(index * travelStep))

        if (index % journeyStep == 0) {
            journey.add(rayPos.toSmall())
        }

        fun finalize(res: RaycastResult, callback: (RaycastResult, SimulatedRaycast) -> RaycastOutput?) {
            result = res
            if (callback(res, this) == RaycastOutput.KILL) {
                kill()
            }
        }

        // Chunk check
        if (!instance.isChunkLoaded(rayPos)) {
            finalize(RaycastResult(
                hitBlock = false, hitBlockPosition = Pos.ZERO, hitBlockType = Block.AIR,
                hitEntity = false, entityHit = null, hitUnloadedChunk = true,
                hitMaxDistance = false, hitPosition = rayPos, relativeHitPosition = Pos.ZERO,
                hitFace = BlockFace.TOP, journey = journey
            ), onUnloadedChunkHit)
            return
        }

        // Entity check
        val entities = instance.getNearbyEntities(rayPos, nearbyEntityRadius)
            .filter { entityFilter(it) }
        val hit = entityHitStrategy(rayPos, dir, travelStep, entities)
        if (hit != null) {
            finalize(RaycastResult(
                hitBlock = false, hitBlockPosition = Pos.ZERO, hitBlockType = Block.AIR,
                hitEntity = true, entityHit = hit.entity, hitUnloadedChunk = false,
                hitMaxDistance = false,
                hitPosition = rayPos.add(dir.mul(hit.distance)),
                relativeHitPosition = Pos.ZERO,
                hitFace = BlockFace.TOP, journey = journey
            ), onEntityHit)
            return
        }

        // Block check
        val block = instance.getBlock(rayPos)
        if (block.solid()) {
            val blockPos = Pos(floor(rayPos.x), floor(rayPos.y), floor(rayPos.z))
            val blockCenter = blockPos.add(0.5, 0.5, 0.5)
            val offset = rayPos.sub(blockCenter)

            val hitFace = when {
                offset.x.absoluteValue + epsilon > offset.y.absoluteValue &&
                        offset.x.absoluteValue + epsilon > offset.z.absoluteValue ->
                    if (offset.x > 0) BlockFace.EAST else BlockFace.WEST

                offset.y.absoluteValue + epsilon > offset.x.absoluteValue &&
                        offset.y.absoluteValue + epsilon > offset.z.absoluteValue ->
                    if (offset.y > 0) BlockFace.TOP else BlockFace.BOTTOM

                else ->
                    if (offset.z > 0) BlockFace.SOUTH else BlockFace.NORTH
            }

            finalize(RaycastResult(
                hitBlock = true, hitBlockPosition = blockPos, hitBlockType = block,
                hitEntity = false, entityHit = null, hitUnloadedChunk = false,
                hitMaxDistance = false, hitPosition = rayPos, relativeHitPosition = offset,
                hitFace = hitFace, journey = journey
            ), onBlockHit)
        }
    }

    fun finish() {
        isFinished = true
    }
    fun kill() {
        isFinished = true
    }

    companion object {

        fun shootFromPos(
            instance: Instance,
            start: Pos,
            direction: Vec,
            maxDistance: Double = 200.0,
            travelStep: Double = 0.05,
            journeyStep: Int = 2,
            excludeTypes: List<EntityType>? = listOf(),
            includeTypes: List<EntityType>? = null,
            excludeEntities: List<Entity>? = null,
            excludeEntity: Entity? = null,
            nearbyEntityRadius: Double = 3.0,
            entityHitStrategy: EntityHitStrategy = boundingBoxStrategy
        ): RaycastResult {
            val ray = SimulatedRaycast(
                instance = instance,
                position = start,
                direction = direction,
                maxDistance = maxDistance,
                travelStep = travelStep,
                journeyStep = journeyStep,
                nearbyEntityRadius = nearbyEntityRadius,
                entityHitStrategy = entityHitStrategy,
                entityFilter = { e ->
                    e !== excludeEntity &&
                            excludeEntities?.contains(e) != true &&
                            (includeTypes == null || e.entityType in includeTypes) &&
                            excludeTypes?.contains(e.entityType) != true
                }
            )
            ray.execute()
            return ray.result
        }
    }
}

enum class RaycastOutput {
    KILL,
    CONTINUE,
    BOUNCE
}