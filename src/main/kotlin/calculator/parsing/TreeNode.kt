package calculator.parsing

import calculator.lexicalAnalysis.TokenKind
import java.util.*

class TreeNode(private val parent: String, private val nodeType: TokenKind) {
    private val children = ArrayList<TreeNode>()

    fun addChild(children: TreeNode) {
        this.children.add(children)
    }

    fun addChildren(children: List<TreeNode>) {
        this.children.addAll(children)
    }

    fun getParent() = parent

    fun getNodeType() = nodeType

    fun getChildren(): MutableList<TreeNode> = Collections.unmodifiableList(children)
}