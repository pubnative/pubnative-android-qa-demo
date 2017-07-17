package net.pubnative.qa.demo.presenters

import net.pubnative.qa.demo.R
import net.pubnative.qa.demo.activities.FeedAdapter
import net.pubnative.qa.demo.views.LayoutAdView
import net.pubnative.sdk.layouts.PNLargeLayout
import net.pubnative.sdk.layouts.PNLayout
import net.pubnative.sdk.layouts.PNMediumLayout
import net.pubnative.sdk.layouts.PNSmallLayout
import java.lang.Exception

class LayoutAdPresenter : BasePresenter<LayoutAdView>() {

    lateinit var mAppToken : String
    lateinit var mPlacementName : String
    lateinit var mLayout : PNLayout
    var mFeedItems : MutableList<FeedAdapter.FeedItem> = mutableListOf()

    enum class Size {
        SMALL, MEDIUM, LARGE
    }

    override fun updateView() {
        for (i in 1..25) {
            mFeedItems.add(FeedAdapter.FeedItem(view()?.getContext()?.resources?.getString(R.string.very_long_string), null))
        }
        view()?.updateView(mAppToken, mPlacementName)
    }

    fun onLoadClick(size: Size) {

        view()?.showIndicator()

        mLayout = when (size) {
            Size.SMALL -> PNSmallLayout()
            Size.MEDIUM -> PNMediumLayout()
            Size.LARGE -> PNLargeLayout()
        }

        mLayout.setLoadListener(object : PNLayout.LoadListener{
            override fun onPNLayoutLoadFinish(layout: PNLayout?) {
                view()?.hideIndicator()
                view()?.loadAdClick()
            }

            override fun onPNLayoutLoadFail(layout: PNLayout?, exception: Exception?) {
                view()?.hideIndicator()
                view()?.showErrorMessage(exception)
            }

        })
        mLayout.load(view()?.getContext(), mAppToken, mPlacementName)
    }

    fun onShowClick() {

        view()?.showAdClick()

        if (mLayout is PNSmallLayout) {
            showSmallAd(mLayout as PNSmallLayout)
        } else if (mLayout is PNMediumLayout) {
            showMediumAd(mLayout as PNMediumLayout)
        } else {
            showLargeAd(mLayout as PNLargeLayout)
        }
    }

    fun onStartTrackingClick() {

        view()?.showIndicator()

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

        view()?.hideIndicator()

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
        view()?.hideIndicator()
    }

    private fun showMediumAd(layout: PNMediumLayout) {
        mFeedItems.removeAt(7)
        mFeedItems.add(7, FeedAdapter.FeedItem("", layout.getView(view()?.getContext())))
        view()?.showAdClick()
        view()?.hideIndicator()
    }

    private fun showLargeAd(layout: PNLargeLayout) {
        layout.setViewListener(object : PNLargeLayout.ViewListener {
            override fun onPNLayoutViewShown(p0: PNLayout?) {
                view()?.hideIndicator()
                view()?.showAdClick()
            }

            override fun onPNLayoutViewHidden(p0: PNLayout?) {
                // Do nothing.
            }

        })
        layout.show()
    }

}