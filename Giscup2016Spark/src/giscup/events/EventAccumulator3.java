package giscup.events;

import java.util.Arrays;
import java.util.LinkedList;

public class EventAccumulator3 {
	
	private final int[][] arrays;
	private final int partitionSize;
	private final int ySize;
	private final int xSize; 
	
	public EventAccumulator3(int xSize, int ySize, int tSize) {
		this.partitionSize = xSize * ySize;
		this.arrays = new int[tSize][];
		this.xSize = xSize;
		this.ySize = ySize;
	}
	
	public void event(int[] xyt) {
		int x = xyt[0];
		int y = xyt[1];
		int t = xyt[2];
		if (arrays[t] == null) {
			arrays[t] = new int[partitionSize];
		}
		int index = y * xSize + x;
		++arrays[t][index];
	}
		
	public long[] getAll() {
		
		int returnArraySize = 0;

		// find out the size of the return array
		for (int t = 0; t < arrays.length; ++t) {
			if (arrays[t] == null) {
				continue;
			}
			for (int c : arrays[t]) {
				if (c != 0) {
					++returnArraySize;
				}
			}
		}
		
		// construct the return array
		long[] returnArray = new long[returnArraySize];
		int returnArrayIndex = 0;
		
		// 
		for (int t = 0; t < arrays.length; ++t) {
			if (arrays[t] == null) {
				continue;
			}
			int tHash = t * xSize * ySize;
			for (int y = 0; y < ySize; ++y) {
				int yIndex = y * xSize;
				for (int x = 0; x < xSize; ++x) {
					int index = yIndex + x;
					if (arrays[t][index] == 0) {
						continue;
					}
					int hash = tHash + index;
					long v = (long)hash * ((long)Integer.MAX_VALUE + 1L) + arrays[t][index]; 
					returnArray[returnArrayIndex] = v;
					++returnArrayIndex;
				}
			}
		}
		
		return returnArray;
	}
	
	public String getStatistics() {
		LinkedList<Integer> usedPartitions = new LinkedList<>();
		int usedArray = 0;
		for (int i = 0; i < arrays.length; ++i) {
			if (arrays[i] != null) {
				++usedArray;
				usedPartitions.add(i);
			}
		}
		return "EventAccumulatorStat: " + usedArray + " / " + arrays.length + "\nPartitions in use: " + Arrays.toString(usedPartitions.toArray(new Integer[0]));
	}
	
}
