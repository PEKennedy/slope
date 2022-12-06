package ca.unb.mobiledev.slope

import android.util.Log

// Collision class. Used to check for player collisions with level and obstacles
class Collision {
    //companion object{
        interface Collider {
            fun collideBox(otherBox:BoxCollider):Boolean
        }

        // Collider box
        class BoxCollider(var position:Vec2,var extents:Vec2):Collider{

            // Get coordinates of the opposite corner of the box as a vector 2
            fun getFarCoords():Vec2{
                return Vec2(position.x+extents.x,position.y+extents.y)
            }

            // Check if collision has happened
            override fun collideBox(otherBox:BoxCollider):Boolean{
                if(position.x < otherBox.getFarCoords().x &&
                        getFarCoords().x > otherBox.position.x){
                    if(getFarCoords().y > otherBox.position.y &&
                        position.y < otherBox.getFarCoords().y){
                        return true
                    }
                }
                return false
            }
        }
}