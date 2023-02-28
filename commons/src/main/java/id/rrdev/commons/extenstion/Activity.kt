package id.rrdev.commons.extenstion

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.app.ActivityCompat
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


//to setting apps
fun Activity.toSetting() {
    startActivity(
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", this.packageName, null),
        ),
    )
}

//check permission is rationale
fun Activity.isPermissionRational(permission: String): Boolean =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

fun Activity.requestPermission(
    permission: List<String>,
    requestCode: Int
) {
    ActivityCompat.requestPermissions(
        this, permission.toTypedArray(),
        requestCode
    )
}