package com.udacity.utils


sealed class DownloadState {
    object Successful : DownloadState()
    object Failed : DownloadState()
    object Unknown : DownloadState()
}

fun DownloadState.stringify() = when(this) {
        DownloadState.Successful -> "Success"
        DownloadState.Failed -> "Fail"
        else -> "Unknown"
    }
