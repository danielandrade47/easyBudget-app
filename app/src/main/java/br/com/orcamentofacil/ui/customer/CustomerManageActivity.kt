package br.com.orcamentofacil.ui.customer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.orcamentofacil.CUSTOMER
import br.com.orcamentofacil.R
import br.com.orcamentofacil.domain.Address
import br.com.orcamentofacil.domain.Customer
import br.com.orcamentofacil.ui.utils.applyMask
import br.com.orcamentofacil.ui.utils.toVisibility
import kotlinx.android.synthetic.main.activity_customer_manage.*

class CustomerManageActivity : AppCompatActivity() {

    private val viewModel: CustomerManageViewModel by lazy {
        CustomerManageViewModel()
    }

    private var customer: Customer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_manage)

        if (intent.hasExtra(CUSTOMER))
            configureLayoutByExtras()

        configureObservers()
        configureListeners()
    }

    private fun configureLayoutByExtras() {
        customer = intent.extras?.get(CUSTOMER) as Customer
        customer?.apply {
            etName.setText(name ?: "")
            etEmail.setText(email ?: "")
            etPhone.setText(phone ?: "")
            address?.apply {
                etState.setText(state ?: "")
                etCity.setText(city ?: "")
                etStreet.setText(street ?: "")
                etNumber.setText(number ?: "")
            }
        }
    }

    private fun configureObservers() {
        viewModel.newCustomer.observe(this) {
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
        etPhone.applyMask("(##)#####-####")
        ivBackArrow.setOnClickListener { finish() }
        btnSave.setOnClickListener {
            if (customer == null)
                createCustomer()
            else
                updateCustomer()
        }
    }

    private fun createCustomer() {
        btnSave.toVisibility(false)
        loader.toVisibility(true)
        val newCustomer = Customer(
            name = etName.text.toString(),
            email = etEmail.text.toString(),
            phone = etPhone.text.toString(),
            address = Address(
                state = etState.text.toString(),
                city = etCity.text.toString(),
                street = etStreet.text.toString(),
                number = etNumber.text.toString()
            )
        )
        viewModel.createCustomer(newCustomer)
    }

    private fun updateCustomer() {
        btnSave.toVisibility(false)
        loader.toVisibility(true)
        customer = Customer(
            id = customer?.id,
            name = etName.text.toString(),
            email = etEmail.text.toString(),
            phone = etPhone.text.toString(),
            address = Address(
                state = etState.text.toString(),
                city = etCity.text.toString(),
                street = etStreet.text.toString(),
                number = etNumber.text.toString()
            )
        )
        customer?.let { viewModel.updateCustomer(it) }
    }

}