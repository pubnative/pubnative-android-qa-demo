package net.pubnative.qa.demo.activities

import android.content.Context
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_layout_ad.*
import net.pubnative.qa.demo.PresenterManager
import net.pubnative.qa.demo.R
import net.pubnative.qa.demo.presenters.LayoutAdPresenter
import net.pubnative.qa.demo.views.LayoutAdView

class LargeLayoutActivity : AppCompatActivity(), LayoutAdView {

    private var presenter : LayoutAdPresenter? = null
    private var presenterId : Long? = null
    private lateinit var progress : View

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

        presenter?.mAppToken = intent.extras?.getString("app_token").toString()
        presenter?.mPlacementName = intent.extras?.getString("placement_name").toString()

        setContentView(R.layout.activity_layout_ad)
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

            btn_load.background.colorFilter = null
            btn_load.setOnClickListener {
                (it as Button).background.colorFilter = null
                btn_show.background.colorFilter = null
                btn_show.isEnabled = false
                presenter?.onLoadClick(LayoutAdPresenter.Size.LARGE)
            }

            btn_show.isEnabled = false
            btn_show.background.colorFilter = null
            btn_show.setOnClickListener {
                presenter?.onShowClick()
            }
        }

    }

    override fun loadAdClick() {
        btn_load.background.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        btn_show.isEnabled = true
    }

    override fun updateClickIndicator() {
        //Do Nothing
    }

    override fun updateImpressionIndicator() {
        //Do Nothing
    }

    override fun showErrorMessage(exception: Exception?) {
        Toast.makeText(this, "Error message: " + exception?.localizedMessage, Toast.LENGTH_LONG).show()
    }

    override fun showAdClick() {
        btn_show.background.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
    }

    override fun hideIndicator() {
        window.decorView.findViewById<ViewGroup>(android.R.id.content).removeView(progress)
    }

    override fun showIndicator() {
        progress = layoutInflater.inflate(R.layout.progress_bar_indicator, null)
        window.decorView.findViewById<ViewGroup>(android.R.id.content).addView(progress)
    }
}