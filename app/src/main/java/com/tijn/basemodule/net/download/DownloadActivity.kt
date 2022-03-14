package com.tijn.basemodule.net.download

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tijn.basemodule.R
import com.tijn.basemodule.R.layout.activity_download
import com.tijn.netlibrary.download.core.AppDownload
import com.tijn.netlibrary.download.core.DownloadInfo


class DownloadActivity : AppCompatActivity(), View.OnClickListener {
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progress_bar) }
    private val tbStatus: TextView by lazy { findViewById(R.id.tb_status) }

    private val downloadScope = AppDownload.request(
        url = "https://imtt.dd.qq.com/16891/apk/C10B2237586138DF3909E47B981B73F9.apk"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_download)
        findViewById<TextView>(R.id.tb_status).setOnClickListener(this)
        findViewById<TextView>(R.id.tb_cancel).setOnClickListener(this)
        downloadScope?.observer(this,
            { t ->
                refreshView(t)
                Log.e("Download", t.toString())
            })


    }


    private fun refreshView(downloadInfo: DownloadInfo?) {

        downloadInfo?.let {
            val progressPercent =
                (it.currentLength.toFloat() / it.contentLength * 100).toInt()
            progressBar.progress = progressPercent
            when (it.status) {
                DownloadInfo.LOADING -> {
                    tbStatus.text = String.format("%s%s", progressPercent, "%")
                    progressBar.visibility = View.VISIBLE
                    tbStatus.setTextColor(Color.argb(0xFF, 0x11, 0xB0, 0x77))
                    tbStatus.setBackgroundResource(R.drawable.bt_background_normal)
                }
                DownloadInfo.PAUSE -> {
                    tbStatus.text = "继续"
                    progressBar.visibility = View.VISIBLE
                    tbStatus.setTextColor(Color.argb(0xFF, 0x11, 0xB0, 0x77))
                    tbStatus.setBackgroundResource(R.drawable.bt_background_normal)
                }
                DownloadInfo.WAITING -> {
                    tbStatus.text = "等待"
                    progressBar.visibility = View.VISIBLE
                    tbStatus.setTextColor(Color.argb(0xFF, 0x11, 0xB0, 0x77))
                    tbStatus.setBackgroundResource(R.drawable.bt_background_normal)
                }
                DownloadInfo.ERROR -> {
                    tbStatus.text = "重试"
                    progressBar.visibility = View.GONE
                    tbStatus.setTextColor(Color.RED)
                    tbStatus.setBackgroundResource(R.drawable.bt_background_error)
                }
                DownloadInfo.DONE -> {
                    tbStatus.text = "完成"
                    tbStatus.setTextColor(Color.DKGRAY)
                    progressBar.visibility = View.GONE
                    tbStatus.setBackgroundResource(R.drawable.bt_background_done)
                }
                else -> {
                    tbStatus.text = "下载"
                    progressBar.visibility = View.GONE
                    tbStatus.setTextColor(Color.argb(0xFF, 0x11, 0xB0, 0x77))
                    tbStatus.setBackgroundResource(R.drawable.bt_background_normal)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tb_status -> {
                val downloadInfo = downloadScope?.downloadInfo()
                when (downloadInfo?.status) {
                    DownloadInfo.ERROR, DownloadInfo.PAUSE, DownloadInfo.NONE -> downloadScope?.start()
                    DownloadInfo.LOADING -> downloadScope?.pause()
                }
            }
            R.id.tb_cancel -> {
                downloadScope?.remove()
            }
        }
    }

}