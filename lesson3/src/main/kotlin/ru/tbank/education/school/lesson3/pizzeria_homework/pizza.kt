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

// меню, где находятся все варианты заказа
abstract class MenuItem(
    open val title: String,
    open val price: Int,
) {
    // abstract fun YourPrice() : Int
}

open class Pizza (
    protected val name : PizzaType,
    protected val size : SizePizza,
    public val ingredients : MutableList<Ingredient>
) : MenuItem("Пицца", 0) {
    // override fun YourPrice() : Int = 0;

    override val title: String
        get() = "${super.title} (${name.value} x ${size})"

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
): MenuItem("Салат", 0) {
    override val price: Int
        get() = 50
    override val title : String
        get() = "${super.title}  (${name.value})"
}


sealed class Progress(val now : String) {
    object Accepted : Progress("Your order has been accepted")
    object Preparing : Progress("Your order is being prepared")
    object Ready : Progress("Your order is ready")
}

data class YourOrder(
    val yourname : String,
    val tablenumber : Int,
    var progressorder : Progress,
    var items: MutableList<MenuItem>
) {
    val price : Int
        get() {
            var ans = 0
            items.forEach { item -> ans += item.price }
            return ans;
        }
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
    var pr : Progress = Progress.Accepted  // насколько заказ готов
    o = YourOrder("Vasya", 2, pr, mutableListOf(s, sal, Pizza(PizzaType.CAESAR)))
    println("Your order price: " + o.price)
    println("Your order items:")
    o.items.forEach {
        item -> println("${item.title} - ${item.price.toInt()}")
    }

    println("Status of your order now: " + pr.now)
    Thread.sleep(1000)
    pr = Progress.Preparing
    println("Status of your order now: " + pr.now)
    Thread.sleep(2000)
    pr = Progress.Ready
    println("Status of your order now: " + pr.now)
    println(o.yourname + ", you can pick up your order at table number " + o.tablenumber)
}

