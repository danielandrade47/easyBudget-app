package br.com.orcamentofacil.ui.deal

import android.annotation.SuppressLint
import android.view.View
import br.com.orcamentofacil.R
import br.com.orcamentofacil.domain.Deal
import br.com.orcamentofacil.ui.utils.GenericAdapter
import br.com.orcamentofacil.ui.utils.formatPriceBR
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_deal_product.view.*

class DealProductAdapter(
    products: List<Deal.DealProduct>,
    listener: (Deal.DealProduct, View) -> Unit
) : GenericAdapter<Deal.DealProduct>(R.layout.item_deal_product, listener) {

    init {
        setupItems(products)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GenericViewHolder<Deal.DealProduct>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.apply {
            val dealProduct = items[position]
            tvProductTotal?.let {
                val productTotal = (dealProduct.quantity ?: 0) * (dealProduct.product?.price ?: 0.0)
                it.text = "Total: ${productTotal.formatPriceBR()}"
            }

            btnMore?.setOnClickListener {
                dealProduct.quantity = dealProduct.quantity?.plus(1)
                tvQuantity.text = dealProduct.quantity.toString()
                val productTotal = (dealProduct.quantity ?: 0) * (dealProduct.product?.price ?: 0.0)
                tvProductTotal.text = "Total: ${productTotal.formatPriceBR()}"
            }

            btnLess?.setOnClickListener {
                if ((dealProduct.quantity ?: 0) > 0) {
                    dealProduct.quantity = dealProduct.quantity?.minus(1)
                    tvQuantity.text = dealProduct.quantity.toString()
                    val productTotal =
                        (dealProduct.quantity ?: 0) * (dealProduct.product?.price ?: 0.0)
                    tvProductTotal.text = "Total: ${productTotal.formatPriceBR()}"
                }
            }
        }
    }
}