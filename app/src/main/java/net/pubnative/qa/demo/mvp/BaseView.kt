package net.pubnative.qa.demo.mvp

import android.content.Context
import java.lang.Exception

interface BaseView {

    fun getContext(): Context

    fun updateView(appToken: String?, placementName: String?)

    fun showErrorMessage(exception: Exception?)
}
