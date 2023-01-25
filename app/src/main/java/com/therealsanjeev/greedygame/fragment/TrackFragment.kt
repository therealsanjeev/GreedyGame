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
import com.therealsanjeev.greedygame.adpter.TrackAdapter
import com.therealsanjeev.greedygame.databinding.FragmentTrackBinding
import com.therealsanjeev.greedygame.model.recycleview.track
import com.therealsanjeev.greedygame.utils.hide
import com.therealsanjeev.greedygame.utils.isNetworkAvailable
import com.therealsanjeev.greedygame.utils.showToast
import com.therealsanjeev.greedygame.viewmodel.ApiViewModel


class TrackFragment : Fragment() {

    private val binding:FragmentTrackBinding by lazy {
        FragmentTrackBinding.inflate(layoutInflater)
    }
    private var responseList= ArrayList<track>()

    private lateinit var tagViewModel: ApiViewModel
    private lateinit var trackTag:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        tagViewModel= ViewModelProvider(requireActivity()).get(ApiViewModel::class.java)
        tagViewModel.tagInfoResponse.observe(
            requireActivity()
        ) { response ->
            if (response.isSuccessful) {

                trackTag = response.body()!!.tag.name
                tagViewModel.getTracksVM(trackTag)
                tagViewModel.trackResponse.observe(
                    requireActivity()
                ) {
                    if (it.isSuccessful) {
                        for (element in it.body()!!.results.trackmatches.track) {
                            val item =
                                track(element.name, element.artist, element.image[3].text)
                            responseList.add(item)
                        }

                        with(binding) {
                            recycleViewTracks.apply {
                                adapter = TrackAdapter(requireActivity(), responseList)
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