package giscup.solutions

import org.apache.spark.SparkContext

trait Solution {
  def run(inputFiles: String, outputFile: String, degreeParameter: Double, timeParameterInDays: Int, sc: SparkContext)
}