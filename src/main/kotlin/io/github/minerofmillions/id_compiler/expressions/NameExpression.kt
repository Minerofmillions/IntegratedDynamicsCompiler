package io.github.minerofmillions.id_compiler.expressions

import io.github.minerofmillions.id_compiler.expressions.OperatorExpression.*
import io.github.minerofmillions.id_compiler.expressions.Expression.Companion.FlipExpression

data class NameExpression(val name: String) : Expression {
    override val functionParameters = null
    override fun has(name: String): Boolean = name == this.name
    override val flipped = null
    override fun getReduced() = when (name) {
        "pipe", "Q" -> PipeExpression
        "pipe2" -> Pipe2Expression
        "apply", "A" -> ApplyExpression
        "apply2", "@" -> Apply2Expression
        "flip", "C" -> FlipExpression
        "identity", "id", "I" -> IdentityExpression
        "constant", "K" -> ConstantExpression
        else -> null
    }
    override fun replace(name: String, expression: Expression) = expression.takeIf { this.name == name }

    override fun toString() = name
}