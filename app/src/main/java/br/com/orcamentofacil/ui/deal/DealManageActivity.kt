package br.com.orcamentofacil.ui.deal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.com.orcamentofacil.CUSTOMER
import br.com.orcamentofacil.ITEM_SELECT
import br.com.orcamentofacil.PRODUCT
import br.com.orcamentofacil.R
import br.com.orcamentofacil.domain.Customer
import br.com.orcamentofacil.domain.Deal
import br.com.orcamentofacil.domain.Product
import br.com.orcamentofacil.ui.customer.CustomerListActivity
import br.com.orcamentofacil.ui.product.ProductListActivity
import br.com.orcamentofacil.ui.utils.toVisibility
import kotlinx.android.synthetic.main.activity_deal_manage.*

class DealManageActivity : AppCompatActivity() {

    private lateinit var activityResult: ActivityResultLauncher<Intent>
    private lateinit var currentCustomer: Customer
    private lateinit var productAdapter: DealProductAdapter

    private val viewModel: DealManageViewModel by lazy {
        DealManageViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal_manage)

        initViews()
        configureObservers()
        configureListeners()
        activityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                result.data?.let {
                    when {
                        it.hasExtra(CUSTOMER) -> {
                            val selectedCustomer = it.extras?.get(CUSTOMER) as Customer
                            configureCurrentCustomer(selectedCustomer)
                        }
                        it.hasExtra(PRODUCT) -> {
                            val selectedProduct = it.extras?.get(PRODUCT) as Product
                            addProductToDeal(selectedProduct)
                        }
                    }
                }
            }
    }

    private fun configureObservers() {
        viewModel.apply {
            createdDeal.observe(this@DealManageActivity) {
                finish()
            }
            error.observe(this@DealManageActivity) {
                loader.toVisibility(false)
                btnSave.toVisibility(true)
                btnAddProduct.toVisibility(true)
                Toast.makeText(this@DealManageActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViews() {
        productAdapter = DealProductAdapter(listOf()) { _, _ ->

        }
        rvProducts.adapter = productAdapter
    }

    private fun configureCurrentCustomer(customer: Customer) {
        currentCustomer = customer
        tvSelectedCustomer.text = currentCustomer.name
    }

    private fun addProductToDeal(product: Product) {
        val productExists = productAdapter.getAdapterList().find { product.id == it.product?.id }
        if (productExists == null) {
            val dealProduct = Deal.DealProduct(product, 1)
            productAdapter.addItem(dealProduct)
            configureEmptyProductsLayout()
        }
    }

    private fun configureEmptyProductsLayout() {
        tvEmptyProduct.visibility = View.GONE
        rvProducts.toVisibility(true)
    }

    private fun configureListeners() {
        cvSelectedCustomer.setOnClickListener {
            val customerIntent = Intent(this, CustomerListActivity::class.java)
            customerIntent.putExtra(ITEM_SELECT, true)
            activityResult.launch(customerIntent)
        }
        btnAddProduct.setOnClickListener {
            val productIntent = Intent(this, ProductListActivity::class.java)
            productIntent.putExtra(ITEM_SELECT, true)
            activityResult.launch(productIntent)
        }
        btnSave.setOnClickListener {
            if (!this::currentCustomer.isInitialized) {
                Toast.makeText(
                    this,
                    "Selecione um cliente para salvar o orçamento",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (productAdapter.getAdapterList().isEmpty()) {
                Toast.makeText(
                    this,
                    "Adicione pelo menos um produto ao orçamento",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            createDeal()
        }
    }

    private fun createDeal() {
        loader.toVisibility(true)
        btnSave.toVisibility(false)
        btnAddProduct.toVisibility(false)
        viewModel.createDeal(currentCustomer, productAdapter.getAdapterList())
    }
}