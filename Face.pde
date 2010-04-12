class Face
{
  int [][] vertexPts;
  int maxHeight=0;
  int offSet =10;
  color faceColor,shadowColor;
  public Face(int [][] vertexPts, color faceColor)
  {
    this.vertexPts = vertexPts;
    this.faceColor = faceColor;

    for(int i=0;i<vertexPts.length;i++)
      maxHeight = max(maxHeight,vertexPts[i][2]);
    float r =  (faceColor >> 16 & 0xFF)/(0.006*maxHeight) ;
    float g =  (faceColor >> 8 & 0xFF)/(0.006*maxHeight);
    float b =  (faceColor  & 0xFF)/(0.006*maxHeight);
    shadowColor= color(r,g,b);
    //  shadowColor =color((int)red(faceColor)/maxHeight,(int)green(faceColor)/maxHeight,(int)blue(faceColor)/maxHeight);s
  } 

  public void render()
  {
    fill(faceColor,180);
    beginShape();
    for(int i=0;i<vertexPts.length;i++)
      vertex(vertexPts[i][0],vertexPts[i][1],vertexPts[i][2]+offSet);
    endShape(CLOSE);
    fill(shadowColor,50);
    beginShape();
    for(int i=0;i<vertexPts.length;i++)
      vertex(vertexPts[i][0],vertexPts[i][1],0);
    endShape(CLOSE);

  }

}




