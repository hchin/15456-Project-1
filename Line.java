
public class Line {
	private Point p1, p2;
	
	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public Line(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Line(int x1, int y1, int x2, int y2) {
		this(new Point(x1, y1), new Point(x2, y2));
	}
	
	public Line() {
		this(0,0,0,0);
	}
	
	public boolean isIntersecting(Line o) {
		int ccw1 = Ccw.ccw(this.p1, this.p2, o.p1);
		int ccw2 = Ccw.ccw(this.p1, this.p2, o.p2);
		
		// endpoints of o are on the same side of this line
		if (ccw1 * ccw2 > 0) return false;
		// endpoints of o are on different sides of this line
		else if (ccw1 * ccw2 < 0) return true;
		
		// at least one endpoint of o is colinear with this line
		if (ccw1 == 0)
			return isInBoundingBox(o.p1);
		else // if (ccw2 == 0)
			return isInBoundingBox(o.p2);
	}
	
	public boolean isInBoundingBox(Point a) {
		if (isInBetween(this.p1.getX(), this.p2.getX(), a.getX()) &&
			isInBetween(this.p1.getY(), this.p2.getY(), a.getY()))
			return true;
		
		return false;
	}
	
	public boolean isInBetween(int a, int b, int query) {
		if (a <= b)
			return (a <= query && query <= b);
		else 
			return (b <= query && query <= a);
	}

	public Point findIntersectionPoint(Line line2) {
		
		return null;
	}
}
