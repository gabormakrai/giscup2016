package giscup.tools

object SpaceTimeCoordinate {
  
  var xSize: Int = 0
  var ySize: Int = 0
  var tSize: Int = 0
  
  def hash(x: Int, y: Int, t: Int): Int = {
    t * ySize * xSize + y * xSize + x
  }
  
  def getXYTFromHash(hash: Int, xyt: Array[Int]) {
    val t = hash / (ySize * xSize)
    val hash1 = hash - t * ySize * xSize
    val y = hash1 / (xSize)
    val hash2 = hash1 - y * xSize
    val x = hash2
    xyt(0) = x
    xyt(1) = y
    xyt(2) = t
  }
  
  def getSpaceTimeCoordinateFromHash(hash: Int): SpaceTimeCoordinate = {
    val xyt: Array[Int] = Array(0, 0, 0)
    getXYTFromHash(hash, xyt)
    new SpaceTimeCoordinate(xyt(0), xyt(1), xyt(2))
  }
}

@SerialVersionUID(1L)
class SpaceTimeCoordinate(val x: Int, val y: Int, val t: Int) extends Serializable {
    
  override def hashCode(): Int = {
    SpaceTimeCoordinate.hash(x, y, t)
  }
  
  override def equals(that: Any): Boolean = {
     that match {
       case that: SpaceTimeCoordinate => {
         if (this.x == that.x && this.y == that.y && this.t == that.t) true
         else false
       }
       case _ => false
     }
  }
  
  override def toString(): String = {
    "SpaceTimeCoordinate(x: " + x + ",y:" + y + ",t:" + t + ")" 
  }
  
}