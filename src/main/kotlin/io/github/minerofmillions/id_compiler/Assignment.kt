package io.github.minerofmillions.id_compiler

import io.github.minerofmillions.id_compiler.expressions.Expression
import io.github.minerofmillions.id_compiler.expressions.LambdaExpression
import io.github.minerofmillions.id_parser.InDyParser
import org.antlr.v4.runtime.tree.TerminalNode

data class Assignment(val name: String, val body: Expression) {
    override fun toString() = "$name := $body"

    companion object {
        fun parseAssignment(assignment: InDyParser.AsgnContext): Assignment {
            val name = assignment.ID(0).text
            val params = assignment.ID().drop(1).map(TerminalNode::getText).asReversed()
            val body = Expression.parseExpression(assignment.expr())
            val fullBody = params.fold(body) { acc, variable -> LambdaExpression(variable, acc) }
            return Assignment(name, Expression.reduceExpression(fullBody))
        }
    }
}
