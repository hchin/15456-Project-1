//imports
import controlP5.*;

final int gridSize=10;

ControlP5 controlP5;

Textlabel myTextAlgo;
Textlabel myTextAlgoControl;
Textlabel myTextVisualControl;

PFont axesFont, mainFont;

float a; 
float camX=0,camY=0;
int [][] flyPtsFromFile;
int flyInd = 0;
ArrayList flyPts = new ArrayList();
Face [] facesArr;

char [] tmp = {
  'w','a','s','d',' '};
MultiKeyReader mkr = new MultiKeyReader(tmp,4);
PGraphics dImg;
boolean isFlying,writePt;


//msg prompt 
String curMsg="Waiting for user input";
int msgCount=0,msgPeriod=10;
boolean msgBlink;


//mouse
int mouseTipXOffSet=20, mouseTipYOffSet=20;

ArrayList myVertexList;
int state;
int gridXOffSet=50,gridYOffSet=50;


boolean gridOn;
boolean detailOn;
boolean finPoints;

void setup() 
{
  size(800, 600);
  smooth();
  frameRate(30);

  //load fonts
  axesFont = loadFont("Agency.vlw");
  mainFont = loadFont("AgencyFB-Bold-60.vlw");

  //initialize the UI
  controlP5 = new ControlP5(this);
  myTextAlgo = controlP5.addTextlabel("1","CYCLE",490,505);
  myTextAlgo.setColorValue(0x00000000);
  myTextVisualControl = controlP5.addTextlabel("2","OPTIONS",490,532);
  myTextVisualControl.setColorValue(0x00000000);
  myTextAlgoControl = controlP5.addTextlabel("3","FLOW",490,557);
  myTextAlgoControl.setColorValue(0x00000000);
  controlP5.addButton("FINALIZE",100,550,500,60,19);
  controlP5.addButton("VISUALIZE",100,620,500,60,19);
  controlP5.addButton("RESET",100,690,500,60,19);
  controlP5.addButton("FLYTHROUGH",100,550,525,60,19);
  controlP5.addButton("GRID",100,620,525,60,19);
  controlP5.addButton("DETAIL",100,690,525,60,19);
  controlP5.addButton("OVERVIEW",100,550,550,60,19);
  controlP5.addButton("STEP FRONT",100,620,550,60,19);
  controlP5.addButton("STEP BACK",100,690,550,60,19);



  isFlying=true;
  writePt=false;
  gridOn=true;
  loadFlyPts();
  readFacesFromFile("testFaces.txt");

  state=0;
  myVertexList = new ArrayList();
}

void draw() 
{
  //clear the canvas
  background(255);
  //persistant graphics


  switch(state)
  {
  case 0:
    pushMatrix();
    translate(gridXOffSet,gridYOffSet);
    plot2DAxes();
    drawVertexList();

    popMatrix();
    drawLabel();
    dispMouseTip();
    break;
  }
  // if

  /* lights();
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
   flyPts.add(""+mouseX+","+mouseY);*/

  noStroke();
  fill(200,200);
  rect(480,465,285,130);
  fill(3,20,111);
  textAlign(RIGHT);
  textFont(axesFont,24);
  text("Simplicial Depth Visualization",760,490);
  dispMsg();
}
public void dispMsg()
{
  textAlign(LEFT);
  textFont(axesFont,20);
  fill(0);
  if(msgCount==msgPeriod)
  {
    msgBlink=!msgBlink;
    msgCount=0;
  }
  if(msgBlink)
    text("> "+curMsg+"_",550,590);
  else
    text("> "+curMsg,550,590);
  msgCount++;
}
public void dispMouseTip()
{


  if(detailOn)
  {
    noStroke();
    fill(200,100);
    rect(mouseX+20,mouseY,60,24);
    textFont(axesFont,20);
    textAlign(LEFT);

    fill(0);
    text("X: "+((mouseX-gridXOffSet)/gridSize)+" Y: "+((mouseY-gridYOffSet)/gridSize),mouseX+20,mouseY+20);
  } 
}

