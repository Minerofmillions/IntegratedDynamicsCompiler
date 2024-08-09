package io.github.minerofmillions.id_compiler.expressions

import io.github.minerofmillions.utils.compareTo

sealed class OperatorExpression : Expression {
    override fun has(name: String) = false
    override fun replace(name: String, expression: Expression) = null
    override fun getReduced() = null
    override val flipped = null

    abstract fun getParameterizedReduced(parameters: List<Expression>): Expression?

    data object Pipe2Expression : OperatorExpression() {
        override fun toString() = "2"
        override fun getParameterizedReduced(parameters: List<Expression>): Expression? {
            if (parameters.size != 4) return null
            val (f, g, c, x) = parameters
            return ApplicationExpression(
                c, ApplicationExpression(f, x), ApplicationExpression(g, x)
            )
        }

        override val functionParameters = 3
    }

    data object PipeExpression : OperatorExpression() {
        override fun toString() = "Q"
        override fun getParameterizedReduced(parameters: List<Expression>): Expression? {
            if (parameters.size != 3) return null
            val (f, g, x) = parameters
            return ApplicationExpression(
                g, ApplicationExpression(f, x)
            )
        }

        override val functionParameters = 2
    }

    data object IdentityExpression : OperatorExpression() {
        override fun toString() = "I"
        override fun getParameterizedReduced(parameters: List<Expression>): Expression? {
            if (parameters.size != 1) return null
            return parameters.first()
        }

        override val functionParameters = 1
    }

    data object ApplyExpression : OperatorExpression() {
        override fun toString() = "A"
        override fun getParameterizedReduced(parameters: List<Expression>): Expression? {
            return when (parameters.size) {
                1 -> parameters.first().takeIf { it.functionParameters >= 1 }
                2 -> ApplicationExpression(parameters)
                else -> null
            }
        }

        override val functionParameters = 2
    }

    data object CExpression : OperatorExpression() {
        override fun toString() = "C"
        override fun getParameterizedReduced(parameters: List<Expression>): Expression? = when (parameters.size) {
            1 -> parameters.first().flipped
            3 -> {
                val (f, a, b) = parameters
                ApplicationExpression(f, b, a)
            }

            else -> null
        }

        override val functionParameters = 1
    }

//    data object BExpression : OperatorExpression() {
//        override fun toString() = "B"
//        override fun getParameterizedReduced(parameters: List<Expression>): Expression? {
//            if (parameters.size != 3) return null
//            val (f, g, x) = parameters
//            return ApplicationExpression(f, ApplicationExpression(g, x))
//        }
//
//        override val functionParameters: Int = 2
//    }

    data object ConstantExpression : OperatorExpression() {
        override fun toString() = "K"
        override fun getParameterizedReduced(parameters: List<Expression>): Expression? {
            if (parameters.size != 2) return null
            return parameters.first()
        }

        override val functionParameters = 2
    }

    data object Apply2Expression : OperatorExpression() {
        override fun toString() = "@"
        override fun getParameterizedReduced(parameters: List<Expression>): Expression? = when (parameters.size) {
            1 -> parameters.first().takeIf { it.functionParameters >= 2 }
            3 -> ApplicationExpression(parameters)
            else -> null
        }

        override val functionParameters = 3
    }
}