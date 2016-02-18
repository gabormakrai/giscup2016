package giscup.main;

import giscup.tools.BoundaryCalculator;
import giscup.tools.SpaceTimeCoordinate;
import giscup.tools.SpaceTimeValueTuple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class Step3Main {
	
	public static void main(String[] args) throws IOException {
		
		BoundaryCalculator bc = BoundaryCalculator.getInstance();
		
		String inputDir = "d:\\data\\giscup2016\\step2";
		
		HashMap<SpaceTimeCoordinate, AtomicInteger> map1 = new HashMap<>();
		LinkedList<SpaceTimeValueTuple> listForTheNextRound = new LinkedList<>();
		
		int largestT = -1;
		SpaceTimeValueTuple[] top100 = null;
		
		for (File f : new File(inputDir).listFiles()) {
			
			map1.clear();
			for (SpaceTimeValueTuple stvt : listForTheNextRound) {
				map1.put(new SpaceTimeCoordinate(stvt.x, stvt.y, stvt.t), new AtomicInteger(stvt.v));
			}
			listForTheNextRound.clear();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line = "";
			
			while ((line = br.readLine()) != null) {
				
				String[] splittedLine = line.split("\\,");
				int x = Integer.parseInt(splittedLine[0]);
				int y = Integer.parseInt(splittedLine[1]);
				int t = Integer.parseInt(splittedLine[2]);
				int v = Integer.parseInt(splittedLine[3]);
				
				if (t > bc.tMax) {
					continue;
				}
				
				if (t > largestT) {
					largestT = t;
				}
				
				for (int i = -1; i <= +1; ++i) {
					for (int j = -1; j <= +1; ++j) {
						for (int k = -1; k <= +1; ++k) {
							int x1 = x + i;
							int y1 = y + j;
							int t1 = t + k;
							if (x1 < 0 || y1 < 0 || t1 < 0 || x1 >= bc.xMax || y1 >= bc.yMax || t1 >= bc.tMax) {
								continue;
							}
							SpaceTimeCoordinate stc = new SpaceTimeCoordinate(x1, y1, t1);
							AtomicInteger aInt  = map1.get(stc);
							if (aInt == null) {
								aInt = new AtomicInteger(0);
								map1.put(stc, aInt);
							}
							aInt.addAndGet(v);
						}
					}
				}
			}
			
			br.close();

			LinkedList<SpaceTimeValueTuple> list = new LinkedList<>();
			for (Entry<SpaceTimeCoordinate, AtomicInteger> entry : map1.entrySet()) {
				SpaceTimeCoordinate stc = entry.getKey();
				SpaceTimeValueTuple stvt = new SpaceTimeValueTuple(stc.x, stc.y, stc.t, entry.getValue().get());
				if (stc.t < largestT - 24) {
					list.add(stvt);
				} else {
					listForTheNextRound.add(stvt);
				}
			}
			
			SpaceTimeValueTuple[] array = list.toArray(new SpaceTimeValueTuple[0]);
			Arrays.sort(array);
						
			System.out.println("" + f.getName() + " -> " + map1.size());
			System.out.println("largestT: " + largestT);
			
			if (top100 == null) {
				top100 = new SpaceTimeValueTuple[100];
				for (int i = 0; i < 50; ++i) {
					top100[i] = array[i];
				}
			} else {
				for (int i = 0; i < 50; ++i) {
					top100[50 + i] = array[i];
				}
				Arrays.sort(top100);
			}

			for (int i = 0; i < 50; ++i) {
				System.out.println(top100[i]);
			}
			
		}
	}
}
