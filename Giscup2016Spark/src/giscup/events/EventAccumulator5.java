package giscup.events;

import giscup.tools.SpaceTimeCoordinate;
import giscup.top50.Top50Storage2;

/**
 * 
 * EventAccumulator class
 * 
 * Improved version of the EventAccumulator4 class where the top50 calculation is multi-threaded
 *
 */
public class EventAccumulator5 {
	
	final int[] array;
	final int xSize;
	final int ySize;
	final int tSize;
	
	public EventAccumulator5(int xSize, int ySize, int tSize) {
		this.array = new int[xSize * ySize * tSize];
		this.xSize = xSize;
		this.ySize = ySize;
		this.tSize = tSize;
	}
	
	public void eventIndex(int index, int counter) {
		this.array[index] += counter;
	}
	
	public void top50(Top50Storage2 top50, int threads) {
		WorkerThread[] wThreads = new WorkerThread[threads];
		for (int i = 0; i < threads; ++i) {
			wThreads[i] = new WorkerThread(i, threads, xSize, ySize, tSize, array);
		}
		for (int i = 0; i < threads; ++i) {
			wThreads[i].start();
		}
		for (int i = 0; i < threads; ++i) {
			try {
				wThreads[i].join();
			} catch (InterruptedException e) {
				// do nothing...
			}
		}
		for (int i = 0; i < threads; ++i) {
			top50.merge(wThreads[i].topStorage);
		}		
	}
	
	class WorkerThread extends Thread {
		int xSize;
		int ySize;
		int tSize;
		int[] array;
		int t0;
		int t1;
		public Top50Storage2 topStorage = new Top50Storage2();
		
		public WorkerThread(int offset, int threads, int xSize, int ySize, int tSize, int[] array) {
			t0 = (int)(offset * (double)tSize / (double)threads);
			t1 = (int)((offset+1) * (double)tSize / (double)threads);
			this.xSize = xSize;
			this.ySize = ySize;
			this.tSize = tSize;
		}
		
		@Override
		public void run() {
			for (int x = 0; x < xSize; ++x) {
				for (int y = 0; y < ySize; ++y) {
					for (int t = t0; t < t1 ; ++t) {
						int value = calculateValueForOneCell(x, y, t);
						topStorage.add(x, y, t, value);
					}
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
