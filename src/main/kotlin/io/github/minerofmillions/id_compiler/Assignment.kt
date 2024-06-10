package io.github.minerofmillions.id_compiler

import io.github.minerofmillions.id_parser.InDyParser
import org.antlr.v4.runtime.tree.TerminalNode

data class Assignment(val name: String, val body: Expression) {
    companion object {
        fun parseAssignment(assignment: InDyParser.AsgnContext): Assignment {
            val name = assignment.ID(0).text
            val params = assignment.ID().drop(1).map(TerminalNode::getText)
            val body = Expression.parseExpression(assignment.expr())
            val fullBody = params.fold(body) { acc, variable -> Expression.LambdaExpression(variable, acc) }
            return Assignment(name, Expression.reduceExpression(fullBody))
        }
    }
}
