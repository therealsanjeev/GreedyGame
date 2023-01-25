package com.therealsanjeev.greedygame.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.therealsanjeev.greedygame.databinding.ItemTrackBinding
import com.therealsanjeev.greedygame.model.recycleview.track
import com.therealsanjeev.greedygame.utils.setImage

class TrackAdapter(private val context: Context, private var tracks:List<track>): RecyclerView.Adapter<TrackAdapter.MyViewHolder>() {
    inner class MyViewHolder(private val binding: ItemTrackBinding):RecyclerView.ViewHolder(binding.root) {


        fun bind(item:track){
            with(binding){
                name.text=item.name
                artistNameTrack.text=item.artist
              image.setImage(item.image)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackAdapter.MyViewHolder {

        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackAdapter.MyViewHolder, position: Int) {
        val response=tracks[position]
        holder.bind(response)

    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}