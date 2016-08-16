package giscup.events

import giscup.tools.SpaceTimeCoordinate
import scala.collection.mutable.ListBuffer

@SerialVersionUID(1L)
class EventAccumulator2(bufferSize: Int) extends Serializable {
  
  val array: Array[Int] = {
    Array.fill(bufferSize){ 0 }
  }
  
  def event(x: Int, y: Int, t: Int) {
    val index = SpaceTimeCoordinate.hash(x, y, t)
    array(index) = array(index) + 1
  }
  
  def event(x: Int, y: Int, t: Int, v: Int) {
    val index = SpaceTimeCoordinate.hash(x, y, t)
    array(index) = array(index) + v
  }
  
  def eventIndex(index: Int) {
    array(index) = array(index) + 1
  }
      
  def eventIndex(index: Int, v: Int) {
    array(index) = array(index) + v
  }
  
  def getAll(): Array[Long] = {
    
    var arraySize = 0
    
    // find out the size of the array
    for (i <- 0 until array.length) {
      if (array(i) != 0) {
        arraySize = arraySize + 1
      }
    }
    
    val returnArray: Array[Long] = new Array(arraySize)
    var returnArrayIndex = 0
    
    for (i <- 0 until array.length) {
      if (array(i) != 0) {
        val index: Int = i
        val freq: Int = array(i)
        val value: Long = i.toLong * (Int.MaxValue.toLong + 1L) + freq
        returnArray(returnArrayIndex) = value
        returnArrayIndex = returnArrayIndex + 1
      }
    }
    
    returnArray
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
