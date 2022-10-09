package ca.unb.mobiledev.slope

import android.util.Log

open class GameObject(protected val id: Int) {
    val isActive = true

    open fun start(){

    }

    open fun update(deltaT : Long){
        Log.i("gameThread", "Called from object id:$id")
    }

    open fun render(){

    }
}