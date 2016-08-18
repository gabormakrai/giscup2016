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

class Solution3 extends Solution {
  
  override def run(inputFiles: String, outputFile: String, degreeParameter: Double, timeParameterInDays: Int, sc: SparkContext) {
    
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
      
      events.getAll().iterator
                          
    }, true)

    // run the previous spark job and collect the results
    val c = b.collect()

    // local coordinate calculator
    val coordinateCalculator = new GiscupCoordinateCalculator(degreeParameter, timeParameterInDays)    
    
    // final event accumulator
    val finalEvents = new EventAccumulator2(coordinateCalculator.xSize * coordinateCalculator.ySize * coordinateCalculator.tSize)
    
    // add events from the spark job
    c.foreach(t => {
      val index: Int = (t / (Int.MaxValue.toLong + 1L)).toInt
      val freq: Int = (t - index * (Int.MaxValue.toLong + 1L)).toInt
      finalEvents.eventIndex(index, freq)
    })
    
    // create the top50 storage
    val topStorage = new Top50Storage1()
    
    // in a single thread, calculate the value for each cell and give the cell and the value to the storage
    for (x <- 0 to coordinateCalculator.xMax) {
      for (y <- 0 to coordinateCalculator.yMax) {
        for (t <- 0 to coordinateCalculator.tMax) {
          val value = calculateValueForOneCell(x, y, t, finalEvents.array, coordinateCalculator)
          topStorage.add(x,y,t,value)
        }
      }
    }

    // get the top50
    val top = topStorage.getTop50()
    
    // calculate constant variables for calculating the zScore     
    val sum = finalEvents.calculateSumX()
    val sum2 = finalEvents.calculateSumX2()
    val n = finalEvents.array.length
    
    // generate an in-memory rdd with the top 50 one
    sc.parallelize(top,1).map(stcv => {
      val zScore = GetisOrdStatistic.calculateZScore(stcv.v, sum, sum2, n)
      val pValue = GetisOrdStatistic.calculatePValue(zScore)
      val x = stcv.x + coordinateCalculator.longitudeOffset
      val y = stcv.y + coordinateCalculator.latitudeOffset
      val t = stcv.t + coordinateCalculator.timeOffset
      
      "" + x + "," + y + "," + t + "," + zScore + "," + pValue
    }).saveAsTextFile(outputFile)
    
  }

  // calculate the value for one cell
  private def calculateValueForOneCell(x: Int, y: Int, t: Int, events: Array[Int], coordinateCalculator: CoordinateCalculator): Int = {
    var value = 0
    for (dx <- -1 to 1) {
      for (dy <- -1 to 1) {
        for (dt <- -1 to 1) {
          val x1 = x + dx
          val y1 = y + dy
          val t1 = t + dt
          if (x1 >= 0 && x1 <= coordinateCalculator.xMax &&
              y1 >= 0 && y1 <= coordinateCalculator.yMax &&
              t1 >= 0 && t1 <= coordinateCalculator.tMax) {
            val index = SpaceTimeCoordinate.hash(x1, y1, t1)
            value = value + events(index)
          }
        }
      }
    }
    value
  }
}
