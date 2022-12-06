package ca.unb.mobiledev.slope.objects

import android.content.Context
import ca.unb.mobiledev.slope.Collision
import ca.unb.mobiledev.slope.ObjectView
import ca.unb.mobiledev.slope.R
import ca.unb.mobiledev.slope.Vec2

// Player obstacle.
class Obstacle(context: Context?, displayWidth: Int, displayHeight: Int, objId: Int)
    :ObjectView(context,displayWidth,displayHeight,objId) {

    override val defaultBitmap = R.drawable.crate//texture

    var extents = Vec2(32f,32f)
    var collider = Collision.BoxCollider(position,extents)

    var obType = 0

    // Initialize the obstacle, including type and position
    override fun start(objMap:Map<String,ObjectView>){

        setObstacleType(obType)
        collider.position = position
    }

    // Unused default update method
    override fun update(deltaT : Float, objMap:Map<String,ObjectView>){

    }

    // Set the type of obstacle it is
    fun setObstacleType(type:Int=0){
        if(type == 1){ //snowman
            val texture = R.drawable.snowman1
            extents = Vec2(64f,64f)
            collider = Collision.BoxCollider(position,Vec2(64f,64f))
            setBitmap(texture)
        }
        else if(type==2){
            val texture = R.drawable.firtree1
            extents = Vec2(32f,64f)
            collider = Collision.BoxCollider(position,Vec2(32f,64f))
            setBitmap(texture)
        }
        else{ //crate
            extents = Vec2(32f,32f)
            setBitmap()
        }
    }
}