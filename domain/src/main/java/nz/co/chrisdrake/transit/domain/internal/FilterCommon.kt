package nz.co.chrisdrake.transit.domain.internal

internal fun <T> List<List<T>>.filterCommon(): List<T> {
    val common = first().toMutableList()

    listIterator(1).forEach {
        common.retainAll(it)
    }

    return common.toList()
}
