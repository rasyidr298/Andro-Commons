package id.rrdev.commons.utils.state

sealed class DownloadState<out R> {
    data class Progress<out T>(val progress: T) : DownloadState<T>()
    data class Error(val errorMessage: String) : DownloadState<Nothing>()
    object Success : DownloadState<Nothing>()
}