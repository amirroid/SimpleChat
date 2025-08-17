package ir.amirroid.simplechat.extensions

fun <K, V> Map<K, V>.except(obj: K): List<V> {
    return this.filter { (item, _) -> obj != item }
        .map { it.value }
}