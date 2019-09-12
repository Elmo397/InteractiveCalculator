package calculator.parsing

import calculator.lexicalAnalysis.Token
import calculator.lexicalAnalysis.TokenKind

class TreeCreator(private val allTokens: MutableList<Token>) {
    private var tokenNumb = 0

    fun create(): TreeNode {
        return when{
            allTokens.size > 1 && allTokens[tokenNumb + 1].getTokenKind() ==  TokenKind.AssignOp -> assignment()
            allTokens[tokenNumb].getTokenKind() == TokenKind.Declaration -> variableDeclaration()
            else -> expression()
        }
    }

    private fun variableDeclaration(): TreeNode {
        val variable = TreeNode(allTokens[tokenNumb].getToken(), TokenKind.Declaration)

        tokenNumb++
        variable.addChild(identifier(allTokens[tokenNumb]))

        if (allTokens.size > 2 && allTokens[++tokenNumb].getTokenKind() == TokenKind.AssignOp) {
            tokenNumb++
            variable.addChild(expression())
        }

        return variable
    }

    private fun assignment(): TreeNode {
        val expression = expression()

        tokenNumb++
         val assignment = TreeNode(allTokens[tokenNumb].getToken(), TokenKind.AssignOp)
        assignment.addChild(identifier(allTokens[tokenNumb - 1]))
        tokenNumb++
        assignment.addChild(expression)

        return assignment
    }

    private fun identifier(id: Token) = TreeNode(id.getToken(), TokenKind.Identifier)

    private fun expression(): TreeNode {
        val expression = createTree(allTokens, tokenNumb)
        tokenNumb = position

        return expression
    }
}