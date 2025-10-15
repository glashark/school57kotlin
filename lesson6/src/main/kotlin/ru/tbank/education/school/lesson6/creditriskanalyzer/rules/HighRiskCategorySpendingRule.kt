package ru.tbank.education.school.lesson6.creditriskanalyzer.rules

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.TransactionCategory
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.TransactionRepository

/**
 * Анализирует долю расходов клиента в рисковых категориях.
 *
 * Идея:
 * - Получить все транзакции клиента.
 * - Исключить доходы (SALARY).
 * - Посчитать общие сумму переводов в категориях GAMBLING, CRYPTO, TRANSFER.
 * - Посчитать общие сумму всех переводов (без SALARY).
 * - Рассчитать долю переводов в категориях GAMBLING, CRYPTO, TRANSFER.
 *
 * Как считать risk:
 * - Если доля > 0.6 → HIGH
 * - Если доля > 0.3 → MEDIUM
 * - Иначе → LOW
 */
class HighRiskCategorySpendingRule(
    private val transactionRepo: TransactionRepository
) : ScoringRule {

    override val ruleName: String = "High-Risk Category Spending"

    override fun evaluate(client: Client): ScoringResult {
        val transactions = transactionRepo.getTransactions(client.id)
        var gambsum : Long = 0
        var crypsum : Long = 0
        var transsum : Long = 0
        var sum : Long = 0

        for (transaction in transactions) {
            if (transaction.category == TransactionCategory.GAMBLING) {
                gambsum += transaction.amount
            } else if (transaction.category == TransactionCategory.CRYPTO) {
                crypsum += transaction.amount
            } else if (transaction.category == TransactionCategory.TRANSFER) {
                transsum += transaction.amount
            }

            if (transaction.category != TransactionCategory.SALARY) {
                sum += transaction.amount
            }
        }

        val part : Double = (gambsum + crypsum + transsum).toDouble() / sum.toDouble()
        val risk = when {
            part > 0.6 -> PaymentRisk.HIGH
            part > 0.3 || sum == 0.toLong() -> PaymentRisk.MEDIUM
            else -> PaymentRisk.LOW
        }

        return ScoringResult(ruleName, risk)
    }
}
