import calculator.lexicalAnalysis.LexicalAnalyzer
import calculator.lexicalAnalysis.TokenKind
import calculator.parsing.TreeCreator
import calculator.parsing.TreeNode
import calculator.parsing.position

var variables = mutableMapOf<String, String>()
var run = true

fun main() {
    while (run) {
        print(">> ")
        val line = read()
        val tokens = LexicalAnalyzer().analysis(line!!)
        val tree = TreeCreator(tokens.getAllTokens()).create()
        write(tree)
    }
}

private fun read() = when(val input = readLine()) {
    "q" -> {
        run = false
        println("Calculator stopped")
        null
    }
    else -> input
}

private fun write(tree: TreeNode) {
    when(tree.getNodeType()) {
        TokenKind.Constant -> println(tree.getParent())
        TokenKind.Identifier -> {
            val id = tree.getChildren()[0].getParent()

            if(variables.containsKey(id)){
                println(variables[id])
            } else {
                println("No variables with name $id")
            }
        }
        TokenKind.AssignOp -> {
            val id = tree.getChildren()[0].getParent()
            val value = tree.getChildren()[1].getParent()

            if(variables.containsKey(id)){
                variables[id] = value
            } else {
                println("No variables with name $id")
            }
        }
        TokenKind.Declaration -> {
            if (tree.getChildren().size == 1) {
                val id = tree.getChildren()[0].getParent()
                variables[id] = "null"
            } else {
                val id = tree.getChildren()[0].getParent()
                val value = tree.getChildren()[1].getParent()

                variables[id] = value
            }
        }
        else -> println("Invalid token in position ${position + 1}: ${tree.getParent()}")
    }
}