
public class Ccw {
	public static final Point ORIGIN = new Point(0, 0);
	
	public static int ccw(Point a, Point b, Point c) {
		int det;
		
		det = (b.getX() - a.getX()) * (c.getY() - a.getY())
			  - (b.getY() - a.getY()) * (c.getX() - a.getX());
		
		if (det > 0) return 1;
		else if (det < 0) return -1;
		return 0;
	}

	public static boolean inAngleRange(double polarAngle, double antipoleAngle,
			double antipoleAngle2) {
		if (antipoleAngle < antipoleAngle2) 
			return (antipoleAngle <= polarAngle) && (polarAngle <= antipoleAngle2);
		else
			return (polarAngle >= antipoleAngle) || (polarAngle <= antipoleAngle2);
	}
}
