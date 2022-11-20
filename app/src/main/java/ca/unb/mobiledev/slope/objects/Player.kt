package ca.unb.mobiledev.slope.objects

import android.content.Context
import android.util.Log
import ca.unb.mobiledev.slope.Collision
import ca.unb.mobiledev.slope.ObjectView
import ca.unb.mobiledev.slope.Vec2
import ca.unb.mobiledev.slope.R.drawable.skifrog as texture

class Player(context: Context?, displayWidth: Int, displayHeight: Int, objId: Int)
    :ObjectView(context,displayWidth,displayHeight,objId) {

    override val defaultBitmap = texture

    var velocity = Vec2(0f,0.0f)
    val gravity = 9.81f

    var extents = Vec2(50f,50f)
    var collider = Collision.Companion.BoxCollider(position,extents)



    override fun start(){
        setBitmap()
    }

    override fun update(deltaT : Float){
        Log.i("gameThread", "Called from object id:$id")

        position += velocity*deltaT
        collider.position = position

        velocity.y += gravity
    }

}