import java.util.*;

public class Algo {

	public static int CCW(MyPoint src, MyPoint tgt, MyPoint c) {
		int res = (tgt.x - src.x) * (c.y - src.y) - (c.x - src.x)
				* (tgt.y - src.y);
		if (res > 0)
			return 1;
		if (res < 0)
			return -1;
		return 0;
	}
	
	public static int CCW(double x1, double y1,double x2, double y2,double x3, double y3) {
		double res = (x2 - x1) * (y3 - y1) - (x3 - x1)
				* (y2 - y1);
		if (res > 0)
			return 1;
		if (res < 0)
			return -1;
		return 0;
	}

	public static ArrayList<MyPoint> ConvexHull(ArrayList<MyPoint> pointList1) {
		if (pointList1.size() < 3)
			throw new RuntimeException();

		// deepcopy of list
		ArrayList<MyPoint> pointList = new ArrayList<MyPoint>(pointList1);
		// sort points by x-axis
		Collections.sort(pointList, new Comparator<MyPoint>() {
			@Override
			public int compare(MyPoint p1, MyPoint p2) {
				return p1.x - p2.x;
			}
		});
		System.out.println("sort by x "+pointList);
		MyPoint leftMostPoint = pointList.get(0);
		// remove the first pt
		pointList.remove(0);
		MyComparator mc = new MyComparator(leftMostPoint);
		Collections.sort(pointList, mc);
		System.out.println("sort with ang "+ pointList);
		Stack<MyPoint> input = new Stack<MyPoint>();
		for (int i = pointList.size() - 1; i >= 0; i--)
			input.push(pointList.get(i));
		Stack<MyPoint> output = new Stack<MyPoint>();
		output.push(leftMostPoint);
		output.push(input.pop());
		//output.push(input.pop());
		while (input.size() > 0) {
			MyPoint x = input.pop();
			System.out.println(output.elementAt(output.size()-2)+":"+output.elementAt(output.size()-1)+":"+x+" /"+CCW(output.elementAt(output.size()-2), output.elementAt(output.size()-1), x)); 
			
			while (CCW(output.elementAt(output.size()-2), output.elementAt(output.size()-1), x) > -1)
			{
				output.pop();
				if(output.size()<2) break;
			}

			output.push(x);
		}
		return new ArrayList<MyPoint>(output);
	}

	public static boolean InHull(ArrayList<MyPoint> convexHull, MyPoint p) {
		int initSign = CCW(p, convexHull.get(convexHull.size()-1), convexHull.get(0));
		for (int i = 0; i < convexHull.size() - 1; i++)
			if (initSign != CCW(p, convexHull.get(i), convexHull.get(i + 1)))
				return false;
		return true;
	}
	
	public static boolean InHull(ArrayList<MyPoint> convexHull, double x, double y) {
		MyPoint last = convexHull.get(convexHull.size()-1);
		MyPoint first = convexHull.get(0);
		int initSign = CCW(x,y, last.x, last.y, first.x,first.y);
		for (int i = 0; i < convexHull.size() - 1; i++)
			if (initSign != CCW(x,y, convexHull.get(i).x, convexHull.get(i).y, convexHull.get(i+1).x,convexHull.get(i+1).y))
				return false;
		return true;
	}

}

class MyComparator implements Comparator<MyPoint> {
	MyPoint lp;

	public MyComparator(MyPoint lp) {
		this.lp = lp;
	}

	@Override
	public int compare(MyPoint p1, MyPoint p2) {
		return Algo.CCW(lp, p1, p2);
	}
}
