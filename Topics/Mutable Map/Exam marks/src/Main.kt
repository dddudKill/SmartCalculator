fun main() {
    val studentsMarks = mutableMapOf<String, Int>()
    while (true) {
        val input = readln()
        if (input == "stop") break
        val mark = readln().toInt()
        if (studentsMarks.containsKey(input)) continue else {
            studentsMarks[input] = mark
        }
    }
    println(studentsMarks)
}