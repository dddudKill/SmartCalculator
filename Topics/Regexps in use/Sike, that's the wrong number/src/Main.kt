fun main() {
    val number = readln()
    val regex = "[a-zA-Z]+".toRegex()
    val matchResult = number.replace(regex, "")
    print(matchResult)
}
