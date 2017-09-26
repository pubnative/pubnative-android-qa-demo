package net.pubnative.qa.demo.presenters

import net.pubnative.qa.demo.AppSettings
import net.pubnative.qa.demo.views.ChooseServerView

class ChooseServerPresenter : BasePresenter<ChooseServerView>() {

    override fun updateView() {
        view()?.updateView()
    }

    fun onSaveServerUrl(url: String) {
        view()?.showIndicator()

        AppSettings.baseApiUrl = url
        view()?.saveServerUrl()

        view()?.hideIndicator()
    }

}