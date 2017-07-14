package net.pubnative.qa.demo.presenters

import android.content.Context
import android.view.ViewGroup
import net.pubnative.qa.demo.views.NativeAdView
import net.pubnative.sdk.core.request.PNAdModel
import net.pubnative.sdk.core.request.PNAdModelHelper
import net.pubnative.sdk.core.request.PNRequest
import java.lang.Exception

class NativeAdPresenter : BasePresenter<NativeAdView>() {

    lateinit var mAppToken : String
    lateinit var mPlacementName : String
    lateinit var mNativeAd : PNAdModel
    var mResourcesCache : Boolean? = null

    override fun updateView() {
        view()?.updateView("", "")
    }

    fun makeRequest(context: Context) {
        val request : PNRequest = PNRequest()
        if (mResourcesCache != null) request.setCacheResources(mResourcesCache as Boolean)
        request.start(context, mAppToken, mPlacementName, object : PNRequest.Listener {
            override fun onPNRequestLoadFail(request: PNRequest?, exception: Exception?) {
                view()?.showErrorMessage(exception)
            }

            override fun onPNRequestLoadFinish(request: PNRequest?, adModel: PNAdModel?) {
                adModel?.let {
                    mNativeAd = it
                    view()?.loadAdClick()
                }
            }

        })
    }

    fun fetchResources() {
        val helper = PNAdModelHelper()
        helper.fetchResources(mNativeAd, {exception ->
            if (exception == null) {
                view()?.fetchAdClick()
            } else {
                view()?.showErrorMessage(exception)
            }
        })
    }

    fun showAd() {
        view()?.showAdContainer()
        view()?.showAdClick(mNativeAd)
    }

    fun  enableResourcesCache(state: Boolean) {
        mResourcesCache = state
    }

    fun startTracking(view: ViewGroup) {
        mNativeAd.setListener(object : PNAdModel.Listener {
            override fun onPNAdClick(p0: PNAdModel?) {
                view()?.updateClickIndicator()
            }

            override fun onPNAdImpression(p0: PNAdModel?) {
                view()?.updateImpressionIndicator()
            }

        })
        mNativeAd.startTracking(view)
    }

    fun stopTracking() {
        mNativeAd.stopTracking()
    }

}