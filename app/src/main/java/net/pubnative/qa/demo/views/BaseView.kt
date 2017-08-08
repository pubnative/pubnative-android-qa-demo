package net.pubnative.qa.demo.views

import android.content.Context
import java.lang.Exception

interface BaseView {

    companion object {
        const val PRESENTER_ID = "presenter_id"
        const val APPTOKEN = "apptoken"
        const val PLACEMENT = "placement"
    }

    fun getContext(): Context

    fun updateView(appToken: String?, placementName: String?)

    fun showErrorMessage(exception: Exception?)

    fun showIndicator()

    fun hideIndicator()
}
