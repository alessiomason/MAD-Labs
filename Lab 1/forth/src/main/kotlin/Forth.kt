class Forth {
    fun evaluate(vararg line: String): List<Int> {
        val list = mutableListOf<Int>()

        for (str in line) {
            val stack = str.split(' ').toMutableList()

            while (stack.isNotEmpty()) {
                val result = operation(stack, stack.count() - 1)
                if (result != null)     // only case when this happens is if the stack is "x drop"
                    list.add(0, result)
            }
        }

        return list
    }
}

fun operation(stack: MutableList<String>, i: Int): Int? {
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
        s == "dup" -> {
            val op = try {
                operation(stack, i - 1)
            } catch (e: Exception) {
                throw Exception("empty stack")
            }

            // duplicates by adding to the stack and returning the same operand
            stack.add(op.toString())
            return op
        }
        s == "drop" -> {
            var j = i - 1
            var nDrop = 1
            // counts consecutive drop and operates them all at once
            // the current one is no more on the stack, it is already considered
            while (j >= 0 && stack[j].lowercase() == "drop") {
                nDrop++
                j--
            }

            try {
                for (k in 0 until nDrop) {
                    operation(stack, i - nDrop - k)     // execute all the consecutive drops
                    if (k != nDrop - 1)     // the first drop token has already been removed, avoid removing one more
                        stack.removeAt(i - nDrop - k) // remove the drop token
                }
            } catch (e: Exception) {
                throw Exception("empty stack")
            }


            return null
        }
        s == "swap" -> {
            val (op1, op2) = calculateLhsRhs(stack, i)

            // swaps by adding to the stack the top value and returning the second one
            stack.add(op2.toString())
            return op1
        }
        s == "over" -> {
            val (op1, op2) = calculateLhsRhs(stack, i)

            // takes the top of stack and the previous one, puts them again on top
            // of the stack and returns the previous of the two
            stack.add(op1.toString())
            stack.add(op2.toString())
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