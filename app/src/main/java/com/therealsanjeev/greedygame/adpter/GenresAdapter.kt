package com.therealsanjeev.greedygame.adpter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.therealsanjeev.greedygame.activities.GenreDetailActivity
import com.therealsanjeev.greedygame.databinding.ItemSingleBinding
import com.therealsanjeev.greedygame.model.recycleview.genres


class GenresAdapter(private val context: Context, private var tags: List<genres>): RecyclerView.Adapter<GenresAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemSingleBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(item:genres){
            with(binding){

                tagName.text=item.name

                tagCard.setOnClickListener {
                    val intent = Intent(context, GenreDetailActivity::class.java).apply {
                        putExtra("tag", item.name)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSingleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(tags[position])

    }

    override fun getItemCount(): Int {
        return tags.size
    }


}