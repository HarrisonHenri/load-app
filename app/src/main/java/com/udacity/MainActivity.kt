package com.udacity

import android.app.DownloadManager
import android.app.DownloadManager.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.udacity.ButtonState.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var downloadObserver: ContentObserver
    private var fileName = ""
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }
    private lateinit var downloadState: DownloadState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        loading_button.setOnClickListener {
            loadingButtonListener()
        }
    }

    private fun loadingButtonListener() = when(radio_list.checkedRadioButtonId) {
            View.NO_ID ->
                Toast.makeText(
                        this,
                        getString(R.string.empty_radio_button),
                        Toast.LENGTH_SHORT
                ).show()
            else -> {
                fileName =
                        findViewById<RadioButton>(radio_list.checkedRadioButtonId)
                                .text.toString()
                download()
            }
        }

    private fun download() {
        val request =
                Request(Uri.parse(URL))
                        .setTitle(getString(R.string.app_name))
                        .setDescription(getString(R.string.app_description))
                        .setRequiresCharging(false)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        registerDownloadObserver(downloadManager)
    }

    private fun registerDownloadObserver(downloadManager: DownloadManager) {
       downloadObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                downloadStatus(downloadManager)
            }
        }

        contentResolver.registerContentObserver(
                "content://downloads/".toUri(),
                true,
                downloadObserver
        )
    }

    private fun downloadStatus(downloadManager: DownloadManager) {
        downloadManager.query(Query().setFilterById(downloadID)).also {
            with(it) {
                if (this != null && moveToFirst()) {
                    when (getInt(getColumnIndex(COLUMN_STATUS))) {
                        STATUS_RUNNING -> {
                            loading_button.changeButtonState(Loading)
                        }
                        STATUS_FAILED -> {
                            loading_button.changeButtonState(Completed)
                            changeDownloadState(DownloadState.Failed)
                        }
                        STATUS_SUCCESSFUL -> {
                            loading_button.changeButtonState(Completed)
                            changeDownloadState(DownloadState.Successful)
                        }
                        else -> changeDownloadState(DownloadState.Unknown)
                    }
                }
            }
        }
    }

    private fun changeDownloadState(state: DownloadState) {
        if (!::downloadState.isInitialized || state != downloadState) {
            downloadState = state
        }
    }

    companion object {
        private const val URL =
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }
}