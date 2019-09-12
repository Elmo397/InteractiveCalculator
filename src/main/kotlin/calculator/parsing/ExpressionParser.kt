package calculator.parsing

import calculator.lexicalAnalysis.Token
import calculator.lexicalAnalysis.TokenKind
import variables
import java.util.*
import java.util.ArrayDeque
import java.util.regex.Pattern

private val exprStack = Stack<TreeNode>()
private val exprResultList = ArrayList<TreeNode>()
var position = 0

/**
 * @return корневой элемент дерева выражения.
 */
fun createTree(allTokens: List<Token>, pos: Int): TreeNode {
    exprResultList.clear()
    exprStack.clear()

    position = pos
    return if(isCorrectExpression(allTokens)) {
        position = pos
        createPostfixForm(allTokens)

        val result = simplify()
        TreeNode(result, TokenKind.Constant)
    } else {
        TreeNode(allTokens[position].getToken(), TokenKind.Unknown)
    }
}

private fun isCorrectExpression(allTokens: List<Token>): Boolean {
    while (position < allTokens.size) {
        val token = allTokens[position]

        if(token.getTokenKind() == TokenKind.Identifier) {
            return variables.containsKey(token.getToken())
        }

        position++
    }

    return true
}

private fun createPostfixForm(allTokens: List<Token>) {
    while (position < allTokens.size) {
        val token = allTokens[position]

        when (token.getTokenKind()) {
            TokenKind.Identifier -> exprResultList.add(TreeNode(token.getToken(), TokenKind.Identifier))
            TokenKind.Constant -> exprResultList.add(TreeNode(token.getToken(), TokenKind.Constant))
            TokenKind.OpenBracket -> exprStack.add(TreeNode(token.getToken(), TokenKind.OpenBracket))
            TokenKind.CloseBracket -> {
                while (!exprStack.empty() && exprStack.lastElement().getNodeType() !== TokenKind.OpenBracket) {
                    exprResultList.add(exprStack.pop())
                }

                exprStack.pop()
            }
            TokenKind.BinaryOp -> {
                while (!exprStack.empty() && priority(token.getToken()) < priority(exprStack.lastElement().getParent())) {
                    exprResultList.add(exprStack.pop())
                }

                exprStack.push(TreeNode(token.getToken(), token.getTokenKind()))
            }
        }

        position++
    }

    while (!exprStack.empty()) {
        exprResultList.add(exprStack.pop())
    }
}

private fun priority(token: String) = when (token) {
    "(" -> 1
    "+" -> 2
    "-" -> 2
    "*" -> 3
    "/" -> 3
    else -> 4
}

private fun simplify(): String {
    val exprResult = ArrayDeque<Double>()

    exprResultList.forEach { token ->
        when (token.getParent()) {
            "+" -> exprResult.push(exprResult.pop() + exprResult.pop())
            "*" -> exprResult.push(exprResult.pop() * exprResult.pop())
            "-" -> {
                val right = exprResult.pop()
                val left = exprResult.pop()
                exprResult.push(left - right)
            }
            "/" -> {
                val right = exprResult.pop()
                val left = exprResult.pop()
                exprResult.push(left / right)
            }
            else -> {
                val value = token.getParent()
                val isIdentifier = Pattern.compile("[a-z]+\\d*[a-z]*").matcher(value).matches()
                val isKeyword = value == Keywords.Var.name

                if(isIdentifier && !isKeyword) {
                    getVariableValue(value)?.let { exprResult.push(it) }
                } else {
                    exprResult.push(value.toDouble())
                }
            }
        }
    }

    return if(!exprResult.isEmpty()) {
        exprResult.pop().toString()
    } else {
        "Undefined"
    }
}

private fun getVariableValue(token: String): Double? {
    return if(variables.containsKey(token)){
        val variableValue = variables.getValue(token)
        variableValue.toDouble()
    } else {
        null
    }
}