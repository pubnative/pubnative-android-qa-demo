package net.pubnative.qa.demo.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_select_ad.*
import net.pubnative.qa.demo.AppSettings
import net.pubnative.qa.demo.PresenterManager
import net.pubnative.qa.demo.R
import net.pubnative.qa.demo.presenters.SelectAdPresenter
import net.pubnative.qa.demo.views.BaseView
import net.pubnative.qa.demo.views.SelectAdView
import java.lang.Exception

class SelectAdActivity : AppCompatActivity(), SelectAdView {

    private var presenter: SelectAdPresenter? = null
    private var presenterId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.extras?.getLong(BaseView.PRESENTER_ID, -1L)

        if (id != null && id > -1) {
            presenter = PresenterManager.instance.restorePresenter<SelectAdPresenter>(id)
            presenterId = id
        } else {
            presenter = SelectAdPresenter()
            presenterId = PresenterManager.instance.savePresenter(presenter)
        }

        setContentView(R.layout.activity_select_ad)

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
            outState?.putLong(BaseView.PRESENTER_ID, presenterId as Long)
        }

        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        presenterId = savedInstanceState?.getLong(BaseView.PRESENTER_ID)
    }

    override fun goToNative() {
        startActivity(Intent(this, NativeAdActivity::class.java))
    }

    override fun getContext(): Context {
        return this
    }

    override fun goToSmallLayout() {
        startActivity(Intent(this, SmallLayoutAdActivity::class.java))
    }

    override fun updateView() {
        tv_apptoken.text = AppSettings.appToken
        tv_placement.text = AppSettings.placement

        btn_native.setOnClickListener {
            goToNative()
        }

        btn_small.setOnClickListener {
            goToSmallLayout()
        }

        btn_medium.setOnClickListener {
            goToMediumLayout()
        }

        btn_large.setOnClickListener {
            goToLargeLayout()
        }
    }

    override fun goToMediumLayout() {
        startActivity(Intent(this, MediumLayoutActivity::class.java))
    }

    override fun goToLargeLayout() {
        startActivity(Intent(this, LargeLayoutActivity::class.java))
    }

    override fun showErrorMessage(exception: Exception?) {
        // Do nothing
    }

    override fun hideIndicator() {
        // Do nothing
    }

    override fun showIndicator() {
        // Do nothing
    }
}
