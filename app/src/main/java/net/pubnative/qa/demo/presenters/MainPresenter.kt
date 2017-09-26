package net.pubnative.qa.demo.presenters


import net.pubnative.qa.demo.AppSettings
import net.pubnative.qa.demo.model.TrackingParam
import net.pubnative.qa.demo.views.MainView
import net.pubnative.sdk.core.PNSettings
import net.pubnative.sdk.core.Pubnative
import net.pubnative.sdk.core.config.PNConfigManager
import net.pubnative.sdk.core.request.PNAdTargetingModel

class MainPresenter : BasePresenter<MainView>() {

    private val mTrackingParams : PNAdTargetingModel = PNAdTargetingModel()
    private var adapterValues : MutableList<TrackingParam> = mutableListOf()

    override fun updateView() {
        view()?.updateView()
    }

    fun onAppTokenSave(appToken: String) {
        view()?.showIndicator()
        if (appToken.isEmpty()) {
            view()?.hideIndicator()
            view()?.showErrorMessage(Exception("App Token is empty!"))
            view()?.hideConfigs()
        } else {
            AppSettings.appToken = appToken
            PNConfigManager.getConfig(view()?.getContext(), appToken) {  model ->
                view()?.showConfigs()
                val list = ArrayList<String>()
                if (model == null || model.placements.isEmpty()) {
                    view()?.showErrorMessage(Exception("Config is not loaded!"))
                    view()?.hideConfigs()
                } else {
                    for ((name, _) in model.placements) {
                        list.add(name)
                    }
                    view()?.updatePlacementsList(list)
                    adapterValues.clear()
                    adapterValues.addAll((mTrackingParams.toDictionaryWithIap().flatMap {
                        listOf(TrackingParam(it.key.toString(), it.value.toString()))
                    }).toMutableList())
                }
                view()?.hideIndicator()
            }
        }
    }

    fun onPubnativeInitialize() {
        view()?.showIndicator()
        Pubnative.setTargeting(mTrackingParams)
        PNConfigManager.getConfig(view()?.getContext(), AppSettings.appToken, {
            view()?.updateInitButton()
            view()?.updateTrackingParams(adapterValues)
            view()?.hideIndicator()
        })
    }

    fun onTestModeChanged(state : Boolean) {
        Pubnative.setTestMode(state)
    }

    fun onCoppaModeChanged(state: Boolean) {
        Pubnative.setCoppaMode(state)
    }

    fun onPlacementSelect(placementName : String) {
        AppSettings.placement = placementName
    }

    fun onNext() {
        PNSettings.baseApiUrl = AppSettings.baseApiUrl
        view()?.goToNext()
    }

    fun OnChooseServer() {
        view()?.goToChooseServer()
    }

    fun onTrackingParamAdded(key: String, value: String) {
        when (key) {
            "age" -> mTrackingParams.age = value.toInt()
            "gender" -> mTrackingParams.gender = value
            "education" -> mTrackingParams.education = value
            "iap_total" -> mTrackingParams.iap_total = value.toFloat()
            "iap" -> mTrackingParams.iap = value.toBoolean()
            "interests" -> value.split(",").forEach { mTrackingParams.addInterest(it) }
        }
        adapterValues.clear()
        adapterValues.addAll((mTrackingParams.toDictionaryWithIap().flatMap {
            listOf(TrackingParam(it.key.toString(), it.value.toString()))
        }).toMutableList())
        (view() as MainView).updateTrackingParams(adapterValues)
    }
}