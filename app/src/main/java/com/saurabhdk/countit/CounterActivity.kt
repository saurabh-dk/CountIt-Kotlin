package com.saurabhdk.countit

import android.content.Context
import android.os.*
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView

class CounterActivity : AppCompatActivity() {

    var targetCount: Int = 0
    var count: Int = 0
    lateinit var countText: AppCompatTextView
    lateinit var backButton: AppCompatImageButton
    private var doubleBackToExitPressedOnce = false
    private var mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        targetCount = intent.getIntExtra("COUNT", 0)

        countText = findViewById(R.id.countText)
        backButton = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                ++count
                updateText()
                if (targetCount > 0) {
                    when {
                        count == targetCount - 2 -> vibrateNow()
                        count == targetCount - 1 -> vibrateNow()
                        count == targetCount -> vibrateNow(750)
                    }
                }
                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (count > 0) --count
                updateText()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun updateText() {
        countText.text = count.toString()
    }

    // https://stackoverflow.com/a/48250684/8744668
    private fun vibrateNow(milliseconds: Long = 250) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        milliseconds,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                // Vibrate method for below API Level 26
                vibrator.vibrate(milliseconds)
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    // https://developer.android.com/training/system-ui/immersive
    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private val runnable = Runnable {
        doubleBackToExitPressedOnce = false;
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        mHandler.postDelayed(runnable, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(runnable)
    }

}