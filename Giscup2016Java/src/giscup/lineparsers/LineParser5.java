package giscup.lineparsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.koloboke.collect.map.hash.HashIntLongMap;
import com.koloboke.collect.map.hash.HashIntLongMaps;

import giscup.tools.BoundaryCalculator;

public class LineParser5 implements LineParser {

	private final BoundaryCalculator bc;
	
	private final SimpleDateFormat sdf;
	
	private final HashIntLongMap dateMap = HashIntLongMaps.newMutableMap();
	
	public LineParser5(BoundaryCalculator bc) {
		this.bc = bc;
		this.sdf = new SimpleDateFormat("yyyyMMdd");
	}
	
	int state = 0;
	int counter1 = 0;
	long timeLong = 0;
	char[] dateBuffer = new char[8];
	
	int[] i1 = new int[4];
	int[] i2 = new int[12];
	
	@Override
	public boolean parseLine(int[] xyt, String line) {
		
		char[] lineChars = line.toCharArray();				
		
		double longitude = 0.0;
		double latitude = 0;
		
		state = 0;
		counter1 = 0;
		double i0 = 0.0;
		int i1c = 0;
		for (int i = 0; i < 4; ++i) {
			i1[i] = 0;
		}
		int i2c = 0;
		for (int i = 0; i < 12; ++i) {
			i2[i] = 0;
		}
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
			} else if (state == 30) {
				if (lineChars[i] == '+') {
					i0 = 1.0;
					state = 31;
				} else if (lineChars[i] == '-') {
					i0 = -1.0;
					state = 31;
				} else if (lineChars[i] >= '0' && lineChars[i] <= '9') {
					i0 = 1.0;
					i1[0] = lineChars[i] - '0';
					++i1c;
					state = 31;
				} else {
					// invalid input
					state = -100;
					break;
				}
			} else if (state == 31 && lineChars[i] == '.') {
				state = 32;
			} else if ((state == 31 || state == 32) && lineChars[i] == ',') {				
				// convert i0, i1, i2 to double...
				int i1coefficient = 1;
				for (int j = 1; j < i1c; ++j) {
					i1coefficient *= 10;
				}
				
				for (int j = 0; j < i1c; ++j) {
					longitude += i1coefficient * i1[j];
					i1coefficient /= 10;
				}
				
				double i2coefficient = 0.1;
				for (int j = 0; j < i2c; ++j) {
					longitude += i2coefficient * i2[j];
					i2coefficient /= 10.0;
				}
				longitude *= i0;
				
				// reset
				i1c = 0;
				i2c = 0;
				state = 40;
			} else if (state == 31) {
				if (i1c == 4) {
					state = -101;
					break;
				}
				i1[i1c] = lineChars[i] - '0';
				++i1c;
			} else if (state == 32) {
				if (lineChars[i] < '0' || lineChars[i] > '9') {
					state = -102;
					break;
				}
				if (i2c != 12) { 
					i2[i2c] = lineChars[i] - '0';
					++i2c;
				}		
			} else if (state == 40) {
				if (lineChars[i] == '+') {
					i0 = 1.0;
					state = 41;
				} else if (lineChars[i] == '-') {
					i0 = -1.0;
					state = 41;
				} else if (lineChars[i] >= '0' && lineChars[i] <= '9') {
					i0 = 1.0;
					i1[0] = lineChars[i] - '0';
					state = 41;
					++i1c;
				} else {
					// invalid input
					state = -103;
					break;
				}
			} else if (state == 41 && lineChars[i] == '.') {
				state = 42;
			} else if ((state == 41 || state == 42) && lineChars[i] == ',') {				
				// convert i0, i1, i2 to double...
				int i1coefficient = 1;
				for (int j = 1; j < i1c; ++j) {
					i1coefficient *= 10;
				}
				
				for (int j = 0; j < i1c; ++j) {
					latitude += i1coefficient * i1[j];
					i1coefficient /= 10;
				}
				
				double i2coefficient = 0.1;
				for (int j = 0; j < i2c; ++j) {
					latitude += i2coefficient * i2[j];
					i2coefficient /= 10.0;
				}
				latitude *= i0;
				
				// reset
				i1c = 0;
				i2c = 0;
				state = 50;
				break;
			} else if (state == 41) {
				if (i1c == 4) {
					state = -104;
					break;
				}
				i1[i1c] = lineChars[i] - '0';
				++i1c;
			} else if (state == 42) {
				if (lineChars[i] < '0' || lineChars[i] > '9') {
					state = -105;
					break;
				}
				if (i2c != 12) { 
					i2[i2c] = lineChars[i] - '0';
					++i2c;
				}		
			}				
		}
		
		if (state < 0) {
			return false;
		}
		
		String dateString = new String(dateBuffer);
		
//		System.out.println("lp5: lon:" + longitude + ", lat:" + latitude);
				
		xyt[0] = bc.getX(longitude);
		xyt[1] = bc.getY(latitude);
		
		long time = timeLong;
		
		int dateInt = 0;

		try {
			dateInt = Integer.parseInt(dateString);
		} catch (NumberFormatException e) {
			// do nothing
		}
		
		if (dateInt == 0) {
			return false;
		}
		
		if (dateMap.containsKey(dateInt)) {
			time += dateMap.get(dateInt);
		} else {
			try {
				long t = sdf.parse(dateString).getTime();
				dateMap.put(dateInt, t);
				time += t;
			} catch (ParseException e) {
				// do nothing
			}
		}
		
		xyt[2] = bc.getT(time);
		
		return true;
	}
}
