package br.com.orcamentofacil.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Deal(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("customer") var customer: Customer? = null,
    @SerializedName("products") val products: List<DealProduct>? = null,
    @SerializedName("total") val total: Double? = null,
    @SerializedName("created") val created: Date? = null
) : Parcelable {

    @Parcelize
    data class DealProduct(
        @SerializedName("product") val product: Product? = null,
        @SerializedName("quantity") var quantity: Int? = null
    ) : Parcelable

}
