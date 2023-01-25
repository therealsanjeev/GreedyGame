package com.therealsanjeev.greedygame.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.therealsanjeev.greedygame.adpter.ArtistAdapter
import com.therealsanjeev.greedygame.databinding.FragmentArtistBinding
import com.therealsanjeev.greedygame.model.recycleview.artist
import com.therealsanjeev.greedygame.utils.hide
import com.therealsanjeev.greedygame.utils.isNetworkAvailable
import com.therealsanjeev.greedygame.utils.showToast
import com.therealsanjeev.greedygame.viewmodel.ApiViewModel

class ArtistFragment : Fragment() {

    private val binding:FragmentArtistBinding by lazy {
        FragmentArtistBinding.inflate(layoutInflater)
    }

    private lateinit var tagViewModel: ApiViewModel
    private var responseList= ArrayList<artist>()

    private lateinit var artistTag:String
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

                artistTag = response.body()!!.tag.name
                tagViewModel.getArtistsVM(artistTag)
                tagViewModel.artistsResponse.observe(
                    requireActivity()
                ) {
                    if (it.isSuccessful) {
                        for (element in it.body()!!.results.artistmatches.artist) {
                            val item =
                                artist(element.name, element.image[4].text)
                            responseList.add(item)
                        }

                        with(binding) {

                            recycleViewArtists.apply {
                                adapter = ArtistAdapter(requireActivity(), responseList)
                                layoutManager = GridLayoutManager(requireActivity(), 2)
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