package com.example.digitalframe_part2_chapter05

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()

    private var currentPosition = 0

    private var timer: Timer? = null

    private val photoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.photoImageView)
    }

    private val backgroundPhotoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.backgroundPhotoImageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)

        getPhotoUriFromIntent()
    }

    private fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize", 0)
        for (i in 0..size) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        timer = timer(period = 5 * 1000) {
            runOnUiThread { // 타이머는 메인 스레드가 아님
                Log.d("PhotoFrame", "Timer")
                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1

                backgroundPhotoImageView.setImageURI(photoList[current])

                photoImageView.alpha = 0f
                photoImageView.setImageURI(photoList[next])
                photoImageView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next
            }
        }
    }

    // 어플 종료 및 액티비티를 사용하지 않을 때에는 백그라운드에서 타이머가 실행되지 않도록 함
    override fun onStop() {
        super.onStop()

        Log.d("PhotoFrame", "onStop")

        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()

        Log.d("PhotoFrame", "onStart")

        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("PhotoFrame", "onDestroy")

        timer?.cancel()
    }
}