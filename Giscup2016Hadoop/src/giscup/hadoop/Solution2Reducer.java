package giscup.hadoop;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import giscup.tools.SpaceTimeCoordinate;
import giscup.tools.SpaceTimeValueTuple;

public class Solution2Reducer extends Reducer<IntWritable, Text, Text, NullWritable>{
	
	@Override
	protected void reduce(IntWritable partition, Iterable<Text> valueIterator, Context context) throws IOException, InterruptedException {

		HashMap<SpaceTimeCoordinate, AtomicInteger> map = new HashMap<>();
		
		for (Text value : valueIterator) {
			String[] splittedValue = value.toString().split("\\,");
			int x = Integer.parseInt(splittedValue[0]);
			int y = Integer.parseInt(splittedValue[1]);
			int t = Integer.parseInt(splittedValue[2]);
			int v = Integer.parseInt(splittedValue[3]);
			
			for (int i = -1; i <= +1; ++i) {
				for (int j = -1; j <= +1; ++j) {
					for (int k = -1; k <= +1; ++k) {
						SpaceTimeCoordinate stc = new SpaceTimeCoordinate(x + i, y + j, t + k);
						if (!map.containsKey(stc)) {
							map.put(stc, new AtomicInteger(v));
						} else {
							map.get(stc).addAndGet(v);
						}
					}
				}
			}
		}		
		
		LinkedList<SpaceTimeValueTuple> list = new LinkedList<>();
		for (Entry<SpaceTimeCoordinate, AtomicInteger> entry : map.entrySet()) {
			SpaceTimeCoordinate stc = entry.getKey();
			SpaceTimeValueTuple stvt = new SpaceTimeValueTuple(stc.x, stc.y, stc.t, entry.getValue().get());
			list.add(stvt);
		}
		
		SpaceTimeValueTuple[] array = list.toArray(new SpaceTimeValueTuple[0]);
		Arrays.sort(array);
		
		Text outputText = new Text();
		
		for (int i = 0; i < Math.min(array.length, 50); ++i) {
			outputText.set(array[i].toString());
			context.write(outputText, NullWritable.get());
		}
		
	}
	
}
