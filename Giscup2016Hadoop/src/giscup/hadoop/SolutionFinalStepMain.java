package giscup.hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;

import giscup.tools.SpaceTimeValueTuple;

public class SolutionFinalStepMain {
	
	public static void main(String[] args) throws IOException {
		
		String inputDir = "/media/sf_D_DRIVE/Data/GisCup2016/step2v4";		
		
		LinkedList<SpaceTimeValueTuple> list = new LinkedList<>();
		for (File f : new File(inputDir).listFiles()) {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			
			String line = "";
			
			while ((line = br.readLine()) != null) {
				String s1 = line.substring(22);
				s1 = s1.substring(0, s1.indexOf(','));
				String s2 = line.substring(22);
				s2 = s2.substring(s2.indexOf(':')+1);
				s2 = s2.substring(0, s2.indexOf(','));
				String s3 = line.substring(22);
				s3 = s3.substring(s3.indexOf(':')+1);
				s3 = s3.substring(s3.indexOf(':')+1);
				s3 = s3.substring(0, s3.indexOf(','));
				String s4 = line.substring(22);
				s4 = s4.substring(s4.indexOf(':')+1);
				s4 = s4.substring(s4.indexOf(':')+1);
				s4 = s4.substring(s4.indexOf(':')+1);
				s4 = s4.substring(0, s4.indexOf(')'));
				
				int x = Integer.parseInt(s1);
				int y = Integer.parseInt(s2);
				int t = Integer.parseInt(s3);
				int v = Integer.parseInt(s4);
				
				SpaceTimeValueTuple stvt = new SpaceTimeValueTuple(x, y, t, v);
				list.add(stvt);
			}
			
			br.close();
		}
		
		SpaceTimeValueTuple[] array = list.toArray(new SpaceTimeValueTuple[0]);
		Arrays.sort(array);
		for (int i = 0; i < 50; ++i) {
			System.out.println(array[i]);
		}
	}
}
