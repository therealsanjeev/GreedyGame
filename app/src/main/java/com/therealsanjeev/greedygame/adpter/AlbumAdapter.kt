package com.therealsanjeev.greedygame.adpter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.therealsanjeev.greedygame.activities.AlbumActivity
import com.therealsanjeev.greedygame.databinding.ItemAlbumBinding
import com.therealsanjeev.greedygame.model.recycleview.album
import com.therealsanjeev.greedygame.utils.setImage

class AlbumAdapter(private val context: Context, private var albums: List<album>): RecyclerView.Adapter<AlbumAdapter.MyViewHolder>() {
    inner class MyViewHolder(private val binding: ItemAlbumBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(item:album){

            with(binding){
                albumName.text=item.name
                artistName.text=item.artist

                albumBackGD.setImage(item.image)


                albumCardView.setOnClickListener {
                    val intent = Intent(context, AlbumActivity::class.java).apply {
                        putExtra("album", item.name)
                        putExtra("artist", item.artist)
                        putExtra("image", item.image)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumAdapter.MyViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumAdapter.MyViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int {
        return albums.size
    }
}