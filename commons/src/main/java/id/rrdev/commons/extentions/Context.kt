package id.rrdev.commons.extentions

import android.content.Context
import android.widget.Toast

//toast
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}