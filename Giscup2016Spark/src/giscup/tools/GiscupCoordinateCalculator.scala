package giscup.tools
/**
 * 
 * GiscupCoordinateCalculator class
 * 
 * Setting up the space and time boundaries to the one which was given
 * on the website (NY area and 2015 only)
 * 
 */
class GiscupCoordinateCalculator(override val degreeParameter: Double, override val timeParameterInDays: Double) extends CoordinateCalculator(-74.25, -73.70, 40.5, 40.9, degreeParameter, "2015-01-01 00:00:00", "2016-01-01 00:00:00", timeParameterInDays, 25)

