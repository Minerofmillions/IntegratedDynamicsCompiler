package io.github.minerofmillions.id_compiler

import io.github.minerofmillions.id_parser.InDyParser

sealed interface Expression {
    fun has(name: String): Boolean
    val reduced: Expression?

    data class NameExpression(val name: String) : Expression {
        override fun has(name: String): Boolean = name == this.name
        override val reduced = null
    }
    data class ApplicationExpression(val function: Expression, val parameter: Expression) : Expression {
        override fun has(name: String): Boolean = function.has(name) || parameter.has(name)
        override val reduced: ApplicationExpression? by lazy {
                function.reduced?.let {
                    return@lazy ApplicationExpression(it, parameter)
                }

                parameter.reduced?.let {
                    ApplicationExpression(function, it)
                }
            }
    }
    data class LambdaExpression(val variable: String, val expression: Expression) : Expression {
        override fun has(name: String): Boolean = name != variable && expression.has(name)
        override val reduced: Expression? by lazy {
            expression.reduced?.let { return@lazy LambdaExpression(variable, it) }

        }
    }
    companion object {
        fun parseExpression(expression: InDyParser.ExprContext): Expression {
            if (expression.expr().isEmpty()) return NameExpression(expression.ID().text)
            if (expression.ID() != null) return LambdaExpression(expression.ID().text, parseExpression(expression.expr(0)))
            val function = parseExpression(expression.expr(0))
            val parameter = parseExpression(expression.expr(1))
            return ApplicationExpression(function, parameter)
        }

        fun reduceExpression(expression: Expression): Expression {
            var expr = expression
            while (true) {
                expr = expr.reduced ?: break
            }
            return expr
        }
    }

}