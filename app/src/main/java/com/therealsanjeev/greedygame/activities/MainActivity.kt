package com.therealsanjeev.greedygame.activities

import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.therealsanjeev.greedygame.R
import com.therealsanjeev.greedygame.adpter.GenresAdapter
import com.therealsanjeev.greedygame.databinding.ActivityMainBinding
import com.therealsanjeev.greedygame.model.recycleview.genres
import com.therealsanjeev.greedygame.utils.hide
import com.therealsanjeev.greedygame.utils.isNetworkAvailable
import com.therealsanjeev.greedygame.utils.showToast
import com.therealsanjeev.greedygame.viewmodel.ApiViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ApiViewModel

    private var responseList = ArrayList<genres>()
    private var top10 = ArrayList<genres>()


    var flag = false

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //accessing ViewModel :
        viewModel = ViewModelProvider(this)[ApiViewModel::class.java]

        val method = "tag.getTopTags"


        val internet =isNetworkAvailable()
        if (!internet){
            binding.progressbar.hide()
            showToast( "Please connect to the Internet")
        }

        else{
            viewModel.getDataAllVM(method)
            getData()
        }


        val vibe = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        } else {
            TODO("VERSION.SDK_INT < M")

        }
        with(binding) {
            btnExpend.setOnClickListener {
                vibe.vibrate(50);

                if (!flag) {

                    recycleView.apply {
                        adapter = GenresAdapter(this@MainActivity, responseList)
                        layoutManager = GridLayoutManager(applicationContext, 3)
                    }

                    flag = true
                    btnExpend.setImageResource(R.drawable.down)
                } else {

                    recycleView.apply {
                        adapter = GenresAdapter(this@MainActivity, top10)
                        layoutManager = GridLayoutManager(applicationContext, 3)
                    }
                    btnExpend.setImageResource(R.drawable.start)
                    flag = false
                }

            }
        }


    }

    private fun getData(){
        viewModel.apiResponse.observe(
            this
        ) {
            binding.progressbar.hide()
            if (it.isSuccessful) {

                for (element in it.body()!!.toptags.tag) {
                    val item = genres(element.name.uppercase())
                    responseList.add(item)
                }

                top10 = responseList.slice(0..11) as ArrayList<genres>

                with(binding) {
                    recycleView.apply {
                        adapter = GenresAdapter(this@MainActivity, top10)
                        layoutManager = GridLayoutManager(applicationContext, 3)
                    }

                }


            }
            else {
                if (!isNetworkAvailable())
                    showToast( "Please connect to the Internet")
                else
                    showToast("Something went wrong")
            }
        }
    }

}