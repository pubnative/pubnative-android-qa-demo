package net.pubnative.qa.demo

import net.pubnative.qa.demo.presenters.BasePresenter
import java.util.concurrent.atomic.AtomicLong

class PresenterManager {

    val presenters : LinkedHashMap<Long, BasePresenter<*>> = LinkedHashMap()
    val currentId : AtomicLong by lazy { AtomicLong() }

    private object Holder {
        val INSTANCE = PresenterManager()
    }

    fun savePresenter(presenter: BasePresenter<*>?) : Long {
        if (presenter != null) {
            val presenterId = currentId.incrementAndGet()
            presenters.put(presenterId, presenter)
            return presenterId
        }
        return 0L
    }

    fun <P : BasePresenter<*>> restorePresenter(id: Long?): P {
        return presenters.get(id) as P
    }

    companion object {
        val instance: PresenterManager by lazy { Holder.INSTANCE }
    }

}