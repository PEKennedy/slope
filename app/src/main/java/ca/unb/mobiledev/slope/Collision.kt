package ca.unb.mobiledev.slope

import android.util.Log


class Collision {
    companion object{
        interface Collider {
            fun collideBox(otherBox:BoxCollider):Boolean
        }

        class BoxCollider(var position:Vec2,var extents:Vec2):Collider{

            init {
                Log.i("Collider_Created",position.toString() + getFarCoords().toString())

            }

            fun getFarCoords():Vec2{
                return Vec2(position.x+extents.x,position.y+extents.y)
            }

            override fun collideBox(otherBox:BoxCollider):Boolean{
                if(position.x < otherBox.getFarCoords().x &&
                        getFarCoords().x > otherBox.position.x){
                //Log.i("Collider",position.toString())
                //Log.i("Collider",otherBox.position.toString())
                    //if(position.y < otherBox.getFarCoords().y &&
                    //    getFarCoords().y > otherBox.position.y){
                    if(getFarCoords().y > otherBox.position.y &&
                        position.y < otherBox.getFarCoords().y){
                        Log.i("Collision",position.toString() + getFarCoords().toString())
                        Log.i("Collision",otherBox.position.toString() + otherBox.getFarCoords().toString())
                        return true
                    }
                }
                return false
            }

            //fun collideSlope()
        }
    }
}