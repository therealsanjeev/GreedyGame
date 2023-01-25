package com.therealsanjeev.greedygame.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.therealsanjeev.greedygame.databinding.ActivityGenreDetailsBinding
import com.therealsanjeev.greedygame.fragment.AlbumFragment
import com.therealsanjeev.greedygame.fragment.ArtistFragment
import com.therealsanjeev.greedygame.fragment.TrackFragment
import com.therealsanjeev.greedygame.utils.isNetworkAvailable
import com.therealsanjeev.greedygame.utils.showToast
import com.therealsanjeev.greedygame.viewmodel.ApiViewModel

class GenreDetailActivity : AppCompatActivity() {

    private lateinit var tagViewModel: ApiViewModel
    private val binding:ActivityGenreDetailsBinding by lazy {
        ActivityGenreDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //getting data from Intent :
        val tag = intent.getStringExtra("tag")

        //binding data with views
        with(binding){
            tagName.text=tag.toString()
            backBtn.setOnClickListener {
                super.onBackPressed()
            }


            //SetUp ViewPager
            setupViewPager(viewpager)
            tabs.setupWithViewPager(viewpager)
        }

        //accessing ViewModel :
        tagViewModel= ViewModelProvider(this)[ApiViewModel::class.java]
        tagViewModel.getTagInfoVM(tag.toString())
        tagViewModel.tagInfoResponse.observe(
            this
        ) { response ->
            if (response.isSuccessful) {
                val summary = response.body()!!.tag.wiki.summary
                binding.tagSummary.text = removeTags(summary)

            } else {

                if (!isNetworkAvailable())
                    showToast("Please connect to the Internet")
                else
                    showToast("Something went wrong")
            }
        }


    }
    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(AlbumFragment(), "Albums")
        adapter.addFragment(ArtistFragment(), "Artists")
        adapter.addFragment(TrackFragment(), "Tracks")
        viewPager.adapter = adapter
    }
    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
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