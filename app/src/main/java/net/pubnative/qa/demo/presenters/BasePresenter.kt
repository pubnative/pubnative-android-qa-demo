package net.pubnative.qa.demo.presenters

import android.content.Context
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicLong

abstract class BasePresenter<V> {

    val mContext : Context

    constructor(context: Context) {
        mContext = context
    }

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
