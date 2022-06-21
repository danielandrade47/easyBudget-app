package br.com.orcamentofacil.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.widget.doOnTextChanged
import com.google.gson.Gson
import com.google.gson.JsonParser
import retrofit2.Response
import java.text.DecimalFormat

fun Double.formatPriceBR(): String {
    val df = DecimalFormat("R$#,##0.00")
    val dfs = df.decimalFormatSymbols
    dfs.decimalSeparator = ','
    dfs.groupingSeparator = '.'
    df.decimalFormatSymbols = dfs

    return df.format(this)
}

fun Double.formatPrice(): String {
    val df = DecimalFormat("#,##0.00")
    val dfs = df.decimalFormatSymbols
    dfs.decimalSeparator = ','
    dfs.groupingSeparator = '.'
    df.decimalFormatSymbols = dfs

    return df.format(this)
}

inline fun <reified T : Any> newIntent(
    context: Context,
    noinline init: Intent.() -> Unit = {}
): Intent {
    val intent = Intent(context, T::class.java)
    intent.init()
    return intent
}

inline fun <reified T : Any> Activity.launchActivityForSharedElements(
    args: Bundle? = null,
    vararg pairs: Pair<View, String>,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this, init)
    args?.run {
        intent.putExtras(args)
    }

    val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
        this,
        *pairs
    )
    startActivity(intent, options.toBundle())
}

fun View.toVisibility(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

fun Activity.hideKeyboard() {
    if (this.currentFocus == null) View(this) else this.currentFocus?.run {
        val inputMethodManager =
            this@hideKeyboard.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

fun EditText.applyMask(mask: String) {
    var old = ""
    var isUpdating = false
    this.doOnTextChanged { text, _, _, _ ->
        val str = text.toString().removeSpecialCharacters()
        var maskedText = ""
        if (isUpdating) {
            old = str
            isUpdating = false
            return@doOnTextChanged
        }
        var i = 0
        for (char in mask.toCharArray()) {
            if (char != '#' && str.length > old.length) {
                maskedText += char
                continue
            }
            try {
                maskedText += str[i]
            } catch (e: Exception) {
                break
            }
            i++
        }
        isUpdating = true
        this.setText(maskedText)
        this.setSelection(maskedText.length)
    }
}

fun String.removeSpecialCharacters(): String {
    return this.replace(("[^A-Za-z0-9 ]").toRegex(), "").replace(" ", "")
}

fun <T> Response<T>.getErrorMessage(): String {
    return try {
        val errorMessage = this.errorBody()?.string()
        val jsonError = JsonParser().parse(errorMessage).asJsonObject
        jsonError.get("error").asString
    } catch (e: Exception) {
        "Erro inesperado"
    }

}