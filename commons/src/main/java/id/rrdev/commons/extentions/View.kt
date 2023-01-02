package id.rrdev.commons.extentions

import android.view.View
import com.google.android.material.snackbar.Snackbar


//snacbar
fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("OK") {
            snackbar.dismiss()
        }
    }.show()
}

//show view
fun View.show() {
    visibility = View.VISIBLE
}

//hide view
fun View.hide() {
    visibility = View.GONE
}
