package me.znotchill.kiwi.blossom.particle

fun particle(
    block: ParticleBuilder.() -> Unit = {}
): ParticleBuilder {
    val builder = ParticleBuilder().apply(block)
    return builder
}