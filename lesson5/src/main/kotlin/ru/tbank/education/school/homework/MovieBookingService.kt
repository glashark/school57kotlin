package ru.tbank.education.school.homework

import kotlin.IllegalArgumentException
import kotlin.collections.all


// Исключение, которое выбрасывается при попытке забронировать занятое место
class SeatAlreadyBookedException(message: String) : Exception(message)

// Исключение, которое выбрасывается при попытке забронировать место при отсутствии свободных мест
class NoAvailableSeatException(message: String) : Exception(message)

data class BookedSeat(
    val movieId: String, // идентификатор фильма
    val seat: Int // номер места
)


class MovieBookingService(
    private val maxQuantityOfSeats: Int // Максимальное кол-во мест
) {
    init {
        if (maxQuantityOfSeats <= 0) {
            throw IllegalArgumentException("The max quantity of seats must be greater than zero")
        }
    }

    val book = mutableListOf<BookedSeat>()
    fun bookSeat(movieId: String, seat: Int) {
        if (seat > maxQuantityOfSeats || seat < 1) {
            throw IllegalArgumentException("The seat $seat is not available")
        }
        if (book.contains(BookedSeat(movieId, seat))) {
            throw SeatAlreadyBookedException("The seat $seat is already booked")
        }
        var cnt : Int = 0
        for (bookedSeat in book) {
            if (bookedSeat.movieId == movieId) {
                cnt++
            }
        }
        if (cnt == maxQuantityOfSeats) {
            throw NoAvailableSeatException("The seat $seat isn't available")
        }
        book.add(BookedSeat(movieId, seat))
    }

    fun cancelBooking(movieId: String, seat: Int) {
        if (!book.contains(BookedSeat(movieId, seat))) {
            throw SeatAlreadyBookedException("The seat $seat isn't available")
        }
        book.remove(BookedSeat(movieId, seat))
    }

    fun isSeatBooked(movieId: String, seat: Int): Boolean {
        return book.contains(BookedSeat(movieId, seat))
    }
}



fun main() {
    val books = MovieBookingService(10)
    try {
        books.bookSeat("harry Potter", 8)
        books.bookSeat("harry Potter chapter 2", 1)
        books.bookSeat("harry Potter", 3)
    } catch (e: IllegalArgumentException) {
        println(e.message)
    }
    try {
        books.bookSeat("harry Potter", 11)
    } catch (e: IllegalArgumentException) {
        println(e.message)
    }

    try {
        books.cancelBooking("harry Potter", 2)
    } catch (e: NoSuchElementException) {
        println(e.message)
    }
    try {
        books.cancelBooking("harry Potter", 3)
    } catch (e: NoSuchElementException) {
        println(e.message)
    }

    books.isSeatBooked("harry Potter chapter 2", 1)
}
