package ru.tbank.education.school.lesson6.creditriskanalyzer.rules

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.LoanRepository
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.OverdueRepository

/**
 * Необходимо посчитать количество открытых кредитов с просрочкой больше 30 дней
 *
 * Как считать score:
 * - Если таких кредитов больше 3 → HIGH (слишком свежие)
 * - Если такой кредит один → MEDIUM
 * - Если таких кредитов нет → LOW
 */
class LoanCountRule(
    private val loanRepository: LoanRepository,
    private val overdueRepository: OverdueRepository
) : ScoringRule {


    override val ruleName: String = "Loan Count"

    override fun evaluate(client: Client): ScoringResult {
        val loans = loanRepository.getLoans(client.id)
        val overdues = overdueRepository.getOverdues(client.id)
        var cnt : Int = 0

        for (loan in loans) {
            if (loan.isClosed) {
                continue
            }
            for (overdue in overdues) {
                if (overdue.loanId == loan.id && (overdue.daysOverdue > 30)) {
                    cnt++
                }
            }
        }

        val score = when {
            cnt > 3 -> PaymentRisk.HIGH
            cnt > 0 -> PaymentRisk.MEDIUM
            cnt == 0 -> PaymentRisk.LOW
            else -> PaymentRisk.HIGH // точно так?
        }
        return ScoringResult(ruleName, score)
    }
}