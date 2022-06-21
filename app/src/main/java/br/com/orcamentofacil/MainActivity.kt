package br.com.orcamentofacil

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.orcamentofacil.ui.customer.CustomerListActivity
import br.com.orcamentofacil.ui.deal.DealListActivity
import br.com.orcamentofacil.ui.product.ProductListActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cvClients.setOnClickListener {
            startActivity(Intent(this, CustomerListActivity::class.java))
        }
        cvProducts.setOnClickListener {
            startActivity(Intent(this, ProductListActivity::class.java))
        }
        cvDeals.setOnClickListener {
            startActivity(Intent(this, DealListActivity::class.java))
        }
    }
}