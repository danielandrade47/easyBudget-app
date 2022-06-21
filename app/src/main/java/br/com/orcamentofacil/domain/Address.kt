package br.com.orcamentofacil.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    @SerializedName("state") val state: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("street") val street: String? = null,
    @SerializedName("number") val number: String? = null
) : Parcelable
