package utils

object SequenceUtils {

    fun <T> splitSequence(sequence: Sequence<T>, groupSize: Int): Sequence<List<T>> {
        return sequence
            .withIndex()
            .groupBy { indexed -> indexed.index / groupSize }
            .asSequence()
            .map { indexedList -> indexedList.value }
            .map { indexedValues -> indexedValues.map { idxVal -> idxVal.value } }
    }

}