package net.pubnative.qa.demo.views

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.Exception

interface BaseView {

    fun getContext(): Context

    fun updateView(appToken: String?, placementName: String?)

    fun showErrorMessage(exception: Exception?)

    fun showIndicator()

    fun hideIndicator()
}
