package com.udacity.utils


sealed class DownloadState {
    object Successful : DownloadState()
    object Failed : DownloadState()
    object Unknown : DownloadState()
}