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

class DealManageViewModel : ViewModel() {

    private val _createdDeal = MutableLiveData<Deal>()
    val createdDeal: LiveData<Deal> get() = _createdDeal

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun createDeal(customer: Customer?, adapterList: MutableList<Deal.DealProduct>) {
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
            val response = retrofit.createDeal(dealUpdatePayload)
            if (response.isSuccessful)
                _createdDeal.postValue(response.body())
            else
                _error.postValue("Não foi possível atualizar o orçamento")

        }
    }
}