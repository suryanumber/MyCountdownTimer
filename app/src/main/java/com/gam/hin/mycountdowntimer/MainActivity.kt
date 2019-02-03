package com.gam.hin.mycountdowntimer

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        var isRunning = false

        override fun onTick(millisUntilFinished: Long) {
            val minute = millisUntilFinished / 1000L / 60L
            val second = millisUntilFinished / 1000L % 60L
            timerText.text = "%1$02d:%2$02d".format(minute, second)
        }

        override fun onFinish() {
            timerText.text = "end"

            val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(2000)
            ApplicationData.soundPool.play(ApplicationData.soundResId, 1.0f, 100f, 0, 0, 1.0f)

            // タイマーが終了したら
            ApplicationData.isTimerCreate = true
            playStop.setImageResource(
                R.drawable.ic_play_circle_filled_black_24dp
            )
            //ApplicationData.soundResId = ApplicationData.soundPool.load(this@MainActivity, R.raw.bellsound, 1)
            ApplicationData.soundResId = ApplicationData.soundPool.load(this@MainActivity, R.raw.shock5, 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerText.text = "00:00"
        var timer = MyCountDownTimer( 0, 100)
        playStop.setOnClickListener {
            if (ApplicationData.isTimerCreate){
                var millisInFuture = ((ApplicationData.time_minute * 60L) + ApplicationData.time_secound) * 1000L
                timer = MyCountDownTimer(millisInFuture, 100)
            }
            when (timer.isRunning){
                true -> timer.apply{
                    isRunning = false
                    cancel()
                    playStop.setImageResource(
                        R.drawable.ic_play_circle_filled_black_24dp
                    )
                    timerText.text = "%1$02d:%2$02d".format(ApplicationData.time_minute, ApplicationData.time_secound)
                }
                false -> timer.apply {
                    isRunning = true
                    ApplicationData.isTimerCreate = false
                    start()
                    playStop.setImageResource(
                        R.drawable.ic_stop_black_24dp
                    )
                }
            }
        }

        // 時間を設定ボタン押下時の処理
        time_select_button.setOnClickListener {
            val newFragment = DurationPicker()
            newFragment.show(fragmentManager, "timePicker")
        }
    }

    override fun onResume(){
        super.onResume()
        ApplicationData.soundPool = SoundPool(2, AudioManager.STREAM_ALARM, 0)
        //ApplicationData.soundResId = ApplicationData.soundPool.load(this, R.raw.bellsound, 1)
        ApplicationData.soundResId = ApplicationData.soundPool.load(this, R.raw.shock5, 1)
    }

    override fun onPause(){
        super.onPause()
        ApplicationData.soundPool.release()
    }
}


