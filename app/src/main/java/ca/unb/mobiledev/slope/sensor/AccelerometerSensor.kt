package ca.unb.mobiledev.slope.sensor

import android.content.Context.SENSOR_SERVICE
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class AccelerometerSensor : SensorEventListener{
    private var mSensorManager : SensorManager? = null
    private var mAccelerometer : Sensor? = null

    // Arrays for storing filtered values //TODO figure out if needed
    public val mGravity = FloatArray(3)
    public val mAccel = FloatArray(3)

    private var mLastUpdate : Long = 0
    private var mTilt : Float = 0F

    lateinit var mContext: Context

    constructor(sensorManager: SensorManager, context: Context){
        mSensorManager = sensorManager
        mContext = context

        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mLastUpdate = System.currentTimeMillis()

//        mSensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
//        if (null != mSensorManager) {
//            // Get reference to Accelerometer
//            mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
//            mLastUpdate = System.currentTimeMillis()
//        }
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
            mTilt = event.values[1]
        }
    }

    public fun getTilt(): Float {
        return mTilt
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {


    }
}