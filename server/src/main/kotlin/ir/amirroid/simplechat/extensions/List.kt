package ir.amirroid.simplechat.extensions

fun <T> List<T>.without(obj: T) = filter { obj != it }