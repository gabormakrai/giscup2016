package giscup.main

import giscup.tools.GiscupCoordinateCalculator
import giscup.parser.LineParser
import giscup.parser.LineParser1
import giscup.tools.SpaceTimeCoordinate
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import giscup.solutions.Solution
import giscup.solutions.Solution2

object SolutionMain {
  
  def main(args: Array[String]) = {
    
    val conf = new SparkConf().setAppName("Giscup G* calculation app")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    
    val sc = new SparkContext(conf)
    
    val inputFiles = args(0)
    
    val outputFile = args(1)
    
    val degreeParameter = args(2).toDouble
    
    val timeParameterInDays = args(3).toInt

    val solution: Solution = new Solution2()
    
    solution.run(inputFiles, outputFile, degreeParameter, timeParameterInDays, sc)
  }
  
}