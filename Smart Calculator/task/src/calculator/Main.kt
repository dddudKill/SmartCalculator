package calculator

import kotlin.math.pow
import java.math.BigInteger

fun input() {
    val variableAssignmentRegex = Regex("^[a-zA-Z]+\\s*=\\s*([+-]?[0-9]+\$|[a-zA-Z]+\$)")
    val singleInputRegex = Regex("^[-+]?((0|[1-9][0-9]*)\$|[a-zA-Z]+\$)")
    val expressionRegex = Regex("^[-+(]?([0-9]+|[a-zA-Z]+)(\\s*([-+]+|[*/^])\\s*(\\(*[0-9]+\\)*|\\(*[a-zA-Z]+\\)*))+\$")

    val input = readln().trim()
    if (input.first() == '/') help(input)
    if (singleInputRegex.matches(input)) { singleInput(input); return }
    if (expressionRegex.matches(input)) { expression(input); return }
    if (variableAssignmentRegex.matches(input)) { variableAssignment(input); return } else throw Exception("Invalid identifier")
}

fun help(input: String) {
    when (input) {
        "/help" -> println("The program calculates the sum or difference of numbers")
        "/exit" -> { println("Bye!"); throw Exception("Exit") }
        else -> throw Exception("Unknown command")
    }
}

fun variableAssignment(input: String) {
    val keyAndValue = input.split("=")
    val key = keyAndValue.first().trim()
    val value = keyAndValue.last().trim()
    if (isNumeric(value)) variables[key] = value.toBigInteger()
    else if (variables.containsKey(value)) variables[key] = variables[value]!!
    else throw Exception("Invalid identifier")
}

fun singleInput(input: String) {
    val signRegex = "[-+][a-zA-Z]+\$".toRegex()
    if (signRegex.matches(input)) {
        val sign = input.first().toString()
        val key = input.removePrefix(sign)
        if (variables.containsKey(key)) println(sign + variables[key])
    } else if (variables.containsKey(input)) println(variables[input])
    else if (isNumeric(input)) println(input)
    else println("Unknown variable")
}

fun expression(input: String) {
    var inputSplit = input
    inputSplit = inputSplit.replace("(", "( ")
    inputSplit = inputSplit.replace(")", " )")
    val flow = inputSplit.split("\\s+".toRegex())
    if (flow.joinToString() == inputSplit) throw Exception("Invalid expression")
    if (flow.count {it == "(" } != flow.count { it == ")" }) throw Exception("Invalid expression")
    val operandsRegex = "[-()*/+^]+".toRegex()

    val postfix = mutableListOf<String>()
    val toPostfixStack = ArrayDeque<String>()
    for (item in flow) {
        if (operandsRegex.matches(item)) {
            when (item) {
                "(" -> toPostfixStack.addLast(item)
                ")" -> while (true) {
                    val operator = toPostfixStack.removeLast()
                    if (operator == "(") break
                    postfix.add(operator)
                }

                else -> {
                    if (toPostfixStack.lastOrNull() == "(" || toPostfixStack.isEmpty()) { toPostfixStack.add(item); continue }
                    if (higherPrecedence(item, toPostfixStack.lastOrNull())) toPostfixStack.add(item)
                    else {
                        while (!higherPrecedence(item, toPostfixStack.lastOrNull())) {
                            val operator = toPostfixStack.removeLast()
                            postfix.add(operator)
                        }
                        toPostfixStack.add(item)
                    }
                }
            }
        } else {
            postfix.add(item)
        }
    }
    while (!toPostfixStack.isEmpty()) {
        val operator = toPostfixStack.removeLast()
        postfix.add(operator)
    }

    val calcStack = ArrayDeque<String>()
    for (item in postfix) {
        if (!operandsRegex.matches(item)) calcStack.add(item)
        else {
            val top = calcStack.removeLast()
            val bottom = calcStack.removeLast()
            operator(top, bottom, item)?.let { calcStack.add(it) }
        }
    }
    println(calcStack.last())
}

fun operator(secondIn: String, firstIn: String, operatorIn: String): String? {
    val first = if (isNumeric(firstIn)) firstIn.toBigInteger()
    else if (variables.containsKey(firstIn)) variables[firstIn]
    else throw Exception("Unknown variable")

    val second = if (isNumeric(secondIn)) secondIn.toBigInteger()
    else if (variables.containsKey(secondIn)) variables[secondIn]
    else throw Exception("Unknown variable")

    val operator = if ("\\++".toRegex().matches(operatorIn)) "+"
    else if ("-+".toRegex().matches(operatorIn)) {
    if (operatorIn.length % 2 == 0) "+"
    else "-"
    }
    else if (operatorIn == "/") "/"
    else if (operatorIn == "*") "*"
    else if (operatorIn == "^") "^"
    else throw Exception("Invalid expression")

    if (first != null && second != null) {
        return when (operator) {
            "+" -> (first + second).toString()
            "-" -> (first - second).toString()
            "*" -> (first * second).toString()
            "/" -> (first / second).toString()
            "^" -> (first.toDouble().pow(second.toDouble()).toInt()).toString()
            else -> null
        }
    }
    return null
}

fun isNumeric(toCheck: String): Boolean = toCheck.toBigIntegerOrNull() != null

fun higherPrecedence(next: String, last: String?): Boolean {
    if (next == "^") return true
    if (last == "^") return false
    if (next == "*" || next == "/" || last == null || last == "(") return true
    return false
}

val variables = mutableMapOf<String, BigInteger>()

fun main() {
    while (true) {
        try {
            input()
        }
        catch (e: Exception) {
            if (e.message == "Exit") break
            if (e.message == "Char sequence is empty.") continue
            println(e.message)
        }
    }
}
