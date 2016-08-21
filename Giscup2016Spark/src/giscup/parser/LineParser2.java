package giscup.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import giscup.tools.CoordinateCalculator;
/**
 * 
 * Java implementation of the LineParser interface
 * 
 * It is the Java copy of the LineParser1 scala class
 *
 */
public class LineParser2 implements LineParser{

	private final CoordinateCalculator cc;
	
	private final SimpleDateFormat sdf;	
	
	public LineParser2(CoordinateCalculator cc) {
		this.cc = cc;
		this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	// function that checks that a given String is a Double
	public boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	// process a line and determines XYT parameters if the line contains a valid event
	@Override
	public boolean parseLine(int[] xyt, String line) {
		
		String[] splittedLine = line.split("\\,");
		
		if (splittedLine.length > 11 && isDouble(splittedLine[9])) {
			
			String timestampString = splittedLine[2];
			String longitudeString = splittedLine[9];
			String latitudeString = splittedLine[10];
								
			double longitude = Double.parseDouble(longitudeString);
			double latitude = Double.parseDouble(latitudeString);
			
			xyt[0] = cc.getX(longitude);
			
			if (xyt[0] < 0 || xyt[0] > cc.xMax()) {
				return false;
			}
			
			xyt[1] = cc.getY(latitude);
			
			if (xyt[1] < 0 || xyt[1] > cc.yMax()) {
				return false;
			}				
			
			long time = 0;
			try {
				time = sdf.parse(timestampString).getTime();
			} catch (ParseException e) {
				// do nothing
			}		
			
			xyt[2] = cc.getT(time);
			
			if (xyt[2] < 0 || xyt[2] > cc.tMax()) {
				return false;
			}			
			
			return true;
		}
		return false;		
	}
}
