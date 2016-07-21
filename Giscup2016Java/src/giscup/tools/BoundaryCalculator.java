package giscup.tools;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BoundaryCalculator {
		
	public static BoundaryCalculator giscupBoundaryCalculator = new BoundaryCalculator(-74.25, -73.70, 40.5, 40.9, 0.001, "2015-01-01 00:00:00", "2016-01-01 00:00:00", 7, 25);
	
	public final int timePartitions;
	
	public final double long0;
	public final double long3;
	public final double lat0;
	public final double lat3;
	public final double degreeParameter;
	
	public final long time1;
	public final long timeParameter; // in ms
	
	public final int xMax;
	public final int yMax;
	public final int tMax;
	
	public final int xSize;
	public final int ySize;
	public final int tSize;
	
	private final SimpleDateFormat sdf;
		
	public BoundaryCalculator(double long1, double long2, double lat1, double lat2, double degreeParameter, String date1, String date2, int timeParameter, int timePartitions) {
		
		this.degreeParameter = degreeParameter;
		
		double x = (long)(long1 / degreeParameter);
		x = x * degreeParameter;
		if (x > long1) x -= degreeParameter;
		this.long0 = x;

		x = (long)(long2 / degreeParameter);
		x = x * degreeParameter;
		if (x < long2) x += degreeParameter;
		this.long3 = x;
		
		xMax = (int)((long3 - long0) / degreeParameter) + 1;
		
		x = (long)(lat1 / degreeParameter);
		x = x * degreeParameter;
 		if (x > lat1) x -= degreeParameter;
		this.lat0 = x;

		x = (long)(lat2 / degreeParameter);
		x = x * degreeParameter;
		if (x < lat2) x += degreeParameter;
		this.lat3 = x;
		
		yMax = (int)((lat3 - lat0) / degreeParameter) + 1;
		
		this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		this.timeParameter = 24 * 3600 * 1000 * timeParameter ;
		
		try {
			Date d1 = sdf.parse(date1);
			Date d2 = sdf.parse(date2);
			
			time1 = d1.getTime();
			tMax = (int)((d2.getTime() - time1) / (this.timeParameter));
			
		} catch (ParseException e) {
			throw new RuntimeException("Problem: ", e);
		}
		
		this.timePartitions = timePartitions;
		this.xSize = this.xMax + 1;
		this.ySize = this.yMax + 1;
		this.tSize = this.tMax + 1;
	}
	
	public int getX(double longitude) {
		if (longitude < long0 || longitude > long3) {
			return -1;
		}
		return (int)Math.floor((longitude - long0) / degreeParameter);
	}
	
	public int getY(double latitude) {
		if (latitude < lat0 || latitude > lat3) {
			return -1;
		}
		return (int)Math.floor((latitude - lat0) / degreeParameter);
	}
	
	@Deprecated
	public int getT(String timestamp) {
		int t = -1;
		try {
			Date d = sdf.parse(timestamp);
			t = (int)((d.getTime() - time1) / (this.timeParameter));
		} catch (ParseException e) {
			t = -2;
		}
		return t;
	}
	
	public int getT(long time) {
		return (int)((time - time1) / this.timeParameter);
	}
	
	public void getTimePartition(int t, int[] partitionIds) {
		
		int partition = (int)((double)t / (double)tSize * (timePartitions));
		int p1 = (int)((double)(t - 1) / (double)tSize * (timePartitions));
		int p2 = (int)((double)(t + 1) / (double)tSize * (timePartitions));
		
		if (partition != p1 && partition == p2) {
			partitionIds[0] = partition;
			partitionIds[1] = partition - 1;
		} else if (t != tMax && partition != p2 && partition == p1) {
			partitionIds[0] = partition;
			partitionIds[1] = partition + 1;
		} else {
			partitionIds[0] = partition;
			partitionIds[1] = -1;
		}
	}
}
