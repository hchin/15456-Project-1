public class MyLine {
	MyPoint src, tgt;
	double len;
	public MyLine(MyPoint src, MyPoint tgt) {
		this.src = src;
		this.tgt = tgt;
		len=Math.sqrt((src.x-tgt.x)*(src.x-tgt.x)+(src.y-tgt.y)*(src.y-tgt.y));
	}

	public int lineSide(MyPoint c) {
		int res = Algo.CCW(src, tgt, c);
		return res;
	}
	//for the perturbation point
	public int lineSide(double x, double y) {
		int res = Algo.CCW(src.x, src.y,tgt.x,tgt.y,x,y);
	//	System.out.println(res);
		return res;
	}
	
	public String toString()
	{
		return ""+src+"  "+tgt;
	}


}
