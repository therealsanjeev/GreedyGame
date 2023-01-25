package com.therealsanjeev.greedygame.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.therealsanjeev.greedygame.databinding.ItemTopSingleBinding
import com.therealsanjeev.greedygame.model.recycleview.track
import com.therealsanjeev.greedygame.utils.setImage

class TopTracksAdapter(private val context: Context, private var tracks: List<track>): RecyclerView.Adapter<TopTracksAdapter.MyViewHolder>() {
    inner class MyViewHolder(private val binding: ItemTopSingleBinding): RecyclerView.ViewHolder(binding.root) {


        fun bind(item:track){
            with(binding){
                albumName.text=item.name
                artistName.text=item.artist

                albumBackGD.setImage(item.image)
            }
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopTracksAdapter.MyViewHolder {

        val binding = ItemTopSingleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopTracksAdapter.MyViewHolder, position: Int) {
        val response=tracks[position]
        holder.bind(response)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}