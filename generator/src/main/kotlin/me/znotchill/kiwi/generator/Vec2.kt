package me.znotchill.kiwi.generator

import me.znotchill.kiwi.generator.annotations.Constant
import me.znotchill.kiwi.generator.annotations.GenerateVector
import me.znotchill.kiwi.generator.annotations.Operation

@GenerateVector(
    operators = [
        Operation.PLUS,
        Operation.MINUS,
        Operation.MULTIPLY,
        Operation.DIVIDE,
        Operation.MODULE
    ],
    constants = [
        Constant("ZERO", [0.0, 0.0]),
        Constant("ONE", [1.0, 1.0])
    ]
)
data class Vec2(
    val x: Double = 0.0,
    val y: Double = 0.0
)