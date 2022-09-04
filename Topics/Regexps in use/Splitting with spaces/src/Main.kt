fun main() {
    val string = readLine()!!
    val n = readLine()!!.toInt()
    val output = string.split("\\s+".toRegex(), n)
    for (out in output) println(out)
}