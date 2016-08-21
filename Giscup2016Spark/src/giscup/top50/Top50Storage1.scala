package giscup.top50

import scala.util.Sorting
import giscup.tools.SpaceTimeCoordinateValue
/**
 * 
 * Top50Storage class
 * 
 * It can store the top50 SpaceTimeCoordinateValue for the final result
 * It massively uses the Sorting.quickSort function to always keep the best 50 candidate
 * in the top51 array
 * 
 */
class Top50Storage1 {
  
  val top51: Array[SpaceTimeCoordinateValue] = new Array(51)
  var top50Index = 0
  
  def add(x: Int, y: Int, t: Int, value: Int) {
    if (top50Index < 50) {
      top51(top50Index) = new SpaceTimeCoordinateValue(x, y, t, value)
      top50Index = top50Index + 1
     if (top50Index == 50) {
       top51(50) = new SpaceTimeCoordinateValue(x, y, t, value)
       Sorting.quickSort(top51)
       top50Index = 51
     }
    } else {
      if (value > top51(50).v) {
        top51(50) = new SpaceTimeCoordinateValue(x, y, t, value)
        Sorting.quickSort(top51)
      }
    }
  }
  
  def getTop50(): Array[SpaceTimeCoordinateValue] = {
    val top = new Array[SpaceTimeCoordinateValue](50)
    Array.copy(top51, 0, top, 0, 50)
    top
  }
  
}