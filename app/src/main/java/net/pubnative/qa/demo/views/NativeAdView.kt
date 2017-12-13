package net.pubnative.qa.demo.views

import net.pubnative.sdk.request.PNAdModel

interface NativeAdView : BaseView {

    fun loadAdClick()

    fun fetchAdClick()

    fun showAdClick(nativeAd: PNAdModel)

    fun enableResourcesCache(state: Boolean)

    fun showAdContainer()

    fun hideAdContainer()

    fun updateClickIndicator()

    fun updateImpressionIndicator()

}