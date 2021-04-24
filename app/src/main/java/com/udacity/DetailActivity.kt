package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var fileName:String
    private lateinit var downloadState:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        setDownloadStatus()
        if (downloadState != "Success") {
            setDownloadStateErrorColor()
        }
        finish_button.setOnClickListener {
            finish()
        }
    }

    private fun setDownloadStatus() {
        fileName = intent.getStringExtra("fileName").toString()
        downloadState = intent.getStringExtra("downloadState").toString()
        file_name_text.text = fileName
        download_state_text.text = downloadState
    }

    private fun setDownloadStateErrorColor() {
        ContextCompat.getColor(this, R.color.donwloadError)
                .also { color ->
                    download_state_text.setTextColor(color)
                }
    }
}
