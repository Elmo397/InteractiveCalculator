package calculator.lexicalAnalysis

class Token(private val position: Int, private val token: String, private val tokenKind: TokenKind) {
    fun getTokenKind() = tokenKind
    fun getToken() = token
    fun getPosition() = position
}