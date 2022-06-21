package br.com.orcamentofacil.ui.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orcamentofacil.domain.Product
import br.com.orcamentofacil.framework.retrofit
import br.com.orcamentofacil.ui.utils.getErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductManageViewModel : ViewModel() {

    val newProduct = MutableLiveData<Product>()

    val error = MutableLiveData<String>()

    fun createProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofit.createProduct(product)
            if (response.isSuccessful)
                newProduct.postValue(response.body())
            else
                error.postValue(response.getErrorMessage())
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofit.updateProduct(product.id ?: "", product)
            if (response.isSuccessful)
                newProduct.postValue(response.body())
            else
                error.postValue(response.getErrorMessage())
        }
    }
}