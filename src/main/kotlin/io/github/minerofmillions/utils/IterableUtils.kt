package io.github.minerofmillions.utils

fun <T> Sequence<T>.filterWithNext(predicate: (T, next: T) -> Boolean): Sequence<T> = sequence {
    var lagger: T = firstOrNull() ?: return@sequence
    forEach {
        if (predicate(lagger, it)) {
            yield(lagger)
        }
        lagger = it
    }
    yield(lagger)
}

fun <T> Collection<T>.split(predicate: (T) -> Boolean): List<List<T>> = buildList {
    val buffer = mutableListOf<T>()
    this@split.forEach {
        if (predicate(it)) {
            add(buffer.toList())
            buffer.clear()
        } else buffer.add(it)
    }
    add(buffer.toList())
}