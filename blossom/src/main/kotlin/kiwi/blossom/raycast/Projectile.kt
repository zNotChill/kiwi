package me.znotchill.kiwi.blossom.raycast

import me.znotchill.kiwi.blossom.particle.ParticleBuilder
import me.znotchill.kiwi.blossom.raycast.strategies.boundingBoxStrategy
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.LivingEntity
import net.minestom.server.instance.Instance
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.sign

class SimulatedProjectile(
    var position: Pos = Pos(0.0, 0.0, 0.0),
    var velocity: Vec = Vec(0.0, 0.0, 0.0),
    var shooter: LivingEntity? = null,
    var particles: List<ParticleBuilder>? = null,
    var excludedEntities: List<Entity>? = null,
    var includedEntityTypes: List<EntityType>? = null,

    /**
     * Called when a projectile raycast hits a surface.
     */
    var onSurfaceHit: (RaycastResult, SimulatedProjectile) -> Unit = { _, _ -> },

    /**
     * Called when a projectile raycast hits a [LivingEntity]
     */
    var onEntityHit: (RaycastResult, SimulatedProjectile) -> Unit = { _, _ -> },

    /**
     * Called when a projectile raycast hits a [LivingEntity] or a surface.
     *
     * If [RaycastOutput.KILL] is returned, the [me.znotchill.core.physics.SimulatedProjectile] is removed.
     */
    var onGenericHit: (RaycastResult, SimulatedProjectile) -> RaycastOutput = { _, _ -> RaycastOutput.CONTINUE },

    /**
     * Called every Minecraft tick when the projectile is
     * ticked forwards.
     */
    var onTick: (SimulatedProjectile) -> Unit = {},

    /**
     * Called when the projectile's max lifetime is hit.
     */
    var onExpire: (SimulatedProjectile) -> Unit = {},

    /**
     * The maximum lifetime of the projectile in ticks
     */
    var maxLifetime: Int = 300,

    /**
     * The projectile's realtime lived duration in ticks
     * Do not modify.
     */
    var lifetime: Int = 0,

    /**
     * If this value is true, we do not tick the projectile anymore.
     */
    var isFinished: Boolean = false,

    /**
     * If we should simulate "proper" physics instead
     * of flying in a straight line, where the projectile
     * never falls.
     *
     * When this value is true, we take into account gravity
     * and collision.
     */
    var simulatePhysics: Boolean = false,
    var bounceFactor: Double = 0.6,
    var gravity: Double = 0.04,

    var size: Double = 3.0,
    var entityHitStrategy: EntityHitStrategy = boundingBoxStrategy,

    /**
     * If the simulated projectile is too slow, stop it to avoid
     * extra computation
     *
     * Only works when [simulatePhysics] is true.
     */
    var stopSpeedThreshold: Double = 0.01,
) {
    var instance: Instance? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }

    lateinit var lastRaycast: RaycastResult

    fun kill() {
        isFinished = true
    }

    fun simulatePhysicsTick() {
        if (instance == null) return

        velocity = velocity.sub(0.0, gravity, 0.0)
        if (velocity.length() <= stopSpeedThreshold) {
            return
        }

        val steps = ceil(velocity.length()).toInt().coerceAtLeast(1)
        val stepVec = velocity.mul(1.0 / steps)

        for (i in 0 until steps) {
            val dir = stepVec.normalize()
            val nextPos = position.add(stepVec)

            val ray = SimulatedRaycast.shootFromPos(
                instance!!,
                position,
                dir,
                maxDistance = stepVec.length() + 1e-6,
                excludeEntity = shooter,
                excludeEntities = excludedEntities,
                nearbyEntityRadius = size,
                entityHitStrategy = entityHitStrategy,
            )

            lastRaycast = ray

            if (ray.hitEntity) {
                onEntityHit(ray, this)
                onGenericHit(ray, this)
                break
            }

            if (ray.hitBlock) {
                val ax = abs(dir.x)
                val ay = abs(dir.y)
                val az = abs(dir.z)

                val normal = when {
                    ax > ay && ax > az -> Vec(-sign(dir.x), 0.0, 0.0)
                    ay > ax && ay > az -> Vec(0.0, -sign(dir.y), 0.0)
                    else -> Vec(0.0, 0.0, -sign(dir.z))
                }

                val vDot = velocity.dot(normal)
                if (vDot < 0.0) {
                    val reflected = velocity.sub(normal.mul(2 * vDot))
                    val damped = reflected.sub(normal.mul(reflected.dot(normal) * (1 - bounceFactor)))
                    velocity = damped.mul(0.65)
                    position = position.add(normal.mul(0.01))
                }

                onSurfaceHit(ray, this)
                onGenericHit(ray, this)

                break
            } else {
                position = nextPos
            }
        }
    }

    fun draw() {
        if (instance == null) return
        particles?.forEach { particle ->
            instance!!.players.forEach {
                it.sendPacket(
                    particle.toPacket(position)
                )
            }
        }
    }
}

object ProjectileManager {
    private val projectiles = mutableListOf<SimulatedProjectile>()

    fun spawn(
        instance: Instance,
        block: SimulatedProjectile.() -> Unit
    ) {
        projectiles += SimulatedProjectile().apply {
            this.instance = instance
            block()
        }
    }

    fun tick(instance: Instance) {
        val iterator = projectiles.iterator()
        while (iterator.hasNext()) {
            val proj = iterator.next()

            if (proj.isFinished) continue

            if (proj.lifetime >= proj.maxLifetime) {
                proj.onExpire(proj)
                continue
            }

            proj.lifetime += 1
            proj.onTick(proj)
            if (proj.simulatePhysics) {
                proj.simulatePhysicsTick()
            } else {
                val result = SimulatedRaycast.shootFromPos(
                    instance = instance,
                    start = proj.position,
                    direction = proj.velocity,
                    maxDistance = proj.velocity.length(),
                    travelStep = 0.05,
                    includeTypes = proj.includedEntityTypes,
                    excludeEntities = proj.excludedEntities,
                    excludeEntity = proj.shooter,
                    nearbyEntityRadius = proj.size,
                    entityHitStrategy = proj.entityHitStrategy,
                )
                proj.lastRaycast = result
                if (result.hitBlock || result.hitEntity) {
                    if (result.hitBlock) {
                        proj.onSurfaceHit(result, proj)
                    } else {
                        proj.onEntityHit(result, proj)
                    }
                    val output = proj.onGenericHit(result, proj)
                    if (output == RaycastOutput.KILL) {
                        if (!proj.isFinished) proj.kill()
                        iterator.remove()
                    }
                }

                proj.position = proj.position.add(proj.velocity)
            }

            proj.draw()
        }
    }
}