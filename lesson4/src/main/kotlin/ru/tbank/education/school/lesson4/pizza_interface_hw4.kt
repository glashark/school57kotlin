package ru.tbank.education.school.lesson3.pizzeria_homework

enum class SizePizza(val value: Int) {
    SMALL(0),
    MEDIUM(75),
    LARGE(150),
}

enum class Ingredient(val value: Int) {
    TOMATO(50),
    CUCUMBER(50),
    SAUSAGE(80),
    MUSHROOM(100),
    CHEESE(80)
}

enum class PizzaType(val value: String) {
    MARGARET("Маргарита"),
    PEPPERONI("Пепперони"),
    FOURCHEESES("Четыре сыра"),
    CAESAR("Цезарь")
}


fun interface PaymentSystem {
    abstract fun AcceptPayment() : Boolean
}
open class BoolPaymentCash : PaymentSystem {
    override fun AcceptPayment() : Boolean {
        println("Your payment by cash has been accepted")
        return true
    }
}
open class BoolPaymentCard : PaymentSystem {
    override fun AcceptPayment() : Boolean {
        println("Your payment by bank card has been accepted")
        return true
    }
}

fun interface Present {
    abstract fun YourPresent() : String
}


interface Delivery {
    abstract fun StartDelivery() : String
    abstract fun NotifyDelivery() : String
}

open class ToTable : Delivery {
    override fun StartDelivery(): String {
        return "this order is excepted"
    }
    override fun NotifyDelivery(): String {
        return "Your order has been delivered"
    }
}
open class PickUp : Delivery {
    override fun StartDelivery(): String {
        return "you can pick up your order"
    }
    override fun NotifyDelivery(): String {
        return "You picked up your order"
    }
}
open class ToHome : Delivery {
    override fun StartDelivery(): String {
        return "this order is excepted"
    }
    override fun NotifyDelivery(): String {
        return "Your order has been delivered"
    }
}

interface MenuItem {
    val title: String
    val price: Int
}

open class Client(
    val name: String
) {
}

class DumbClient : Client("Dumb") {
}

open class Pizza (
    protected val name : PizzaType,
    protected val size : SizePizza,
    public val ingredients : MutableList<Ingredient>,
) : MenuItem {

    // override fun YourPrice() : Int = 0;

    override val title: String
        get() = "Пицца (${name.value} x ${size})"

    constructor(name : PizzaType) : this(name, SizePizza.SMALL, mutableListOf(Ingredient.CUCUMBER, Ingredient.SAUSAGE, Ingredient.MUSHROOM))

    open val Size: Int
        get() = when (size) {
            SizePizza.SMALL -> 25
            SizePizza.MEDIUM -> 75
            SizePizza.LARGE -> 150
        }
    override val price: Int
        get() {
            var ans = 0
            ingredients.forEach { ingredient ->
                ans += ingredient.value
            }
            ans += when (size) {
                SizePizza.SMALL -> 25
                SizePizza.MEDIUM -> 75
                SizePizza.LARGE -> 150
            }
            return ans;
        }
}


enum class IngredientSalad(val value: Int) {
    TOMATO(30),
    CUCUMBER(30),
    PEPPER(40),
    CHEESE(60),
    GREENERY(50),
    SAUSAGE(60)
}

enum class SaladType(val value: String) {
    GREEK("Греческий"),
    OLIVIER("Оливье"),
    CAESAR("Цезарь")
}

open class Salad (
    protected val name : SaladType,
): MenuItem {
    override val price: Int
        get() = 50
    override val title : String
        get() = "Салат  (${name.value})"
}


sealed class Progress(val now : String) {
    object Accepted : Progress("Your order has been accepted")
    object Preparing : Progress("Your order is being prepared")
    object Ready : Progress("Your order is ready")
}

fun interface PriceCalculator {
    abstract fun Calculate(items: List<MenuItem>) : Int
}

data class YourOrder(
    val client: Client,
    val delivery: Delivery,
    val payment : PaymentSystem,
    val present : Present,
    var progressorder: Progress,
    var items: MutableList<MenuItem>,
    val priceCalculator : PriceCalculator = PriceCalculator { items -> items.sumOf { it.price } }
) {
    val price : Int
        get() = priceCalculator.Calculate(items)
}



fun main() {
    // Fix issue with charset
    System.setOut(java.io.PrintStream(System.out, true, "UTF-8"))

    var s : Pizza
    s = Pizza(PizzaType.MARGARET, SizePizza.MEDIUM, mutableListOf())
    s.ingredients.add(Ingredient.TOMATO)
    s.ingredients.add(Ingredient.CUCUMBER)
    s.ingredients.add(Ingredient.SAUSAGE)

    var sal : Salad
    sal = Salad(SaladType.GREEK)
    var o : YourOrder
    var deliv : Delivery
    deliv = ToHome()
    var pr : Progress = Progress.Accepted  // насколько заказ готов
    var pay : PaymentSystem
    pay = BoolPaymentCard()
    val pres = Present {
        println("A gift is available to you :)")
        ""
    }
    pres.YourPresent()

    o = YourOrder(Client("Vasya"), deliv, pay, pres, pr, mutableListOf(s, sal, Pizza(PizzaType.CAESAR)))
    println("Your order price: " + o.price)
    println("Your order items:")
    o.items.forEach {
            item -> println("${item.title} - ${item.price.toInt()}")
    }
    println()

    if (!o.payment.AcceptPayment()) {
        println("Sorry, you need to pay")
        return
    }
    o.present.YourPresent()
    println()

    println("* Status of your order now: " + pr.now)
    Thread.sleep(1000)
    pr = Progress.Preparing
    println("* Status of your order now: " + pr.now)
    Thread.sleep(2000)
    pr = Progress.Ready
    println("* Status of your order now: " + pr.now)
    println()
    println(o.client.name + ", " + deliv.StartDelivery())
    println("That's all. " + deliv.NotifyDelivery())
}

