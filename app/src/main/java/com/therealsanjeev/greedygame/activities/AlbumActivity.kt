package com.therealsanjeev.greedygame.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.therealsanjeev.greedygame.adpter.GenresAdapter
import com.therealsanjeev.greedygame.databinding.ActivityAlbumActivityBinding
import com.therealsanjeev.greedygame.model.recycleview.genres
import com.therealsanjeev.greedygame.utils.hide
import com.therealsanjeev.greedygame.utils.setImage
import com.therealsanjeev.greedygame.viewmodel.ApiViewModel

class AlbumActivity : AppCompatActivity() {
    private lateinit var albumViewModel: ApiViewModel


    private var responseList= ArrayList<genres>()

    private val binding:ActivityAlbumActivityBinding by lazy {
        ActivityAlbumActivityBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        //getting data from intent:
        val album = intent.getStringExtra("album")
        val artist = intent.getStringExtra("artist")
        Log.d("TAG", "onCreate: $album,$artist")

        //binding data with views
        with(binding){
            albumName.text=album.toString()
            artistName.text=artist.toString()
            backBtnAlbum.setOnClickListener {
                super.onBackPressed()
            }
        }


        //accessing ViewModel :
        albumViewModel= ViewModelProvider(this)[ApiViewModel::class.java]
        albumViewModel.getAlbumVM(album.toString(),artist.toString())

        albumViewModel.albumResponse.observe(this) { response ->
            if (response.isSuccessful) {
                val result = response.body()!!.album

                for (element in result.tags.tag) {
                    val item = genres(element.name.uppercase())
                    responseList.add(item)
                }
                with(binding) {
                    recycleViewAlbumDetails.apply {
                        adapter = GenresAdapter(this@AlbumActivity, responseList)
                        layoutManager = LinearLayoutManager(this@AlbumActivity, LinearLayoutManager.HORIZONTAL, false)

                    }
                    albumActivityBg.setImage(result.image[4].text)


                    if (result.wiki != null) {
                        val desc = result.wiki.summary
                        val descWithoutTags = removeTags(desc)
                        albumSummary.text = descWithoutTags
                    } else {
                        albumSummary.text = "No Description Found!!!"
                    }

                    progressbar.hide()
                }




            }
        }

    }


    private fun removeTags(s: String) : String{
        var target = 0
        for (i in s.indices) {
            if (s[i] == '<') {
                target = i
                break
            }
        }
        return s.substring(0, target)
    }


}