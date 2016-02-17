package giscup.tools;

public class SpaceTimeValueTuple implements Comparable<SpaceTimeValueTuple> {
	
	public int x;
	public int y;
	public int t;
	public int v;
	
	public SpaceTimeValueTuple(int x, int y, int t, int v) {
		this.x = x;
		this.y = y;
		this.t = t;
		this.v = v;
	}

	@Override
	public int compareTo(SpaceTimeValueTuple o) {
		return o.v - this.v;
	}
	
	@Override
	public String toString() {
		return "SpaceTimeValueTuple(x:" + x + ",y:" + y + ",t:" + t +",v:" + v + ")";
	}
	
}
