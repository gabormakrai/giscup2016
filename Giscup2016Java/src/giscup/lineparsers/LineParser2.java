package giscup.lineparsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import giscup.tools.BoundaryCalculator;

public class LineParser2 implements LineParser {

	private final BoundaryCalculator bc;
	
	private final SimpleDateFormat sdf;
	
	public LineParser2(BoundaryCalculator bc) {
		this.bc = bc;
		this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	int state = 0;
	int counter1 = 0;
	char[] latitudeBuffer = new char[12];
	char[] longitudeBuffer = new char[12];
	char[] timestampBuffer = new char[19];
	
	@Override
	public boolean parseLine(int[] xyt, String line) {
		
		state = 0;
		counter1 = 0;
		char[] lineChars = line.toCharArray();				
		for (int i = 0; i < 12; ++i) {
			latitudeBuffer[i] = '0';
			longitudeBuffer[i] = '0';
		}
		longitudeBuffer[0] = 'a';				
		
		for (int i = 0; i < line.length(); ++i) {
			
			if (state == 0 && lineChars[i] == ',') {
				++counter1;
				if (counter1 == 2) {
					state = 1;
					counter1 = 0;
				}
			} else if (state == 1){
				timestampBuffer[counter1] = lineChars[i];
				++counter1;
				if (counter1 == 19) {
					state = 2;
					counter1 = 0;
				}
			} else if (state == 2 && lineChars[i] == ',') {
				++counter1;
				if (counter1 == 7) {
					state = 3;
					counter1 = 0;
				}
			} else if (state == 3 && lineChars[i] == ',') {
				state = 4;
				counter1 = 0;
			} else if (state == 3) {
				if (counter1 < 10) {
					longitudeBuffer[counter1] = lineChars[i];
				}
				++counter1;
			} else if (state == 4 && lineChars[i] == ',') {
				state = 5;
				counter1 = 0;
				break;
			} else if (state == 4) {
				if (counter1 < 10) {
					latitudeBuffer[counter1] = lineChars[i];
				}
				++counter1;
			}
		}
		
		String longitudeString = new String(longitudeBuffer);
		String latitudeString = new String(latitudeBuffer);
		String timestampString = new String(timestampBuffer);
		
		double longitude = Double.POSITIVE_INFINITY;
		double latitude = Double.POSITIVE_INFINITY;
		
		try {
			longitude = Double.parseDouble(longitudeString);
			latitude = Double.parseDouble(latitudeString);
		} catch (NumberFormatException e) {
			// do nothing
		}
		
		if (longitude == Double.POSITIVE_INFINITY) {
			return false;
		}
		
		xyt[0] = bc.getX(longitude);
		xyt[1] = bc.getY(latitude);
		
		long time = 0;
		try {
			time = sdf.parse(timestampString).getTime();
		} catch (ParseException e) {
			// do nothing
		}
		
		xyt[2] = bc.getT(time);
		
		return true;
	}

}
