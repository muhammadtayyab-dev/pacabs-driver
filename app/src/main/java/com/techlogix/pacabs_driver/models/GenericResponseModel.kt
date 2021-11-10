package com.techlogix.pacabs_driver.models

import com.google.gson.annotations.SerializedName

data class GenericResponseModel<T>(@SerializedName("status") var status: Boolean,
                                   @SerializedName("message") var message: String,
                                   @SerializedName("result") var result: T?,
                                   @SerializedName("error") var error: String

)