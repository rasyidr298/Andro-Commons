package id.rrdev.commons.extenstion

import android.os.Build
import java.util.regex.Matcher
import java.util.regex.Pattern

//get id link youtubr
fun extractYoutubeVideoId(ytUrl: String?): String? {
    var vId: String? = null
    val pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
    val compiledPattern: Pattern = Pattern.compile(pattern)
    val matcher: Matcher = compiledPattern.matcher(ytUrl)
    if (matcher.find()) {
        vId = matcher.group()
    }
    return vId
}

//load tumbnail youtube
fun tumbnailYt(ytId: String): String {
    return "https://img.youtube.com/vi/${ytId}/hqdefault.jpg"
}

//get device name
fun getDeviceName(): String {
    return Build.MODEL + " || " + "SDK " + Build.VERSION.SDK_INT
}

val dateInString = getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")