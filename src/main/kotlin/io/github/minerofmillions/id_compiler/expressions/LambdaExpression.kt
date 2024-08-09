package io.github.minerofmillions.id_compiler.expressions

import io.github.minerofmillions.id_compiler.expressions.OperatorExpression.*

data class LambdaExpression(val variable: String, val body: Expression) : Expression {
    override fun toString() = "\\$variable:$body"

    override val functionParameters = body.functionParameters?.plus(1)
    override fun has(name: String): Boolean = name != variable && body.has(name)
    override val flipped = null
    override fun getReduced(): Expression? {
        return body.getReduced()?.let { LambdaExpression(variable, it) }
            ?: when {
                !body.has(variable) -> ApplicationExpression(ConstantExpression, body)
                body !is ApplicationExpression -> IdentityExpression
                body.parameter is NameExpression && body.parameter.name == variable -> body.function
                else -> {
                    val funcHasVar = body.function.has(variable)
                    val bodyHasVar = body.parameter.has(variable)
                    when {
                        funcHasVar && bodyHasVar -> ApplicationExpression(
                            Pipe2Expression,
                            LambdaExpression(variable, body.function),
                            LambdaExpression(variable, body.parameter),
                            ApplyExpression
                        )
                        funcHasVar -> ApplicationExpression(
                            CExpression, LambdaExpression(variable, body.function), body.parameter
                        )
                        bodyHasVar -> ApplicationExpression(
                            PipeExpression, LambdaExpression(variable, body.parameter), body.function
                        )
                        else -> null
                    }
                }
            }
    }

    fun apply(expression: Expression): Expression = body.replace(variable, expression)!!

    override fun replace(name: String, expression: Expression) =
        body.takeUnless { name == variable }?.replace(name, expression)?.let { LambdaExpression(variable, it) }
}