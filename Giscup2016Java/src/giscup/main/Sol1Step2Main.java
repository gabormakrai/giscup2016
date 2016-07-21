package giscup.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import com.koloboke.collect.map.IntIntCursor;
import com.koloboke.collect.map.hash.HashIntIntMap;
import com.koloboke.collect.map.hash.HashIntIntMaps;

import giscup.tools.BoundaryCalculator;
import giscup.tools.SpaceTimeCoordinate;
import giscup.tools.SpaceTimeValueTuple;

public class Sol1Step2Main {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		
		BoundaryCalculator bc = BoundaryCalculator.giscupBoundaryCalculator;
		SpaceTimeCoordinate.xSize = bc.xSize;
		SpaceTimeCoordinate.ySize = bc.ySize;
		SpaceTimeCoordinate.tSize = bc.tSize;
		
		ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream("/home/makrai/data/referenceArray.obj")));
		int[] events = (int[]) ois.readObject();
		ois.close();
		
		long time1 = System.nanoTime();
		
		SpaceTimeValueTuple[] result = new SpaceTimeValueTuple[51];
		int resultIndex = 0;
		
		HashIntIntMap map = HashIntIntMaps.newUpdatableMap();
		
		for (int event : events) {
			map.addValue(event, 1);
		}
		
		HashIntIntMap map2 = HashIntIntMaps.newUpdatableMap();
		
		for (IntIntCursor cursor = map.cursor(); cursor.moveNext();) {
			int index = cursor.key();
			int value = cursor.value();
			int t = index / (bc.xMax * bc.yMax);
			index = index % (bc.xMax * bc.yMax);
			int y = index / bc.xMax;
			index = index % bc.xMax;
			int x = index;
			for (int x1 = -1; x1 <= 1; ++x1) {
				for (int y1 = -1; y1 <= 1; ++y1) {
					for (int t1 = -1; t1 <= 1; ++t1) {
						int index2 = (t + t1) * bc.yMax * bc.xMax + (y + y1) * bc.xMax + (x + x1);
						map2.addValue(index2, value);
					}
				}
			}
		}
		
		for (IntIntCursor cursor = map2.cursor(); cursor.moveNext();) {
			int index = cursor.key();
			int value = cursor.value();
			int t = index / (bc.xMax * bc.yMax);
			index = index % (bc.xMax * bc.yMax);
			int y = index / bc.xMax;
			index = index % bc.xMax;
			int x = index;
			if (resultIndex < 50) {
				SpaceTimeValueTuple stvt = new SpaceTimeValueTuple(x, y, t, value);
				result[resultIndex] = stvt;
				++resultIndex;
			} else if (resultIndex == 50){
				SpaceTimeValueTuple stvt = new SpaceTimeValueTuple(x, y, t, value);
				result[50] = stvt;
				Arrays.sort(result);
				++resultIndex;
			} else {
				SpaceTimeValueTuple stvt = result[50];
				stvt.x = x;
				stvt.y = y;
				stvt.t = t;
				stvt.v = value;
				Arrays.sort(result);
			}
		}		
		
		long time2 = System.nanoTime();
		
		System.out.println(map.size());
		System.out.println(map2.size());
		
		System.out.println("Final result:");
		for (int i = 0; i < 50; ++i) {
			System.out.println(result[i]);
		}
		
		System.out.println("Elapsed time: " + ((time2 - time1) / 1000000 ) + "ms");
		
	}

}
