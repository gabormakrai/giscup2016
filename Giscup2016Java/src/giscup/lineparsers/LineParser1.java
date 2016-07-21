package giscup.lineparsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import giscup.tools.BoundaryCalculator;

public class LineParser1 implements LineParser{
	
	private final BoundaryCalculator bc;
	
	private final SimpleDateFormat sdf;
	
	public LineParser1(BoundaryCalculator bc) {
		this.bc = bc;
		this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public boolean parseLine(int[] xyt, String line) {
		
		String[] splittedLine = line.split("\\,");
		
		if (splittedLine.length > 11 && isDouble(splittedLine[9])) {
			
			String timestampString = splittedLine[2];
			String longitudeString = splittedLine[9];
			String latitudeString = splittedLine[10];
								
			double longitude = Double.parseDouble(longitudeString);
			double latitude = Double.parseDouble(latitudeString);
			
//			System.out.println("lp1: lon:" + longitude + ", lat:" + latitude);
			
			long time = 0;
			try {
				time = sdf.parse(timestampString).getTime();
			} catch (ParseException e) {
				// do nothing
			}
			
			xyt[0] = bc.getX(longitude);
			xyt[1] = bc.getY(latitude);
			xyt[2] = bc.getT(time);
			
			return true;
			
		}
		
		return false;
		
	}
}
