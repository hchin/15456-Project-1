import java.util.Arrays;
import java.util.Comparator;


public class CountTriangles {
	public static int count(Point[] points, Point query) {
		/* Arrays are twice the length so we can simulate wrap-around */
		Point[] offset = new Point[2*points.length+2];
		int[] N = new int[2*points.length+2];
		int[] T = new int[points.length+1];
		
		for (int i = 1; i < points.length+1; i++) {
			offset[i] = new Point(points[i-1].getX() - query.getX(), points[i-1].getY() - query.getY());
		}
		
		Arrays.sort(offset, 1, points.length+1, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				double angle1 = o1.getPolarAngle();
				double angle2 = o2.getPolarAngle();
				
				if (angle1 < angle2) return -1;
				else if (angle1 > angle2) return 1;
				return 0;
			}
		});
		
//		for (int i = 1; i < points.length+1; i++)
//			System.out.println(offset[i].getPolarAngle() + " " + offset[i].getAntipoleAngle());
		
		int start = 1;
		for (int n = 1; n < points.length+1; n++) {
			for (int i = start; i < points.length+1; i++) {
				int nplus1 = n+1;
				if (nplus1 >= points.length+1) nplus1 = 1;
				if (Ccw.inAngleRange(offset[i].getPolarAngle(), offset[n].getAntipoleAngle(), offset[nplus1].getAntipoleAngle())) {
					start = i;
					break;
				}
			}
			
			for (int i = start, count = 0; count < points.length; count++) {
				int nplus1 = n+1;
				if (nplus1 >= points.length+1) nplus1 = 1;
				if (!Ccw.inAngleRange(offset[i].getPolarAngle(), offset[n].getAntipoleAngle(), offset[nplus1].getAntipoleAngle())) {
					N[n] = count;
					start = i;
					break;
				}
				i++;
				if (i >= points.length+1) i = 1;
			}
		}
		
		for (int i = 1; i < points.length+1; i++) System.out.println("i = " + i + ", N[i] = " + N[i]);
		
		/* Duplicate the second part of the array */
		for (int i = points.length+1; i < 2*points.length+1; i++) {
			offset[i] = offset[i-points.length];
			N[i] = N[i-points.length];
		}
		
		for (int i = 1; i < 2*points.length+1; i++)
			if (!Ccw.inAngleRange(offset[i].getPolarAngle(), offset[1].getPolarAngle(),
					offset[1].getAntipoleAngle())) {
				start = i-1;
				break;
			}

		for (int i = 1; i <= start-1; i++) {
			T[1] += (start-i) * N[i];
		}
		
		System.out.println("start = " + start);
		
		for (int n = 2; n < points.length+1; n++) {
			T[n] = T[n-1];
			
			int m = 0;
			for (int i = start+1; ; ) {
				if (!Ccw.inAngleRange(offset[i].getPolarAngle(), offset[n].getPolarAngle(), offset[n].getAntipoleAngle())) {
					break;
				}
				m++;
				
				i++;
				if (i == points.length+1) i = 1; 
			}
//			System.out.println("m = " + m);
			
			for (int i = n; i <= start-1; i++) {
				T[n] += m * N[i];
				System.out.print(i);
			}
			System.out.println();
			
			for (int i = start; i <= start+m-1; i++) {
				T[n] += (start+m-i) * N[i];
				System.out.print(i);
			}
			System.out.println();
			
			T[n] -= (start - n + 1) * N[n-1];
			
			start += m;
			
			System.out.println("start = " + start);
			
		}
		
		int totalcount = 0;
		for (int i = 1; i < points.length+1; i++)
			totalcount += T[i];
		
		for (int i : T) System.out.println("T = " + i);
		
		return totalcount;
	}
}
