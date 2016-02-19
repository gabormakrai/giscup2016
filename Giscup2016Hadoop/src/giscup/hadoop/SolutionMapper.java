package giscup.hadoop;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import giscup.tools.BoundaryCalculator;
import giscup.tools.SpaceTimeCoordinate;

public class SolutionMapper extends Mapper<LongWritable, Text, IntWritable, Text>{

	private boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	private static final BoundaryCalculator bc = BoundaryCalculator.getInstance();
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
	private final HashMap<SpaceTimeCoordinate, AtomicInteger> map = new HashMap<>();
	
	@Override
	protected void setup(Mapper<LongWritable, Text, IntWritable, Text>.Context context) throws IOException, InterruptedException {
		map.clear();
	}
	
	@Override
	protected void cleanup(Mapper<LongWritable, Text, IntWritable, Text>.Context context) throws IOException, InterruptedException {
		
		Text outputText = new Text();
		
		IntWritable outputInteger = new IntWritable();
				
		for (Entry<SpaceTimeCoordinate, AtomicInteger> entry : map.entrySet()) {
			
			SpaceTimeCoordinate stc = entry.getKey();
			
			int[] partitions = bc.getTimePartition(stc.t);
			
			outputText.set("" + stc.x + "," + stc.y + "," + stc.t + "," + entry.getValue().intValue());
			
			for (int p : partitions) {
				outputInteger.set(p);
				context.write(outputInteger, outputText);
			}
		}
	}
	
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, IntWritable, Text>.Context context) throws IOException, InterruptedException {
		
        String line = value.toString();
        
        String[] splittedLine = line.split("\\,");
        
		if (splittedLine.length > 11 && isDouble(splittedLine[9])) {
			
			String timestampString = splittedLine[2];
			String longitudeString = splittedLine[9];
			String latitudeString = splittedLine[10];
			try {
				Date timestamp = sdf.parse(timestampString);
			
				double longitude = Double.parseDouble(longitudeString);
				double latitude = Double.parseDouble(latitudeString);
				
				int x = bc.getX(longitude);
				int y = bc.getY(latitude);
				int t = bc.getT(timestamp.getTime());
				
				if (x > -1 && y > -1 && y > -1 && t > -1 && x <= bc.xMax && y <= bc.yMax && t <= bc.tMax) {
					
					for (int i = -1; i <= +1; ++i) {
						for (int j = -1; j <= +1; ++j) {
							for (int k = -1; k <= +1; ++k) {
								SpaceTimeCoordinate stc = new SpaceTimeCoordinate(x + i, y + j, t + k);
								if (!map.containsKey(stc)) {
									map.put(stc, new AtomicInteger(1));
								} else {
									map.get(stc).incrementAndGet();
								}
							}
						}
					}
					
				}
			} catch (ParseException e) {
				// do nothing
			}
		}
	}
	
}
