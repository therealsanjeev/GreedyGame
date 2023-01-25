package com.therealsanjeev.greedygame.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.therealsanjeev.greedygame.adpter.AlbumAdapter
import com.therealsanjeev.greedygame.databinding.FragmentAlbumBinding
import com.therealsanjeev.greedygame.model.recycleview.album
import com.therealsanjeev.greedygame.utils.hide
import com.therealsanjeev.greedygame.utils.isNetworkAvailable
import com.therealsanjeev.greedygame.utils.showToast
import com.therealsanjeev.greedygame.viewmodel.ApiViewModel

class AlbumFragment : Fragment() {

    private val binding:FragmentAlbumBinding by lazy {
        FragmentAlbumBinding.inflate(layoutInflater)
    }

    private lateinit var tagViewModel: ApiViewModel
    private var responseList= ArrayList<album>()

    private lateinit var albumTag:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        //getting tag name by MVVM
        tagViewModel= ViewModelProvider(requireActivity()).get(ApiViewModel::class.java)
        tagViewModel.tagInfoResponse.observe(
            requireActivity()
        ) { response ->
            if (response.isSuccessful) {
                albumTag = response.body()!!.tag.name
                Log.d("TAG", "onCreateView: $albumTag")
                tagViewModel.getAlbumsVM(albumTag)
                tagViewModel.albumsResponse.observe(
                    requireActivity()
                ) {
                    if (it.isSuccessful) {
                        for (element in it.body()!!.results.albummatches.album) {
                            val item =
                                album(element.name, element.artist, element.image[3].text)
                            Log.d("album", "onCreateView: ${element.name},${element.artist}")
                            responseList.add(item)
                        }
                        with(binding){

                            recycleViewAlbums.apply {
                                adapter=AlbumAdapter(requireActivity(),responseList)
                                layoutManager= GridLayoutManager(requireActivity(), 2)
                            }
                            progressbar.hide()
                        }


                    } else {

                        if (!requireActivity().isNetworkAvailable())
                            requireActivity().showToast("Please connect to the Internet")
                        else
                            requireActivity().showToast("Something went wrong")

                    }
                }

            } else {
                if (!requireActivity().isNetworkAvailable())
                    requireActivity().showToast("Please connect to the Internet")
                else
                    requireActivity().showToast("Something went wrong")
            }
        }

        return binding.root
    }

}