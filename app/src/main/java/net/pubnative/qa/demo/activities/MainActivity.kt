package net.pubnative.qa.demo.activities

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_tracking_param_dialog.view.*
import net.pubnative.qa.demo.PresenterManager
import net.pubnative.qa.demo.R
import net.pubnative.qa.demo.model.TrackingParam
import net.pubnative.qa.demo.presenters.MainPresenter
import net.pubnative.qa.demo.views.BaseView
import net.pubnative.qa.demo.views.MainView
import net.pubnative.sdk.core.Pubnative
import java.lang.Exception


class MainActivity : AppCompatActivity(), MainView {

    private  var presenter : MainPresenter? = null
    private var presenterId : Long? = null
    private lateinit var progress : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.extras?.getLong(BaseView.PRESENTER_ID, -1L)

        if (id != null && id > -1) {
            presenter = PresenterManager.instance.restorePresenter<MainPresenter>(id)
            presenterId = id
        } else {
            presenter = MainPresenter(applicationContext)
            presenterId = PresenterManager.instance.savePresenter(presenter)
        }

        setContentView(R.layout.activity_main)
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

        presenterId?.let {
            outState?.putLong(BaseView.PRESENTER_ID, it)
        }

        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        presenterId = savedInstanceState?.getLong(BaseView.PRESENTER_ID)
    }

    override fun saveAppToken(appToken: String) {
        presenter?.onAppTokenSave(appToken)
    }

    override fun getContext(): Context {
        return this
    }

    override fun initializePubnative() {
        presenter?.onPubnativeInitialize()
    }

    override fun updateView() {
        btn_save.setOnClickListener {
            saveAppToken(et_app_token.text.toString())
            et_app_token.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et_app_token.windowToken, 0)
        }

        btn_init.setOnClickListener {
            initializePubnative()
        }

        sw_testing.setOnCheckedChangeListener({ _, state ->
            presenter?.onTestModeChanged(state)
        })

        sw_coppa.setOnCheckedChangeListener({ _, state ->
            presenter?.onCoppaModeChanged(state)
        })

        sp_placement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent : AdapterView<*>, view : View, pos : Int, id : Long) {
                presenter?.onPlacementSelect(parent.getItemAtPosition(pos).toString())
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {
                // Another interface callback
            }
        }

        btn_next.setOnClickListener {
            presenter?.onNext()
        }

        btn_switch_url.setOnClickListener {
            presenter?.OnChooseServer()
        }

        trackingParamsList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        trackingParamsList.layoutManager = LinearLayoutManager(this)
        trackingParamsList.setHasFixedSize(true)

        btn_add_param.setOnClickListener {
            showAddTargetParamDialog()
        }
    }

    override fun showConfigs() {
        cl_configuration.visibility = View.VISIBLE
    }

    override fun hideConfigs() {
        cl_configuration.visibility = View.INVISIBLE
    }

    override fun updatePlacementsList(list: ArrayList<String>) {
        sp_placement.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
    }

    override fun goToNext() {
        startActivity(Intent(this, SelectAdActivity::class.java))
    }

    override fun goToChooseServer() {
        startActivity(Intent(this, ChooseServerActivity::class.java))
    }

    override fun updateInitButton() {
        btn_init.background.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
        btn_next.isEnabled = true
    }

    override fun updateTrackingParams(params: MutableList<TrackingParam>) {
        if (trackingParamsList.adapter == null) {
            val adapter = TrackingParamsAdapter(params)
            trackingParamsList.adapter = adapter
        }
        trackingParamsList.adapter.notifyDataSetChanged()
    }

    fun showAddTargetParamDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.add_tracking_param_dialog, null)
        dialogBuilder.setView(dialogView)

        val spinnerItems = arrayOf("gender", "age", "education", "interests", "iap", "iap_total")
        dialogView.sp_param_name.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerItems)

        dialogBuilder.setTitle("Add new param")
        dialogBuilder.setPositiveButton("Add", { _, _ ->
            presenter?.onTrackingParamAdded(dialogView.sp_param_name.selectedItem.toString(), dialogView.et_param_value.text.toString())
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dialogView.windowToken, 0)
        })
        dialogBuilder.setNegativeButton("Cancel", { _, _ ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
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
