package net.pubnative.qa.demo.activities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import net.pubnative.qa.demo.R

class FeedAdapter(val items: MutableList<FeedItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder?.itemViewType == 0) {
            items[position].adView?.let {
                (holder as ViewAdHolder).bindAdView(it)
            }
        } else {
            (holder as ViewHolder)
        }
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.infeed_item, parent, false)
            val holder = ViewAdHolder(itemView)
            return holder
        } else {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.infeed_dummy_item, parent, false)
            return ViewHolder(itemView)
        }
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int): Int {
        if (items[position].adView != null) {
            return 0
        } else {
            return 1
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class ViewAdHolder(view: View) : RecyclerView.ViewHolder(view) {
        val adContainer : LinearLayout = view.findViewById(R.id.ll_ad_container)

        fun bindAdView(view: View) {
            adContainer.visibility = View.VISIBLE
            adContainer.removeAllViews()
            adContainer.addView(view)
        }
    }

    data class FeedItem(val text : String?, val adView: View?)
}