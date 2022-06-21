package br.com.orcamentofacil.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("brand") val brand: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("price") val price: Double? = null
) : Parcelable
