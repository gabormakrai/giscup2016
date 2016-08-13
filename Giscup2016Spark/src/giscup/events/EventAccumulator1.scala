package giscup.events

import giscup.tools.SpaceTimeCoordinate
import scala.collection.mutable.ListBuffer

@SerialVersionUID(1L)
class EventAccumulator1(bufferSize: Int) extends Serializable {
  
  val array: Array[Int] = {
    Array.fill(bufferSize){ 0 }
  }
  
  def event(stc: SpaceTimeCoordinate) {
    val index = stc.hashCode()
    array(index) = array(index) + 1
  }
  
  def event(stc: SpaceTimeCoordinate, i: Int) {
    val index = stc.hashCode()
    array(index) = array(index) + i
  }
  
  def getAll(): ListBuffer[Tuple2[SpaceTimeCoordinate, Int]] = {
    
    val frequencyList = ListBuffer[Tuple2[SpaceTimeCoordinate, Int]]()
    
    for (i <- 0 until array.length) {
      if (array(i) != 0) {
        val tuple: Tuple2[SpaceTimeCoordinate, Int] = (SpaceTimeCoordinate.getSpaceTimeCoordinateFromHash(i), array(i))
        frequencyList += tuple
      }
    }
    
    frequencyList
  }
  
  def calculateSumX(): Long = {
    var sum: Long = 0
    array.foreach(x => sum = sum + x)
    sum
  }
  
  def calculateSumX2(): Long = {
    var sum: Long = 0
    array.foreach(x => sum = sum + x * x)
    sum
  }
  
}
