package giscup.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

import giscup.lineparsers.LineParser;
import giscup.lineparsers.LineParser1;
import giscup.tools.BoundaryCalculator;
import giscup.tools.SpaceTimeCoordinate;

public class Reference1Step1Main {

	public static void main(String[] args) throws IOException {
		
		BoundaryCalculator bc = BoundaryCalculator.giscupBoundaryCalculator;
		SpaceTimeCoordinate.xSize = bc.xSize;
		SpaceTimeCoordinate.ySize = bc.ySize;
		SpaceTimeCoordinate.tSize = bc.tSize;
		
		String inputDir = "/media/sf_GiscupCache/original2015/";
		
		int[] data = new int[143744399];
		int dataIndex = 0;
		System.out.println(data.length);
		for (int i = 0; i < data.length; ++i) {
			data[i] = 0;
		}
		
		int[] xyt = new int[] {0, 0, 0};
		
		LineParser lineParser1 = new LineParser1(bc);
				
		for (File f : new File(inputDir).listFiles()) {
						
			int counter = 0;
			System.out.println(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f.getAbsoluteFile())));
			String line = "";
						
			while ((line = br.readLine()) != null) {
				
				if (!lineParser1.parseLine(xyt, line)) {
					continue;
				}
								
				int x = xyt[0];
				int y = xyt[1];
				int t = xyt[2];
				
				if (x > -1 && y > -1 && y > -1 && t > -1 && x <= bc.xMax && y <= bc.yMax && t <= bc.tMax) {
					SpaceTimeCoordinate stc = new SpaceTimeCoordinate(x, y, t);
					++counter;
					int index = stc.hashCode();
					data[dataIndex] = index;
					++dataIndex;
				}
			}
			
			System.out.println("counter = " + counter);
			br.close();
		}
		
		ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("/home/makrai/data/referenceArray.obj")));
		oos.writeObject(data);
		oos.close();
		
	}			
}
