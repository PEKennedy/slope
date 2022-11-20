package ca.unb.mobiledev.slope.old

import android.graphics.Canvas
import ca.unb.mobiledev.slope.Vec2

open class GameObject(protected val id: Int) {
    val isActive = true

    open fun start(){

    }

    open fun update(deltaT : Long){
        //Log.i("gameThread", "Called from object id:$id")
    }

    open fun render(canvas: Canvas, screenPos: Vec2){
        //Log.i("renderThread", "Called render obj id:$id")
    }

    protected fun WorldToScreenCoordinates(pos: Vec2, screenPos: Vec2): Vec2 {
        return pos - screenPos
    }
}