package calculator.lexicalAnalysis

import java.util.*

class TokensList {
    private val allTokens = arrayListOf<Token>()

    fun addToken(token: Token) {
        allTokens.add(token)
    }

    fun getAllTokens(): MutableList<Token> = Collections.unmodifiableList(allTokens)
}