package ru.tbank.education.school.lesson6.creditriskanalyzer.rules

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Transaction
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.TransactionRepository
import java.time.LocalDateTime

/**
 * Проверяет разнообразие категорий трат клиента.
 *
 * Идея:
 * - Получить все транзакции клиента за последние 3 месяца.
 * - Посчитать, в скольких уникальных категориях клиент тратит деньги.
 *
 * Как считать score:
 * - Если меньше 3 категорий → HIGH (тратит только на одно и то же)
 * - Если от 3 до 6 категорий → MEDIUM
 * - Если больше 6 категорий → LOW (разнообразное поведение, меньше риск)
 *
 */
class SpendingCategoryDiversityRule(
    private val transactionRepo: TransactionRepository
) : ScoringRule {

    override val ruleName: String = "Spending Category Diversity"

    override fun evaluate(client: Client): ScoringResult {
        val threeMonthsAgo = LocalDateTime.now().minusMonths(3)
        val transactions = transactionRepo.getTransactions(client.id).filter{ it.date.isAfter(threeMonthsAgo)}
        val cnt = mutableSetOf<Transaction>()

        for (transaction in transactions) {
            cnt.add(transaction)
        }

        val score = when {
            cnt.size < 3 -> PaymentRisk.HIGH
            cnt.size < 6 -> PaymentRisk.MEDIUM
            else -> PaymentRisk.LOW
        }
        return ScoringResult(ruleName, score)
    }
}