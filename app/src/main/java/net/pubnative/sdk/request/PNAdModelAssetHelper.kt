package net.pubnative.sdk.request

import java.lang.Exception

class PNAdModelAssetHelper {

    fun fetchResources(adModel: PNAdModel?, listener : (exception : Exception?) -> Unit) {
        adModel?.fetchAssets(object : PNAdModel.FetchListener {
            override fun onFetchFail(model: PNAdModel?, exception: Exception?) {
                listener(exception)
            }

            override fun onFetchFinish(model: PNAdModel?) {
                listener(null)
            }

        })
    }

}
