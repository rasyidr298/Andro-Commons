package id.rrdev.commons.model.dataBaseApp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseApp(
    var token: String? = "",
    var baseFiles: String? = ""
) : Parcelable