package giscup.tools
/**
 * 
 * SpaceTimeCoordinate static functions (comapnion object)
 * 
 */
object SpaceTimeCoordinate {
  
  var xSize: Int = 0
  var ySize: Int = 0
  var tSize: Int = 0
  
  // hash calculation based on the different dimension sizes
  def hash(x: Int, y: Int, t: Int): Int = {
    t * ySize * xSize + y * xSize + x
  }
  
  // calculate back xyt from a hash value
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
  
  // generate a SpaceTimeCoordinate object from a hash value
  def getSpaceTimeCoordinateFromHash(hash: Int): SpaceTimeCoordinate = {
    val xyt: Array[Int] = Array(0, 0, 0)
    getXYTFromHash(hash, xyt)
    new SpaceTimeCoordinate(xyt(0), xyt(1), xyt(2))
  }
}

/**
 * 
 * SpaceTimeCoordinate class
 * 
 * It represents a cell in the 3D space-time coordinate system
 * 
 */
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
