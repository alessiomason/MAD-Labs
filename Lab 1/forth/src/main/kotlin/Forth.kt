class Forth {
    fun evaluate(vararg line: String): List<Int> {
        var lista=  mutableListOf<Int>()
        var j=0;
        for (str in line) {
            val stack = str.split(' ').reversed().toMutableList()
            for((i, _) in stack.withIndex()) {
                lista.add(j,operation(stack,i));
                j++;

            }

        }
        return lista;
    }


}
fun operation(stack:MutableList<String>, i: Int): Int {
    val s = stack.removeFirst();

    return when {
        s == "+" ->{ operation(stack, i) + operation(stack, i+1) }
        s == "*" -> operation(stack, i) * operation(stack, i+1)
        s == "-" -> operation(stack, i) - operation(stack, i+1)
        s == "/" -> operation(stack, i) / operation(stack, i+1)
        s.first().isDigit() -> s.toInt()
        else -> 0
    }
}