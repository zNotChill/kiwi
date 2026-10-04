package me.znotchill.kiwi.generator.annotations

@Target(AnnotationTarget.CLASS)
annotation class GenerateVector(
    val operators: Array<Operation> = [],
    val constants: Array<Constant> = []
)

enum class Operation(
    val symbol: String,
    val functionName: String,
    val operatorFunction: String,
) {
    PLUS("+", "add", "plus"),
    MINUS("-", "sub", "minus"),
    MULTIPLY("*", "mul", "times"),
    DIVIDE("/", "divide", "div"),
    MODULE("%", "mod", "rem"),
}

annotation class Constant(
    val name: String,
    val values: DoubleArray
)
