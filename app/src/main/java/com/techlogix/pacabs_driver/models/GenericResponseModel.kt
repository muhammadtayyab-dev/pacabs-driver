package com.techlogix.pacabs_driver.models

import com.google.gson.annotations.SerializedName

data class GenericResponseModel<T>(@SerializedName("status") val status: Boolean,
                                   @SerializedName("message") val message: String,
                                   @SerializedName("result") val result: T,
                                   @SerializedName("error") val error: String

)