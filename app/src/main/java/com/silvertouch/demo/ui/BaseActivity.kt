package com.silvertouch.demo.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.silvertouch.demo.R
import com.silvertouch.demo.database.MyDatabase

@SuppressLint("Registered")
open class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var mBinding: T

    private lateinit var progressDialog: Dialog

    protected lateinit var mDatabase: MyDatabase

    protected fun bindView(layoutId: Int) {
        mBinding = DataBindingUtil.setContentView(this, layoutId)
        mDatabase = MyDatabase.getInstance(this)!!
    }

    protected fun initToolbar(
        toolbar: Toolbar,
        title: Int,
        txtTitle: TextView,
        showBackButton: Boolean
    ) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if (title != 0)
            txtTitle.text = getString(title)
        else
            txtTitle.visibility = View.GONE

        if (showBackButton)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    protected fun showLoader(context: Context) {
        progressDialog = Dialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(R.layout.layout_progress_dialog)
        progressDialog.window?.setDimAmount(0.75f)
        progressDialog.show()
    }

    protected fun dismissLoader() {
        if (::progressDialog.isInitialized)
            progressDialog.dismiss()
    }

    fun showNoData(
        rv: RecyclerView,
        noData: View,
        isNullOrEmpty: Boolean
    ) {
        if (isNullOrEmpty) {
            rv.visibility = View.GONE
            noData.visibility = View.VISIBLE
        } else {
            rv.visibility = View.VISIBLE
            noData.visibility = View.GONE
        }
    }

    /*protected fun isNetworkAvailable(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }

        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }*/

    protected fun displayShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun displayLongToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun getTrimString(textInputEditText: TextInputEditText): String {
        return textInputEditText.text.toString().trim()
    }

    fun getTrimString(inputString: String): String {
        return inputString.trim()
    }
}