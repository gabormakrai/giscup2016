package giscup.tools

object GetisOrdStatistic {
  
  def calculateZScore(value: Int, sum: Long, sum2: Long, n: Int): Double = {
    val X: Double = sum.toDouble / n.toDouble
    val S: Double = Math.sqrt(sum2.toDouble / n.toDouble - X * X)
    val c: Double = S * Math.sqrt((n.toDouble * 27.0 - 729.0) / (n - 1.0).toDouble)
    val b: Double = X * 27.0
    (value.toDouble - b) / c
  }
  
  val ltone=7.0; val utzero=18.66; val con=1.28; val a1 = 0.398942280444; val a2 = 0.399903438504
  val a3 = 5.75885480458; val a4 =29.8213557808; val a5 = 2.62433121679; val a6 =48.6959930692
  val a7 = 5.92885724438; val b1 = 0.398942280385; val b2 = 3.8052e-8; val b3 = 1.00000615302
  val b4 = 3.98064794e-4; val b5 = 1.986153813664; val b6 = 0.151679116635; val b7 = 5.29330324926
  val b8 = 4.8385912808; val b9 =15.1508972451; val b10= 0.742380924027; val b11=30.789933034
  val b12= 3.99019417011  
  
  /**
   * Function to approximate the pNorm function
   *    
   * Based on I. D. Hill, Algorithm AS 66: "The Normal Integral", Applied Statistics   
   * 
   */
  
  def pnorm(z: Double) = {
    if(z<=ltone || z<=utzero) {
      val y=0.5*z*z
      if(z>con) {
        b1*Math.exp(-y)/(z-b2+b3/(z+b4+b5/(z-b6+b7/(z+b8-b9/(z+b10+b11/(z+b12))))))
      }
      else {
        0.5-z*(a1-a2*y/(y+a3-a4/(y+a5+a6/(y+a7))))
      }
    }
    else {
      0.0
    }
  }
  
  def calculatePValue(zScore: Double): Double = {
    2.0 * pnorm(zScore)
  }  
  
}