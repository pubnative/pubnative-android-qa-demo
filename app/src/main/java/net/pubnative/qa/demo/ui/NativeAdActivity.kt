package net.pubnative.qa.demo.ui

import android.content.Context
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_native_ad.*
import kotlinx.android.synthetic.main.native_ad_layout.view.*
import net.pubnative.qa.demo.R
import net.pubnative.qa.demo.mvp.NativeAdView
import net.pubnative.qa.demo.PresenterManager
import net.pubnative.qa.demo.mvp.NativeAdPresenter
import net.pubnative.sdk.core.request.PNAdModel
import java.lang.Exception


class NativeAdActivity : AppCompatActivity(), NativeAdView {

    private var presenter : NativeAdPresenter? = null
    private var presenterId : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.extras?.getLong("presenter_id", -1L)

        if (id != null && id > -1) {
            presenter = PresenterManager.instance.restorePresenter<NativeAdPresenter>(id)
            presenterId = id
        } else {
            presenter = NativeAdPresenter()
            presenterId = PresenterManager.instance.savePresenter(presenter)
        }

        presenter?.mAppToken = intent.extras?.getString("app_token")
        presenter?.mPlacementName = intent.extras?.getString("placement_name")

        setContentView(R.layout.activity_native_ad)
    }

    override fun onResume() {
        super.onResume()

        presenter?.bindView(this)
    }

    override fun onPause() {
        super.onPause()

        presenter?.unbindView()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {

        if (presenterId != null) {
            outState?.putLong("presenter_id", presenterId as Long)
        }

        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        presenterId = savedInstanceState?.getLong("presenter_id")
    }

    override fun loadAdClick() {
        btn_load.background.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        if (!cb_cache_resources.isChecked) {
            btn_fetch.visibility = View.VISIBLE
        }
        btn_show.visibility = View.VISIBLE
    }

    override fun getContext(): Context {
        return this
    }

    override fun fetchAdClick() {
        btn_fetch.background.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
    }

    override fun updateView(appToken: String?, placementName: String?) {
        btn_load.setOnClickListener {
            reseteFieldsState()
            presenter?.enableResourcesCache(cb_cache_resources.isChecked)
            presenter?.makeRequest(this)
        }
        btn_fetch.setOnClickListener {
            presenter?.fetchResources()
        }
        btn_show.setOnClickListener {
            presenter?.showAd()
        }
    }

    override fun showAdClick(nativeAd: PNAdModel?) {
        btn_show.background.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        val adView : View = layoutInflater.inflate(R.layout.native_ad_layout, cl_ad_container, true)

        iv_click_indicator.setImageDrawable(resources.getDrawable(R.drawable.indicator_red))
        iv_impression_indicator.setImageDrawable(resources.getDrawable(R.drawable.indicator_red))

        nativeAd?.withBanner(adView.fl_native_banner)?.
                withCallToAction(adView.btn_native_cta)?.
                withTitle(adView.tv_native_title)?.
                withDescription(adView.tv_native_description)?.
                withIcon(adView.iv_native_icon)

        btn_start_tracking.setOnClickListener {
            presenter?.startTracking(adView as ViewGroup)
        }

        btn_stop_tracking.setOnClickListener {
            presenter?.stopTracking()
        }

    }

    override fun enableResourcesCache(state: Boolean) {
        presenter?.enableResourcesCache(state)
    }

    override fun showAdContainer() {
        cl_native_ad_container.visibility = View.VISIBLE
    }

    override fun hideAdContainer() {
        cl_native_ad_container.visibility = View.INVISIBLE
    }

    override fun updateClickIndicator() {
        iv_click_indicator.setImageDrawable(resources.getDrawable(R.drawable.indicator_green))
    }

    override fun updateImpressionIndicator() {
        iv_impression_indicator.setImageDrawable(resources.getDrawable(R.drawable.indicator_green))
    }

    override fun showErrorMessage(exception: Exception?) {
        Toast.makeText(this, "Error message: " + exception?.localizedMessage, Toast.LENGTH_LONG).show()
    }

    private fun reseteFieldsState() {
        btn_load.background.colorFilter = null
        btn_show.background.colorFilter = null
        btn_show.visibility = View.INVISIBLE
        btn_fetch.background.colorFilter = null
        btn_fetch.visibility = View.INVISIBLE
        iv_click_indicator.setImageDrawable(resources.getDrawable(R.drawable.indicator_red))
        iv_impression_indicator.setImageDrawable(resources.getDrawable(R.drawable.indicator_red))
        cl_ad_container.visibility = View.INVISIBLE
    }
}
