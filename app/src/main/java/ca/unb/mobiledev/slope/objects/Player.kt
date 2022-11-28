package ca.unb.mobiledev.slope.objects

import android.content.Context
import android.util.Log
import ca.unb.mobiledev.slope.Collision
import ca.unb.mobiledev.slope.GameActivity
import ca.unb.mobiledev.slope.ObjectView
import ca.unb.mobiledev.slope.Vec2
import kotlin.random.Random
import ca.unb.mobiledev.slope.R.drawable.skifrog as texture

class Player(context: Context?, displayWidth: Int, displayHeight: Int, objId: Int,val activity: GameActivity,
    val obstacles: MutableList<Obstacle>)
    :ObjectView(context,displayWidth,displayHeight,objId) {

    override val defaultBitmap = texture

    var velocity = Vec2(0f,0.0f)
    val gravity = 18f//9.81f

    var extents = Vec2(64f,64f)
    var collider = Collision.BoxCollider(position,extents)

    var hitObstacle = false

    var playerSpeed = 10f

    var grounded = false

    override fun start(objMap:Map<String,ObjectView>){
        setBitmap()
        hitObstacle = false
        //500,200 vs 200,300  //450
        position += Vec2(100f,200f)
    }

    override fun update(deltaT : Float, objMap:Map<String,ObjectView>){

        //val obstacle:Obstacle = objMap["Obstacle1"] as Obstacle
        val terrain:Terrain = objMap["Terrain"] as Terrain

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
                grounded = true
            }

            obstacles.forEach {
                val collide = collider.collideBox(it.collider)
                if(collide){
                    //hitObstacle = true
                    velocity.y = 0F
                }
            }

            //physics & controls
            if(activity.wasTouched && grounded){
                Log.i("PLAYER","JUMP")
                velocity.y -= 800f
                activity.wasTouched = false
                grounded = false
            }

            position += velocity*deltaT
            velocity.y += gravity
            velocity.x = playerSpeed/deltaT

            //Terrain Generation logic
            val segmentNum = terrain.getSegmentNum(position)
            val finalSegment = terrain.getNumSegments()
            if(finalSegment - segmentNum < 8){
                //val displaceVec = Vec2(-250f*2f,0f)
               // position += displaceVec
               // terrain.offset += displaceVec

                terrain.generateNewSegments(2)
                if((0..10).random() < 4) terrain.cycleObstacle()

                //terrain.removeOldSegments()
            }

        }


        collider.position = position



    }

}