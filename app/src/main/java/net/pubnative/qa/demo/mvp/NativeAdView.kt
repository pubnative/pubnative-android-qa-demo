package net.pubnative.qa.demo.mvp

import net.pubnative.sdk.core.request.PNAdModel
import java.lang.Exception

interface NativeAdView : BaseView {

    fun loadAdClick()

    fun fetchAdClick()

    fun showAdClick(nativeAd: PNAdModel?)

    fun enableResourcesCache(state: Boolean)

    fun showAdContainer()

    fun hideAdContainer()

    fun updateClickIndicator()

    fun updateImpressionIndicator()

}