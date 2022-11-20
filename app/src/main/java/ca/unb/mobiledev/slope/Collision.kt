package ca.unb.mobiledev.slope



class Collision {
    companion object{
        interface Collider {
            fun collideBox(otherBox:BoxCollider):Boolean
        }

        class BoxCollider(var position:Vec2,var extents:Vec2):Collider{

            fun getFarCoords():Vec2{
                return Vec2(position.x+extents.x,position.y+extents.y)
            }

            override fun collideBox(otherBox:BoxCollider):Boolean{
                if(position.x < otherBox.getFarCoords().x &&
                        getFarCoords().x > otherBox.position.x){
                    if(position.y < otherBox.getFarCoords().y &&
                        getFarCoords().y > otherBox.position.y){
                        return true
                    }
                }
                return false
            }

            //fun collideSlope()
        }
    }
}