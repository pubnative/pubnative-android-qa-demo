package net.pubnative.qa.demo.mvp

import android.content.Context
import android.view.ViewGroup
import net.pubnative.sdk.core.request.PNAdModel
import net.pubnative.sdk.core.request.PNAdModelHelper
import net.pubnative.sdk.core.request.PNRequest
import java.lang.Exception
import kotlin.properties.Delegates

class NativeAdPresenter : BasePresenter<NativeAdView>() {

    var mAppToken : String? = null
    var mPlacementName : String? = null
    var mNativeAd : PNAdModel? = null
    var mResourcesCache : Boolean? = null

    override fun updateView() {
        view()?.updateView("", "")
    }

    override fun onNext() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun makeRequest(context: Context) {
        val request : PNRequest = PNRequest()
        if (mResourcesCache != null) request.setCacheResources(mResourcesCache as Boolean)
        request.start(context, mAppToken, mPlacementName, object : PNRequest.Listener {
            override fun onPNRequestLoadFail(request: PNRequest?, exception: Exception?) {
                view()?.showErrorMessage(exception)
            }

            override fun onPNRequestLoadFinish(request: PNRequest?, adModel: PNAdModel?) {
                mNativeAd = adModel
                view()?.loadAdClick()
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
        view()?.showAdClick(mNativeAd)
        view()?.showAdContainer()
    }

    fun  enableResourcesCache(state: Boolean) {
        mResourcesCache = state
    }

    fun startTracking(view: ViewGroup) {
        mNativeAd?.setListener(object : PNAdModel.Listener {
            override fun onPNAdClick(p0: PNAdModel?) {
                view()?.updateClickIndicator()
            }

            override fun onPNAdImpression(p0: PNAdModel?) {
                view()?.updateImpressionIndicator()
            }

        })
        mNativeAd?.startTracking(view)
    }

    fun stopTracking() {
        mNativeAd?.stopTracking()
    }

}