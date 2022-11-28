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



    override fun start(objMap:Map<String,ObjectView>){
        setBitmap()
        //val terrain:Terrain = objMap["Terrain"] as Terrain
        //position = terrain.playerCollide(Vec2(2400f,300f))
        //TODO: the +Vec2 is to raise obstacle for testing
        collider.position = position //+ Vec2(0f,-500f) //- Vec2(32f,32f)
        //position += Vec2(250f*10f,300f)
        //collider.position = position-Vec2(0f,100f)
    }

    override fun update(deltaT : Float, objMap:Map<String,ObjectView>){

    }

}