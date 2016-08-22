package giscup.main

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import giscup.solutions.Solution
import giscup.solutions.Solution1
import giscup.solutions.Solution2
import giscup.solutions.Solution3
import giscup.solutions.Solution4
import giscup.solutions.Solution3Stat
import giscup.solutions.Solution5
import giscup.solutions.Solution6
import giscup.solutions.Solution7
import giscup.solutions.Solution8

/**
 * 
 * SolutionTestMain
 * 
 * Main file for running different solutions
 * 
 */
object SolutionTestMain {
  
  def main(args: Array[String]) = {
    
    val conf = new SparkConf().setAppName("Giscup G* calculation app - " + args(4))
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    
    val sc = new SparkContext(conf)
    
    val inputFiles = args(0)
    
    val outputFile = args(1)
    
    val degreeParameter = args(2).toDouble
    
    val timeParameterInDays = args(3).toDouble

    if (args(4).compareTo("sol1") == 0) {
      val solution: Solution = new Solution1()
      solution.run(inputFiles, outputFile, degreeParameter, timeParameterInDays, sc)
    } else if (args(4).compareTo("sol2") == 0) {
      val solution: Solution = new Solution2()
      solution.run(inputFiles, outputFile, degreeParameter, timeParameterInDays, sc)
    } else if (args(4).compareTo("sol3") == 0) {
      val solution: Solution = new Solution3()
      solution.run(inputFiles, outputFile, degreeParameter, timeParameterInDays, sc)
    } else if (args(4).compareTo("sol3stat") == 0) {
      val solution: Solution = new Solution3Stat()
      solution.run(inputFiles, outputFile, degreeParameter, timeParameterInDays, sc)
    } else if (args(4).compareTo("sol4") == 0) {
      val solution: Solution = new Solution4()
      solution.run(inputFiles, outputFile, degreeParameter, timeParameterInDays, sc)
    } else if (args(4).compareTo("sol5") == 0) {
      val solution: Solution = new Solution5()
      solution.run(inputFiles, outputFile, degreeParameter, timeParameterInDays, sc)
    } else if (args(4).compareTo("sol6") == 0) {
      val solution: Solution = new Solution6()
      solution.run(inputFiles, outputFile, degreeParameter, timeParameterInDays, sc)
    } else if (args(4).compareTo("sol7") == 0) {
      val solution: Solution = new Solution7()
      solution.run(inputFiles, outputFile, degreeParameter, timeParameterInDays, sc)
    } else if (args(4).compareTo("sol8") == 0) {
      val solution: Solution = new Solution8()
      solution.run(inputFiles, outputFile, degreeParameter, timeParameterInDays, sc)
    } else {
      println("Not supported solution version")
    }
  }
  
}