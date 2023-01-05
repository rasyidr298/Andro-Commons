package id.rrdev.commons.extenstion

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout


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

//clear input layout
fun List<View>.clearErrorInputLayout() {
    forEach { vi ->
        if (vi is TextInputLayout) {
            vi.isErrorEnabled = false
        }
    }
}