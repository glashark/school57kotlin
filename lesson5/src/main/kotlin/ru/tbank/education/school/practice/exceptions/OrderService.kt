package ru.tbank.education.school.practice.exceptions

/**
 * Интерфейс для управления заказами в магазине.
 */
interface OrderService {

    /**
     * Добавляет товар в корзину.
     *
     * @param productId ID товара
     * @param quantity количество
     * @throws IllegalArgumentException если quantity <= 0
     */
    fun addToCart(productId: String, quantity: Int)

    /**
     * Удаляет товар из корзины.
     *
     * @param productId ID товара
     * @throws NoSuchElementException если товара нет в корзине
     */
    fun removeFromCart(productId: String)

}

data class OrderItem(
    val productId: String,
    val quantity: Int
)

class OrderServiceImpl : OrderService {
    val cart = mutableListOf<OrderItem>()

    override fun addToCart(productId: String, quantity: Int) {
        if (quantity <= 0) {
            throw IllegalArgumentException(
                "Попытка добавить продукт в количестве меньшем, чем единица"
            )
        }

        val orderItem = OrderItem(productId, quantity)
        cart.add(orderItem)
    }

    override fun removeFromCart(productId: String) {
        if (!cart.removeIf { it.productId == productId }) {
            throw NoSuchElementException("Не найдены элементы для удаления из заказа")
        }
    }
}

//fun main() {
//    val orderServiceImpl = OrderServiceImpl()
//    orderServiceImpl.addToCart("123", 1)
//    orderServiceImpl.removeFromCart("123")
//    orderServiceImpl.removeFromCart("123")
//}




//package ru.tbank.education.school.trycatchpractise
//
///**
// * Интерфейс сервиса бронирования билетов в кинотеатр
// */
//interface MovieBookingService {
//
//    /**
//     * Бронирует указанное место для фильма.
//     *
//     * @param movieId идентификатор фильма
//     * @param seat номер места
//     * @throws IllegalArgumentException если место вне допустимого диапазона
//     * @throws SeatAlreadyBookedException если место уже забронировано
//     */
//    fun bookSeat(movieId: String, seat: Int)
//
//    /**
//     * Отменяет бронь указанного места.
//     *
//     * @param movieId идентификатор фильма
//     * @param seat номер места
//     * @throws NoSuchElementException если место не было забронировано
//     */
//    fun cancelBooking(movieId: String, seat: Int)
//
//    /**
//     * Проверяет, забронировано ли место
//     *
//     * @return true если место занято, false иначе
//     */
//    fun isSeatBooked(movieId: String, seat: Int): Boolean
//}
//
///**
// * Исключение, которое выбрасывается при попытке забронировать занятое место
// */
//class SeatAlreadyBookedException(message: String) : Exception(message) {
//    if (maxQuantityOfSeats <= 0) {
//        catch (e: IllegalArgumentException) {
//            println("Количество мест меньше нуля")
//            throw e
//        }
//    }
//    private val book = mutableMapOf<String, MutableList<Boolean>>()
//
//    override fun bookSeat(movieId: String, seat: Int) {
//        if (seat < 1 || seat > maxQuantityOfSeats) {
//            catch (e: IllegalArgumentException) {
//                println("Недопустимый номер места")
//                throw e
//            }
//        }
//        if (book.containsKey(movieId)) {
//            if (book[movieId]?.all { !it } ?: false) {
//                catch (e: NoAvailableSeatException) {
//                    println("Все места на этот фильм уже заняты")
//                    throw e
//                }
//            }
//            if (book[movieId][seat - 1]) { // если true, то место не занято
//                book[movieId][seat - 1] = false
//                println("Вы успешно забронировали место ${seat} на фильм ${movieId}")
//            } else {
//                catch (e: SeatAlreadyBookedException) {
//                    println("Это место уже занято")
//                    throw e
//                }
//            }
//
//        } else {
//            book[movieId] = MutableList(maxQuantityOfSeats) { true }
//            book[movieId][seat - 1] = false
//            println("Вы успешно забронировали место ${seat} на фильм ${movieId}")
//        }
//    }
//
//    override fun cancelBooking(movieId: String, seat: Int) {
//        if (book[movieId][seat - 1]) {
//            catch (e: NoSuchElementException) {
//                println("Место не было забронировано")
//                throw e
//            }
//        }
//        book[movieId][seat - 1] = true
//        println("Теперь это место не забронировано")
//    }
//
//    override fun isSeatBooked(movieId: String, seat: Int): Boolean {
//        if (!book[movieId][seat - 1]) {
//            return true
//        } else {
//            return false
//        }
//    }
//}
//
//
//fun main() {
//    val booking = SeatAlreadyBookedException()
//    booking.maxQuantityOfSeats = 3
//    println(booking.bookSeat("harry potter", 1))
//    println(booking.bookSeat("harry potter", 2))
//}