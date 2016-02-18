package giscup.main;

import giscup.tools.BoundaryCalculator;
import giscup.tools.SpaceTimeCoordinate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class Step2v2Main {

	public static void main(String[] args) throws IOException {
		
		String inputDir = "d:\\data\\giscup2016\\step1";
		String outputDir = "d:\\data\\giscup2016\\step2v2\\";
		
		BoundaryCalculator bc = BoundaryCalculator.getInstance();
		
		BufferedWriter[] bws = new BufferedWriter[bc.timePartitions+1];
		
		for (int p = 0; p < bws.length; ++p) {
			bws[p] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputDir + "part_" + p + ".txt")));
		}
		
		for (File f : new File(inputDir).listFiles()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			HashMap<SpaceTimeCoordinate, AtomicInteger> map = new HashMap<>();
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] splittedLine = line.split("\\,");
				int x = Integer.parseInt(splittedLine[0]);
				int y = Integer.parseInt(splittedLine[1]);
				int t = Integer.parseInt(splittedLine[2]);
				SpaceTimeCoordinate stc = new SpaceTimeCoordinate(x, y, t);
				AtomicInteger i = map.get(stc);
				if (i == null) {
					i = new AtomicInteger(0);
					map.put(stc, i);
				}
				i.incrementAndGet();
			}
			br.close();
			
			for (Entry<SpaceTimeCoordinate, AtomicInteger> entry : map.entrySet()) {
				SpaceTimeCoordinate stc = entry.getKey();
				AtomicInteger value = entry.getValue();
				
				int[] timePartition = bc.getTimePartition(stc.t);
				for (int i : timePartition) {
					bws[i].write("" + stc.x + "," + stc.y + "," + stc.t + "," + value.get() + "\n");
				}
			}
			//break;
		}
		
		for (int p = 0; p < bws.length; ++p) {
			bws[p].close();
		}
	}

}
