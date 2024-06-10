package io.github.minerofmillions.id_compiler

import io.github.minerofmillions.id_parser.InDyLexer
import io.github.minerofmillions.id_parser.InDyParser
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams

fun main(args: Array<String>) {
    if (args.isEmpty()) error("Need file to compile.")

    val lexer = InDyLexer(CharStreams.fromFileName(args[0]))
    val parser = InDyParser(BufferedTokenStream(lexer))
    val program = parser.program()

    val assignments = mutableListOf<Assignment>()
    val expressions = mutableListOf<Expression>()

    program.line().forEach { line ->
        line.expr()?.let { expression ->
            expressions += Expression.reduceExpression(Expression.parseExpression(expression))
        }
        line.asgn()?.let { assignment ->
            assignments += Assignment.parseAssignment(assignment)
        }
    }

    assignments.forEach(::println)
    expressions.forEach(::println)
}
