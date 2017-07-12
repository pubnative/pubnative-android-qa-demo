package net.pubnative.qa.demo.mvp


import net.pubnative.qa.demo.model.TrackingParam
import net.pubnative.sdk.core.Pubnative
import net.pubnative.sdk.core.config.PNConfigManager
import net.pubnative.sdk.core.request.PNAdTargetingModel

class MainPresenter : BasePresenter<BaseView>() {

    var mAppToken : String? = null
    var mPlacementName : String? = null
    val mTrackingParams : PNAdTargetingModel = PNAdTargetingModel()
    var adapterValues : MutableList<TrackingParam> = mutableListOf()

    override fun updateView() {
        view()?.updateView(mAppToken, mPlacementName)
    }

    fun onAppTokenSave(appToken: String) {
        if (appToken.isEmpty()) {
            view()?.showErrorMessage(Exception("App Token is empty!"))
        } else {
            mAppToken = appToken
            (view() as MainView).showConfigs()
        }
    }

    fun onPubnativeInitialize() {
        Pubnative.setTargeting(mTrackingParams)
        PNConfigManager.getConfig(view()?.getContext(), mAppToken, { model ->
            (view() as MainView).updateInitButton()
            val list = ArrayList<String>()
            for ((name, _) in model.placements) {
                list.add(name)
            }
            (view() as MainView).updatePlacementsList(list)
            adapterValues.clear()
            adapterValues.addAll((mTrackingParams.toDictionaryWithIap().flatMap {
                listOf(TrackingParam(it.key.toString(), it.value.toString()))
            }).toMutableList())
            (view() as MainView).updateTrackingParams(adapterValues)
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

    override fun onNext() {
        (view() as MainView).goToNext()
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
        adapterValues?.addAll((mTrackingParams.toDictionaryWithIap().flatMap {
            listOf(TrackingParam(it.key.toString(), it.value.toString()))
        }).toMutableList())
        (view() as MainView).updateTrackingParams(adapterValues)
    }
}