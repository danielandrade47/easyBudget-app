package br.com.orcamentofacil.ui.product

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.orcamentofacil.ITEM_SELECT
import br.com.orcamentofacil.PRODUCT
import br.com.orcamentofacil.R
import br.com.orcamentofacil.domain.Product
import br.com.orcamentofacil.ui.utils.GenericAdapter
import br.com.orcamentofacil.ui.utils.hideKeyboard
import br.com.orcamentofacil.ui.utils.toVisibility
import kotlinx.android.synthetic.main.activity_product_list.*

class ProductListActivity : AppCompatActivity() {

    private val viewModel: ProductListViewModel by lazy {
        ProductListViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        configureObservers()
        configureListeners()
    }

    override fun onResume() {
        super.onResume()
        loader.toVisibility(true)
        rvProducts.toVisibility(false)
        viewModel.getProductsList()
    }

    private fun configureObservers() {
        viewModel.products.observe(this) { products ->
            configureAdapter(products)
        }
        viewModel.filterProducts.observe(this) { products ->
            configureAdapter(products)
        }
        viewModel.delete.observe(this) { status ->
            if (status)
                viewModel.getProductsList()
        }
    }

    private fun configureAdapter(products: List<Product>) {
        loader.toVisibility(false)
        rvProducts.toVisibility(true)
        rvProducts.adapter = GenericAdapter<Product>(R.layout.item_product,
            { product, _ ->
                val isSelect = intent.hasExtra(ITEM_SELECT)
                if (isSelect) {
                    setResult(1, Intent().putExtra(PRODUCT, product))
                    finish()
                } else {
                    startActivity(
                        Intent(this, ProductManageActivity::class.java)
                            .putExtra(PRODUCT, product)
                    )
                }
            },
            { productToRemove ->
                AlertDialog.Builder(this)
                    .setTitle("Tem certeza que quer excluir o produto ${productToRemove.name}?")
                    .setNegativeButton("Não") { _, _ -> }
                    .setPositiveButton("Sim") { _, _ ->
                        loader.toVisibility(true)
                        rvProducts.toVisibility(false)
                        viewModel.deleteProduct(
                            productToRemove.id ?: ""
                        )
                    }.show()
            }).apply {
            setupItems(products)
        }
    }

    private fun configureListeners() {
        ivBackArrow.setOnClickListener { finish() }
        fabAddProduct.setOnClickListener {
            val intent = Intent(this, ProductManageActivity::class.java)
            startActivity(intent)
        }
        etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                val query = v.text.toString()
                viewModel.searchProduct(query)
                true
            } else {
                false
            }
        }
    }
}