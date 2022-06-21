package br.com.orcamentofacil.ui.customer

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.orcamentofacil.CUSTOMER
import br.com.orcamentofacil.ITEM_SELECT
import br.com.orcamentofacil.R
import br.com.orcamentofacil.domain.Customer
import br.com.orcamentofacil.ui.utils.GenericAdapter
import br.com.orcamentofacil.ui.utils.hideKeyboard
import br.com.orcamentofacil.ui.utils.toVisibility
import kotlinx.android.synthetic.main.activity_customer_list.*

class CustomerListActivity : AppCompatActivity() {

    private val viewModel: CustomerListViewModel by lazy {
        CustomerListViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_list)

        configureObservers()
        configureListeners()
    }

    override fun onResume() {
        super.onResume()
        loader.toVisibility(true)
        rvCustomers.toVisibility(false)
        viewModel.getCustomersList()
    }

    private fun configureObservers() {
        viewModel.customers.observe(this) { customers ->
            configureAdapter(customers)
        }
        viewModel.filterCustomers.observe(this) { customers ->
            configureAdapter(customers)
        }
        viewModel.delete.observe(this) { status ->
            if (status)
                viewModel.getCustomersList()
        }
    }

    private fun configureAdapter(customers: List<Customer>) {
        loader.toVisibility(false)
        rvCustomers.toVisibility(true)
        rvCustomers.adapter = GenericAdapter<Customer>(R.layout.item_customer,
            { customer, _ ->
                val isSelect = intent.hasExtra(ITEM_SELECT)
                if (isSelect) {
                    setResult(1, Intent().putExtra(CUSTOMER, customer))
                    finish()
                } else {
                    startActivity(
                        Intent(this, CustomerManageActivity::class.java)
                            .putExtra(CUSTOMER, customer)
                    )
                }
            },
            { customerToRemove ->
                AlertDialog.Builder(this)
                    .setTitle("Tem certeza que quer excluir o cliente ${customerToRemove.name}?")
                    .setNegativeButton("Não") { _, _ -> }
                    .setPositiveButton("Sim") { _, _ ->
                        loader.toVisibility(true)
                        rvCustomers.toVisibility(false)
                        viewModel.deleteCustomer(
                            customerToRemove.id ?: ""
                        )
                    }.show()
            }).apply {
            setupItems(customers)
        }
    }

    private fun configureListeners() {
        ivBackArrow.setOnClickListener { finish() }
        etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                val query = v.text.toString()
                viewModel.searchCustomer(query)
                true
            } else {
                false
            }
        }
        fabAddCustomer.setOnClickListener {
            val intent = Intent(this, CustomerManageActivity::class.java)
            startActivity(intent)
        }
    }
}