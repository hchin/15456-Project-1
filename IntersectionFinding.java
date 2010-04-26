import java.util.HashSet;
import java.util.Set;


public class IntersectionFinding {
	
	public static Point[] findIntersections(Point[] points) {
		Set<Point> list = new HashSet<Point>();
		
		for (int i = 0; i < points.length; i++) {
			for (int j = i+1; j < points.length; j++) {
				Line line1 = new Line(points[i], points[j]);
				
				for (int k = 0; k < points.length; k++) {
					for (int l = k+1; l < points.length; l++) {
						if (k == i && j == l) continue;
						
						Line line2 = new Line(points[k], points[l]);
						
						if (line1.isIntersecting(line2)) {
							list.add(line1.findIntersectionPoint(line2));
						}
					}
				}
			}
		}
		
		return list.toArray(new Point[0]);
	}
	
	public static void main(String[] args) {
		Line p1 = new Line(0, 0, 100, 100);
		Line p2 = new Line(70, 33, 39, 65);
		Line p3 = new Line(100, 100, 110, 90);
		Line p4 = new Line(105, 105, 120, 120);
		
//		System.out.println(p1.isIntersecting(p2) == true);
//		System.out.println(p1.isIntersecting(p3) == true);
//		System.out.println(p2.isIntersecting(p3) == false);
//		
//		System.out.println(p2.isIntersecting(p1) == true);
//		System.out.println(p3.isIntersecting(p1) == true);
//		System.out.println(p3.isIntersecting(p2) == false);
//		
//		System.out.println(p1.isIntersecting(p4) == false);
//		System.out.println(p4.isIntersecting(p1) == false);
		
		/* Basic test: CountTriangles should return 2 */
		Point[] points = new Point[4];
		points[0] = new Point(3,3);
		points[1] = new Point(-2, 2);
		points[2] = new Point(-1, -5);
		points[3] = new Point(10, -1);
		
		System.out.println(CountTriangles.count(points, new Point(0,0)));
		
		/* Pentagon test: Star shape centered at the origin */
		points = new Point[5];
		points[0] = new Point(1, 4);
		points[1] = new Point(5, 0);
		points[2] = new Point(3, -4);
		points[3] = new Point(-1, -4);
		points[4] = new Point(-3, 0);
		
		System.out.println(CountTriangles.count(points, new Point(0,0)));
	}
}
