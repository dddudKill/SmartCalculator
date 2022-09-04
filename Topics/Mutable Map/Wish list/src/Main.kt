fun makeMyWishList(wishList: Map<String, Int>, limit: Int): MutableMap<String, Int> {
    val _wishList = mutableMapOf<String, Int> ()
    for (wish in wishList) {
        if (wish.value <= limit) _wishList[wish.key] = wish.value
    }
    return _wishList
}