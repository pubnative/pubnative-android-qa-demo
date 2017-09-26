package net.pubnative.qa.demo.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_choose_server.*
import net.pubnative.qa.demo.AppSettings
import net.pubnative.qa.demo.PresenterManager
import net.pubnative.qa.demo.R
import net.pubnative.qa.demo.presenters.ChooseServerPresenter
import net.pubnative.qa.demo.views.BaseView
import net.pubnative.qa.demo.views.ChooseServerView
import java.lang.Exception

class ChooseServerActivity : AppCompatActivity(), ChooseServerView {

    val STANDARD_URL = "http://api.pubnative.net/api/v3/native"
    val STAGING_URL = "http://api-staging.pubnative.net/api/v3/native"
    val BACKUP_URL = "http://api-backup.pubnative.net/api/v3/native"

    private  var presenter : ChooseServerPresenter? = null
    private var presenterId : Long? = null
    private lateinit var progress : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.extras?.getLong(BaseView.PRESENTER_ID, -1L)

        if (id != null && id > -1) {
            presenter = PresenterManager.instance.restorePresenter(id)
            presenterId = id
        } else {
            presenter = ChooseServerPresenter()
            presenterId = PresenterManager.instance.savePresenter(presenter)
        }

        setContentView(R.layout.activity_choose_server)

        btn_save.setOnClickListener {
            when(rg_server_url.checkedRadioButtonId) {
                rb_standard.id -> presenter?.onSaveServerUrl(STANDARD_URL)
                rb_backup.id -> presenter?.onSaveServerUrl(BACKUP_URL)
                rb_staging.id -> presenter?.onSaveServerUrl(STAGING_URL)
                else -> presenter?.onSaveServerUrl(STANDARD_URL)
            }
        }
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

    override fun saveServerUrl() {
        finish()
    }

    override fun getContext(): Context {
        return this@ChooseServerActivity
    }

    override fun updateView(appToken: String?, placementName: String?) {
        when(AppSettings.baseApiUrl) {
            STANDARD_URL -> rb_standard.isChecked = true
            STAGING_URL -> rb_staging.isChecked = true
            BACKUP_URL -> rb_backup.isChecked = true
            else -> rb_standard.isChecked = true
        }
    }

    override fun showErrorMessage(exception: Exception?) {
        Toast.makeText(this, exception?.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    override fun hideIndicator() {
        window.decorView.findViewById<ViewGroup>(android.R.id.content).removeView(progress)
    }

    override fun showIndicator() {
        progress = layoutInflater.inflate(R.layout.progress_bar_indicator, null)
        window.decorView.findViewById<ViewGroup>(android.R.id.content).addView(progress)
    }
}
