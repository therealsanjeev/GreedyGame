package com.therealsanjeev.greedygame.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therealsanjeev.greedygame.BuildConfig
import com.therealsanjeev.greedygame.model.album.albums
import com.therealsanjeev.greedygame.model.albumactivity.api.albumactivity
import com.therealsanjeev.greedygame.model.artistActivity.artistActivity
import com.therealsanjeev.greedygame.model.artistActivity.topalbums.topalbums
import com.therealsanjeev.greedygame.model.artistActivity.toptracks.toptracks
import com.therealsanjeev.greedygame.model.artists.artists
import com.therealsanjeev.greedygame.model.taginfo.tagInfo
import com.therealsanjeev.greedygame.model.topgenre.Toptags
import com.therealsanjeev.greedygame.model.tracks.tracks
import com.therealsanjeev.greedygame.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class ApiViewModel(): ViewModel() {

    val tagRepo=Repository()
    var apiResponse: MutableLiveData<Response<Toptags>> = MutableLiveData()
    fun getDataAllVM(method:String){
        viewModelScope.launch {
            val response=tagRepo.getAllDataRepo(method, BuildConfig.API_KEY)
            apiResponse.value=response
        }
    }

    var tagInfoResponse:MutableLiveData<Response<tagInfo>> = MutableLiveData()
    fun getTagInfoVM(tag:String){
        viewModelScope.launch {

            val response=tagRepo.getTagInfoRepo(tag, BuildConfig.API_KEY)
            tagInfoResponse.value=response
        }
    }

    val albumsResponse:MutableLiveData<Response<albums>> = MutableLiveData()
    fun getAlbumsVM(album:String){
        viewModelScope.launch {
            val response=tagRepo.getAlbumsRepo(album, BuildConfig.API_KEY)
            albumsResponse.value=response
        }

    }

    val artistsResponse:MutableLiveData<Response<artists>> = MutableLiveData()
    fun getArtistsVM(artist:String){
        viewModelScope.launch {
            val response=tagRepo.getArtistsRepo(artist, BuildConfig.API_KEY)
            artistsResponse.value=response
        }

    }

    val trackResponse:MutableLiveData<Response<tracks>> = MutableLiveData()
    fun getTracksVM(track:String){
        viewModelScope.launch {
            val response=tagRepo.getTracksRepo(track, BuildConfig.API_KEY)
            trackResponse.value=response
        }

    }

    //Album Activity:
    val albumResponse:MutableLiveData<Response<albumactivity>> = MutableLiveData()
    fun getAlbumVM(album:String,artist: String){
        viewModelScope.launch {
            val response=tagRepo.getAlbumRepo(album,artist, BuildConfig.API_KEY)
            albumResponse.value=response
        }

    }

    val artistResponse:MutableLiveData<Response<artistActivity>> = MutableLiveData()
    fun getArtistVM(artist:String){
        viewModelScope.launch {
            val response=tagRepo.getArtistRepo(artist, BuildConfig.API_KEY)
            artistResponse.value=response
        }

    }

    val topTracksResponse:MutableLiveData<Response<toptracks>> = MutableLiveData()
    fun getTopTracksVM(artist:String){
        viewModelScope.launch {
            val response=tagRepo.getTopTracksRepo(artist, BuildConfig.API_KEY)
            topTracksResponse.value=response
        }

    }

    val topAlbumResponse:MutableLiveData<Response<topalbums>> = MutableLiveData()
    fun getTopAlbumVM(artist:String){
        viewModelScope.launch {
            val response=tagRepo.getTopAlbumRepo(artist, BuildConfig.API_KEY)
            topAlbumResponse.value=response
        }

    }


}
