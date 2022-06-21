package br.com.orcamentofacil.ui.product

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.orcamentofacil.PRODUCT
import br.com.orcamentofacil.R
import br.com.orcamentofacil.domain.Product
import br.com.orcamentofacil.ui.utils.formatPrice
import br.com.orcamentofacil.ui.utils.toVisibility
import kotlinx.android.synthetic.main.activity_product_manage.*

class ProductManageActivity : AppCompatActivity() {

    private val viewModel: ProductManageViewModel by lazy {
        ProductManageViewModel()
    }

    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_manage)

        if (intent.hasExtra(PRODUCT))
            configureLayoutByExtras()

        configureObservers()
        configureListeners()
    }

    private fun configureLayoutByExtras() {
        product = intent.extras?.get(PRODUCT) as Product
        product?.apply {
            etName.setText(name ?: "")
            etBrand.setText(brand ?: "")
            etDescription.setText(description ?: "")
            etPrice.setText(price?.formatPrice()?.replace(",", ".") ?: "")
        }
    }

    private fun configureObservers() {
        viewModel.newProduct.observe(this) {
            btnSave.toVisibility(true)
            loader.toVisibility(false)
            finish()
        }
        viewModel.error.observe(this) {
            btnSave.toVisibility(true)
            loader.toVisibility(false)
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun configureListeners() {
        ivBackArrow.setOnClickListener { finish() }
        btnSave.setOnClickListener {
            if (product == null)
                createProduct()
            else
                updateProduct()
        }
    }

    private fun createProduct() {
        btnSave.toVisibility(false)
        loader.toVisibility(true)
        val newProduct = Product(
            name = etName.text.toString(),
            brand = etBrand.text.toString(),
            description = etDescription.text.toString(),
            price = if (etPrice.text.isNullOrEmpty()) 0.0 else etPrice.text.toString().toDouble()
        )
        viewModel.createProduct(newProduct)
    }

    private fun updateProduct() {
        btnSave.toVisibility(false)
        loader.toVisibility(true)
        product = Product(
            id = product?.id ?: "",
            name = etName.text.toString(),
            brand = etBrand.text.toString(),
            description = etDescription.text.toString(),
            price = etPrice.text.toString().toDouble()
        )
        product?.let { viewModel.updateProduct(it) }
    }
}