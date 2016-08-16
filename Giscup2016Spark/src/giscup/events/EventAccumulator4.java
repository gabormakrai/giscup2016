package giscup.events;

import giscup.tools.SpaceTimeCoordinate;
import giscup.top50.Top50Storage2;

public class EventAccumulator4 {
	
	final int[] array;
	final int xSize;
	final int ySize;
	final int tSize;
	
	public EventAccumulator4(int xSize, int ySize, int tSize) {
		this.array = new int[xSize * ySize * tSize];
		this.xSize = xSize;
		this.ySize = ySize;
		this.tSize = tSize;
	}
	
	public void eventIndex(int index, int counter) {
		this.array[index] += counter;
	}
	
	public void top50(Top50Storage2 top50) {
		for (int x = 0; x < xSize; ++x) {
			for (int y = 0; y < ySize; ++y) {
				for (int t = 0; t < tSize; ++t) {
					int value = calculateValueForOneCell(x, y, t);
					top50.add(x, y, t, value);
				}
			}
		}
	}
		
	private int calculateValueForOneCell(int x, int y, int t) {
		int value = 0;
		for (int dx = -1; dx <= 1; ++dx) {
			for (int dy = -1; dy <= 1; ++dy) {
				for (int dt = -1; dt <= 1; ++dt) {
					int x1 = x + dx;
					int y1 = y + dy;
					int t1 = t + dt;
			        if (x1 >= 0 && x1 < xSize &&
			        		y1 >= 0 && y1 < ySize &&
			        		t1 >= 0 && t1 < tSize) {
			        	int index = SpaceTimeCoordinate.hash(x1, y1, t1);
			        	value += array[index];
		          }
				}
			}
		}
		return value;
	}
	
	public long calculateSumX() {
		long sum = 0;
		for (int i : array) {
			sum += i;
		}
		return sum;
	}
	
	public long calculateSumX2() {
		long sum = 0;
		for (int i : array) {
			sum += i * i;
		}
		return sum;
	}
	
	public int getN() {
		return array.length;
	}
	  
}
