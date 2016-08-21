package giscup.tools
/**
 * 
 * Space-Time coordinate Value class
 * 
 * It helps to find the space-time coordinate where most of the events happened
 * Implements the Ordered trait to support built-in sort functions
 * 
 */
@SerialVersionUID(1L)
class SpaceTimeCoordinateValue(val x: Int, val y: Int, val t: Int, val v: Int) extends Ordered[SpaceTimeCoordinateValue] with Serializable {
  override def compare(that: SpaceTimeCoordinateValue): Int = {
    that.v - this.v
  }
  override def toString(): String = {
    "STCF(x:" + x + ",y:" + y + ",t:" + t + ",v:" + v + ")"
  }
}