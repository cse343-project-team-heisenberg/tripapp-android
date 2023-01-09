package com.example.runningapplication.Model

import com.google.android.gms.maps.model.LatLng

data class PostDetail (val pictureUrl: String? = null,
                       val description: String? = null,
                       val like:ArrayList<String>? = null,
                       var save:ArrayList<String>? = null,
                       val comment:ArrayList<String>? = null,
                       val ltlng: LatLng?=null
                       )