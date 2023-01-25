package com.therealsanjeev.greedygame.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.therealsanjeev.greedygame.adpter.GenresAdapter
import com.therealsanjeev.greedygame.adpter.TopAlbumAdapter
import com.therealsanjeev.greedygame.adpter.TopTracksAdapter
import com.therealsanjeev.greedygame.databinding.ActivityArtistActivityBinding
import com.therealsanjeev.greedygame.model.recycleview.album
import com.therealsanjeev.greedygame.model.recycleview.genres
import com.therealsanjeev.greedygame.model.recycleview.track
import com.therealsanjeev.greedygame.utils.hide
import com.therealsanjeev.greedygame.viewmodel.ApiViewModel

class ArtistActivity : AppCompatActivity() {

    private val binding: ActivityArtistActivityBinding by lazy {
        ActivityArtistActivityBinding.inflate(layoutInflater)
    }

    private lateinit var artistViewModel: ApiViewModel

    private var responseList = ArrayList<genres>()
    private var responseListTopTracks = ArrayList<track>()
    private var responseListTopAlbums = ArrayList<album>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //getting data from intent:
        val artist = intent.getStringExtra("artist")

        //binding data with views
        with(binding) {
            artistNameActivity.text = artist.toString()
            backBtn.setOnClickListener {
                super.onBackPressed()
            }

        }


        //accessing ViewModel :
        artistViewModel = ViewModelProvider(this)[ApiViewModel::class.java]
        artistViewModel.getArtistVM(artist.toString())
        artistViewModel.artistResponse.observe(this) { response ->
            if (response.isSuccessful) {
                //tags
                val result = response.body()!!.artist
                for (element in result.tags.tag) {
                    val item = genres(element.name.uppercase())
                    responseList.add(item)
                }




                //setting Playcount & followers
                var playCount = result.stats.playcount.toInt()
                var follower = result.stats.listeners.toInt()
                with(binding) {

                    recyclerviewArtistTag.apply {
                        adapter = GenresAdapter(this@ArtistActivity, responseList)
                        layoutManager = LinearLayoutManager(
                            this@ArtistActivity, LinearLayoutManager.HORIZONTAL, false
                        )
                    }

                    recyclerviewArtistTag.apply {
                        adapter = GenresAdapter(this@ArtistActivity, responseList)
                        layoutManager = LinearLayoutManager(
                            this@ArtistActivity, LinearLayoutManager.HORIZONTAL, false
                        )
                    }
                    when {
                        playCount > 1000000 -> {
                            playCount /= 1000000;
                            playcountInt.text = "$playCount" + "M"
                        }
                        playCount > 1000 -> {
                            playCount /= 1000;
                            playcountInt.text = "$playCount" + "K"
                        }
                        else -> {
                            playcountInt.text = "$playCount"
                        }
                    }
                    when {
                        follower > 1000000 -> {
                            follower /= 1000000;
                            followersInt.text = "$follower" + "M"
                        }
                        follower > 1000 -> {
                            follower /= 1000;
                            followersInt.text = "$follower" + "K"
                        }
                        else -> {
                            followersInt.text = "$follower"
                        }
                    }
                    //summary
                    if (result.bio != null) {
                        artistSummary.text = removeTags(result.bio.summary)
                    } else {
                        artistSummary.text = "No Bio :("
                    }
                }

                //top Tracks:
                artistViewModel.getTopTracksVM(artist.toString())
                artistViewModel.topTracksResponse.observe(this) {
                    if (it.isSuccessful) {
                        for (element in it.body()!!.toptracks.track) {
                            val item =
                                track(element.name, element.artist.name, element.image[3].text)
                            responseListTopTracks.add(item)
                        }
                        binding.topTracksRecyclerView.apply {
                            adapter = TopTracksAdapter(this@ArtistActivity, responseListTopTracks)
                            layoutManager = LinearLayoutManager(
                                this@ArtistActivity, LinearLayoutManager.HORIZONTAL, false
                            )
                        }

                    }

                }

                //top Album:
                artistViewModel.getTopAlbumVM(artist.toString())
                artistViewModel.topAlbumResponse.observe(this) {
                    if (it.isSuccessful) {
                        for (element in it.body()!!.topalbums.album) {
                            val item =
                                album(element.name, element.artist.name, element.image[3].text)
                            responseListTopAlbums.add(item)
                        }

                        binding.topAlbumsRecyclerView.apply {
                            adapter = TopAlbumAdapter(this@ArtistActivity, responseListTopAlbums)
                            layoutManager = LinearLayoutManager(this@ArtistActivity, LinearLayoutManager.HORIZONTAL, false)

                        }

                    }

                }

                binding.progressbar.hide()
            }
        }

    }

    private fun removeTags(s: String): String {
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