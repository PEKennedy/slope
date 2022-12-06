package ca.unb.mobiledev.slope.objects

import android.content.Context
import ca.unb.mobiledev.slope.Collision
import ca.unb.mobiledev.slope.GameActivity
import ca.unb.mobiledev.slope.ObjectView
import ca.unb.mobiledev.slope.Vec2
import ca.unb.mobiledev.slope.R//.drawable.skifrog as texture
import ca.unb.mobiledev.slope.sensor.AccelerometerSensor

class Player(context: Context?, displayWidth: Int, displayHeight: Int, objId: Int,val activity: GameActivity,
    val obstacles: MutableList<Obstacle>, var accelerometerSensor: AccelerometerSensor)
    :ObjectView(context,displayWidth,displayHeight,objId) {

    override val defaultBitmap = R.drawable.skifrog //texture

    var velocity = Vec2(0f,0.0f)
    val gravity = 18f//9.81f

    var extents = Vec2(64f,64f)
    var collider = Collision.BoxCollider(position,extents)

    var hitObstacle = false

    var playerSpeed = 10f

    var grounded = false

    // How fast to rotate the player character to a new rotation
    var rotationRate = 10.0f

    var mAccelerometer: AccelerometerSensor = accelerometerSensor

    override fun start(objMap:Map<String,ObjectView>){
        setBitmap()
        hitObstacle = false
        //500,200 vs 200,300  //450

        position += Vec2(100f,200f)
    }

    override fun update(deltaT : Float, objMap:Map<String,ObjectView>){

        val terrain:Terrain = objMap["Terrain"] as Terrain

        if(terrain != null){
            val collided = terrain.checkPlayerCollide(position)

            objRotation = lerp1d( objRotation, terrain.getPlayerAngle(position), 0.0f ,1.0f, rotationRate*deltaT )

            if(collided){

                position = terrain.playerCollide(position)
                velocity.y = 0F
                grounded = true
                setBitmap(R.drawable.skifrog)
            }

            // Check for collision with all obstacles
            obstacles.forEach {
                val collide = collider.collideBox(it.collider)
                if(collide){
                    hitObstacle = true
                    velocity.y = 0F
                    setBitmap(R.drawable.skifrogcrash)
                }
            }

            //physics & controls
            playerSpeed = Math.max(5f, Math.min(12f, mAccelerometer.getTilt())) // Clamp value between 0 and 10

            // Jumping.  Only allow jumping if already on the ground
            if( activity.wasTouched ){
                if( grounded ){
                    velocity.y = -700f
                    grounded = false
                    setBitmap(R.drawable.skifrogjump3)
                }
                activity.wasTouched = false
            }

            position += velocity*deltaT
            velocity.y += gravity
            velocity.x = playerSpeed/deltaT

            //Terrain Generation logic
            val segmentNum = terrain.getSegmentNum(position)
            val finalSegment = terrain.getNumSegments()
            if(finalSegment - segmentNum < 10){

                terrain.generateNewSegments(4,position.y)
                if((0..10).random() < 8){
                    terrain.cycleObstacle()
                }
            }
        }
        collider.position = position
    }

    // Linear interpolation between x1y1 and x2y2, given x
    fun lerp1d(y1:Float, y2:Float, x1: Float, x2: Float, x:Float): Float {
        val y = y1+(x-x1)*(( y2-y1 )/(x2-x1))
        return y
    }
}