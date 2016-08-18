package giscup.parser

import java.text.SimpleDateFormat
import giscup.tools.CoordinateCalculator
/**
 * 
 * LineParser scala implementation
 * 
 * It implements the LineParser trait
 * 
 */
class LineParser1(cc: CoordinateCalculator) extends LineParser{
  
  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  
  // function that checks that a given String is a Double
  def isDouble(x: String): Boolean = {
    try {
      val y = x.toDouble
      true
    } catch {
      case e: Throwable => false
    }
  }

  // process a line and determines XYT parameters if the line contains a valid event
  def parseLine(xyt: Array[Int], line: String): Boolean = {
    
    val splittedLine = line.split("\\,")
    
    if (splittedLine.length > 11 && isDouble(splittedLine(9))) {
      
      val timestampString = splittedLine(2)
      val longitudeString = splittedLine(9)
      val latitudeString = splittedLine(10)
      
      val longitude = longitudeString.toDouble
      val latitude = latitudeString.toDouble
      var time: Long = 0
      try {
        time = sdf.parse(timestampString).getTime()
      } catch {
        case e:Throwable => 
      }
      
      xyt(0) = cc.getX(longitude);
			xyt(1) = cc.getY(latitude);
			xyt(2) = cc.getT(time);
			
			if (xyt(0) < 0 || xyt(0) > cc.xMax || xyt(1) < 0 || xyt(1) > cc.yMax || xyt(2) < 0 || xyt(2) > cc.tMax) false
			else true
    } else {
      false
    }

  }
  
}