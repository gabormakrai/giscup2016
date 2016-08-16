package giscup.top50;

import giscup.tools.SpaceTimeCoordinateValue;

public class Top50Storage2 {
	
	private final int[] xArray = new int[50];
	private final int[] yArray = new int[50];
	private final int[] tArray = new int[50];
	private final int[] vArray = new int[50];
	
    public int indexOf(int v) {
        int lo = 0;
        int hi = 49;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if      (v > vArray[mid]) hi = mid - 1;
            else if (v < vArray[mid]) lo = mid + 1;
            else return mid;
        }
        return lo;
    }
    
	
	public void add(int x, int y, int t, int v) {
		
		// if v < vArray[49] then do nothing
		if (v < vArray[49]) {
			return;
		}
		    	
    	// find out the position
    	int newIndex = indexOf(v);
    	    	
    	// shift arrays
    	for (int i = 49; i > newIndex; --i) {
    		xArray[i] = xArray[i - 1];
    		yArray[i] = yArray[i - 1];
    		tArray[i] = tArray[i - 1];
    		vArray[i] = vArray[i - 1];
    	}

    	// put the new item in position
    	xArray[newIndex] = x;
    	yArray[newIndex] = y;
    	tArray[newIndex] = t;
    	vArray[newIndex] = v;
	}
	
	public SpaceTimeCoordinateValue[] getTop50() {
		SpaceTimeCoordinateValue[] top50 = new SpaceTimeCoordinateValue[50];
		for (int i = 0; i < 50; ++i) {
			top50[i] = new SpaceTimeCoordinateValue(xArray[i], yArray[i], tArray[i], vArray[i]);
		}
		return top50;
	}
	
	public void merge(Top50Storage2 otherStorage) {
		for (int i = 0; i < 50; ++i) {
			add(otherStorage.xArray[i], otherStorage.yArray[i], otherStorage.tArray[i], otherStorage.vArray[i]);
		}
	}
	
}
