package ru.tbank.education.school.lesson6.creditriskanalyzer.rules

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.TicketRepository

/**
 * Проверяет, насколько часто клиент имеет нерешённые обращения в поддержку.
 *
 * Идея:
 * - Получить все тикеты клиента.
 * - Определить количество нерешённых обращений.
 * - Рассчитать долю нерешённых тикетов.
 *
 * Как считать risk:
 * - Если доля > 0.5 → HIGH
 * - Если доля > 0.2 → MEDIUM
 * - Иначе → LOW
 */
class UnresolvedTicketsRule(
    private val ticketRepo: TicketRepository
) : ScoringRule {

    override val ruleName: String = "Unresolved Tickets"

    override fun evaluate(client: Client): ScoringResult {
        val tickets = ticketRepo.getTickets(client.id)
        val ticketsUnresolved = ticketRepo.getTickets(client.id).count{ !it.resolved }
        val part: Double = ticketsUnresolved.toDouble() / tickets.size

        val risk = when {
            part > 0.5 -> PaymentRisk.HIGH
            part > 0.2 -> PaymentRisk.MEDIUM
            else -> PaymentRisk.LOW
        }
        return ScoringResult(ruleName, risk)
    }
}
