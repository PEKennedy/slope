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

    override fun start(objMap:Map<String,ObjectView>){
        setBitmap()
        hitObstacle = false
        //500,200 vs 200,300  //450
        position += Vec2(100f,200f)
    }

    override fun update(deltaT : Float, objMap:Map<String,ObjectView>){

        val obstacle:Obstacle = objMap["Obstacle1"] as Obstacle
        val terrain:Terrain = objMap["Terrain"] as Terrain

        //position = terrain.playerCollide(position)

        if(obstacle != null){
            val collide = collider.collideBox(obstacle.collider)
            //Log.i("Player",collide.toString())
            if(collide){
                //gameOver
                hitObstacle = true //UI is checking for this, and calls gameover once it sees it
                velocity.y = 0F
            }
            else{
                position += velocity*deltaT //physics
                velocity.y += gravity
                velocity.x = 5f/deltaT
            }
        }

        if(terrain != null){
            //val offsetPos = position //+ Vec2(32f,64f)
            val collided = terrain.checkPlayerCollide(position)

            //would probably want to lerp this
            //TODO: Seems rotation gets messed up based on how far to the
            //right on the screen the player is??
            objRotation = terrain.getPlayerAngle(position)

            if(collided){
                //Log.i("COLLISION",collided.toString())
                //position = terrain.playerCollide(position) - Vec2(0f,10f)
                position = terrain.playerCollide(position)
                velocity.y = 0F
            }

            val segmentNum = terrain.getSegmentNum(position)
            if(segmentNum > 3){
                val displaceVec = Vec2(-250f*2f,0f)
               // position += displaceVec
               // terrain.offset += displaceVec
            }

        }


        collider.position = position



    }

}