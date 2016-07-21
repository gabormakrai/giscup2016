package giscup.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import giscup.tools.BoundaryCalculator;
import giscup.tools.SpaceTimeCoordinate;
import giscup.tools.SpaceTimeValueTuple;

public class Reference1Step2Main {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		
		BoundaryCalculator bc = BoundaryCalculator.giscupBoundaryCalculator;
		SpaceTimeCoordinate.xSize = bc.xSize;
		SpaceTimeCoordinate.ySize = bc.ySize;
		SpaceTimeCoordinate.tSize = bc.tSize;
		
		ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream("/home/makrai/data/referenceArray.obj")));
		int[] events = (int[]) ois.readObject();
		ois.close();
		
		long t1 = System.nanoTime();
		
		int[] eventCounter = new int[(bc.xMax + 1) * (bc.yMax + 1) * (bc.tMax + 1)];
		for (int i = 0; i < events.length; ++i) {
			++eventCounter[events[i]];
		}
		
		int zeroItems = 0;
		int sum = 0;
		long sum2 = 0;
		for (int i = 0; i < eventCounter.length; ++i) {
			if (eventCounter[i] == 0) {
				++zeroItems;
			}
			sum += eventCounter[i];
			sum2 += eventCounter[i] * eventCounter[i];
		}
				
		int index = 0;
		SpaceTimeValueTuple[] result = new SpaceTimeValueTuple[51];
				
		for (int x = 0; x <= bc.xMax; ++x) {
			for (int y = 0; y <= bc.yMax; ++y) {
				for (int t = 0; t <= bc.tMax; ++t) {
					SpaceTimeValueTuple stvc = processOneCell(x, y, t, eventCounter, bc);
					if (index < 50) {
						result[index] = stvc;
						++index;
					} else {
						result[50] = stvc;
						Arrays.sort(result);
					}
				}
			}
		}
		long t2 = System.nanoTime();
		
		System.out.println("Final result:");
		for (int i = 0; i < 50; ++i) {
			System.out.println(result[i]);
		}
		
		System.out.println("eventCounter.length = " + eventCounter.length);
		System.out.println("zeroItems: " + zeroItems);
		System.out.println("non-zeroItems: " + (eventCounter.length - zeroItems));

		System.out.println("sum: " + sum);
		System.out.println("sum2: " + sum2);
		
		System.out.println("Elapsed time: " + ((t2 - t1) / 1000000 ) + "ms");
	}
	
	public static SpaceTimeValueTuple processOneCell(int x, int y, int t, int[] eventCounter, BoundaryCalculator bc) {
		
		int value = 0;
		for (int x1 = -1; x1 <= 1; ++x1) {
			for (int y1 = -1; y1 <= 1; ++y1) {
				for (int t1 = -1; t1 <= 1; ++t1) {
					int index = (t + t1) * bc.yMax * bc.xMax + (y + y1) * bc.xMax + (x + x1);
					if (index >= 0 && index < eventCounter.length) {
						value += eventCounter[index];
					}
				}
			}
		}
		
		return new SpaceTimeValueTuple(x, y, t, value);
	}
}