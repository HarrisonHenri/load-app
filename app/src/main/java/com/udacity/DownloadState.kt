package com.udacity


sealed class DownloadState {
    object Successful : DownloadState()
    object Failed : DownloadState()
    object Unknown : DownloadState()
}