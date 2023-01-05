package id.rrdev.commons.model.dataBaseApp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestMethod(
    var id: Int? = 0,
    var name: String? = "",
    var isOnlineRequest: Boolean? = false
) : Parcelable