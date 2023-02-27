package id.rrdev.commons.extenstion

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.view.WindowInsetsControllerCompat

//transparans statusbar
fun Activity.hideStatusBar() {
    this.window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        statusBarColor = Color.TRANSPARENT
    }
}

//update statusBar color
fun Activity.updateStatusBarColor(color: String?) {
    val window: Window = window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = Color.parseColor(color)
}

//set color statusbar in fragment
fun Activity.setColorStatusBar(context: Context) {
    when(context.checkTheme()) {
        Configuration.UI_MODE_NIGHT_NO -> {
            this.updateStatusBarColor("#FFFFFF")
            WindowInsetsControllerCompat(
                this.window,
                this.window?.decorView!!
            ).isAppearanceLightStatusBars = true
        }
        Configuration.UI_MODE_NIGHT_YES -> {
            this.updateStatusBarColor("#121212")
            WindowInsetsControllerCompat(
                this.window!!,
                this.window?.decorView!!
            ).isAppearanceLightStatusBars = false
        }
    }
}