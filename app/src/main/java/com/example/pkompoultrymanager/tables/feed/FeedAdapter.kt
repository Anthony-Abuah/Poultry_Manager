package com.example.pkompoultrymanager.tables.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.pkompoultrymanager.R

class FeedAdapter: RecyclerView.Adapter<FeedAdapter.MyViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun onDeleteFeedClickListener(feed: Feed, position: Int)
        fun onItemClickListener(feed: Feed)
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener=listener
    }


    private var feedList = emptyList<Feed>()

    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView){
        val itemMenu: ImageView
        val itemFeedId: TextView
        val itemFeedName: TextView
        val itemFeedType: TextView
        val itemFeedSource: TextView
        val itemFeedBrand: TextView

        init {
            itemMenu = itemView.findViewById(R.id.ivUpdateDeleteMenu_FeedLayout)
            itemFeedName = itemView.findViewById(R.id.tvFeedName_FeedLayout)
            itemFeedType = itemView.findViewById(R.id.tvFeedType_FeedLayout)
            itemFeedSource = itemView.findViewById(R.id.tvFeedSource_FeedLayout)
            itemFeedBrand = itemView.findViewById(R.id.tvFeedBrand_FeedLayout)
            itemFeedId = itemView.findViewById(R.id.tvFeedId_FeedLayout)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.feed_layout,parent,false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFeed= feedList[position]

        holder.itemView.apply {
            holder.itemFeedId.text= currentFeed.FeedId.toString()
            holder.itemFeedName.text = currentFeed.FeedName
            holder.itemFeedType.text = currentFeed.FeedType
            holder.itemFeedSource.text = currentFeed.FeedSource
            holder.itemFeedBrand.text = currentFeed.FeedBrand
        }
        holder.itemMenu.setOnClickListener {
            mListener.onDeleteFeedClickListener(currentFeed, position)
        }
        holder.itemView.setOnClickListener {
            mListener.onItemClickListener(currentFeed)
        }
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    fun setData(feed: List<Feed>){
        this.feedList = feed
        notifyDataSetChanged()
    }
}
