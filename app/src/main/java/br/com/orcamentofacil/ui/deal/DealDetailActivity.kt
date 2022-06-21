package br.com.orcamentofacil.ui.deal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.com.orcamentofacil.DEAL
import br.com.orcamentofacil.ITEM_SELECT
import br.com.orcamentofacil.PRODUCT
import br.com.orcamentofacil.R
import br.com.orcamentofacil.domain.Customer
import br.com.orcamentofacil.domain.Deal
import br.com.orcamentofacil.domain.Product
import br.com.orcamentofacil.ui.product.ProductListActivity
import br.com.orcamentofacil.ui.utils.formatPriceBR
import br.com.orcamentofacil.ui.utils.toVisibility
import kotlinx.android.synthetic.main.activity_deal_detail.*

class DealDetailActivity : AppCompatActivity() {

    private lateinit var activityResult: ActivityResultLauncher<Intent>

    private val viewModel: DealDetailViewModel by lazy {
        DealDetailViewModel()
    }

    private lateinit var deal: Deal
    private lateinit var dealProductAdapter: DealProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal_detail)

        if (intent.hasExtra(DEAL)) {
            deal = intent.getParcelableExtra(DEAL) ?: Deal()
        }

        configureObservers()
        configureListeners()
        configureLayoutByExtra()

        activityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                result.data?.let {
                    if (it.hasExtra(PRODUCT)) {
                        val selectedProduct = it.extras?.get(PRODUCT) as Product
                        addProductToDeal(selectedProduct)
                    }
                }
            }
    }

    private fun addProductToDeal(product: Product) {
        val productExists = dealProductAdapter.getAdapterList().find { product.id == it.product?.id }
        if (productExists == null) {
            val dealProduct = Deal.DealProduct(product, 1)
            dealProductAdapter.addItem(dealProduct)
        }
    }

    private fun configureObservers() {
        viewModel.apply {
            updatedDeal.observe(this@DealDetailActivity) {
                finish()
            }
            error.observe(this@DealDetailActivity) {
                btnSave.toVisibility(true)
                btnAddProduct.toVisibility(true)
                loader.toVisibility(false)
                Toast.makeText(this@DealDetailActivity, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configureListeners() {
        ivBackArrow.setOnClickListener { finish() }
        btnSave.setOnClickListener {
            btnSave.toVisibility(false)
            btnAddProduct.toVisibility(false)
            loader.toVisibility(true)
            viewModel.updateDeal(
                deal.id ?: "",
                deal.customer,
                dealProductAdapter.getAdapterList()
            )
        }
        btnAddProduct.setOnClickListener {
            val productIntent = Intent(this, ProductListActivity::class.java)
            productIntent.putExtra(ITEM_SELECT, true)
            activityResult.launch(productIntent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun configureLayoutByExtra() {
        deal.apply {
            tvDealTotal.text = total?.formatPriceBR() ?: "R$0,00"
            updateCustomer(customer)
            dealProductAdapter = DealProductAdapter(products!!) { _, _ ->

            }
            rvProducts.adapter = dealProductAdapter
        }
    }

    private fun updateCustomer(customer: Customer?) {
        deal.customer = customer
        tvClientName.text = customer?.name
        tvClientEmail.text = customer?.email
        tvClientPhone.text = customer?.phone
        tvAddressState.text = customer?.address?.state ?: "Não informado"
        tvAddressCity.text = customer?.address?.city ?: "Não informado"
        tvAddressStreetNumber.text =
            if (null == customer?.address?.street) "Não informado" else
                "${customer.address.street}, ${customer.address.number ?: ""}"
    }
}