package id.rrdev.commons.utils

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

object Analytics {
    private const val CLICK = "CLICK-"
    private const val SWIPE = "SWIPE-"
    private const val HOLD = "HOLD-"
    private const val INPUT = "INPUT-"
    private const val LOG = "LOG-"

    fun trackClick(event: String, param1: String = "", param2: String = "") {
        track(CLICK+event, param1, param2)
    }

    fun trackSwipe(event: String, param1: String = "", param2: String = "") {
        track(SWIPE+event, param1, param2)
    }

    fun trackHold(event: String, param1: String = "", param2: String = "") {
        track(HOLD+event, param1, param2)
    }

    fun trackInput(event: String, param1: String = "", param2: String = "") {
        track(INPUT+event, param1, param2)
    }

    fun trackLog(event: String, param1: String = "", param2: String = "") {
        track(LOG+event, param1, param2)
    }

    private fun track(event: String, param1: String, param2: String) {
        Firebase.analytics.logEvent(event) {
            param("param1", param1)
            param("param2", param2)
        }
    }
}