package ru.tbank.education.school.lesson8.homework.payments

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class PaymentProcessorTest {
    private lateinit var processor: PaymentProcessor

    @BeforeEach
    fun setUp() {
        processor = PaymentProcessor()
    }

    @Test
    @DisplayName("Некорректный срок жизни карты")
    fun validateExpiryDate() {
        val amount = 10
        val cardNumber = "2200150677255090"
        val currency = "USD"
        val customerId = "1"

        var expiryYear = 2025
        for (expiryMonth in (0..10) + (13..14)) {
            assertThrows(IllegalArgumentException::class.java) {
                processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
            }
        }
        for (expiryMonth in 11..12) {
            assertEquals("SUCCESS", processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId).status)
        }

        expiryYear = 2026
        for (expiryMonth in 1..12) {
            assertEquals("SUCCESS", processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId).status)
        }
        for (expiryMonth in (0..0) + (13..13)) {
            assertThrows(IllegalArgumentException::class.java) {
                processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
            }
        }

        expiryYear = 2024
        val expiryMonth = 1
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        }

    }


    @Test
    @DisplayName("Сумма платежа должна быть больше нуля")
    fun amountMustBePositive() {
        val amount = 0
        val cardNumber = "4111111111111111"
        val expiryMonth = 12
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        }
    }

    @Test
    @DisplayName("Номер карты должен состоять только из цифр")
    fun consistsOnlyOfNumbers() {
        val amount = 13
        val cardNumber = "a12345678909876"
        val expiryMonth = 12
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        }
    }

    @Test
    @DisplayName("Номер карты должен состоять из 13-19 цифр")
    fun consistsOf1319Digits() {
        val amount = 13
        val cardNumber = "1234567890"
        val expiryMonth = 12
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        }
    }

    @Test
    @DisplayName("Номер карты должен быть не пустым")
    fun numberIsntEmpty() {
        val amount = 13
        val cardNumber = ""
        val expiryMonth = 12
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        }
    }

    @Test
    @DisplayName("Месяц должен быть от 1 до 12")
    fun expirationDateMonth() {
        val amount = 13
        val cardNumber = "4111111111111111"
        val expiryMonth = 13
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        }
    }
    @Test
    @DisplayName("Год действия должен быть не просрочен")
    fun expirationDateYear() {
        val amount = 13
        val cardNumber = "4111111111111111"
        val expiryMonth = 11
        val expiryYear = 2024
        val currency = "USD"
        val customerId = "1"
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        }
    }

    @Test
    @DisplayName("Валюта должна быть заполнена")
    fun currencyIsntEmpty() {
        val amount = 13
        val cardNumber = "4111111111111111"
        val expiryMonth = 11
        val expiryYear = 2026
        val currency = ""
        val customerId = "1"
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        }
    }
    @Test
    @DisplayName("Id пользователя должнен быть заполнен")
    fun customerIdIsntEmpty() {
        val amount = 13
        val cardNumber = "4111111111111111"
        val expiryMonth = 11
        val expiryYear = 2026
        val currency = "USD"
        val customerId = ""
        assertThrows(IllegalArgumentException::class.java) {
            processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        }
    }

    @Test
    @DisplayName("Номер карты является подозрительным")
    fun suspiciousCards() {
        val amount = 13
        var cardNumber = "55556789012345"
        val expiryMonth = 11
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"

        assertEquals("REJECTED", processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId).status)
        assertEquals("Payment blocked due to suspected fraud", processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId).message)

        cardNumber = "44406789012348"
        assertEquals("Card is blocked", processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId).message)

    }

    @Test
    @DisplayName("Номер карты не соблюдает алгоритм Луна")
    fun luhnAlgorithm() {
        val amount = 13
        var cardNumber = "5062821734567892"
        val expiryMonth = 11
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"
        var result = processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)

        assertEquals("REJECTED", result.status)
        assertEquals("Payment blocked due to suspected fraud", result.message)

        cardNumber = "506282173456"
        assertThrows(IllegalArgumentException::class.java) {
            result = processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        }

    }

    @Test
    @DisplayName("Конвертация только поддерживаемых валют")
    fun currencyConversion() {
        val amount = 1000
        val validCard = "4111111111111111"
        val expiryMonth = 11
        val expiryYear = 2026
        val results = listOf(
            processor.processPayment(amount, validCard, expiryMonth, expiryYear, "USD", "1"),
            processor.processPayment(amount, validCard, expiryMonth, expiryYear, "EUR", "2"),
            processor.processPayment(amount, validCard, expiryMonth, expiryYear, "GBP", "3"),
            processor.processPayment(amount, validCard, expiryMonth, expiryYear, "JPY", "4"),
            processor.processPayment(amount, validCard, expiryMonth, expiryYear, "RUB", "5")
        )

        results.forEach { result ->
            assertTrue(result.status in listOf("SUCCESS", "FAILED"))
        }
    }

    @Test
    @DisplayName("Неподдерживаемые валюты используют USD с предупреждением")
    fun unsupportedCurrency() {
        val amount = 13
        val cardNumber = "4111111111111111"
        val expiryMonth = 11
        val expiryYear = 2026
        val currency = "HI"
        val customerId = "1"
        assertTrue(processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId).status in listOf("SUCCESS", "FAILED"))
    }

    @Test
    @DisplayName("Оплата через шлюз не прошла")
    fun gatewayPayment() {
        val amount = 13
        val cardNumber = "4111111111111111"
        val expiryMonth = 11
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"

        assertEquals("SUCCESS", processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId).status)
        assertEquals("Payment completed", processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId).message)
    }

    @Test
    @DisplayName("Произошла ошибка: недостаточно средств")
    fun insufficientFunds() {
        val amount = 13
        val cardNumber = "5500000000000004"
        val expiryMonth = 11
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"

        val processAns = processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        assertEquals("FAILED", processAns.status)
        assertEquals("Insufficient funds", processAns.message)
    }

    @Test
    @DisplayName("Произошла ошибка: карта заблокирована")
    fun cardBlocked() {
        val amount = 13
        val cardNumber = "4444123456789012"
        val expiryMonth = 11
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"

        val processAns = processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        assertEquals("REJECTED", processAns.status)
        assertEquals("Payment blocked due to suspected fraud",processAns.message)
    }

    @Test
    @DisplayName("Произошла ошибка: превышен лимит транзакции")
    fun transactionLimit() {
        val amount = 500000
        val cardNumber = "4111111111111111"
        val expiryMonth = 11
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"

        val processAns = processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        assertEquals("FAILED", processAns.status)
        assertEquals("Transaction limit exceeded",processAns.message)
    }

    @Test
    @DisplayName("Произошла ошибка: таймаут шлюза")
    fun gatewayTimeout() {
        val amount = 1700
        val cardNumber = "4111111111111111"
        val expiryMonth = 11
        val expiryYear = 2026
        val currency = "USD"
        val customerId = "1"

        val processAns = processor.processPayment(amount, cardNumber, expiryMonth, expiryYear, currency, customerId)
        assertEquals("FAILED", processAns.status)
        assertEquals("Gateway timeout",processAns.message)
    }

    @Test
    @DisplayName("Неправильная пакетная обработка пустого списка")
    fun bulkProcessEmptyList() {
        assertTrue(processor.bulkProcess(emptyList()).isEmpty())
    }

    @Test
    @DisplayName("Пакетная обработка только валидных платежей")
    fun bulkProcessAllValid() {
        val payments = listOf(
            PaymentData(10, "4111111111111111", 12, 2026, "USD", "1"),
            PaymentData(10, "4111111111111111", 12, 2027, "USD", "2")
        )

        val results = processor.bulkProcess(payments)
        assertEquals(2, results.size)
        results.forEach { result ->
            assertEquals("SUCCESS", result.status)
            assertEquals("Payment completed", result.message)
        }
    }

    @Test
    @DisplayName("Пакетная обработка только невалидных платежей")
    fun bulkProcessAllInvalid() {
        val payments = listOf(
            PaymentData(-10, "4111111111111111", 12, 2026, "USD", "1"),
            PaymentData(10, "", 11, 2027, "USD", "2"),
            PaymentData(10, "4111111111111111", 0, 2026, "USD", "3"),
            PaymentData(10, "4111111111111111", 12, 2020, "USD", "4"),
            PaymentData(10, "4111111111111111", 12, 2026, "", "5"),
        )

        val results = processor.bulkProcess(payments)
        assertEquals(5, results.size)
        results.forEach { result ->
            assertEquals("REJECTED", result.status)
        }
    }

    @Test
    @DisplayName("Обработка исключений внутри цикла")
    fun bulkProcessException() {
        val payments = listOf(
            PaymentData(-10, "4111111111111111", 12, 2026, "USD", "1"),
            PaymentData(10, "4111111111111111", 11, 2027, "USD", "2")
        )

        val results = processor.bulkProcess(payments)
        assertEquals(2, results.size)

        assertEquals("REJECTED", results[0].status)
        assertEquals("Amount must be positive", results[0].message)

        assertEquals("SUCCESS", results[1].status)
    }

    @Test
    @DisplayName("Пакетная обработка с подозрительными картами")
    fun bulkDifferentCurrencies() {
        val payments = listOf(
            PaymentData(10, "4111111111111111", 12, 2026, "USD", "1"),
            PaymentData(10, "5555123456789012", 12, 2026, "EUR", "2")
        )

        val results = processor.bulkProcess(payments)

        assertEquals(2, results.size)
        assertEquals("SUCCESS", results[0].status)

        assertEquals("REJECTED", results[1].status)
        assertEquals("Payment blocked due to suspected fraud", results[1].message)
    }

    @Test
    @DisplayName("Пакетная обработка с логированием итогов")
    fun bulkLogging() {
        val payments = listOf(
            PaymentData(10, "4111111111111111", 12, 2026, "USD", "1"),
            PaymentData(10, "5500000000000004", 12, 2026, "USD", "2"),
            PaymentData(0, "4111111111111111", 12, 2026, "USD", "3"),
            PaymentData(170000, "4111111111111111", 12, 2026, "USD", "4")
        )

        val results = processor.bulkProcess(payments)

        assertEquals(4, results.size)
        val success = results.count { it.status == "SUCCESS" }
        val failed = results.count { it.status == "FAILED" }
        val rejected = results.count { it.status == "REJECTED" }

        assertEquals(1, success)
        assertEquals(2, failed)
        assertEquals(1, rejected)
    }

    @Test
    @DisplayName("Сумма должна быть больше нуля")
    fun loyalDiscountAmount() {
        assertThrows<IllegalArgumentException> {
            processor.calculateLoyaltyDiscount(10, 0)
        }
        assertThrows<IllegalArgumentException> {
            processor.calculateLoyaltyDiscount(10, -10)
        }
    }

    @Test
    @DisplayName("Если баллов не менее 10000")
    fun loyalDiscountPoints10000() {
        assertEquals(2000, processor.calculateLoyaltyDiscount(10000, 10000))
        assertEquals(5000, processor.calculateLoyaltyDiscount(15000, 30000))
        assertEquals(4000, processor.calculateLoyaltyDiscount(10000, 20000))
    }
    @Test
    @DisplayName("Если баллов не менее 5000")
    fun loyalDiscountPoints5000() {
        assertEquals(3000, processor.calculateLoyaltyDiscount(9999, 20000))
        assertEquals(1500, processor.calculateLoyaltyDiscount(5000, 10000))
    }
    @Test
    @DisplayName("Если баллов не менее 2000")
    fun loyalDiscountPoints2000() {
        assertEquals(1500, processor.calculateLoyaltyDiscount(4000, 20000))
        assertEquals(500, processor.calculateLoyaltyDiscount(2000, 5000))
    }
    @Test
    @DisplayName("Если баллов не менее 500")
    fun loyalDiscountPoints500() {
        assertEquals(500, processor.calculateLoyaltyDiscount(1999, 20000))
        assertEquals(400, processor.calculateLoyaltyDiscount(1000, 8000))
    }
    @Test
    @DisplayName("Если баллов менее 500")
    fun loyalDiscountPointsLess500() {
        assertEquals(0, processor.calculateLoyaltyDiscount(400, 20000))
        assertEquals(0, processor.calculateLoyaltyDiscount(300, 1000))
    }
}