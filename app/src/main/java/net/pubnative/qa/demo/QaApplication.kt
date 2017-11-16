package net.pubnative.qa.demo

import android.app.Application
import net.pubnative.sdk.core.Pubnative

/**
 * Created by erosgarciaponte on 16.11.17.
 */
class QaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Pubnative.init(applicationContext, "")
    }
}