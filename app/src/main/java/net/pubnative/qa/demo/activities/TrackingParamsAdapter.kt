package net.pubnative.qa.demo.activities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.tracking_param_item.view.*
import net.pubnative.qa.demo.R
import net.pubnative.qa.demo.model.TrackingParam

class TrackingParamsAdapter(val params: List<TrackingParam>) : RecyclerView.Adapter<TrackingParamsAdapter.ListRowHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListRowHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.tracking_param_item, parent, false)
        return ListRowHolder(view)
    }

    override fun onBindViewHolder(holder: ListRowHolder, position: Int) {
        holder.bindTrackingParam(params[position])
    }

    override fun getItemCount() = params.size


    class ListRowHolder(row: View) : RecyclerView.ViewHolder(row) {

        fun bindTrackingParam(param : TrackingParam) {
            with(param) {
                itemView.tv_tracking_key.text = param.name
                itemView.tv_tracking_value.text = param.value
            }
        }
    }

}