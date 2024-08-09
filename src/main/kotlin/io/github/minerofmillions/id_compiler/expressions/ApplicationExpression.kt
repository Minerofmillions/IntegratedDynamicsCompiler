package io.github.minerofmillions.id_compiler.expressions

data class ApplicationExpression(val function: Expression, val parameter: Expression) : Expression {
    override val functionParameters = function.functionParameters?.minus(1)
    override fun has(name: String): Boolean = function.has(name) || parameter.has(name)
    override val flipped = null
    override fun getReduced(): Expression? {
        function.getReduced()?.let {
            return ApplicationExpression(it, parameter)
        }
        if (function is LambdaExpression) {
            return function.apply(parameter)
        }
        getParameterizedReduced(emptyList())?.let {
            return it
        }
        parameter.getReduced()?.let {
            return ApplicationExpression(function, it)
        }
        return null
    }

    private fun getParameterizedReduced(parameters: List<Expression>): Expression? =
        (function as? ApplicationExpression)?.getParameterizedReduced(parameters + parameter)
            ?: (function as? OperatorExpression)?.getParameterizedReduced((parameters + parameter).reversed())

    override fun replace(name: String, expression: Expression): Expression? {
        val function = function.replace(name, expression)
        val parameter = parameter.replace(name, expression)
        return if (function == null && parameter == null) null
        else ApplicationExpression(function ?: this.function, parameter ?: this.parameter)
    }

    override fun toString(): String = if (parameter !is ApplicationExpression) "$function $parameter"
    else "$function ($parameter)"
}

fun ApplicationExpression(function: Expression, vararg parameters: Expression): Expression =
    parameters.fold(function, ::ApplicationExpression)

fun ApplicationExpression(expressions: List<Expression>): Expression =
    ApplicationExpression(expressions.first(), *expressions.drop(1).toTypedArray())