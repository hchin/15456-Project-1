import java.util.*;
public class MyFace  {
	ArrayList<MyPoint> pointList;
	
	public MyFace(ArrayList<MyPoint> pointList)
	{
		this.pointList = new ArrayList<MyPoint>(pointList);
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof MyFace)
		{
			MyFace tmpFace = (MyFace)obj;
			if(pointList.size()!= tmpFace.pointList.size()) return false; 
			for(int i=0;i<pointList.size();i++)
				if(!pointList.get(i).equals(tmpFace.pointList.get(i)))
					return false;
			return true;
		}
		else
			return false;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for(MyPoint p: pointList)
			sb.append(p);
		return sb.toString();
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
}
