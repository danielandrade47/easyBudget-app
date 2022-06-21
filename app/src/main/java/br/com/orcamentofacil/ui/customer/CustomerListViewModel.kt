package br.com.orcamentofacil.ui.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orcamentofacil.domain.Customer
import br.com.orcamentofacil.framework.retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerListViewModel : ViewModel() {

    private val _customers = MutableLiveData<List<Customer>>()
    val customers: LiveData<List<Customer>> get() = _customers

    private val _filterCustomers = MutableLiveData<List<Customer>>()
    val filterCustomers: LiveData<List<Customer>> get() = _filterCustomers

    private val _delete = MutableLiveData<Boolean>()
    val delete: LiveData<Boolean> get() = _delete

    fun getCustomersList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofit.getCustomersList()
            if (response.isSuccessful) {
                _customers.postValue(response.body())
            }
        }
    }

    fun searchCustomer(query: String) {
        _filterCustomers.postValue(
            if (query.isNotEmpty())
                _customers.value?.filter {
                    it.name?.lowercase()?.contains(query.lowercase()) == true
                }
            else
                _customers.value
        )
    }

    fun deleteCustomer(customerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofit.deleteCustomer(customerId)
            _delete.postValue(response.isSuccessful)
        }
    }
}