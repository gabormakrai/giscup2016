package giscup.tools;

public class SpaceTimeCoordinate {
		
	static final int xMax;
	static final int yMax;
	static final int tMax;
	
	static {
		BoundaryCalculator bc = BoundaryCalculator.getInstance();
		xMax = bc.xMax;
		yMax = bc.yMax;
		tMax = bc.tMax;
	}
	
	public int x;
	public int y;
	public int t;
	
	public SpaceTimeCoordinate(int x, int y, int t) {
		this.x = x;
		this.y = y;
		this.t = t;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if ( this == obj ) {
			return true;
		}
		
		if (!(obj instanceof SpaceTimeCoordinate)) {
			return false;
		}
		
		SpaceTimeCoordinate spc = (SpaceTimeCoordinate)obj;
		if (spc.x == this.x && spc.y == this.y && spc.t == this.t) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return t * yMax * xMax + y * xMax + x;
	}
	
	@Override
	public String toString() {
		return "SpaceTimeCoordinate(x:" + this.x + ",y:" + this.y + ",t:" + this.t + ")";
	}
}
