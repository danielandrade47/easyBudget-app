package br.com.orcamentofacil.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orcamentofacil.domain.Product
import br.com.orcamentofacil.framework.retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _filterProducts = MutableLiveData<List<Product>>()
    val filterProducts: LiveData<List<Product>> get() = _filterProducts

    private val _delete = MutableLiveData<Boolean>()
    val delete: LiveData<Boolean> get() = _delete

    fun getProductsList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofit.getProductsList()
            if (response.isSuccessful) {
                _products.postValue(response.body())
            }
        }
    }

    fun searchProduct(query: String) {
        _filterProducts.postValue(
            if (query.isNotEmpty())
                _products.value?.filter {
                    it.name?.lowercase()?.contains(query.lowercase()) == true
                }
            else
                _products.value
        )
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofit.deleteProduct(productId)
            _delete.postValue(response.isSuccessful)
        }
    }
}