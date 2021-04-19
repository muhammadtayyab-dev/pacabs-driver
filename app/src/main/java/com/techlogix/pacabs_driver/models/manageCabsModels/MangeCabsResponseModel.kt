package com.techlogix.pacabs_driver.models.manageCabsModels

import com.google.gson.annotations.SerializedName

data class MangeCabsResponseModel(
    @SerializedName("cabImageUrl") val cabImageUrl: String,
    @SerializedName("cabName") val cabName: String,
    @SerializedName("vehicalNum") val vehicalNum: String,
    @SerializedName("vehicalDriverName") val vehicalDriverName: String,
    @SerializedName("vehicalDriverStatus") val vehicalDriverStatus: String,
    @SerializedName("vehicalDriverNum") val vehicalDriverNum: String,
    @SerializedName("cabStatus") val cabStatus: Boolean
) {
}