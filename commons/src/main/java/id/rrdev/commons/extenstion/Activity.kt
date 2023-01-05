package id.rrdev.commons.extenstion

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager

//transparans statusbar
fun Activity.hideStatusBar() {
    this.window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        statusBarColor = Color.TRANSPARENT
    }
}