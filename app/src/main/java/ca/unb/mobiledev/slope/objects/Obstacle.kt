package ca.unb.mobiledev.slope.objects

import android.content.Context
import android.util.Log
import ca.unb.mobiledev.slope.Collision
import ca.unb.mobiledev.slope.ObjectView
import ca.unb.mobiledev.slope.Vec2
import ca.unb.mobiledev.slope.R.drawable.crate as texture

class Obstacle(context: Context?, displayWidth: Int, displayHeight: Int, objId: Int)
    :ObjectView(context,displayWidth,displayHeight,objId) {

    override val defaultBitmap = texture

    var extents = Vec2(32f,32f)
    var collider = Collision.BoxCollider(position,extents)



    override fun start(){
        setBitmap()
        position += Vec2(200f,300f)
        collider.position = position-Vec2(0f,100f)
    }

    override fun update(deltaT : Float, objMap:Map<String,ObjectView>){

    }

}