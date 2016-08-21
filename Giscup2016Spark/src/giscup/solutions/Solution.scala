package giscup.solutions

import org.apache.spark.SparkContext

/**
 * 
 * Solution trait
 * 
 * Classes implements this trait have to use the listed parameters and
 * calculate the top50 space-time coordinate with the corresponding
 * Getis-Ord G* z-score and p-value and save the result into a file
 * 
 */
trait Solution {
  def run(inputFiles: String, outputFile: String, degreeParameter: Double, timeParameterInDays: Double, sc: SparkContext)
}