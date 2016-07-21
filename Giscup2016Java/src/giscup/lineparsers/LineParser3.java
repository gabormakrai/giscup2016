package giscup.lineparsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import giscup.tools.BoundaryCalculator;

public class LineParser3 implements LineParser {

	private final BoundaryCalculator bc;
	
	private final SimpleDateFormat sdf;
	
	public LineParser3(BoundaryCalculator bc) {
		this.bc = bc;
		this.sdf = new SimpleDateFormat("yyyyMMdd");
	}
	
	int state = 0;
	int counter1 = 0;
	char[] latitudeBuffer = new char[12];
	char[] longitudeBuffer = new char[12];
	long timeLong = 0;
	char[] dateBuffer = new char[8];
	
	@Override
	public boolean parseLine(int[] xyt, String line) {
		
		char[] lineChars = line.toCharArray();				
		
		state = 0;
		counter1 = 0;
		for (int i = 0; i < 12; ++i) {
			latitudeBuffer[i] = '0';
			longitudeBuffer[i] = '0';
		}
		longitudeBuffer[0] = 'a';
		timeLong = 0;
		
		for (int i = 0; i < line.length(); ++i) {
			
			if (state == 0 && lineChars[i] == ',') {
				++counter1;
				if (counter1 == 2) {
					state = 1;
					counter1 = 0;
				}
			} else if (state == 1){
				if ((lineChars[i] < '0' || lineChars[i] > '9') && lineChars[i] != '-') {
					state = -1;
					break;
				}
				if (lineChars[i] != '-') {
					dateBuffer[counter1] = lineChars[i];
					++counter1;
					if (counter1 == 8) {
						state = 2;
						counter1 = 0;
					}
				}
			} else if (state == 2) {
				state = 3;
				counter1 = 0;
			} else if (state == 3) {
				if (lineChars[i] < '0' || lineChars[i] > '9') {
					state = -2;
					break;
				}
				timeLong += (lineChars[i] - '0') * 10 * 3600 * 1000;
				state = 4;
			} else if (state == 4) {
				if (lineChars[i] < '0' || lineChars[i] > '9') {
					state = -3;
					break;
				}
				timeLong += (lineChars[i] - '0') * 3600 * 1000;
				state = 5;
			} else if (state == 5) {
				state = 6;
			} else if (state == 6) {
				if (lineChars[i] < '0' || lineChars[i] > '9') {
					state = -4;
					break;
				}
				timeLong += (lineChars[i] - '0') * 10 * 60 * 1000;
				state = 7;
			} else if (state == 7) {
				if (lineChars[i] < '0' || lineChars[i] > '9') {
					state = -5;
					break;
				}
				timeLong += (lineChars[i] - '0') * 60 * 1000;
				state = 8;
			} else if (state == 8) {
				state = 9;
			} else if (state == 9) {
				if (lineChars[i] < '0' || lineChars[i] > '9') {
					state = -6;
					break;
				}
				timeLong += (lineChars[i] - '0') * 10 * 1000;
				state = 10;
			} else if (state == 10) {
				if (lineChars[i] < '0' || lineChars[i] > '9') {
					state = -6;
					break;
				}
				timeLong += (lineChars[i] - '0') * 1000;
				state = 11;
			} else if (state == 11) {
				if (lineChars[i] == ',') {
					state = 20;
				} else {
					state = -7;
					break;
				}
			} else if (state == 20 && lineChars[i] == ',') {
				++counter1;
				if (counter1 == 6) {
					state = 30;
					counter1 = 0;
				}
			} else if (state == 30 && lineChars[i] == ',') {
				state = 40;
				counter1 = 0;
			} else if (state == 30) {
				if (counter1 < 10) {
					longitudeBuffer[counter1] = lineChars[i];
				}
				++counter1;
			} else if (state == 40 && lineChars[i] == ',') {
				state = 50;
				counter1 = 0;
			} else if (state == 40) {
				if (counter1 < 10) {
					latitudeBuffer[counter1] = lineChars[i];
				}
				++counter1;
			}
		}
		
		if (state < 0) {
			return false;
		}
		
		String longitudeString = new String(longitudeBuffer);
		String latitudeString = new String(latitudeBuffer);
		String dateString = new String(dateBuffer);
		
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
		
		long time = timeLong;
		try {
			time = sdf.parse(dateString).getTime();
		} catch (ParseException e) {
			// do nothing
		}
		
		xyt[2] = bc.getT(time);
		
		return true;
	}
}
