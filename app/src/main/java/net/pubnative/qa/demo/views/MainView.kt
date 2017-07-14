package net.pubnative.qa.demo.views

import net.pubnative.qa.demo.model.TrackingParam

interface MainView : BaseView {

    fun saveAppToken(appToken: String)

    fun initializePubnative()

    fun showConfigs()

    fun hideConfigs()

    fun updatePlacementsList(list: ArrayList<String>)

    fun updateTrackingParams(params: MutableList<TrackingParam>)

    fun goToNext()

    fun updateInitButton()
}