class Forth {
    fun evaluate(vararg line: String): List<Int> {
        val list = mutableListOf<Int>()

        for (str in line) {
            val stack = str.split(' ').toMutableList()


            while (stack.isNotEmpty()) {
                val result = operation(stack, stack.count() - 1)
                list.add(0, result)
            }
        }

        return list
    }
}

// 3 4 + 5 swap
// 1 2 swap 3 swap 4 swap
// 2 1 3 swap 4 swap
// 2 3 1 4 swap
// 2 3 4 1

// swap 4 swap 3 swap 2 1
fun operation(stack: MutableList<String>, i: Int): Int {
    val s = stack.removeAt(i).lowercase()
    println(s)
    return when {
        s == "+" -> {
            val (op1, op2) = calculateLhsRhs(stack, i)
            return op1 + op2
        }
        s == "*" -> {
            val (op1, op2) = calculateLhsRhs(stack, i)
            return op1 * op2
        }
        s == "-" -> {
            val (op1, op2) = calculateLhsRhs(stack, i)
            return op1 - op2
        }
        s == "/" -> {
            val (op1, op2) = calculateLhsRhs(stack, i)
            if (op2 == 0)
                throw Exception("divide by zero")

            return op1 / op2
        }
        s == "swap" -> {
            val (op1, op2) = calculateLhsRhs(stack, i)

            stack.add("$op2")
            return op1
        }
        s.first().isDigit() -> s.toInt()
        else -> throw Exception("empty stack")
    }
}

fun calculateLhsRhs(stack: MutableList<String>, i: Int): Pair<Int, Int> {
    var op1: Int? = null
    var op2: Int? = null
    var op1Failed = false
    var op2Failed = false

    try {
        op2 = operation(stack, i - 1)
    } catch (e: Exception) {
        op2Failed = true
    }

    try {
        op1 = operation(stack, i - 2)
    } catch (e: Exception) {
        op1Failed = true
    }

    if (op1Failed && op2Failed)
        throw Exception("empty stack")
    else if (op1Failed || op2Failed)
        throw Exception("only one value on the stack")

    return Pair(op1!!, op2!!)
}