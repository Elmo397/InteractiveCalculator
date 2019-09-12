package calculator.lexicalAnalysis

import java.util.*
import java.util.regex.Pattern

/**
 * A class that implements a lexical analyzer that converts a sequence of characters
 * into a sequence of tokens of type Token.
 *
 * @author Mamedova Elnara
 */
class LexicalAnalyzer {
    private val keywords = arrayListOf("Var")
    private val operators = arrayListOf("+", "-", "*", "/", "=")
    private val delimiters = arrayListOf("(", ")")
    private val constants = Pattern.compile("\\d+")
    private val identifiers = Pattern.compile("[a-z]+\\d*[a-z]*")

    fun analysis(line: String): TokensList {
        val modifiedLine = modify(line)
        val tokensList = TokensList()
        var position = 0

        var delim = " " + "\n" + "\t" + "\r"
        operators.forEach { elem -> delim += elem }
        delimiters.forEach { elem -> delim += elem }

        val stringTokenizer = StringTokenizer(modifiedLine, delim, true)

        while (stringTokenizer.hasMoreTokens()) {
            val token = stringTokenizer.nextToken()

            when {
                keywords.contains(token) -> tokensList.addToken(defineKeyword(position, token))
                operators.contains(token) -> tokensList.addToken(defineOperator(position, token))
                constants.matcher(token).matches() -> tokensList.addToken(Token(position, token, TokenKind.Constant))
                identifiers.matcher(token).matches() -> tokensList.addToken(Token(position, token, TokenKind.Identifier))
                delimiters.contains(token) -> tokensList.addToken(defineDelimiters(position, token))
                !isWhitespace(token) -> {
                    printErrorMsg(position, token)
                    tokensList.addToken(Token(position, token, TokenKind.Unknown))
                }
            }

            position += token.length
        }

        return tokensList
    }

    private fun modify(line: String): String {
        line.replace(Regex("^-"), "0 -")
            .replace(Regex("\\(\\s*-"), "(0 -")
            .replace(Regex("=\\s*-"), "= 0 -")

        return line
    }

    private fun defineKeyword(position: Int, token: String) = Token(position, token, TokenKind.Declaration)

    private fun defineOperator(position: Int, token: String) = when (token) {
        "=" -> Token(position, token, TokenKind.AssignOp)
        else -> Token(position, token, TokenKind.BinaryOp)
    }

    private fun defineDelimiters(position: Int, token: String) = when (token) {
        "(" -> Token(position, token, TokenKind.OpenBracket)
        else -> Token(position, token, TokenKind.CloseBracket)
    }

    private fun printErrorMsg(position: Int, token: String) {
        System.err.println("Incorrect token in position $position: $token")
    }

    private fun isWhitespace(token: String) = Pattern.compile("\\s").matcher(token).matches()
}