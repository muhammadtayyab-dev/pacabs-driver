package com.techlogix.pacabs_driver.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.techlogix.pacabs_driver.PacabDriver
import com.techlogix.pacabs_driver.models.GenericResponseModel
import com.techlogix.pacaps.dialogs.AlertDialogCallback
import com.techlogix.pacaps.dialogs.ErrorSuccessDialog

abstract class BaseActivity : AppCompatActivity() {

    var pacap: PacabDriver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pacap = application as PacabDriver?;
        pacap?.setActivity(this)
    }


    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    open fun openActivity(calledActivity: Class<*>?, @Nullable bundle: Bundle?) {
        val myIntent = Intent(this, calledActivity)
        if (bundle != null) myIntent.putExtras(bundle)
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        this.startActivity(myIntent)
    }

    fun closeKeyboard() {
        val inputManager =
            applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager != null && this.currentFocus != null) inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }

    open fun onDismiss(param: Any?) {

        if (param != null && param is GenericResponseModel<*>) {

        }
    } /*else if (param!!.equals("rooted")) {
            finishAffinity()
        }*/

    fun showErrorDialog(title: String, msg: String, response: GenericResponseModel<*>?) {

        ErrorSuccessDialog(this, title, msg, object : AlertDialogCallback {
            override fun onDissmiss() {
                onDismiss(response)
            }
        }).show()
    }

    fun activityFullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }


}





