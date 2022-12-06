package ca.unb.mobiledev.slope.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class AccelerometerSensor(sensorManager: SensorManager) : SensorEventListener{
    private var mSensorManager : SensorManager? = sensorManager
    private var mAccelerometer : Sensor? = null

    private var mLastUpdate : Long = 0
    private var mTilt : Float = 0F

    init {
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mLastUpdate = System.currentTimeMillis()
    }

    public fun register() {
        mSensorManager!!.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    public fun unregister() {
        mSensorManager!!.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val actualTime = System.currentTimeMillis()
            if (actualTime - mLastUpdate > 500) {
                mLastUpdate = actualTime
            }
            mTilt = event.values[1] * 2 // Double value to make tilt more sensitive
        }
    }

    public fun getTilt(): Float {
        return mTilt
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}