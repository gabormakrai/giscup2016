package giscup.solutions

import org.apache.spark.SparkContext
import giscup.tools.GiscupCoordinateCalculator
import giscup.parser.LineParser1
import giscup.tools.SpaceTimeCoordinate
import scala.collection.mutable.ListBuffer
import giscup.tools.SpaceTimeCoordinateValue
import giscup.tools.SpaceTimeCoordinateValue
import scala.util.Sorting
import giscup.tools.GetisOrdStatistic
import giscup.tools.GiscupCoordinateCalculator
import giscup.tools.CoordinateCalculator
import giscup.top50.Top50Storage1
import java.io.ObjectOutputStream
import java.io.FileOutputStream
import org.apache.hadoop.io.compress.GzipCodec.GzipOutputStream
import giscup.events.EventAccumulator2
import giscup.events.EventAccumulator3

/**
 * 
 * Solution3Stat class
 * 
 * It helps to find out how much of the small arrays are utilized during stage-1
 * 
 */
class Solution3Stat extends Solution {
  
  override def run(inputFiles: String, outputFile: String, degreeParameter: Double, timeParameterInDays: Double, sc: SparkContext) {
    
    // open the file for parsing
    val a = sc.textFile(inputFiles)
    
    // in each partition
    // 1. parse the line
    // 2. use the event accumulator to aggregate events locally
    // 3. return all the events which has at least 1 occurrence
    val b = a.mapPartitions(lines => {
      
      // local coordinate calculator
      val coordinateCalculator = new GiscupCoordinateCalculator(degreeParameter, timeParameterInDays)
      
      // event accumulator
      val events = new EventAccumulator3(coordinateCalculator.xSize, coordinateCalculator.ySize, coordinateCalculator.tSize)
      
      // line parser
      val lineParser = new LineParser1(coordinateCalculator)
      
      // xyt tuple for result
      val xyt: Array[Int] = Array(0, 0, 0)
      
      // parse the lines
      lines.foreach(line => {
        // if the line contains a valid event
        if (lineParser.parseLine(xyt, line)) {
          // then use the event's x,y,t to accumulate it
          events.event(xyt)
        }
      })
      val statString = events.getStatistics
      List(statString).iterator
                          
    }, true)

    // run the previous spark job and collect the results
    val c = b.collect()

    c.foreach { println(_) } 
  }
}
