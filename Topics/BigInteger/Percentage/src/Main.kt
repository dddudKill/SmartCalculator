
fun main() {
    val a = readln().toBigInteger()
    val b = readln().toBigInteger()
    val sum = a + b
    val percentageA = (a * 100.toBigInteger() / sum).toInt()
    val percentageB = (b * 100.toBigInteger() / sum).toInt()
    println("$percentageA% $percentageB%")
}