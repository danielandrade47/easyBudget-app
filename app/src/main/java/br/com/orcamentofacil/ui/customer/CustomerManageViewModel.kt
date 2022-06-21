package br.com.orcamentofacil.ui.customer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orcamentofacil.domain.Customer
import br.com.orcamentofacil.framework.retrofit
import br.com.orcamentofacil.ui.utils.getErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerManageViewModel : ViewModel() {

    val newCustomer = MutableLiveData<Customer>()

    val error = MutableLiveData<String>()

    fun createCustomer(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofit.createCustomer(customer)
            if (response.isSuccessful)
                newCustomer.postValue(response.body())
            else
                error.postValue(response.getErrorMessage())
        }
    }

    fun updateCustomer(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofit.updateCustomer(customer.id ?: "", customer)
            if (response.isSuccessful)
                newCustomer.postValue(response.body())
            else
                error.postValue(response.getErrorMessage())
        }
    }
}