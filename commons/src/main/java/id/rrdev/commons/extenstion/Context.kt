package id.rrdev.commons.extenstion

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import id.rrdev.commons.R
import id.rrdev.commons.utils.*

//toast
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

//get tag context
fun Context.tag(): String {
    return this.javaClass.simpleName
}

fun Context.checkTheme(): Int? {
    return this.resources.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
}

fun Context.intentToPlaystore(packageName: String) {
    try {
        this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.packagePlayStore + packageName)))
    } catch (e: ActivityNotFoundException) {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constants.packagePlayStore2 + packageName)
            )
        )
    }
}

fun Context.intentToDeveloper(name: String) {
    try {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constants.packageDeveloper + name)
            )
        )
    } catch (e: ActivityNotFoundException) {
        this.toast(e.message!!)
    }
}


fun Context.intentToWeb(url: String) {
    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    try {
        startActivity(i)
    } catch (e: ActivityNotFoundException) {
        Log.e(tag(), e.message.toString())
    }
}

fun Context.intentToYoutube(urlYtb: String) {
    val ytId = extractYoutubeVideoId(urlYtb)
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://$ytId")))
    } catch (e: ActivityNotFoundException) {
        Log.e(tag(), e.message.toString())
    }
}

fun Context.intentToGallery() {
    val intent = Intent()
    intent.let {
        it.action = Intent.ACTION_VIEW
        it.type = "image/*"
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.e(tag(), e.message.toString())
    }
}

fun Context.isAppInstalled(packageName: String): Boolean {
    return try {
        val packageManager = packageManager
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

//intent to app
fun Context.intentApp(packageName: String, onPackageIsNull: () -> Unit) {
    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)

    if (launchIntent != null) {
        try {
            startActivity(launchIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e(tag(), e.message.toString())
        }
    } else {
        onPackageIsNull.invoke()
    }
}

//setup view carousel
fun Context.setupCarousel(
    viewPager: ViewPager2,
    scrollHandler: Handler,
    scrollRunnable: Runnable,
    SCROLL_DELAY: Long
) {
    val compositePageTransformer = CompositePageTransformer()
    val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin).toFloat()
    val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset).toFloat()

    compositePageTransformer.addTransformer { page, position ->
        val myOffset: Float = position * -(2 * pageOffset + pageMargin)
        val r = 1 - kotlin.math.abs(position)
        if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                page.translationX = -myOffset
            } else {
                page.translationX = myOffset
            }
        } else {
            page.translationY = myOffset
        }
        page.scaleY = 0.85f + r * 0.15f
    }

    viewPager.apply {
        setPageTransformer(compositePageTransformer)
        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                scrollHandler.removeCallbacks(scrollRunnable)
                scrollHandler.postDelayed(scrollRunnable, SCROLL_DELAY)
            }
        })
    }
}

//intent custom tabs
fun Context.intentCustomTabs(url: String) {
    var mCustomTabsServiceConnection: CustomTabsServiceConnection? = null
    var mClient: CustomTabsClient? = null
    var mCustomTabsSession: CustomTabsSession? = null

    mCustomTabsServiceConnection = object : CustomTabsServiceConnection() {
        override fun onCustomTabsServiceConnected(
            componentName: ComponentName,
            customTabsClient: CustomTabsClient
        ) {
            //Pre-warming
            mClient = customTabsClient
            mClient?.warmup(0L)
            mCustomTabsSession = mClient?.newSession(null)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mClient = null
        }
    }

    CustomTabsClient.bindCustomTabsService(
        this, Constants.packageChrome,
        mCustomTabsServiceConnection as CustomTabsServiceConnection
    )

    val customTabsIntent = CustomTabsIntent.Builder(mCustomTabsSession)
        .setShowTitle(true)
//        .setUrlBarHidingEnabled(true)
        .build()

    customTabsIntent.launchUrl(this, Uri.parse(url))
}

fun Context.isPermissionGranted(permission: String): Boolean =
    ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

//check if network available
fun Context.isNetworkAvailable(): Pair<Boolean, String> {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return Pair(true, "")
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return Pair(true, "")
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return Pair(true, "")
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return Pair(true, "")
        }
    }
    return Pair(false, "No Internet Connection")
}

//intent to Dial Up
fun Context.actionToView(uri: String) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse(uri)
    startActivity(i)
}