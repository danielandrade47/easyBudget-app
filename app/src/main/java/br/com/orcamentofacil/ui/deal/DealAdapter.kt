package br.com.orcamentofacil.ui.deal

import android.annotation.SuppressLint
import android.view.View
import br.com.orcamentofacil.R
import br.com.orcamentofacil.domain.Deal
import br.com.orcamentofacil.ui.utils.GenericAdapter
import br.com.orcamentofacil.ui.utils.formatPriceBR
import kotlinx.android.synthetic.main.item_deal.view.*

class DealAdapter(
    listener: (Deal, View) -> Unit,
    delete: (Deal) -> Unit
) : GenericAdapter<Deal>(R.layout.item_deal, listener, delete) {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GenericViewHolder<Deal>, position: Int) {
        super.onBindViewHolder(holder, position)
        val deal = items[position]

        holder.itemView.apply {
            val itemCount = deal.products?.map { it.quantity ?: 0 }?.sum() ?: 0
            tvItems.text =
                context.resources.getQuantityString(R.plurals.product_plural, itemCount, itemCount)
            tvAmount.text = deal.total?.formatPriceBR() ?: "R$0,00"
        }
    }
}