package ca.unb.mobiledev.slope.objects

import android.content.Context
import android.util.Log
import ca.unb.mobiledev.slope.Collision
import ca.unb.mobiledev.slope.GameActivity
import ca.unb.mobiledev.slope.ObjectView
import ca.unb.mobiledev.slope.Vec2
import ca.unb.mobiledev.slope.R.drawable.skifrog as texture

class Player(context: Context?, displayWidth: Int, displayHeight: Int, objId: Int,val activity: GameActivity)
    :ObjectView(context,displayWidth,displayHeight,objId) {

    override val defaultBitmap = texture

    var velocity = Vec2(0f,0.0f)
    val gravity = 9.81f

    var extents = Vec2(64f,64f)
    var collider = Collision.BoxCollider(position,extents)

    var hitObstacle = false

    override fun start(){
        setBitmap()
    }

    override fun update(deltaT : Float, objMap:Map<String,ObjectView>){

        val obstacle:Obstacle = objMap["Obstacle1"] as Obstacle
        if(obstacle != null){
            val collide = collider.collideBox(obstacle.collider)
            //Log.i("Player",collide.toString())
            if(collide){
                //gameOver
                hitObstacle = true //UI is checking for this, and calls gameover once it sees it
            }
            else{
                position += velocity*deltaT //physics
                velocity.y += gravity
            }
        }
        collider.position = position


    }

}