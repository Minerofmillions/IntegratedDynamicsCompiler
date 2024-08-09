package io.github.minerofmillions.id_compiler.expressions

import io.github.minerofmillions.id_parser.InDyParser

sealed interface Expression {
    fun has(name: String): Boolean

    /**
     * Replaces the given name with the given expression, returning null if no change is necessary.
     */
    fun replace(name: String, expression: Expression): Expression?

    /**
     * Gets the reduced form of the expression, returning null if no change is necessary.
     */
    fun getReduced(): Expression?
    val functionParameters: Int?
    val flipped: Expression?

    companion object {
        fun parseExpression(expression: InDyParser.ExprContext): Expression {
            if (expression.expr().isEmpty()) return NameExpression(expression.ID().text)
            if (expression.ID() != null) return LambdaExpression(
                expression.ID().text, parseExpression(expression.expr(0))
            )
            val function = parseExpression(expression.expr(0))
            val parameter = parseExpression(expression.expr(1))
            return reduceExpression(ApplicationExpression(function, parameter))
        }

        fun reduceExpression(expression: Expression): Expression {
            var expr = expression
            while (true) {
                expr = expr.getReduced() ?: return expr
            }
        }

        val FlipExpression = ApplicationExpression(
            OperatorExpression.PipeExpression,
            OperatorExpression.Apply2Expression,
            OperatorExpression.CExpression
        )
    }

}