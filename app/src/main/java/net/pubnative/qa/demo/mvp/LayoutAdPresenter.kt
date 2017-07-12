package net.pubnative.qa.demo.mvp

import net.pubnative.qa.demo.R
import net.pubnative.qa.demo.ui.FeedAdapter
import net.pubnative.sdk.layouts.PNLargeLayout
import net.pubnative.sdk.layouts.PNLayout
import net.pubnative.sdk.layouts.PNMediumLayout
import net.pubnative.sdk.layouts.PNSmallLayout
import java.lang.Exception

class LayoutAdPresenter : BasePresenter<LayoutAdView>() {

    var mAppToken : String? = null
    var mPlacementName : String? = null
    lateinit var mLayout : PNLayout
    var mFeedItems : MutableList<FeedAdapter.FeedItem> = mutableListOf()

    enum class Size {
        SMALL, MEDIUM, LARGE
    }

    override fun onNext() {

    }

    override fun updateView() {
        for (i in 1..25) {
            mFeedItems.add(FeedAdapter.FeedItem(view()?.getContext()?.resources?.getString(R.string.very_long_string), null))
        }
        view()?.updateView(mAppToken, mPlacementName)
    }

    fun onLoadClick(size: Size) {

        mLayout = when (size) {
            Size.SMALL -> PNSmallLayout()
            Size.MEDIUM -> PNMediumLayout()
            Size.LARGE -> PNLargeLayout()
        }

        mLayout.setLoadListener(object : PNLayout.LoadListener{
            override fun onPNLayoutLoadFinish(layout: PNLayout?) {
                view()?.loadAdClick()
            }

            override fun onPNLayoutLoadFail(layout: PNLayout?, exception: Exception?) {
                view()?.showErrorMessage(exception)
            }

        })
        mLayout.load(view()?.getContext(), mAppToken, mPlacementName)
    }

    fun onShowClick() {
        if (mLayout is PNSmallLayout) {
            showSmallAd(mLayout as PNSmallLayout)
        } else if (mLayout is PNMediumLayout) {
            showMediumAd(mLayout as PNMediumLayout)
        } else {
            showLargeAd(mLayout as PNLargeLayout)
        }
    }

    fun onStartTrackingClick() {
        mLayout.setTrackListener(object : PNLayout.TrackListener {
            override fun onPNLayoutTrackImpression(p0: PNLayout?) {
                view()?.updateImpressionIndicator()
            }

            override fun onPNLayoutTrackClick(p0: PNLayout?) {
                view()?.updateClickIndicator()
            }

        })
        if (mLayout is PNSmallLayout) {
            (mLayout as PNSmallLayout).startTrackingView()
        }

        if (mLayout is PNMediumLayout) {
            (mLayout as PNMediumLayout).startTrackingView()
        }

    }

    fun onStopTrackingClick() {
        if (mLayout is PNSmallLayout) {
            (mLayout as PNSmallLayout).stopTrackingView()
        }

        if (mLayout is PNMediumLayout) {
            (mLayout as PNMediumLayout).stopTrackingView()
        }
    }

    private fun showSmallAd(layout: PNSmallLayout) {
        mFeedItems.removeAt(7)
        mFeedItems.add(7, FeedAdapter.FeedItem("", layout.getView(view()?.getContext())))
        view()?.showAdClick()
    }

    private fun showMediumAd(layout: PNMediumLayout) {
        mFeedItems.removeAt(7)
        mFeedItems.add(7, FeedAdapter.FeedItem("", layout.getView(view()?.getContext())))
        view()?.showAdClick()
    }

    private fun showLargeAd(layout: PNLargeLayout) {
        layout.setViewListener(object : PNLargeLayout.ViewListener {
            override fun onPNLayoutViewShown(p0: PNLayout?) {
                view()?.showAdClick()
            }

            override fun onPNLayoutViewHidden(p0: PNLayout?) {
                // Do nothing.
            }

        })
        layout.show()
    }

}