package net.pubnative.qa.demo.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_select_ad.*
import net.pubnative.qa.demo.PresenterManager
import net.pubnative.qa.demo.R
import net.pubnative.qa.demo.presenters.MainPresenter
import net.pubnative.qa.demo.views.SelectAdView
import java.lang.Exception

class SelectAdActivity : AppCompatActivity(), SelectAdView {

    private lateinit var presenter: MainPresenter
    private var presenterId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.extras?.getLong("presenter_id", -1L)

        if (id != null && id > -1) {
            presenter = PresenterManager.instance.restorePresenter<MainPresenter>(id)
            presenterId = id
        } else {
            presenter = MainPresenter()
            presenterId = PresenterManager.instance.savePresenter(presenter)
        }

        setContentView(R.layout.activity_select_ad)

    }

    override fun onResume() {
        super.onResume()

        presenter.bindView(this)
    }

    override fun onPause() {
        super.onPause()

        presenter.unbindView()
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

    override fun goToNative() {
        val intent = Intent(this, NativeAdActivity::class.java)
        val bundle = Bundle()
        bundle.putString("app_token", presenter.mAppToken)
        bundle.putString("placement_name", presenter.mPlacementName)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun getContext(): Context {
        return this
    }

    override fun goToSmallLayout() {
        val intent = Intent(this, SmallLayoutAdActivity::class.java)
        val bundle = Bundle()
        bundle.putString("app_token", presenter.mAppToken)
        bundle.putString("placement_name", presenter.mPlacementName)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun updateView(appToken: String?, placementName: String?) {
        tv_apptoken.text = appToken
        tv_placement.text = placementName

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
        val intent = Intent(this, MediumLayoutActivity::class.java)
        val bundle = Bundle()
        bundle.putString("app_token", presenter.mAppToken)
        bundle.putString("placement_name", presenter.mPlacementName)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun goToLargeLayout() {
        val intent = Intent(this, LargeLayoutActivity::class.java)
        val bundle = Bundle()
        bundle.putString("app_token", presenter.mAppToken)
        bundle.putString("placement_name", presenter.mPlacementName)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun showErrorMessage(exception: Exception?) {
        // Do nothing
    }
}
