package com.example.pkompoultrymanager.tables.feed_source

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R

class FeedSourceAdapter: RecyclerView.Adapter<FeedSourceAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onDeleteFeedSourceClickListener(feedSource: FeedSource, position: Int)
        fun onItemClickListener(feedSource: FeedSource)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }


    private var feedSourceList = emptyList<FeedSource>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemFeedSourceName: TextView
        val itemFeedSourceContact: TextView
        val itemFeedSourceLocation: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdateDeleteMenu_PersonLayout)
            itemFeedSourceName = itemView.findViewById(R.id.tvPersonName_PersonLayout)
            itemFeedSourceContact = itemView.findViewById(R.id.tvPersonContact_PersonLayout)
            itemFeedSourceLocation = itemView.findViewById(R.id.tvPersonLocation_PersonLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.person_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFeedSource= feedSourceList[position]

        holder.itemView.apply {
            val name = currentFeedSource.FeedSourceName
            val contact = "Contact: ${currentFeedSource.FeedSourceContact}"
            val location = "Location: ${currentFeedSource.FeedSourceLocation}"

            holder.itemFeedSourceName.text = name
            holder.itemFeedSourceContact.text = contact
            holder.itemFeedSourceLocation.text = location
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteFeedSourceClickListener(currentFeedSource, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentFeedSource)
        }
    }

    override fun getItemCount(): Int {
        return feedSourceList.size
    }

    fun setData(feedSource: List<FeedSource>){
        this.feedSourceList = feedSource
        notifyDataSetChanged()
    }
}
