class MyVertex
{
  int x, y, gridX,gridY;
  public MyVertex(int x , int y)
  {
    gridX=int((x-gridXOffSet+0.0+gridSize/2.0)/gridSize) ;
    gridY=int((y-gridYOffSet+0.0+gridSize/2.0)/gridSize) ;
    println(gridX+":"+gridY);
    this.x=gridX*gridSize;
    this.y=gridY*gridSize;
  } 

  public boolean checkInVertex(int x, int y)
  {
    return(dist(this.x,this.y,x-gridXOffSet,y-gridYOffSet)<=gridSize/2); 
  }

  public void render2D()
  {
    stroke(0);
    strokeWeight(10);
    point(x,y);
  }

  public void render3D()
  {

  }

  public String toString()
  {
    return ""+x+":"+y;
  }
}


