package net.pubnative.qa.demo.presenters

import android.content.Context
import net.pubnative.qa.demo.views.SelectAdView

class SelectAdPresenter(context: Context) : BasePresenter<SelectAdView>(context) {

    override fun updateView() {
        view()?.updateView()
    }

}