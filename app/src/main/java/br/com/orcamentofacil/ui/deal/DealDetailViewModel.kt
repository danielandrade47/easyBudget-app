package br.com.orcamentofacil.ui.deal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orcamentofacil.domain.Customer
import br.com.orcamentofacil.domain.Deal
import br.com.orcamentofacil.domain.DealUpdatePayload
import br.com.orcamentofacil.framework.retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DealDetailViewModel : ViewModel() {

    private val _updatedDeal = MutableLiveData<Deal>()
    val updatedDeal: LiveData<Deal> get() = _updatedDeal

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun updateDeal(dealId: String, customer: Customer?, adapterList: MutableList<Deal.DealProduct>) {
        val productsPayload = arrayListOf<DealUpdatePayload.ProductPayload>()
        adapterList.forEach {
            if ((it.quantity ?: 0) > 0) {
                val productPayload = DealUpdatePayload.ProductPayload(
                    id = it.product?.id ?: "",
                    quantity = it.quantity ?: 0
                )
                productsPayload.add(productPayload)
            }
        }
        val dealUpdatePayload = DealUpdatePayload(
            clientId = customer?.id ?: "",
            products = productsPayload
        )
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofit.updateDeal(dealId, dealUpdatePayload)
            if (response.isSuccessful)
                _updatedDeal.postValue(response.body())
            else
                _error.postValue("Não foi possível atualizar o orçamento")

        }
    }
}