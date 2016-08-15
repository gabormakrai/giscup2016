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
    val timeParameterInDays: Int, 
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
  
  val xMax: Int = (((long3 - long0) / degreeParameter) + 1).toInt
  
  val xSize: Int = {
    SpaceTimeCoordinate.xSize = xMax + 1
    xMax + 1
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
		
	val yMax: Int = (((lat3 - lat0) / degreeParameter) + 1).toInt
	
	val ySize: Int = {
	  SpaceTimeCoordinate.ySize = yMax + 1
	  yMax + 1
	}
				
	val timeParameter = 24 * 3600 * 1000 * timeParameterInDays
	
	val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	
	val time1: Long = sdf.parse(date1).getTime()
	
	val tMax: Int = ((sdf.parse(date2).getTime() - time1) / (timeParameter)).toInt

	val tSize: Int = {
	  SpaceTimeCoordinate.tSize = tMax + 1
	  tMax + 1
	}
	
	def getX(longitude: Double): Int = {
	  if (longitude < long0 || longitude > long3) -1
	  else Math.floor((longitude - long0) / degreeParameter).toInt
	}
	
	def getY(latitude: Double): Int = {
	  if (latitude < lat0 || latitude > lat3) -1
	  else Math.floor((latitude - lat0) / degreeParameter).toInt
	}
	
	def getT(time: Long): Int = ((time - time1) / this.timeParameter).toInt

	def getTimePartition(t: Int, partitionIds: Array[Int]) = {
		val p0 = (t.toDouble / tSize.toDouble * (timePartitions)).toInt;
		val p1 = ((t.toDouble - 1.0) / tSize.toDouble * (timePartitions)).toInt
		val p2 = ((t.toDouble + 1.0) / tSize.toDouble * (timePartitions)).toInt
		
		if (p0 != p1 && p0 == p2) {
			partitionIds(0) = p0
			partitionIds(1) = p0- 1
		} else if (t != tMax && p0 != p2 && p0 == p1) {
			partitionIds(0) = p0
			partitionIds(1) = p0 + 1
		} else {
			partitionIds(0) = p0
			partitionIds(1) = -1
		}
	}
  
}