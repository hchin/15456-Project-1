import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class LineArrangement {
	LinkedList<Edge> firstEdgeList = new LinkedList<Edge>();
	ArrayList<Vertex> infiniteList = new ArrayList<Vertex>();

	Line[] dualLine;
	Point[] points;
	Point[] pointList;

	/*
	 * Mapping point (a, b) to dual line ax+by+1 = 0
	 */
	public LineArrangement(Point[] points) {
		this.points = points;
	}

	public int query(double x, double y) {
		return simplicialDepth(new Point(x, y));
	}
	
	private int simplicialDepth(Point query) {
		int[] sorted = orderPoints(query);
		Point[] pt = new Point[points.length];
		int [] N = new int[points.length];
		
		ArrayList<Point> quad1 = new ArrayList<Point>();
		ArrayList<Point> quad2 = new ArrayList<Point>();
		ArrayList<Point> quad3 = new ArrayList<Point>();
		ArrayList<Point> quad4 = new ArrayList<Point>();
		
		for (int i = 0; i < pt.length; i++) {
			pt[i] = new Point(points[sorted[i]].x - query.x, points[sorted[i]].y - query.y);
		}
		
		for (int i = 0; i < pt.length; i++) {
			if (Math.signum(pt[i].x) > 0.0 && Math.signum(pt[i].y) > 0.0) quad1.add(pt[i]);
			else if (Math.signum(pt[i].x) < 0.0 && Math.signum(pt[i].y) > 0.0) quad2.add(pt[i]);
			else if (Math.signum(pt[i].x) < 0.0 && Math.signum(pt[i].y) < 0.0) quad3.add(pt[i]);
			else if (Math.signum(pt[i].x) > 0.0 && Math.signum(pt[i].y) < 0.0) quad4.add(pt[i]);
		}
		
		for (Point tmp : quad2) quad1.add(tmp);
		for (Point tmp : quad3) quad1.add(tmp);
		for (Point tmp : quad4) quad1.add(tmp);
		
		pt = quad1.toArray(new Point[0]);

		for (int i = 0; i < pt.length; i++) System.out.println(pt[i].x + " " + pt[i].y);
		
		int start = 0;
		for (int n = 0; n < pt.length; n++) {
			if (!inAngleRange(pt[n].getPolarAngle(), pt[0].getPolarAngle(), pt[0].getAntipoleAngle())) {
				start = n;
				break;
			}
		}
		
		N[0] = start - 1;
		
		for (int n = 1; n < pt.length; n++) {
			int count = 0;
			
			int i;
			for (i = start; ; i=(i+1)%pt.length) {
				if (!inAngleRange(pt[i].getPolarAngle(), pt[n].getPolarAngle(), pt[n].getAntipoleAngle())) {
					break;
				}
				count++;
			}
			
			start = i;
			N[n] = N[n-1] + count - 1;
		}
		
		for (int i = 0; i < pt.length; i++) System.out.println("N[" + i + "]: " + N[i]);
		
		int n = pt.length+1;
		int answer = (2*n - 3) * (n-1) * (n-2) / 12;
		int sum = 0;
		for (int i = 0; i < pt.length; i++) sum += N[i]*N[i];
		sum /= 2;
		answer -= sum;
		
		return answer;
	}

	/* All angles are between 0 to 2pi */
	private boolean inAngleRange(double checkAngle, double startAngle, double endAngle) {
		while (endAngle < startAngle) endAngle += 2*Math.PI;
		while (checkAngle < startAngle) checkAngle += 2*Math.PI;
		if (startAngle <= checkAngle && checkAngle <= endAngle) return true;
		return false;
	}
	
	private void arrange() {
		/* the extra space in the array to accommodate query point */
		pointList = new Point[points.length+1];
		for (int i = 0; i < points.length; i++)
			pointList[i] = new Point(points[i].x, points[i].y);

		/* Create all the dual lines */
		dualLine = new Line[points.length+1];
		for (int i = 0; i < points.length; i++) {
			dualLine[i] = new Line(- points[i].x / points[i].y, - 1 / points[i].y);
//			System.out.println("Line " + i + ": " + dualLine[i].m + ", " + dualLine[i].c);
		}

		{
			/* First edge: Just a line from inf to inf */
			Edge firstEdge = new Edge(0);

			infiniteList.add(firstEdge.v1);
			infiniteList.add(firstEdge.v2);

			firstEdgeList.addFirst(firstEdge);
		}

		for (int n = 1; n < points.length; n++) {
			/* Create a fake edge with dualLine[n] */
			Edge edgeN = new Edge(n);

			/* Find the edge on line[0] that the intersection point lies on */
			Edge edge1 = null;
			ListIterator<Edge> iter;
			for (iter = firstEdgeList.listIterator(); iter.hasNext(); ) {
				Edge edge = iter.next();
				if (edge.intersectEdge(edgeN)) {
					edge1 = edge;
					break;
				}
			}

			if (edge1 == null) {
				System.out.println("WTF null???!?!?!?!");
			}

			/* "Create" the intersection point */
			Vertex ret = createIntersection(edge1, edgeN);

			iter.add(ret.nextOppEdge(edge1));

			Edge left = edge1;
			Edge right = ret.nextOppEdge(edge1);

			//			edgeN = edgeN;
			Edge edgeN2 = ret.nextOppEdge(edgeN);

			/* "Walk", starting from left, leftwards */
			Vertex curV = left.otherVertex(ret);
			Edge lastN;

			if (dualLine[0].m > dualLine[n].m) lastN = edgeN;
			else lastN = edgeN2;

			Vertex lastNvertex = ret;
			boolean inf;
			while (true) {
				Edge edge;
				Vertex v = null;

				if (curV.isNull()) {
					edge = curV.getEdge();
					v = edge.otherVertex(curV);

					curV = getNextCcw(infiniteList, curV);
					left = curV.getEdge();

					inf = true;
				}
				else {
					left = curV.nextCcwEdge(left);
					inf = false;
				}

				if (left.intersectEdge(edgeN)) {
//					System.out.println("Using edgeN");

					Vertex tmpV = createIntersection(left, edgeN);

					left = findEdgeWithVertex(curV, left, tmpV.nextOppEdge(left));
					edgeN = findInfiniteEdge(edgeN, tmpV.nextOppEdge(edgeN));

					lastN = edgeN;
					lastNvertex = tmpV;

					continue;
				}
				else if (left.intersectEdge(edgeN2)) {
//					System.out.println("Using edgeN2");

					Vertex tmpV = createIntersection(left, edgeN2);

					left = findEdgeWithVertex(curV, left, tmpV.nextOppEdge(left));
					edgeN2 = findInfiniteEdge(edgeN2, tmpV.nextOppEdge(edgeN2));

					lastN = edgeN2;
					lastNvertex = tmpV;

					continue;
				}
				else if (inf == true) {
					if (!onSameSide(dualLine[n], v, left.otherVertex(curV))) {
						curV = getNextCw(infiniteList, curV);
						infiniteList.add(infiniteList.indexOf(curV), lastN.otherVertex(lastNvertex));
						break;
					}
				}
				curV = left.otherVertex(curV);
			}

			/* "Walk", starting from right, rightwards */
			curV = right.otherVertex(ret);

			if (dualLine[0].m > dualLine[n].m) lastN = edgeN2;
			else lastN = edgeN;

			lastNvertex = ret;
			while (true) {
				Edge edge;
				Vertex v = null;

				if (curV.isNull()) {
					edge = curV.getEdge();
					v = edge.otherVertex(curV);

					curV = getNextCcw(infiniteList, curV);
					right = curV.getEdge();

					inf = true;
				}
				else {
					right = curV.nextCcwEdge(right);
					inf = false;
				}

				if (right.intersectEdge(edgeN)) {
//					System.out.println("Using edgeN");

					Vertex tmpV = createIntersection(right, edgeN);

					edgeN = findInfiniteEdge(edgeN, tmpV.nextOppEdge(edgeN));
					right = findEdgeWithVertex(curV, right, tmpV.nextOppEdge(right));

					lastN = edgeN;
					lastNvertex = tmpV;

					continue;
				}
				else if (right.intersectEdge(edgeN2)) {
//					System.out.println("Using edgeN2");

					Vertex tmpV = createIntersection(right, edgeN2);

					edgeN2 = findInfiniteEdge(edgeN2, tmpV.nextOppEdge(edgeN2));
					right = findEdgeWithVertex(curV, right, tmpV.nextOppEdge(right));

					lastN = edgeN2;
					lastNvertex = tmpV;

					continue;
				}
				else if (inf == true) {
					if (!onSameSide(dualLine[n], v, right.otherVertex(curV))) {
						curV = getNextCw(infiniteList, curV);
						infiniteList.add(infiniteList.indexOf(curV), lastN.otherVertex(lastNvertex));
						break;
					}
				}

				curV = right.otherVertex(curV);
			}
		}
	}

	private int[] orderPoints(Point query) {
		LinkedList<Integer> answer = new LinkedList<Integer>();

		/* Build the arrangement graph */
		arrange();

		/* Create the dual line of the query point */
		dualLine[pointList.length-1] = new Line(- query.x / query.y, - 1 / query.y);

		int n = pointList.length-1;

		/* Create a fake edge with the dual line */
		Edge edgeN = new Edge(n);

		/* Find the edge on line[0] that the intersection point lies on */
		Edge edge1 = null;
		ListIterator<Edge> iter;
		for (iter = firstEdgeList.listIterator(); iter.hasNext(); ) {
			Edge edge = iter.next();
			if (edge.intersectEdge(edgeN)) {
				edge1 = edge;
				break;
			}
		}

		if (edge1 == null) {
			System.out.println("WTF null???!?!?!?!");
		}

		/* "Create" the intersection point */
		Vertex ret = createIntersection(edge1, edgeN);
		answer.add(0);

		iter.add(ret.nextOppEdge(edge1));

		Edge left = edge1;
		Edge right = ret.nextOppEdge(edge1);

		// edgeN = edgeN;
		Edge edgeN2 = ret.nextOppEdge(edgeN);

		/* "Walk", starting from left, leftwards */
		Vertex curV = left.otherVertex(ret);

		boolean inf;
		while (true) {
			Edge edge;
			Vertex v = null;

			if (curV.isNull()) {
				edge = curV.getEdge();
				v = edge.otherVertex(curV);

				curV = getNextCcw(infiniteList, curV);
				left = curV.getEdge();

				inf = true;
			}
			else {
				left = curV.nextCcwEdge(left);
				inf = false;
			}

			if (left.intersectEdge(edgeN)) {
//				System.out.println("Using edgeN");

				answer.add(left.idx);

				Vertex tmpV = createIntersection(left, edgeN);

				left = findEdgeWithVertex(curV, left, tmpV.nextOppEdge(left));
				edgeN = findInfiniteEdge(edgeN, tmpV.nextOppEdge(edgeN));

				continue;
			}
			else if (left.intersectEdge(edgeN2)) {
//				System.out.println("Using edgeN2");

				answer.add(left.idx);

				Vertex tmpV = createIntersection(left, edgeN2);

				left = findEdgeWithVertex(curV, left, tmpV.nextOppEdge(left));
				edgeN2 = findInfiniteEdge(edgeN2, tmpV.nextOppEdge(edgeN2));

				continue;
			}
			else if (inf == true) {
				if (!onSameSide(dualLine[n], v, left.otherVertex(curV))) {
					break;
				}
			}
			curV = left.otherVertex(curV);
		}

		/* "Walk", starting from right, rightwards */
		curV = right.otherVertex(ret);

		while (true) {
			Edge edge;
			Vertex v = null;

			if (curV.isNull()) {
				edge = curV.getEdge();
				v = edge.otherVertex(curV);

				curV = getNextCcw(infiniteList, curV);
				right = curV.getEdge();

				inf = true;
			}
			else {
				right = curV.nextCcwEdge(right);
				inf = false;
			}

			if (right.intersectEdge(edgeN)) {
//				System.out.println("Using edgeN");

				answer.addFirst(right.idx);

				Vertex tmpV = createIntersection(right, edgeN);

				edgeN = findInfiniteEdge(edgeN, tmpV.nextOppEdge(edgeN));
				right = findEdgeWithVertex(curV, right, tmpV.nextOppEdge(right));

				continue;
			}
			else if (right.intersectEdge(edgeN2)) {
//				System.out.println("Using edgeN2");

				answer.addFirst(right.idx);

				Vertex tmpV = createIntersection(right, edgeN2);

				edgeN2 = findInfiniteEdge(edgeN2, tmpV.nextOppEdge(edgeN2));
				right = findEdgeWithVertex(curV, right, tmpV.nextOppEdge(right));

				continue;
			}
			else if (inf == true) {
				if (!onSameSide(dualLine[n], v, right.otherVertex(curV))) {
					break;
				}
			}

			curV = right.otherVertex(curV);
		}

		/* Weird: cannot convert from LinkedList to array??? */
		int[] returnAnswer = new int[answer.size()];
		Iterator<Integer> iterAnswer = answer.iterator();
		for (int i = 0; i < answer.size(); i++)
			returnAnswer[i] = iterAnswer.next();

		return returnAnswer;

	}
	
	private Edge findEdgeWithVertex(Vertex v, Edge e1, Edge e2) {
		boolean one = false;
		boolean two = false;

		if (e1.v1 == v || e1.v2 == v) one = true;
		if (e2.v1 == v || e2.v2 == v) two = true;

		if (one && two) System.out.println("findEdgeWithVertex BOTH WTF");
		if (one) return e1;
		if (two) return e2;

		System.out.println("findEdgeWithVertex NONE WTF");
		return null;
	}

	private Edge findInfiniteEdge(Edge edge1, Edge edge2) {
		boolean inf1 = false;
		boolean inf2 = false;

		if (edge1.v1.isNull() || edge1.v2.isNull()) inf1 = true;
		if (edge2.v1.isNull() || edge2.v2.isNull()) inf2 = true;

		if (inf1 && inf2) System.out.println("infiniteEdge BOTH WTF");
		if (inf1) return edge1;
		if (inf2) return edge2;
		System.out.println("infiniteEdge NONE WTF");
		return null;
	}

	private boolean onSameSide(Line line, Vertex v1, Vertex v2) {
		double y1 = line.evalAtX(v1.point.x);
		double y2 = line.evalAtX(v2.point.x);

		double d1;
		double d2;

		if (Math.abs(y1 - v1.point.y) < 1e-5) d1 = 0.0;
		else d1 = Math.signum(y1 - v1.point.y);

		if (Math.abs(y2 - v2.point.y) < 1e-5) d2 = 0.0;
		else d2 = Math.signum(y2 - v2.point.y);

		if (d1 == 0.0 && d2 == 0.0) return false;
		else return (d1 == d2);
	}

	private Vertex getNextCcw(ArrayList<Vertex> list, Vertex v) {
		int wh = list.indexOf(v);

		if (wh == -1)
			System.out.println("getNextCcw null!");

		return list.get((wh + list.size() - 1) % list.size());
	}

	private Vertex getNextCw(ArrayList<Vertex> list, Vertex v) {
		int wh = list.indexOf(v);

		if (wh == -1) System.out.println("getNextCw null!");

		return list.get((wh + 1) % list.size());
	}

	private Vertex createIntersection(Edge edge1, Edge edge2) {
		if (dualLine[edge1.idx].m < dualLine[edge2.idx].m) {
			Edge tmpEdge = edge1;
			edge1 = edge2;
			edge2 = tmpEdge;
		}

		Point intersection = dualLine[edge1.idx].intersect(dualLine[edge2.idx]);

		Edge edge1a = new Edge(edge1.idx);
		Edge edge2a = new Edge(edge2.idx);

		Vertex v = new Vertex(intersection, new Edge[] { edge1, edge2a, edge1a,
				edge2 });

		edge1.v2.replaceEdge(edge1, edge1a);
		edge1a.v2 = edge1.v2;

		edge2.v2.replaceEdge(edge2, edge2a);
		edge2a.v2 = edge2.v2;

		edge1.v2 = edge2.v2 = v;
		edge1a.v1 = edge2a.v1 = v;

		edge1.sanityCheck();
		edge2.sanityCheck();
		edge1a.sanityCheck();
		edge2a.sanityCheck();

		return v;
	}

	class Edge {
		public int idx;
		public Vertex v1;
		public Vertex v2;

		public Edge(int lineNo) {
			idx = lineNo;
			v1 = new Vertex(null, new Edge[] {this, null, null, null} );
			v2 = new Vertex(null, new Edge[] {this, null, null, null} );
		}

		public void sanityCheck() {
			if (!v1.isNull() && !v2.isNull()) {
				if (v1.point.x > v2.point.x) {
					System.out.println("Edge " + idx + " is in the wrong order!");
				}
			}
		}

		boolean intersectEdge(Edge e) {
			Point intersection = dualLine[e.idx].intersect(dualLine[idx]);
			boolean onOne = false;
			boolean onTwo = false;

			/* Check the intersection point is on this edge */
			if (v1.isNull() && v2.isNull()) {
				onOne = true;
			} else if (v1.isNull()&& !v2.isNull()) {
				if (v2.point.x > intersection.x) {
					onOne = true;
				}
			} else if (!v1.isNull() && v2.isNull()) {
				if (v1.point.x < intersection.x) {
					onOne = true;
				}
			} else {
				if (v1.point.x < intersection.x && intersection.x < v2.point.x) {
					onOne = true;
				}
			}

			/* Check the intersection point is on the other edge */
			if (e.v1.isNull() && e.v2.isNull()) {
				onTwo = true;
			} else if (e.v1.isNull() && !e.v2.isNull()) {
				if (e.v2.point.x > intersection.x) {
					onTwo = true;
				}
			} else if (!e.v1.isNull() && e.v2.isNull()) {
				if (e.v1.point.x < intersection.x) {
					onTwo = true;
				}
			} else {
				if (e.v1.point.x < intersection.x
						&& intersection.x < e.v2.point.x) {
					onTwo = true;
				}
			}

			return (onOne && onTwo);
		}

		Vertex otherVertex(Vertex v) {
			if (v1 == v)
				return v2;
			else if (v2 == v)
				return v1;

			System.out.println("otherVertex null!");

			return null;
		}
	}

	class Vertex {
		Edge[] edges;
		Point point;

		public Vertex(Point point, Edge[] edges) {
			this.point = point;
			this.edges = edges;

			//			if (point != null) {
			//				System.out.println("Created new vertex: " + edges[0].idx + " "
			//						+ edges[1].idx + " "
			//						+ edges[2].idx + " "
			//						+ edges[3].idx);
			//				System.out.println("Point: " + point.x + " " + point.y);
			//			}
		}

		public boolean isNull() {
			return point == null;
		}

		public Edge nextCwEdge(Edge e) {
			for (int i = 0; i < 4; i++)
				if (edges[i] == e)
					return edges[((i - 1) + 4) % 4];

			System.out.println("CW null!");

			return null;
		}

		public Edge nextCcwEdge(Edge e) {
			for (int i = 0; i < 4; i++)
				if (edges[i] == e)
					return edges[(i + 1) % 4];

			System.out.println("CCW null!");

			return null;
		}

		public Edge nextOppEdge(Edge e) {
			return nextCcwEdge(nextCcwEdge(e));
		}

		public void replaceEdge(Edge e, Edge replace) {
			for (int i = 0; i < 4; i++) {
				if (edges[i] == e) {
					edges[i] = replace;
					return;
				}
			}

			System.out.println("ReplaceEdge null!");
		}

		public Edge getEdge() {
			return edges[0];
		}
	}


}
