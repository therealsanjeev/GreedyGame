package com.therealsanjeev.greedygame.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.therealsanjeev.greedygame.R
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow


fun RecyclerView.init(
    mContext: Context,
    mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    decoration: RecyclerView.ItemDecoration? = null,
    isDecorated: Boolean = false,
    enableNestedScrolling: Boolean = false,
    isFixedSize: Boolean = true,
    manager: RecyclerView.LayoutManager = LinearLayoutManager(mContext)
) {
    apply {
        setHasFixedSize(isFixedSize)
        if (!enableNestedScrolling)
            ViewCompat.setNestedScrollingEnabled(this, enableNestedScrolling)
        layoutManager = manager
        if (decoration == null) {
            if (isDecorated)
                addItemDecoration(DividerItemDecoration(mContext, RecyclerView.VERTICAL))
        } else
            addItemDecoration(decoration)
        adapter = mAdapter
    }
}

fun RecyclerView.init(
    mContext: Context,
    decoration: RecyclerView.ItemDecoration? = null,
    isDecorated: Boolean = false,
    enableNestedScrolling: Boolean = false,
    isFixedSize: Boolean = true,
    manager: RecyclerView.LayoutManager = LinearLayoutManager(mContext)
) {
    apply {
        setHasFixedSize(isFixedSize)
        if (!enableNestedScrolling)
            ViewCompat.setNestedScrollingEnabled(this, enableNestedScrolling)
        layoutManager = manager
        if (decoration == null) {
            if (isDecorated)
                addItemDecoration(DividerItemDecoration(mContext, RecyclerView.VERTICAL))
        } else
            addItemDecoration(decoration)
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun EditText.getStringInput(): String {
    return text?.trim()?.toString() ?: ""
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun EditText.onChange(textChanged: ((String) -> Unit)) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textChanged.invoke(s.toString())
        }
    })
}


fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    message?.let {
        Toast.makeText(this, it, length).show()
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
    } else {
        try {
            val activeNetworkInfo = manager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        } catch (e: Exception) {
            e.showLog()
        }
    }
    return false
}

/**
 * Extension method to run block of code after specific Delay.
 */
fun runDelayed(delay: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS, action: () -> Unit) {
    Handler().postDelayed(action, timeUnit.toMillis(delay))
}


fun Double.roundOffDecimal(): Double {
    return DecimalFormat("#.###").apply {
        roundingMode = RoundingMode.FLOOR
    }.format(this).toDouble()
}


fun TextView.setDrawableColor(color: Int) {
    for (drawable in this.compoundDrawablesRelative) {
        drawable?.mutate()
        drawable?.colorFilter = PorterDuffColorFilter(
            color, PorterDuff.Mode.SRC_IN
        )
    }
}

fun ImageView.setImage(
    imageUrl: String?,
    placeHolder: Int = R.drawable.imagebg
) {
    try {
        Glide.with(this).applyDefaultRequestOptions(
            RequestOptions().placeholder(placeHolder).error(placeHolder)
        ).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(this)
    } catch (ex: Exception) {
        ex.showLog()
    }
}

fun Double.currencyLocale(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "in"))
    return formatter.format(this)
}

fun String.parseCommaSeparatedCurrency(): Number? {
    return NumberFormat.getCurrencyInstance(Locale("en", "in")).parse(this)
}

fun Long.formatValue(): String {
    val array = arrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
    val value = floor(log10(toDouble())).toInt()
    val base = value / 3
    return if (value >= 3 && base < array.size) {
        DecimalFormat("#0.0").format(this / 10.0.pow((base * 3).toDouble())) + array[base]
    } else {
        DecimalFormat("#,##0").format(this)
    }
}



@SuppressLint("ClickableViewAccessibility")
fun TextView.addOnDrawableClickListener(onClicked: () -> Unit) {
    setOnTouchListener { view, motionEvent ->
        var hasConsumed = false
        if (view is TextView) {
            if (motionEvent.x >= view.width - view.totalPaddingRight) {
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    onClicked()
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}
fun AppCompatActivity.isPermissionGranted(permissions: Array<String>): Boolean {
    var grantedPermissionsCount = 0
    permissions.forEach { permission ->
        if (ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        )
            grantedPermissionsCount++
    }
    return grantedPermissionsCount == permissions.size
}