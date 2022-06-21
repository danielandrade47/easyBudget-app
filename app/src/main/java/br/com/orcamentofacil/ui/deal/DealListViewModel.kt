package br.com.orcamentofacil.ui.deal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orcamentofacil.domain.Deal
import br.com.orcamentofacil.framework.retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DealListViewModel : ViewModel() {

    private val _deals = MutableLiveData<List<Deal>>()
    val deals: LiveData<List<Deal>> get() = _deals

    private val _filterDeals = MutableLiveData<List<Deal>>()
    val filterDeals: LiveData<List<Deal>> get() = _filterDeals

    private val _delete = MutableLiveData<Boolean>()
    val delete: LiveData<Boolean> get() = _delete

    fun getDealsList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofit.getDealsList()
            if (response.isSuccessful) {
                _deals.postValue(response.body())
            }
        }
    }

    fun searchDeal(query: String) {
        _filterDeals.postValue(
            if (query.isNotEmpty())
                _deals.value?.filter {
                    it.customer?.name?.lowercase()?.contains(query.lowercase()) == true
                }
            else
                _deals.value
        )
    }

    fun deleteDeal(dealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofit.deleteDeal(dealId)
            _delete.postValue(response.isSuccessful)
        }
    }
}