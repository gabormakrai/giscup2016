package giscup.tools

import java.text.SimpleDateFormat

@SerialVersionUID(1L)
class CoordinateCalculator(
    val long1: Double, 
    val long2: Double, 
    val lat1: Double, 
    val lat2: Double, 
    val degreeParameter: Double, 
    val date1: String, 
    val date2: String, 
    val timeParameterInDays: Double, 
    val timePartitions: Int) extends Serializable {
    
  val long0: Double = {
    var x = (long1 / degreeParameter).toLong.toDouble
		x = x * degreeParameter
		if (x > long1) x -= degreeParameter
		x
  }
		
  val long3: Double = {
		var x = (long2 / degreeParameter).toLong.toDouble
		x = x * degreeParameter
		if (x < long2) x += degreeParameter
		x
  }
  
  val xMax: Int = (((long3 - long0) / degreeParameter) - 1).toInt
  
  val xSize: Int = {
    SpaceTimeCoordinate.xSize = xMax + 1
    xMax + 1
  }
  
  val longitudeOffset: Int = {
    (long0 / degreeParameter).toInt
  }
  
  val lat0: Double = {
		var x = (lat1 / degreeParameter).toLong.toDouble
		x = x * degreeParameter
 		if (x > lat1) x -= degreeParameter
		x
  }
  		
  val lat3: Double = {
		var x = (lat2 / degreeParameter).toLong.toDouble
		x = x * degreeParameter
		if (x < lat2) x += degreeParameter
		x
  }
		
	val yMax: Int = (((lat3 - lat0) / degreeParameter) - 1).toInt
	
	val ySize: Int = {
	  SpaceTimeCoordinate.ySize = yMax + 1
	  yMax + 1
	}
	
  val latitudeOffset: Int = {
    (lat0 / degreeParameter).toInt
  }	
				
	val timeParameter = (24.0 * 3600.0 * 1000.0 * timeParameterInDays).toLong
	
	val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	
	val timeOrigin = 1420070400000L //sdf.parse("2015-01-01 00:00:00").getTime
	
	val time1 = sdf.parse(date1).getTime
	
	val time2 = sdf.parse(date2).getTime
	
  val time0: Long = {
		var x = ((time1 - timeOrigin) / timeParameter).toLong
		x = x * timeParameter
 		if (x > time1) x -= timeParameter
		x
  }
  		
  val time3: Long = {
		var x = ((time2 - timeOrigin) / timeParameter).toLong
		x = x * timeParameter
		if (x < time2) x += timeParameter
		x
  }
  
  val tMax: Int = (((time3 - time0) / timeParameter) - 1).toInt  
  
	val tSize: Int = {
	  SpaceTimeCoordinate.tSize = tMax + 1
	  tMax + 1
	}
  
  val timeOffset: Int = {
    (time0 / timeParameter).toInt
  }  
	
	def getX(longitude: Double): Int = {
	  if (longitude < long0 || longitude > long3) -1
	  else Math.floor((longitude - long0) / degreeParameter).toInt
	}
	
	def getY(latitude: Double): Int = {
	  if (latitude < lat0 || latitude > lat3) -1
	  else Math.floor((latitude - lat0) / degreeParameter).toInt
	}
	
	def getT(time: Long): Int = {
	  val time5 = time - timeOrigin
	  if (time5 < time0 || time5> time3) -1
	  else Math.floor((time5 - time0) / timeParameter).toInt
	}
	
//	def getTimePartition(t: Int, partitionIds: Array[Int]) = {
//		val p0 = (t.toDouble / tSize.toDouble * (timePartitions)).toInt;
//		val p1 = ((t.toDouble - 1.0) / tSize.toDouble * (timePartitions)).toInt
//		val p2 = ((t.toDouble + 1.0) / tSize.toDouble * (timePartitions)).toInt
//		
//		if (p0 != p1 && p0 == p2) {
//			partitionIds(0) = p0
//			partitionIds(1) = p0- 1
//		} else if (t != tMax && p0 != p2 && p0 == p1) {
//			partitionIds(0) = p0
//			partitionIds(1) = p0 + 1
//		} else {
//			partitionIds(0) = p0
//			partitionIds(1) = -1
//		}
//	}
	
	def calculateLongitudeRange(x: Int): Array[Double] = {
	  val returnArray = new Array[Double](2)
	  returnArray(0) = long0 + x * degreeParameter
	  returnArray(1) = long0 + (x+1) * degreeParameter
	  returnArray
	}
  
	def calculateLatitudeRange(y: Int): Array[Double] = {
	  val returnArray = new Array[Double](2)
	  returnArray(0) = lat0 + y * degreeParameter
	  returnArray(1) = lat0 + (y+1) * degreeParameter
	  returnArray
	}
	
	def calculateTimeRange(t: Int): Array[Long] = {
	  val returnArray = new Array[Long](2)
	  returnArray(0) = timeOrigin + time0 + t * timeParameter
	  returnArray(1) = timeOrigin + time0 + (t+1) * timeParameter
	  returnArray
	}
}