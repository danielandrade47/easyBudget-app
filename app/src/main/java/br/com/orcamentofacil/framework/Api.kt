package br.com.orcamentofacil.framework

import br.com.orcamentofacil.domain.Customer
import br.com.orcamentofacil.domain.Deal
import br.com.orcamentofacil.domain.DealUpdatePayload
import br.com.orcamentofacil.domain.Product
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("customer/list")
    suspend fun getCustomersList(): Response<List<Customer>>

    @POST("customer/create")
    suspend fun createCustomer(
        @Body customer: Customer
    ): Response<Customer>

    @PATCH("customer/{customerId}")
    suspend fun updateCustomer(
        @Path("customerId") customerId: String,
        @Body customer: Customer
    ): Response<Customer>

    @DELETE("customer/{customerId}")
    suspend fun deleteCustomer(
        @Path("customerId") customerId: String
    ): Response<JsonObject>

    @GET("product/list")
    suspend fun getProductsList(): Response<List<Product>>

    @POST("product/create")
    suspend fun createProduct(
        @Body product: Product
    ): Response<Product>

    @PATCH("product/{productId}")
    suspend fun updateProduct(
        @Path("productId") productId: String,
        @Body product: Product
    ): Response<Product>

    @DELETE("product/{productId}")
    suspend fun deleteProduct(
        @Path("productId") productId: String
    ): Response<JsonObject>

    @GET("deal/list")
    suspend fun getDealsList(): Response<List<Deal>>

    @PATCH("deal/{dealId}")
    suspend fun updateDeal(
        @Path("dealId") dealId: String,
        @Body deal: DealUpdatePayload
    ): Response<Deal>

    @POST("deal/create")
    suspend fun createDeal(
        @Body deal: DealUpdatePayload
    ): Response<Deal>

    @DELETE("deal/{dealId}")
    suspend fun deleteDeal(
        @Path("dealId") dealId: String
    ): Response<JsonObject>
}