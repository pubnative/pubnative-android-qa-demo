package net.pubnative.qa.demo.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import net.pubnative.qa.demo.R

class FeedAdapter(val items: MutableList<FeedItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder?.itemViewType == 0) {
            (holder as ViewAdHolder).bindAdView(items[position].adView as View)
        } else {
            (holder as ViewHolder).textView.text = items[position].text
        }
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.infeed_item, parent, false)
            val holder = ViewAdHolder(itemView)
            return holder
        } else {
            val itemView = LayoutInflater.from(parent?.context).inflate(android.R.layout.simple_list_item_1, parent, false)
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(android.R.id.text1)
    }

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