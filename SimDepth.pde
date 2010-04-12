int [][] temp = {
  {
    0,200,0    }
  ,{
    200,400,0    }
  ,{
    400,200,0    }
  ,{
    300,0,0    }
  ,{
    100,0,0    }
};
PFont axesFont;
Face test = new Face(temp, color(255,0,0));
float a; 
float camX=0,camY=0;
int [][] flyPtsFromFile;
int flyInd = 0;
ArrayList flyPts = new ArrayList();
Face [] facesArr;

char [] tmp = {
  'w','a','s','d',' '};
MultiKeyReader mkr = new MultiKeyReader(tmp,4);
boolean isFlying,writePt;
void setup() 
{
  size(800, 600, P3D);
  colorMode(RGB, 255);
  //smooth();
  fill( 153);
  noStroke();
  isFlying=true;
  writePt=false;
  axesFont = loadFont("Agency.vlw");

  loadFlyPts();
  readFacesFromFile("testFaces.txt");
}

void draw() 
{
  background(100);
  lights();
  // ambientLight(51, 102, 126);

  //translate(width/8, height/8);
  if(isFlying)
  {
    if(flyInd==flyPtsFromFile.length)
      flyInd=0;
    camX = flyPtsFromFile[flyInd][0];
    camY =flyPtsFromFile[flyInd][1];
    flyInd++;
  }
  else
  {
    camX=mouseX;
    camY=mouseY; 
  }
  camera( 3*(camX-width/2), 3*(camY-height/2), 400.0, // eyeX, eyeY, eyeZ
  0, 0, 0.0, // centerX, centerY, centerZ
  0.0, 1.0, 0.0); 

  rotateX(PI/2);
  translate(-width/2, -height/2);
  plotAxes(true);
  //   translate(-width/2, -height/2);
  //camera(width/2.0, height/2.0, (height/2.0) / tan(PI*60.0 / 360.0), width/2.0, height/2.0, 0, 0, 0.1, 0);
  //origin axes



  // test.render();
  for(int i=0;i<facesArr.length;i++)
    facesArr[i].render();
  pushMatrix();
  translate(200,200);
  noStroke();
  fill(255);
  translate(0,200);
  sphere(10);
  translate(0,-200);


  translate(200,400);
  sphere(10);
  translate(-200,-400);


  translate(400,200);
  sphere(10);
  translate(-400,-200);


  translate(300,0);
  sphere(10);
  translate(-300,0);


  translate(100,0);
  sphere(10);
  translate(-100,0);
  popMatrix();

  text(""+mouseX+":"+mouseY,screenX(0,0,0),screenY(0,0,0));
  if(writePt)
    flyPts.add(""+mouseX+","+mouseY);
}

public void plotAxes(boolean grid)
{
  int gridSize=10;
  stroke(0);
  strokeWeight(5);
  point(0,0,0);  
  strokeWeight(1);
  stroke(255,0,0);
  line(-200,0,0, 200,0,0);
  stroke(0,255,0);
  line(0,-200,0, 0,200,0);
  stroke(0,0,255);
  line(0,0,-200, 0,0,200); 
  fill(0);
  textFont(axesFont,32);
  text("-> X",200,0,0);
  text("-> Y",0,200,0);
  text("-> Z",0,0,200);

  stroke(0);
  strokeWeight(1);
  noFill();
  if(grid)
  {
    for(int i=-10;i<100;i++)
      for(int j=-10;j<100;j++)
        rect(i*gridSize,j*gridSize,gridSize,gridSize); 

  }
}

void loadFlyPts()
{
  String [] temp = loadStrings("flyPts.txt");
  flyPtsFromFile = new int[temp.length][2];
  for(int i=0;i<temp.length;i++)
  {
    //  println(temp[i]);
    String[] temp1 = temp[i].split(",");
    flyPtsFromFile[i][0] = Integer.parseInt(temp1[0]);
    flyPtsFromFile[i][1] = Integer.parseInt(temp1[1]);
  }
}

void keyPressed()
{
  if(key==' ')
    isFlying = !isFlying; 
}

void mousePressed()
{
  /*
  if(!writePt)
    writePt=true;
  else
  {
    writePt=false;
    String [] temp = new String[flyPts.size()];
    for(int i=0;i<flyPts.size();i++)
    {
      temp[i] = new String(""+flyPts.get(i));
    }
    saveStrings("flyPts.txt",temp);

  }*/
}

void readFacesFromFile(String fileName )
{
  int index = 0;
  int faceCount =0;
  int xOffSet=200, yOffSet=200;
  String [] strLines = loadStrings(fileName);
  println(strLines[0]);
  facesArr = new Face [Integer.parseInt(strLines[index])];

  for(int i=0;i<facesArr.length;i++)
  {
    index++;
    int vertexCount  = Integer.parseInt(strLines[index]);
    int [][] tempVertexArr = new int[vertexCount][3];
    for(int j=0;j<vertexCount;j++)
    {
      index++;
      String [] tempStrArr = strLines[index].split(",");
      tempVertexArr[j][0] = Integer.parseInt(tempStrArr[0])+xOffSet;
      tempVertexArr[j][1] = Integer.parseInt(tempStrArr[1])+yOffSet;
      tempVertexArr[j][2] = Integer.parseInt(tempStrArr[2])/5 ;
    }
    index++;
    String[] tempStrArr = strLines[index].split(",");
    color tempColor = color(Integer.parseInt(tempStrArr[0]),Integer.parseInt(tempStrArr[1]),Integer.parseInt(tempStrArr[2]));
    facesArr[i] = new Face(tempVertexArr,tempColor);
  }
}




