import java.util.Arrays;
import java.util.Comparator;


public class CountTriangles {
	public static int count(Point[] points, Point query) {
		/* Arrays are twice the length so we can simulate wrap-around */
		Point[] offset = new Point[2*points.length];
		int[] N = new int[2*points.length];
		int[] T = new int[points.length];
		
		for (int i = 0; i < points.length; i++) {
			offset[i] = new Point(points[i].getX() - query.getX(), points[i].getY() - query.getY());
		}
		
		Arrays.sort(offset, 0, points.length, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				double angle1 = o1.getPolarAngle();
				double angle2 = o2.getPolarAngle();
				
				if (angle1 < angle2) return -1;
				else if (angle1 > angle2) return 1;
				return 0;
			}
		});
		
		for (int i = 0; i < points.length; i++)
			System.out.println(offset[i].getPolarAngle() + " " + offset[i].getAntipoleAngle());
		
		int start = 0;
		for (int n = 0; n < points.length-1; n++) {
			for (int i = start; i < points.length; i++) {
				if (Ccw.inAngleRange(offset[i].getPolarAngle(), offset[n].getAntipoleAngle(), offset[n+1].getAntipoleAngle())) {
					start = i;
					break;
				}
			}
			
			for (int i = start, count = 0; count < points.length; i = (i+1)%points.length, count++) {
				if (!Ccw.inAngleRange(offset[i].getPolarAngle(), offset[n].getAntipoleAngle(), offset[n+1].getAntipoleAngle())) {
					N[n] = count;
					start = i;
					break;
				}
			}
		}
		
		/* Duplicate the second part of the array */
//		for (int i = 0; i < points.length; i++) {
//			offset[i+points.length] = offset[i];
//			N[i+points.length] = N[i];
//		}
		
		for (int i = 0; i < 2*points.length; i++)
			if (!Ccw.inAngleRange(offset[i].getPolarAngle(), offset[0].getPolarAngle(),
					offset[0].getAntipoleAngle())) {
				start = i-1;
				break;
			}

		for (int i = 0; i < start; i++) {
			T[0] += (start-i) * N[i];
		}
		
		System.out.println("start = " + start);
		
		for (int n = 1; n < points.length; n++) {
			T[n] = T[n-1];
			
			int m = 0;
			for (int i = start+1; i < points.length; i=(i+1)%points.length) {
				if (!Ccw.inAngleRange(offset[i].getPolarAngle(), offset[n].getPolarAngle(), offset[n].getAntipoleAngle())) {
					break;
				}
				m++;
			}
			System.out.println("m = " + m);
			
			if (m != 0) {
				for (int i = n; i <= start; i++)
					T[n] += m * N[i];
			}
			
			for (int i = start+1; i < start+m; i++)
				T[n] += (start+m-i) * N[i];
			
			T[n] -= (start - n) * N[n-1];
			
			start += m;
			
			start %= points.length;
		}
		
		int totalcount = 0;
		for (int i = 0; i < points.length; i++)
			totalcount += T[i];
		
		for (int i : T) System.out.println("T = " + i);
		
		return totalcount;
	}
}
