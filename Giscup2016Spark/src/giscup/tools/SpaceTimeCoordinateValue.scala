package giscup.tools

class SpaceTimeCoordinateValue(val x: Int, val y: Int, val t: Int, val v: Int) extends Ordered[SpaceTimeCoordinateValue] {
  override def compare(that: SpaceTimeCoordinateValue): Int = {
    that.v - this.v
  }
  override def toString(): String = {
    "STCF(x:" + x + ",y:" + y + ",t:" + t + ",v:" + v + ")"
  }
}