import java.util.*;
public class FindRegionsDriver {
	
	static FindRegions fr;
	static ArrayList<MyPoint> pointList = new ArrayList<MyPoint>();
	
	public static void main (String [] args)
	{
	//	pointList.add(new MyPoint(10,10));
	//	pointList.add(new MyPoint(100,10));
	//	pointList.add(new MyPoint(100,100));
	//	pointList.add(new MyPoint(10,100));
		
		//100,100, 100,300, 300,290
		pointList.add(new MyPoint(100,100));
		pointList.add(new MyPoint(100,300));
		pointList.add(new MyPoint(300,290));
		pointList.add(new MyPoint(100,20));
		pointList.add(new MyPoint(340,230));
		fr = new FindRegions(pointList,400,400);
		//MyPoint [][] temp = fr.computeDepth();
		
		
		
	/*	for(int i=0;i<temp.length;i++)
		{
			for(int j=0;j<temp[0].length;j++)
			{
				System.out.print(temp[i][j].depth+" ");
			}
			System.out.println();
		}*/
	}

}
