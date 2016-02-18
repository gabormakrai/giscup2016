package giscup.main;

import giscup.tools.BoundaryCalculator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Step1Main {
	
	public static void main(String[] args) throws IOException, ParseException {
		
		String inputDir = "d:\\data\\giscup2016\\original";
		
		String outputDir = "d:\\data\\giscup2016\\step1\\";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BoundaryCalculator bc = BoundaryCalculator.getInstance();
		
		for (File f : new File(inputDir).listFiles()) {
			int counter1 = 0;
			int counter2 = 0;
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDir + f.getName())));
			String line = "";
			while ((line = br.readLine()) != null) {
				++counter1;
				String[] splittedLine = line.split("\\,");
				if (splittedLine.length > 11 && isDouble(splittedLine[9])) {
					
					String timestampString = splittedLine[2];
					String longitudeString = splittedLine[9];
					String latitudeString = splittedLine[10];
					
					Date timestamp = sdf.parse(timestampString);
					
					double longitude = Double.parseDouble(longitudeString);
					double latitude = Double.parseDouble(latitudeString);
					
					int x = bc.getX(longitude);
					int y = bc.getY(latitude);
					int t = bc.getT(timestamp.getTime());
					
					if (x > -1 && y > -1 && y > -1 && t > -1 && x <= bc.xMax && y <= bc.yMax && t <= bc.tMax) {
						bw.write("" + x + "," + y + "," + t + "\n" );
						++counter2;
					}
				}
			}
			System.out.println("" + f.getName() + ": " + counter2 + "/" + counter1);
			
			br.close();
			bw.close();
		}
	}
	
	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
}
