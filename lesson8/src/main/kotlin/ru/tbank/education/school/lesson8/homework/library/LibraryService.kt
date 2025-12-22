package ru.tbank.education.school.lesson8.homework.library

class LibraryService {
    private val books = mutableMapOf<String, Book>()
    private val borrowedBooks = mutableSetOf<String>()
    private val borrowerFines = mutableMapOf<String, Int>()

    fun addBook(book: Book) {
        books[book.isbn] = book
    }

    fun borrowBook(isbn: String, borrower: String, daysOverdue: Int = 0) {
        if (hasOutstandingFines(borrower)) {
            throw IllegalArgumentException("У читателя есть непогашенные штрафы")
        }
        if (!books.contains(isbn)) {
            throw IllegalArgumentException("Книги $isbn нет в каталоге")
        }
        if (borrowedBooks.contains(isbn)) {
            throw IllegalArgumentException("Эту книгу $isbn уже взяли")
        }
        borrowedBooks.add(isbn)

        if (daysOverdue > 10) {
            borrowerFines[borrower] = (borrowerFines[borrower] ?: 0) + (daysOverdue - 10) * 60
        }
    }

    fun returnBook(isbn: String) {
        if (!borrowedBooks.contains(isbn)) {
            throw IllegalArgumentException("Эта книга $isbn не была выдана")
        }
        borrowedBooks.remove(isbn)
    }

    fun isAvailable(isbn: String): Boolean {
        return !borrowedBooks.contains(isbn)
    }

    fun calculateOverdueFine(daysOverdue: Int, isbn: String? = null): Int {
        if (isbn != null && !borrowedBooks.contains(isbn)) {
            return 0
        }
        if (daysOverdue <= 10) {
            return 0
        }
        return (daysOverdue - 10) * 60
    }

    private fun hasOutstandingFines(borrower: String): Boolean {
        return (borrowerFines[borrower] ?: 0) > 0
    }
}