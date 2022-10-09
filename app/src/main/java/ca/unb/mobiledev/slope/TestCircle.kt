package ca.unb.mobiledev.slope

import android.util.Log
import kotlin.math.sin

class TestCircle(id: Int) : GameObject(id) {
    protected var posX = 0f
    protected var posY = 10f
    override fun update(deltaT : Long){
        Log.i("gameThread", "Called from Circle id:$id")
        posX = (10f + sin(deltaT.toDouble())).toFloat()
    }
}