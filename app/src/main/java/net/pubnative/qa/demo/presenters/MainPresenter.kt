package net.pubnative.qa.demo.presenters


import net.pubnative.qa.demo.model.TrackingParam
import net.pubnative.qa.demo.views.MainView
import net.pubnative.sdk.core.PNAdTargetingModel
import net.pubnative.sdk.core.Pubnative
import net.pubnative.sdk.core.config.PNConfigManager

class MainPresenter : BasePresenter<MainView>() {

    var mAppToken : String? = null
    var mPlacementName : String? = null
    val mTrackingParams : PNAdTargetingModel = PNAdTargetingModel()
    var adapterValues : MutableList<TrackingParam> = mutableListOf()

    override fun updateView() {
        view()?.updateView(mAppToken, mPlacementName)
    }

    fun onAppTokenSave(appToken: String) {
        view()?.showIndicator()
        if (appToken.isEmpty()) {
            view()?.hideIndicator()
            view()?.showErrorMessage(Exception("App Token is empty!"))
            view()?.hideConfigs()
        } else {
            mAppToken = appToken
            PNConfigManager.getConfig(appToken) {  model ->
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
        PNConfigManager.getConfig(mAppToken, {
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
        mPlacementName = placementName
    }

    fun onNext() {
        view()?.goToNext()
    }

    fun onTrackingParamAdded(key: String, value: String) {
        when (key) {
            "age" -> mTrackingParams.age = value.toInt()
            "gender" -> mTrackingParams.gender = value
            "education" -> mTrackingParams.education = value
            "iap_total" -> mTrackingParams.iap_total = value.toDouble()
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