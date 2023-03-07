import java.util.concurrent.atomic.AtomicLong

class BankAccount {
    private var atomicBalance = AtomicLong(0)
    val balance: Long
        get() {
            if (closed) throw IllegalStateException("The account is closed!")
            else return atomicBalance.get()
        }

    private var closed = false

    fun adjustBalance(amount: Long) {
        if (closed)
            throw IllegalStateException("The account is closed!")

        atomicBalance.addAndGet(amount)
    }

    fun close() {
        closed = true
    }
}
