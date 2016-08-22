package giscup.solutions

import org.apache.spark.SparkContext
import giscup.tools.GiscupCoordinateCalculator
import giscup.events.EventAccumulator3
import giscup.parser.LineParser2
import giscup.events.EventAccumulator5
import giscup.top50.Top50Storage2
import giscup.tools.GetisOrdStatistic

/**
 * 
 * Solution7
 * 
 * It uses LineParser2 for faster file processing
 * 
 */
class Solution7 extends Solution {
  
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
      val lineParser = new LineParser2(coordinateCalculator)
      
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
    val finalEvents = new EventAccumulator5(coordinateCalculator.xSize, coordinateCalculator.ySize, coordinateCalculator.tSize)
    
    // add events from the spark job
    c.foreach(t => {
      val index: Int = (t / (Int.MaxValue.toLong + 1L)).toInt
      val freq: Int = (t - index * (Int.MaxValue.toLong + 1L)).toInt
      finalEvents.eventIndex(index, freq)
    })
    
    // create the top50 storage
    val topStorage = new Top50Storage2()
    
    finalEvents.top50(topStorage, 4)

    // get the top50
    val top = topStorage.getTop50()
    
    // calculate constant variables for calculating the zScore     
    val sum = finalEvents.calculateSumX()
    val sum2 = finalEvents.calculateSumX2()
    val n = finalEvents.getN()
    
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
}
