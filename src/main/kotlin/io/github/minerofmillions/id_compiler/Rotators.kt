package io.github.minerofmillions.id_compiler

import io.github.minerofmillions.id_compiler.expressions.ApplicationExpression
import io.github.minerofmillions.id_compiler.expressions.Expression
import io.github.minerofmillions.id_compiler.expressions.LambdaExpression
import io.github.minerofmillions.id_compiler.expressions.NameExpression
import java.awt.image.BufferedImage

fun main() {
    (0 until factorial(4).toInt()).forEach {
        println(getRotator(it))
    }

//    val rotators = (0 until factorial(4).toInt()).map(::getRotator)
//        .map(Expression::toString)
//        .map { it.replace(" ", "") }
//    val maxHeight = rotators.maxOf(::getHeight)
//
//    val startIndices = rotators.runningFold(0) { acc, exp -> acc + getWidth(exp) + 1 }
//    val maxWidth = startIndices.last() - 1
//
//    val img = BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB)
//    rotators.zip(startIndices).forEach { (expression, index) ->
//        drawRotator(expression, index, img)
//    }
//    ImageIO.write(img, "PNG", File("rotators.png"))
}

fun drawRotator(expression: String, startIndex: Int, img: BufferedImage) {
    var currentY = 0
    var openParens = 0
    for ((index, char) in expression.withIndex()) {
        if (char == 'B') {
            img.setRGB(startIndex + openParens, img.height - 1 - currentY++, 0xff0000)
        } else if (char == 'C') {
            img.setRGB(startIndex + openParens, img.height - 1 - currentY++, 0x00ff00)
        } else if (char == '(') {
            openParens++
        } else if (char == ')') {
            openParens--
            if (index + 1 < expression.length && expression[index + 1] == '(') currentY++
        }
    }
}

fun getHeight(expression: String): Int {
    var count = 0
    for (i in expression.indices) {
        if (expression[i] == 'B' || expression[i] == 'C') count++
        if (expression[i] == ')' && i + 1 < expression.length && expression[i + 1] == '(') count++
    }
    return count
}

fun getWidth(expression: String): Int {
    var maxOpen = 0
    var open = 0
    expression.forEach {
        if (it == ')') open--
        if (it == '(') {
            open++
            if (maxOpen < open) maxOpen = open
        }
    }
    return maxOpen + 1
}

fun intToCode(int: Int): List<Int> {
    if (int == 0) return listOf(0)
    var currentValue = int
    var shouldBreak = false
    return generateSequence(1, Int::inc).map {
        shouldBreak = currentValue == 0
        val remainder = currentValue % it
        val quotient = currentValue / it
        currentValue = quotient
        remainder
    }.takeWhile { !shouldBreak }.toList()
}

fun codeToPermutation(code: List<Int>): List<Int> {
    val freeIndices = code.indices.toMutableList()
    val output = IntArray(code.size)
    code.withIndex().reversed().forEach { (index, shift) ->
        val i = freeIndices.removeAt(index - shift)
        output[i] = index
    }
    return output.toList()
}

fun getRotator(permutationIndex: Int): Expression {
    val permutation = codeToPermutation(intToCode(permutationIndex))
    val output = permutation.map("v"::plus).map(::NameExpression)
    val lambdaBody = ApplicationExpression(NameExpression("f"), *output.toTypedArray())
    val variables = listOf("f") + permutation.indices.map("v"::plus)
    val lambda = variables.foldRight(lambdaBody, ::LambdaExpression)
    println(permutation)
    return Expression.reduceExpression(lambda)
}

fun factorial(n: Int) = (1..n).fold(1L, Long::times)
