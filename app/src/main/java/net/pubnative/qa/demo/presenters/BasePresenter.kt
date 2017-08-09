package net.pubnative.qa.demo.presenters

import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicLong

abstract class BasePresenter<V> {

    lateinit var mView : WeakReference<V>

    var id : AtomicLong? = null

    fun bindView(view: V) {
        mView = WeakReference(view)
        if (mView.get() != null) updateView()
    }

    fun unbindView() {
        mView.clear()
    }

    fun view() : V? {
        return mView.get()
    }

    abstract fun updateView()

}
