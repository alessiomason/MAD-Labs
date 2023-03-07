class Forth {
    fun evaluate(vararg line: String): List<Int> {
        for (str in line) {
            val stack = str.split(' ').reversed().toMutableList()
            for((i, _) in stack.withIndex()) {
                operation(stack,i)
            }
        }
    }

}
fun operation(stack:MutableList<String>, i: Int): Int {
    val s = stack[i]

    return when {
        s == "+" -> operation(stack, i+1) + operation(stack, i+2)
        s == "*" -> operation(stack, i+1) * operation(stack, i+2)
        s == "-" -> operation(stack, i+1) - operation(stack, i+2)
        s == "/" -> operation(stack, i+1) / operation(stack, i+2)
        s.first().isDigit() -> s.toInt()
        else -> 0
    }
}