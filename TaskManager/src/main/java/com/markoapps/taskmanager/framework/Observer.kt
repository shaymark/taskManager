package com.markoapps.taskmanager.framework

interface IObserver <T> {
    fun update(value: T)
}

class IObservable <T> {
    var _value: T? = null

    val observers: ArrayList<IObserver<T>> = arrayListOf()

    fun add(observer: IObserver<T>) {
        observers.add(observer)
    }

    fun remove(observer: IObserver<T>) {
        observers.remove(observer)
    }

    private fun sendUpdateEvent() {
        observers.forEach { it.update(_value!!) }
    }

    fun setValue(value: T) {
        this._value = value
        sendUpdateEvent()
    }

    fun getValue(): T? {
        return _value
    }
}

