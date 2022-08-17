package com.example.androidcalculator

import java.util.*

class Calculator {

    fun calculate(inputText: String): Answer {
        val answer = Answer(inputText)
        answer.error = validate(answer.expression)
        if (answer.error == "") {
            val rpn = expressionToRPN(answer.expression)
            val res = rpnToAnswer(rpn)
            if (res == null) {
                answer.error = "Вы ввели неверное математическое выражение, попробуйте еще раз"
            } else answer.result = res
        }
        return answer
    }

    private fun validate(inputText: String): String {
        var error = ""

        when {
            inputText.isBlank() -> error = "Вы ввели пустую строку, попробуйте еще раз"
            checkOnIllegalSymbols(inputText) -> error =
                "В математическом выражении присутствуют недопустимые символы, попробуйте еще раз"
            checkOnCharacterSequence(inputText) -> error =
                "Вы ввели несколько символов математических операций подряд, попробуйте еще раз"
            checkOnSymbolOfMathExpression(inputText) -> error =
                "Вы не ввели ни одного символа математических операций, попробуйте еще раз"
        }
        return error
    }

    private fun checkOnIllegalSymbols(inputText: String): Boolean {
        val reg = "[0123456789+-/*() ]+".toRegex()
        return !reg.matches(inputText)
    }

    private fun checkOnCharacterSequence(inputText: String): Boolean {
        val editedText = inputText.filterNot { it.isWhitespace() }
        for (index in 0..(editedText.length - 2)) {
            val symbol = editedText[index]
            val symbol2 = editedText[index + 1]
            if (((symbol == '-') || (symbol == '+') || (symbol == '*') || (symbol == '/'))
                && ((symbol2 == '-') || (symbol2 == '+') || (symbol2 == '*') || (symbol2 == '/'))
            ) {
                return true
            }
        }
        return false
    }

    private fun checkOnSymbolOfMathExpression(inputText: String): Boolean {
        for (index in inputText.indices) {
            val symbol = inputText[index]
            if ((symbol == '-') || (symbol == '+') || (symbol == '*') || (symbol == '/')) {
                return false
            }
        }
        return true
    }

    private fun prepareExpression(inputText: String): String {
        var outputText = ""
        for (index in inputText.indices) {
            val symbol = inputText[index]
            if (symbol == '-') {
                if ((index == 0) || (inputText[index - 1] == '(')) outputText += "0"
            }
            outputText += symbol
        }
        return outputText
    }

    private fun expressionToRPN(inputText: String): String {
        val outputText = prepareExpression(inputText)
        var rpn = ""
        val stack: Stack<Char> = Stack()
        var priority: Int

        outputText.forEach {
            priority = getPriority(it)
            when (priority) {
                0 -> rpn += it
                1 -> stack.push(it)
                in 2..3 -> {
                    rpn += ' '
                    while (!stack.empty()) {
                        rpn += if (getPriority(stack.peek()) >= priority) {
                            stack.pop()
                        } else break
                    }
                    stack.push(it)
                }
                -1 -> {
                    rpn += ' '
                    while (getPriority(stack.peek()) != 1) rpn += stack.pop()
                    stack.pop()
                }
            }
        }
        while (!stack.empty()) rpn += stack.pop()
        return rpn
    }

    private fun rpnToAnswer(rpn: String): Double? {
        try {

            val stack: Stack<Double> = Stack()
            var i = 0
            while (i < rpn.length) {
                if (rpn[i] == ' ') {
                    i++
                    continue
                }
                if (getPriority(rpn[i]) == 0) {
                    var operand = ""
                    while (rpn[i] != ' ' && getPriority(rpn[i]) == 0) {
                        operand += rpn[i++]
                        if (i == rpn.length) break
                    }
                    stack.push(operand.toDouble())

                }
                if (getPriority(rpn[i]) > 1) {
                    val tmp1: Double = stack.pop()
                    val tmp2: Double = stack.pop()
                    when (rpn[i]) {
                        '+' -> stack.push(tmp2 + tmp1)
                        '-' -> stack.push(tmp2 - tmp1)
                        '*' -> stack.push(tmp2 * tmp1)
                        '/' -> stack.push(tmp2 / tmp1)
                    }
                }
                i++
            }
            return stack.pop()
        } catch (e: Exception) {
            return null
        }
    }

    private fun getPriority(symbol: Char): Int {
        return when (symbol) {
            '*' -> 3
            '/' -> 3
            '+' -> 2
            '-' -> 2
            '(' -> 1
            ')' -> -1
            else -> 0
        }
    }

}
