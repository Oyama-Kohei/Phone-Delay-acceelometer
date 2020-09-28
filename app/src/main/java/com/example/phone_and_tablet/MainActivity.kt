package com.example.phone_and_tablet

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), SensorEventListener{

    //閾値
    private val threshold: Float = 10f

    //加速度センサーのz軸を記憶する
    //加速度センサーのevent.values[2]が閾値を超えると倒れると判断する
    private var oldValue: Float = 0f

    //CameraManager型のcameraManagerを定義
    //lateinit修飾子 = null非許容だけど、初期化を遅らせたい　いちいちnullチェックするのがめんどくさいから
    private lateinit var cameraManager: CameraManager

    private var cameraID: String? = null

    //LEDがオンかオフか
    private var lightOn: Boolean = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

    }

    override fun onAccuracyChanged(event: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null) return
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){

            //閾値と傾きの差を求める
            val zDiff = Math.abs(event.values[2] - oldValue)

            if(zDiff > threshold){
                torchOn()
            }
            oldValue = event.values[2]
        }
    }

    private fun torchOn() {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()

        //getSystemServiceメソッドでセンサーマネージャーのインスタンスを取得する
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //getDefaultSensor()メソッドでSensorオブジェクト(accelerometer)を取得
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        //registerListener()メソッドでイベントリスナーを登録する
        //SENSOR_DELAY_NORMAL = 画面の向きの変更に最適な通常レート
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        //

        //getSystemServiceメソッドでセンサーマネージャーのインスタンスを取得する
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //　センサーを使い終わったらunregisterListener()メソッドでリスナーの登録を解除する
        sensorManager.unregisterListener(this)
    }

}