// axes plotting
public void plot2DAxes()
{

  int xMax=100;
  int yMax=100;
  int linexMax = xMax*gridSize;
  int lineyMax = yMax*gridSize;


  //grid
  stroke(100);
  strokeWeight(1);
  fill(100);
  if(gridOn)
  {
    for(int i=1;i<xMax;i++)
      line(i*gridSize,0,i*gridSize,lineyMax);
    for(int j=1;j<yMax;j++)
      line(0,j*gridSize,linexMax,j*gridSize);
    strokeWeight(3);
    for(int i=0; i<=xMax;i+=10)
      line(i*gridSize,0,i*gridSize,lineyMax);
    for(int j=0; j<=xMax;j+=10)
      line(0,j*gridSize,linexMax,j*gridSize);
  }
  //x-axis
  strokeWeight(3);
  stroke(255,0,0);
  line(-100,0,linexMax,0);
  //y-axis
  stroke(0,255,0);
  line(0,-100,0,lineyMax);


}

void drawLabel()
{

  if(gridOn)
  {
    textFont(axesFont,20);
    textAlign(CENTER);
    noStroke();
    int xMax=100;
    int yMax=100;
    int linexMax = xMax*gridSize;
    int lineyMax = yMax*gridSize;
    pushMatrix();
    translate(gridXOffSet,0);

    fill(200,200);
    rect(-10,28,linexMax+30,20);
    fill(0);
    for(int i=0; i<=xMax;i+=10)
      text(i,i*gridSize,45); 
    popMatrix();

    pushMatrix();
    translate(0,gridYOffSet);
    fill(200,200);
    rect(30,-10,20,lineyMax+30);
    fill(0);
    for(int j=0; j<=yMax;j+=10)
      text(j,40,j*gridSize+8);
    popMatrix();

  } 
}

void drawVertexList()
{
  for(int i=0;i<myVertexList.size();i++)
  {
    MyVertex temp = (MyVertex)myVertexList.get(i);
    temp.render2D();
    //   println(temp);
  } 
}
void processVertex(int x, int y)
{
  boolean removePt = false;
  Iterator iter = myVertexList.iterator();
  while(iter.hasNext())
  {
    MyVertex temp = (MyVertex)iter.next();
    if(temp.checkInVertex(x,y))
    {
      iter.remove(); 
      removePt=true;
    }
  }
  if(removePt) return;
  myVertexList.add(new MyVertex(x,y));
  println(myVertexList.size());
}


// in visual mode

public void plot3DAxes()
{
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
  if(gridOn)
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
  switch(state)
  {
  case 0:
    println(checkMouse());
    if(checkMouse())
    {
      processVertex(mouseX,mouseY); 
    }
    break; 
  }
}



boolean checkMouse()
{
  println(mouseX-gridXOffSet);
  if(mouseButton==LEFT)
  {
    if(mouseX-gridXOffSet <= 100*gridSize && mouseX-gridXOffSet >= 0 && mouseY-gridYOffSet <= 100*gridSize && mouseY-gridYOffSet >=0) 
    {
      //480,465,285,130 boundary of UI
      if(mouseX>=480 && mouseX<=480+285 && mouseY>=465 && mouseY<=465+130)
        return false;
      else
        return true;  
    }

    else
      return false;
  }
  else
    return false;
}
public void mouseDragged()
{
  println(gridXOffSet);

  if(state==0 && mouseButton ==RIGHT)
  {
    gridXOffSet+=mouseX-pmouseX;
    gridYOffSet+=mouseY-pmouseY;
  } 
}

public void mouseReleased()
{
  if(state==0 && mouseButton ==RIGHT)
  {
    gridXOffSet=constrain(gridXOffSet,-550,300);
    gridYOffSet=constrain(gridYOffSet,-550,300);
  }
}


//button control events
public void GRID(int theValue) 
{
  gridOn=!gridOn;
}

public void DETAIL(int theValue) 
{
  detailOn=!detailOn;
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



























