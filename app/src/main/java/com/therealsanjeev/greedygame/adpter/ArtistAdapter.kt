package com.therealsanjeev.greedygame.adpter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.therealsanjeev.greedygame.activities.ArtistActivity
import com.therealsanjeev.greedygame.databinding.ItemArtistBinding
import com.therealsanjeev.greedygame.model.recycleview.artist
import com.therealsanjeev.greedygame.utils.setImage

class ArtistAdapter(private val context: Context, private var artists: List<artist>): RecyclerView.Adapter<ArtistAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemArtistBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(item:artist){
            with(binding){
                name.text=item.name

                image.setImage(item.image)

                artistCardView.setOnClickListener {
                    val intent = Intent(context, ArtistActivity::class.java).apply {
                        putExtra("artist", item.name)
                    }
                    context.startActivity(intent)
                }
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistAdapter.MyViewHolder {

        val binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistAdapter.MyViewHolder, position: Int) {
        holder.bind(artists[position])


    }

    override fun getItemCount(): Int {
        return artists.size
    }
}