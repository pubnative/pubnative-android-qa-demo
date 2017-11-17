package net.pubnative.qa.demo.presenters

import android.content.Context
import android.view.ViewGroup
import net.pubnative.qa.demo.views.NativeAdView
import net.pubnative.sdk.request.PNAdModel
import net.pubnative.sdk.request.PNAdModelAssetHelper
import net.pubnative.sdk.request.PNRequest
import java.lang.Exception

class NativeAdPresenter(context: Context) : BasePresenter<NativeAdView>(context) {

    lateinit var mAppToken : String
    lateinit var mPlacementName : String
    lateinit var mNativeAd : PNAdModel
    var mResourcesCache : Boolean? = null

    override fun updateView() {
        view()?.updateView("", "")
    }

    fun makeRequest(context: Context) {

        view()?.showIndicator()

        val request : PNRequest = PNRequest()
        if (mResourcesCache != null) request.setCacheResources(mResourcesCache as Boolean)
        request.start(context, mAppToken, mPlacementName, object : PNRequest.Listener {
            override fun onPNRequestLoadFail(request: PNRequest?, exception: Exception?) {
                view()?.hideIndicator()
                view()?.showErrorMessage(exception)
            }

            override fun onPNRequestLoadFinish(request: PNRequest?, adModel: PNAdModel?) {
                adModel?.let {
                    mNativeAd = it
                    view()?.loadAdClick()
                    view()?.hideIndicator()
                }
            }

        })
    }

    fun fetchResources() {

        view()?.showIndicator()

        val helper = PNAdModelAssetHelper()
        helper.fetchResources(mNativeAd, {exception ->
            if (exception == null) {
                view()?.fetchAdClick()
                view()?.hideIndicator()
            } else {
                view()?.hideIndicator()
                view()?.showErrorMessage(exception)
            }
        })
    }

    fun showAd() {
        view()?.showIndicator()
        view()?.showAdContainer()
        view()?.showAdClick(mNativeAd)
        view()?.hideIndicator()
    }

    fun  enableResourcesCache(state: Boolean) {
        mResourcesCache = state
    }

    fun startTracking(view: ViewGroup) {

        view()?.showIndicator()

        mNativeAd.setListener(object : PNAdModel.Listener {
            override fun onPNAdClick(p0: PNAdModel?) {
                view()?.updateClickIndicator()
            }

            override fun onPNAdImpression(p0: PNAdModel?) {
                view()?.updateImpressionIndicator()
            }

        })
        mNativeAd.startTracking(view)
        view()?.hideIndicator()
    }

    fun stopTracking() {
        mNativeAd.stopTracking()
    }

}