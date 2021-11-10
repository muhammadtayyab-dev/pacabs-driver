package com.techlogix.pacabs_driver.models.walletsModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WalletResponseModel(
    @SerializedName("id") var id: Int,
    @SerializedName("walletbalance") var walletBalance: Int
) : Serializable