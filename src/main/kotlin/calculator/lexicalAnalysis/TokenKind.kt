package calculator.lexicalAnalysis

enum class TokenKind {
    AssignOp,
    BinaryOp,
    Constant,
    Identifier,
    Declaration,
    OpenBracket,
    CloseBracket,
    Unknown
}