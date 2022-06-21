package br.com.orcamentofacil.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DealUpdatePayload(
    @SerializedName("clientId") val clientId: String,
    @SerializedName("products") val products: List<ProductPayload>
) : Parcelable {

    @Parcelize
    data class ProductPayload(
        @SerializedName("id") val id: String,
        @SerializedName("quantity") val quantity: Int
    ) : Parcelable
}
