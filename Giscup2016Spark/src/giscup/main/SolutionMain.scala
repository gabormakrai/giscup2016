package giscup.main

import giscup.tools.GiscupCoordinateCalculator
import giscup.parser.LineParser
import giscup.parser.LineParser1
import giscup.tools.SpaceTimeCoordinate
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import giscup.solutions.Solution
import giscup.solutions.Solution1

object SolutionMain {
  
  def main(args: Array[String]) = {
    
    val conf = new SparkConf().setAppName("Giscup G* calculation app")
    
    val sc = new SparkContext(conf)
    
    // TODO: check input dir exists
    val inputFiles = args(0)
    
    // TODO: check output dir does not exist 
    val outputFile = args(1)
    
    // TODO: check args(2) is a double    
    val degreeParameter = args(2).toDouble
    
    // TODO: check that args(3) is an int 
    val timeParameterInDays = args(3).toInt

    val solution: Solution = new Solution1()
    
    solution.run(inputFiles, outputFile, degreeParameter, timeParameterInDays, sc)
    
  }
  
}