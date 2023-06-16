package com.example.frogmitest.data.entities

import com.google.gson.annotations.SerializedName

data class FrogmiResponse(@SerializedName("data") val data: List<Data>, val links:Links)
data class Data(val attributes: Attributes)
data class Links(val next:String?)
data class Attributes(val name: String,
                      val code: String,
                      @SerializedName("full_address") val fullAddress: String)






