import java.io.File
import java.io.IOException

data class Account(val accountNumber: String, var balance: Double)

class BankSimulator(private val fileName: String) {
    private val accounts = mutableMapOf<String, Account>()

    init {
        loadAccounts()
    }

    private fun loadAccounts() {
        val file = File(fileName)
        if (file.exists()) {
            file.forEachLine { line ->
                val parts = line.split(",")
                if (parts.size == 2) {
                    val accountNumber = parts[0]
                    val balance = parts[1].toDoubleOrNull() ?: 0.0
                    accounts[accountNumber] = Account(accountNumber, balance)
                }
            }
        }
    }

    private fun saveAccounts() {
        val file = File(fileName)
        file.printWriter().use { out ->
            accounts.values.forEach { account ->
                out.println("${account.accountNumber},${account.balance}")
            }
        }
    }

    fun createAccount(accountNumber: String) {
        if (accounts.containsKey(accountNumber)) {
            println("Conta já existente.")
        } else {
            accounts[accountNumber] = Account(accountNumber, 0.0)
            saveAccounts()
            println("Conta criada com sucesso.")
        }
    }

    fun deposit(accountNumber: String, amount: Double) {
        val account = accounts[accountNumber]
        if (account != null) {
            account.balance += amount
            saveAccounts()
            println("Depósito de R$ $amount realizado com sucesso.")
        } else {
            println("Conta não encontrada.")
        }
    }

    fun withdraw(accountNumber: String, amount: Double) {
        val account = accounts[accountNumber]
        if (account != null) {
            if (account.balance >= amount) {
                account.balance -= amount
                saveAccounts()
                println("Saque de R$ $amount realizado com sucesso.")
            } else {
                println("Seu saldo é insuficiente.")
            }
        } else {
            println("Conta não encontrada.")
        }
    }

    fun checkBalance(accountNumber: String) {
        val account = accounts[accountNumber]
        if (account != null) {
            println("Saldo da conta $accountNumber: R$ ${account.balance}")
        } else {
            println("Conta não encontrada.")
        }
    }
}

fun main() {
    val simulator = BankSimulator("accounts.txt")

    println("Bem-vindo ao Bank Simulator!")
    println("Escolha uma opção:")
    println("1 - Criar conta")
    println("2 - Depositar")
    println("3 - Sacar")
    println("4 - Consultar saldo")
    println("5 - Sair")

    while (true) {
        print("Digite a opção escolhida: ")
        when (readLine()) {
            "1" -> {
                print("Digite o número da sua conta Bank Simulator: ")
                val accountNumber = readLine() ?: ""
                simulator.createAccount(accountNumber)
            }
            "2" -> {
                print("Digite o número da sua conta Bank Simulator: ")
                val accountNumber = readLine() ?: ""
                print("Digite o valor do depósito: ")
                val amount = readLine()?.toDoubleOrNull() ?: 0.0
                simulator.deposit(accountNumber, amount)
            }
            "3" -> {
                print("Digite o número da sua conta Bank Simulator: ")
                val accountNumber = readLine() ?: ""
                print("Digite o valor do saque: ")
                val amount = readLine()?.toDoubleOrNull() ?: 0.0
                simulator.withdraw(accountNumber, amount)
            }
            "4" -> {
                print("Digite o número da sua conta Bank Simulator: ")
                val accountNumber = readLine() ?: ""
                simulator.checkBalance(accountNumber)
            }
            "5" -> {
                println("Saindo do Bank Simulator...")
                return
            }
            else -> println("Opção inválida.")
        }
    }
}
