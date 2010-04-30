import java.util.*;

public class FindRegions {
	
	static final double epsilon = 0.5;

	ArrayList<MyPoint> pointList = new ArrayList<MyPoint>();
	ArrayList<MyLine> lineList = new ArrayList<MyLine>();
	ArrayList<MyPoint> convexHull;
	HashMap<IdPt, Integer> colorMap = new HashMap<IdPt,Integer>();

	LineArrangement lineArrangement;
	IdPt[][] idPtArr;
	static int noOfLines;
	static int[] colorArr;

	int width, height;
	int maxDepth;
	
	boolean computedDepth;
	
	public FindRegions(ArrayList<MyPoint> pointList, int width, int height) {
		this.pointList = pointList;
		this.width = width;
		this.height = height;
		computedDepth=false;
		lineArrangement = new LineArrangement();
	}
	public int getMaxDepth()
	{
		if(computedDepth)
			return maxDepth;
		else
			return 0;
	}

	public MyPoint[][] computeDepth() {

		MyPoint[][] depthMap = new MyPoint[width][height];

		// sort points by x
		Collections.sort(pointList, new Comparator<MyPoint>() {
			public int compare(MyPoint p1, MyPoint p2) {
				if (p1.x == p2.x)
					return p1.y - p2.y;
				else
					return p1.x - p2.x;
			}
		});

		// compute the convexhull since the infinite face has depth 0
		convexHull = Algo.ConvexHull(pointList);
		System.out.println("Found CVN "+convexHull);
		// get the lines
		for (int i = 0; i < pointList.size(); i++)
			for (int j = i + 1; j < pointList.size(); j++)
				lineList.add(new MyLine(pointList.get(i), pointList.get(j)));
		noOfLines = lineList.size();
		System.out.println("LL"+lineList);
		// compute the depth of all intersections
		 computeIntersection();
		 Set<IdPt> key = colorMap.keySet();
		 for(IdPt t : key)
		 {
			 System.out.println("color"+t+":"+colorMap.get(t));
		 }
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				// only compute for points inside the convex hull
				depthMap[i][j] = new MyPoint(i, j);
				if (Algo.InHull(convexHull, i, j)) {
					IdPt temp = computeColor(depthMap[i][j]);
					//depthMap[i][j].setDepth(2);
				//	if(!temp.equals(new IdPt(noOfLines))) System.out.println(temp+" "+i+","+j);
					// check for the depth of the point in the colorMap
					if (temp!=null && colorMap.containsKey(temp))
						depthMap[i][j].setDepth(colorMap.get(temp));
				}
			}
		computedDepth=true;
		Set<IdPt> keys = colorMap.keySet(); 
		for( IdPt p : keys)
		{
			maxDepth = Math.max(maxDepth, colorMap.get(p));
		}
		return depthMap;
	}

	private void computeIntersection() 
	{
		// TODO Auto-generated method stub
		for (int i = 0; i < lineList.size(); i++)
			for (int j = i + 1; j < lineList.size(); j++) 
			{
				intersect(lineList.get(i), lineList.get(j));
				
			}
	}

	private void intersect(MyLine l1, MyLine l2) {
		System.out.println("l1 "+l1+",l2 "+l2);
		int a1 = -l1.src.y + l1.tgt.y;
		int b1 = (l1.src.x - l1.tgt.x);
		int c1 = l1.src.x*l1.tgt.y-l1.src.y*l1.tgt.x;
		
		int a2 = -l2.src.y + l2.tgt.y;
		int b2 = (l2.src.x - l2.tgt.x);
		int c2 = l2.src.x*l2.tgt.y-l2.src.y*l2.tgt.x;
		
		/* a1 b1
		 * a2 b2
		 */
		int det1 = a1*b2 - a2*b1;
		/* c1 b1
		 * c2 b2
		 */
		int det2 = c1*b2 - c2*b1;
		/* a1 c1
		 * a2 c2
		 */
		int det3 = a1*c2 - a2*c1;
		System.out.println("det1 "+det1+" det2 "+det2+" det3 "+det3);
		if(det1 == 0)
		{
			return;
		}
		else
		{
			double x=(det2+0.0)/det1;
			double y=(det3+0.0)/det1;
			System.out.println("intersection "+x+","+y);
			//if(Algo.InHull(convexHull, x, y)) //if we are in the convex hull 
			
				//perturb the point
	
 
				double dx1 = (l1.tgt.x-l1.src.x)/l1.len;
				double dy1 = (l1.tgt.y-l1.src.y)/l1.len;
				double dx2 = (l2.tgt.x-l2.src.x)/l2.len;
				double dy2 = (l2.tgt.y-l2.src.y)/l2.len;
				
				double x11 = x+epsilon*(dx1+dx2);
				double y11 = y+epsilon*(dy1+dy2);
				
				double x12 = x+epsilon*(dx1-dx2);
				double y12 = y+epsilon*(dy1-dy2);
				
				double x21 = x+epsilon*(-dx1+dx2);
				double y21 = y+epsilon*(-dy1+dy2);
				
				double x22 = x+epsilon*(-dx1-dx2);
				double y22 = y+epsilon*(-dy1-dy2);
				
			
				System.out.println("new set of colors");
				//l1 - 1
				if(Algo.InHull(convexHull, x11, y11))
				colorMap.put(computeColor(x11,y11),lineArrangement.query(x11,y11));
				System.out.println("new color"+computeColor(x11,y11)+" "+lineArrangement.query(x11,y11)+" "+x11+","+y11);
				//l1 - 2
				if(Algo.InHull(convexHull, x12, y12))
				colorMap.put(computeColor(x12,y12),lineArrangement.query(x12,y12));
				System.out.println("new color"+computeColor(x12,y12)+" "+lineArrangement.query(x12,y12)+" "+x12+","+y12);
				//l2 - 1 
				if(Algo.InHull(convexHull, x21, y21))
				colorMap.put(computeColor(x21,y21),lineArrangement.query(x21,y21));
				System.out.println("new color"+computeColor(x21,y21)+" "+lineArrangement.query(x21,y21)+" "+x21+","+y21);
				//l2 - 2
				if(Algo.InHull(convexHull, x22, y22))
				colorMap.put(computeColor(x22,y22),lineArrangement.query(x22,y22));
				System.out.println("new color"+computeColor(x22,y22)+" "+lineArrangement.query(x22,y22)+" "+x22+","+y22);
				
		
			
		}
		
	}

	private IdPt computeColor(MyPoint p) {
		IdPt temp = new IdPt(noOfLines);
		for (int k = 0; k < noOfLines; k++) {
			int res = lineList.get(k).lineSide(p);
			if (res > 0)
				temp.setBit(k, 1);
		}
		return temp;
	}
	
	private IdPt computeColor(double x, double y) {
		IdPt temp = new IdPt(noOfLines);
		for (int k = 0; k < noOfLines; k++) {
			int res = lineList.get(k).lineSide(x,y);
			if (res > 0)
				temp.setBit(k, 1);
		}
		return temp;
	}

	// helper class to track the "color" of the point
	class IdPt {
		int[] idArr;
		int len;

		public IdPt(int len) {
			if (len <= 0)
				throw new RuntimeException("unsupported length");
			int no = len / 32;
			// no++;
			if ((len - no * 32) > 0)
				no++;
			idArr = new int[no];
			for (int i = 0; i < idArr.length; i++)
				idArr[i] = 0;
			this.len = len;

		}

		public void setBit(int index, int bit) {
			if ((bit != 0 && bit != 1) || len < index)
				throw new RuntimeException("");
			int ind = index / 32;
			int bitInd = index % 32;
			if (bit == 0)
				idArr[ind] = idArr[ind] & (0 << bitInd);
			else if (bit == 1)
				idArr[ind] = idArr[ind] | (1 << bitInd);
		}

		public boolean equals(Object obj) {
			if (obj instanceof IdPt) {
				IdPt p = (IdPt) (obj);
				if (idArr.length != p.idArr.length)
					return false;
				else
					for (int i = 0; i < idArr.length; i++)
						if (idArr[i] != p.idArr[i])
							return false;
				return true;
			} else
				return false;
		}
		public int hashCode()
		{
			int temp=0;
			for(int i=0;i<idArr.length;i++)
				temp+=idArr[i];
			return temp;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < idArr.length; i++) {
				sb.append(idArr[i]);
				sb.append(" ");
			}
			sb = sb.reverse();
			return sb.toString();
		}

	}
}
