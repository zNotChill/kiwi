package me.znotchill.kiwi.generator

import com.squareup.kotlinpoet.*
import me.znotchill.kiwi.generator.annotations.GenerateVector
import me.znotchill.kiwi.generator.annotations.Operation
import java.awt.Color
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

fun main() {
    generateClass(Vec2::class)
}
fun generateClass(clazz: KClass<*>) {
    val constructor = clazz.primaryConstructor!!
    val annotation = clazz.annotations
        .filterIsInstance<GenerateVector>()
        .firstOrNull()
        ?: error("${clazz.simpleName} is missing @GenerateVector")

    val type = ClassName(
        "me.znotchill.kiwi.generated",
        clazz.simpleName!!
    )

    val builder = TypeSpec.classBuilder(type)
        .addModifiers(KModifier.DATA)

    val constructorBuilder = FunSpec.constructorBuilder()
    val ctorParams = constructor.parameters

    ctorParams.forEach { parameter ->
        val name = parameter.name!!
        constructorBuilder.addParameter(
            name,
            parameter.type.asTypeName()
        )
        builder.addProperty(
            PropertySpec.builder(
                name,
                parameter.type.asTypeName()
            )
                .initializer(name)
                .build()
        )
    }

    builder.primaryConstructor(
        constructorBuilder.build()
    )

    annotation.operators.forEach { operation ->
        generateOperator(
            operation,
            type,
            clazz
        ).forEach(builder::addFunction)
    }

    // build companion object with constants
    if (annotation.constants.isNotEmpty()) {
        val companionBuilder = TypeSpec.companionObjectBuilder()

        annotation.constants.forEach { constant ->
            require(constant.values.size == ctorParams.size) {
                "Constant '${constant.name}' on ${clazz.simpleName} has ${constant.values.size} values but constructor takes ${ctorParams.size}"
            }

            companionBuilder.addProperty(
                PropertySpec.builder(constant.name, type)
                    .initializer(
                        "%T(%L)",
                        type,
                        constant.values.joinToString(", ") { it.toString() }
                    )
                    .build()
            )
        }

        builder.addType(companionBuilder.build())
    }

    FileSpec.builder(
        type.packageName,
        type.simpleName
    )
        .addType(builder.build())
        .build()
        .writeTo(
            File("library/src/commonMain/kotlin")
        )
}

fun generateOperator(
    operation: Operation,
    owner: ClassName,
    clazz: KClass<*>
): List<FunSpec> {

    val parameterName = "other"
    val ctorParams = clazz.primaryConstructor!!.parameters

    val normalFunction = FunSpec.builder(
        operation.functionName
    )
        .addParameter(
            parameterName,
            owner
        )
        .returns(owner)
        .addStatement(
            "return %T(%L)",
            owner,
            clazz.primaryConstructor!!.parameters.joinToString(", ") {
                "${it.name} ${operation.symbol} $parameterName.${it.name}"
            }
        )
        .build()


    val operatorFunction = FunSpec.builder(
        operation.operatorFunction
    )
        .addModifiers(KModifier.OPERATOR)
        .addParameter(
            parameterName,
            owner
        )
        .returns(owner)
        .addStatement(
            "return %L(%L)",
            operation.functionName,
            parameterName
        )
        .build()


    val componentFunctionBuilder = FunSpec.builder(
        operation.functionName
    )
        .returns(owner)

    ctorParams.forEach { parameter ->
        componentFunctionBuilder.addParameter(
            parameter.name!!,
            parameter.type.asTypeName()
        )
    }

    val componentFunction = componentFunctionBuilder
        .addStatement(
            "return %T(%L)",
            owner,
            ctorParams.joinToString(", ") {
                "this.${it.name} ${operation.symbol} ${it.name}"
            }
        )
        .build()

    return listOf(
        normalFunction,
        operatorFunction,
        componentFunction
    )
}