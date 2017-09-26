package net.pubnative.qa.demo.presenters

import net.pubnative.qa.demo.views.SelectAdView

class SelectAdPresenter : BasePresenter<SelectAdView>() {

    override fun updateView() {
        view()?.updateView()
    }

}