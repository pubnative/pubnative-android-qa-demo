package net.pubnative.qa.demo.ui

import android.content.Context
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_layout_ad.*
import net.pubnative.qa.demo.PresenterManager
import net.pubnative.qa.demo.R
import net.pubnative.qa.demo.mvp.LayoutAdPresenter
import net.pubnative.qa.demo.mvp.LayoutAdView
import java.lang.Exception

class SmallLayoutAdActivity : AppCompatActivity(), LayoutAdView {

    private var presenter : LayoutAdPresenter? = null
    private var presenterId : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.extras?.getLong("presenter_id", -1L)

        if (id != null && id > -1) {
            presenter = PresenterManager.instance.restorePresenter<LayoutAdPresenter>(id)
            presenterId = id
        } else {
            presenter = LayoutAdPresenter()
            presenterId = PresenterManager.instance.savePresenter(presenter)
        }

        presenter?.mAppToken = intent.extras?.getString("app_token")
        presenter?.mPlacementName = intent.extras?.getString("placement_name")

        setContentView(R.layout.activity_layout_ad)

        rv_feed.layoutManager = LinearLayoutManager(this)
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

    override fun getContext(): Context {
        return this
    }

    override fun updateView(appToken: String?, placementName: String?) {
        if (appToken != null && placementName != null) {
            val adapter = FeedAdapter(presenter?.mFeedItems as MutableList<FeedAdapter.FeedItem>)
            rv_feed.adapter = adapter
        }
        rv_feed.adapter.notifyDataSetChanged()

        btn_load.setOnClickListener {
            (it as Button).background.colorFilter = null
            btn_show.background.colorFilter = null
            iv_click_indicator.setImageDrawable(resources.getDrawable(R.drawable.indicator_red))
            iv_impression_indicator.setImageDrawable(resources.getDrawable(R.drawable.indicator_red))
            cl_layout_ad_container.visibility = View.INVISIBLE
            presenter?.onLoadClick(LayoutAdPresenter.Size.SMALL)
        }

        btn_show.setOnClickListener {
            presenter?.onShowClick()
        }

        btn_start_tracking.setOnClickListener {
            presenter?.onStartTrackingClick()
        }

        btn_stop_tracking.setOnClickListener {
            presenter?.onStopTrackingClick()
        }
    }

    override fun loadAdClick() {
        btn_load.background.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
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

    override fun showAdClick() {
        cl_layout_ad_container.visibility = View.VISIBLE
        btn_show.background.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        rv_feed.adapter.notifyItemChanged(7)
    }
}
