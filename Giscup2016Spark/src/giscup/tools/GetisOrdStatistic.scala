package giscup.tools

object GetisOrdStatistic {
  
  def calculateZScore(value: Int, sum: Long, sum2: Long, n: Int): Double = {
    val X: Double = sum.toDouble / n.toDouble
    val S: Double = Math.sqrt(sum2.toDouble / n.toDouble - X * X)
    val c: Double = S * Math.sqrt((n.toDouble * 27.0 - 729.0) / (n - 1.0).toDouble)
    val b: Double = X * 27.0
    (value.toDouble - b) / c
  }
  
  def calculatePValue(zScore: Double): Double = {
    0.0
  }
  
}