package tonals.uehara.talk.util

class Event<T> {
    private val observers: MutableSet<(T) -> Unit> = mutableSetOf()

    operator fun plusAssign(observer: (T) -> Unit) {
        observers.add(observer)
    }

    operator fun minusAssign(observer: (T) -> Unit) {
        observers.remove(observer)
    }

    operator fun invoke(arg: T) {
        for (observer in observers.toList()) {
            observer(arg)
        }
    }
}