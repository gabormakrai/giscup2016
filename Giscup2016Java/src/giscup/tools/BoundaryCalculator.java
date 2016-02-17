package giscup.tools;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BoundaryCalculator {

	public static BoundaryCalculator instance = null;
	public static BoundaryCalculator getInstance() {
		if (instance == null) {
			instance = new BoundaryCalculator();
		}
		return instance;
	}
	
	public final int timePartitions = 199; // 0-199
	
	public final double long1;
	public final double long3;
	public final double lat1;
	public final double lat3;
	public final long time1;
	public final int xMax;
	public final int yMax;
	public final int tMax;
	
	public BoundaryCalculator() {
		lat1 = 40.5;
		long1 = -73.7; 
		double lat2 = 40.9;
		double long2 = -74.25;
		
		xMax = (int)Math.floor(Haversine.haversine(lat1, long1, lat1, long2) / 0.2);
		long3 = long1 + (long2 - long1) * xMax / (Haversine.haversine(lat1, long1, lat1, long2) / 0.2);

		yMax = (int)Math.floor(Haversine.haversine(lat1, long1, lat2, long1) / 0.2);
		lat3 = lat1 + (lat2 - lat1) * yMax / (Haversine.haversine(lat1, long1, lat2, long1) / 0.2);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = sdf.parse("2009-01-01 00:00:00");
			Date d2 = sdf.parse("2015-07-01 00:00:00");
			
			time1 = d1.getTime();
			tMax = (int)((d2.getTime() - time1) / 7200000);
			
		} catch (ParseException e) {
			throw new RuntimeException("Problem: ", e);
		}
			
	}
	
	public int getX(double longitude) {
		if (longitude < long3 || longitude > long1) {
			return -1;
		}
		double dt = (long3 - long1) / xMax;
		return (int)Math.floor((longitude - long1) / dt);
	}
	
	public int getY(double latitude) {
		if (latitude > lat3 || latitude < lat1) {
			return -1;
		}
		double dt = (lat3 - lat1) / yMax;
		return (int)Math.floor((latitude - lat1) / dt);
	}
	
	public int getT(long time) {
		return (int)((time - time1) / 7200000);
	}
	
	public int[] getTimePartition(int t) {
		
		int partition = t / (tMax / timePartitions);
				
		if (partition != 0 && (t % (tMax / timePartitions) == 0 || t % (tMax / timePartitions) == 1)) {
			return new int[] { partition, partition - 1};
		} else if (t % (tMax / timePartitions) == (tMax / timePartitions) - 1 || t % (tMax / timePartitions) == (tMax / timePartitions) - 2) {
			return new int[] { partition, partition + 1};
		} else {
			return new int[] { partition };
		}
	}
}
