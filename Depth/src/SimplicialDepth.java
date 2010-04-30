
public class SimplicialDepth {

	public static void main(String[] args) {

		Point[] points = new Point[4];
		points[0] = new Point(13, 14);
		points[1] = new Point(8, 12);
		points[2] = new Point(9, 5);
		points[3] = new Point(20, 9);
		
		LineArrangement arrange = new LineArrangement(points);
		
		int ans = arrange.query(10, 10);
		System.out.println("Depth: " + ans);
		
		points = new Point[5];
		points[0] = new Point(6, 9);
		points[1] = new Point(10, 5);
		points[2] = new Point(8, 1);
		points[3] = new Point(4, 1);
		points[4] = new Point(2, 5);
		
		arrange = new LineArrangement(points);
		
		ans = arrange.query(5, 4);
		System.out.println("Depth: " + ans);
		
	}

}
