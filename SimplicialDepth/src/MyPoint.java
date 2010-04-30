
public class MyPoint  
{
	int x, y, depth=0;
	double ang;
	public MyPoint(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}
	
	public void setAng(MyPoint ref)
	{
		ang = Math.atan2(ref.y-y, ref.x-x);
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof MyPoint)
		{
			MyPoint p = (MyPoint)obj;
			return (x==p.x && y==p.y);
		}
		else return false;
	}
	
	public void setDepth(int depth)
	{
		this.depth=depth;
	}
	
	public String toString()
	{
		return ""+x+","+y; 
	}

}
