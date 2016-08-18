package giscup.parser
/**
 * 
 * Line Parser trait
 * 
 * it only has one function which calculates the event's XYT parameters only if the line contains a valid event 
 * 
 */
trait LineParser {
  def parseLine(xyt: Array[Int], line: String): Boolean
}