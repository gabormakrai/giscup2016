package giscup.parser

trait LineParser {
  def parseLine(xyt: Array[Int], line: String): Boolean
}