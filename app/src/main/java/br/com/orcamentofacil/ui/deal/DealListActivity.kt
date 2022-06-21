package br.com.orcamentofacil.ui.deal

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.orcamentofacil.DEAL
import br.com.orcamentofacil.R
import br.com.orcamentofacil.domain.Deal
import br.com.orcamentofacil.ui.utils.hideKeyboard
import br.com.orcamentofacil.ui.utils.toVisibility
import kotlinx.android.synthetic.main.activity_deal_list.*

class DealListActivity : AppCompatActivity() {

    private val viewModel: DealListViewModel by lazy {
        DealListViewModel()
    }
    private lateinit var adapter: DealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal_list)

        configureObservers()
        configureDealsAdapter()
        swipeDeals.setOnRefreshListener {
            updateDealsList()
        }
        fabAddDeal.setOnClickListener {
            startActivity(Intent(this, DealManageActivity::class.java))
        }
        ivBackArrow.setOnClickListener { finish() }
        etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                val query = v.text.toString()
                viewModel.searchDeal(query)
                true
            } else {
                false
            }
        }
    }

    private fun configureDealsAdapter() {
        adapter = DealAdapter(
            { deal, _ ->
                startActivity(
                    Intent(this, DealDetailActivity::class.java)
                        .putExtra(DEAL, deal)
                )
            },
            { dealToRemove ->
                AlertDialog.Builder(this)
                    .setTitle("Tem certeza que quer excluir o orçamento do cliente ${dealToRemove.customer?.name}?")
                    .setNegativeButton("Não") { _, _ -> }
                    .setPositiveButton("Sim") { _, _ ->
                        loader.toVisibility(true)
                        rvDeals.toVisibility(false)
                        viewModel.deleteDeal(
                            dealToRemove.id ?: ""
                        )
                    }.show()
            })
        rvDeals.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        updateDealsList()
    }

    private fun updateDealsList() {
        loader.toVisibility(true)
        rvDeals.toVisibility(false)
        viewModel.getDealsList()
    }

    private fun configureObservers() {
        viewModel.deals.observe(this) { deals ->
            configureAdapter(deals)
        }
        viewModel.filterDeals.observe(this) { deals ->
            configureAdapter(deals)
        }
        viewModel.delete.observe(this) { status ->
            if (status)
                updateDealsList()
        }
    }

    private fun configureAdapter(deals: List<Deal>) {
        val list = deals.filter { it.customer != null && (it.products?.size ?: 0) > 0 }
        loader.toVisibility(false)
        rvDeals.toVisibility(true)
        swipeDeals.isRefreshing = false
        adapter.setupItems(list)
    }
}