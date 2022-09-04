fun removing(currentMap: MutableMap<Int, String>, value: String): MutableMap<Int, String> {
    val out = mutableMapOf<Int, String>()
    for (cur in currentMap) {
        if (cur.value != value) out[cur.key] = cur.value
    }
    return out
}