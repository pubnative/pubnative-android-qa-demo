package net.pubnative.qa.demo.presenters

import net.pubnative.qa.demo.views.SelectAdView

class SelectAdPresenter : BasePresenter<SelectAdView>() {

    lateinit var mAppToken : String
    lateinit var mPlacementName : String

    override fun updateView() {
        view()?.updateView(mAppToken, mPlacementName)
    }

}