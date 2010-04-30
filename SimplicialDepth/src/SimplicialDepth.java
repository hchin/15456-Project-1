import processing.core.*;
import controlP5.*;
import java.util.*;

public class SimplicialDepth extends PApplet {

	final int gridSize = 10;
	final int xMax = 40;
	final int yMax = 40;
	final int depthScale = 10;
	int linexMax = xMax * gridSize;
	int lineyMax = yMax * gridSize;
	ControlP5 controlP5;

	Textlabel myTextAlgo;
	Textlabel myTextAlgoControl;
	Textlabel myTextVisualControl;

	PFont axesFont, mainFont;

	float a;
	float camX = 0, camY = 0;
	int[][] flyPtsFromFile;
	int flyInd = 0;

	PGraphics dImg, depthImg;
	boolean isFlying, writePt;

	// for state 0
	ArrayList<MyPoint> pointList = new ArrayList<MyPoint>();

	// for state 1
	FindRegions myFr;
	MyPoint[][] depthMap;
	int[] depthColor;
	int[][] depthValue;

	// mouse
	int mouseTipXOffSet = 20, mouseTipYOffSet = 20;

	int state;
	int gridXOffSet = 0, gridYOffSet = 0;

	// flags
	boolean gridOn;
	boolean detailOn;
	boolean finPoints;
	boolean lockCam;

	public void setup() {
		size(420, 500);
		smooth();
		frameRate(30);

		// load fonts
		axesFont = loadFont("Agency.vlw");
		mainFont = loadFont("AgencyFB-Bold-60.vlw");

		// initialize the UI
		controlP5 = new ControlP5(this);
		controlP5.addButton("VISUALIZE", 101, 100, 430, 60, 19);
		controlP5.addButton("TOPVIEW", 305, 170, 430, 60, 19);
		controlP5.addButton("RESET", 102, 240, 430, 60, 19);

		controlP5.addButton("GRID", 100, 100, 450, 60, 19);
		controlP5.addButton("DETAIL", 170, 170, 450, 60, 19);
		controlP5.addButton("OVERVIEW", 120, 240, 450, 60, 19);

		// load the 3d fly through
		dImg = createGraphics(400, 400, P3D);
		depthImg = createGraphics(400, 400, P2D);
		reset();

	}

	private void reset() {
		isFlying = true;
		writePt = false;
		lockCam = false;
		gridOn = true;
		state = 0;
		pointList.clear();
		dImg.beginDraw();
		dImg.background(0, 0);
		dImg.endDraw();
		depthImg.beginDraw();
		depthImg.background(0, 0);
		depthImg.endDraw();
	}

	public void draw() {
		// clear the canvas
		background(255);

		// Persistent graphics
		switch (state) {
		case 0:
			pushMatrix();
			// translate(gridXOffSet,gridYOffSet);
			plot2DAxes();
			drawPointList();
			popMatrix();
			drawLabel();
			dispMouseTip();
			break;
		case 1:
			dImg.beginDraw();
			dImg.background(255);

			if (lockCam) {
				dImg.camera(0, 0, 355, 0, 0, 0, 0, 1, 0);
				dImg.translate(-200, -200);
				plot3DAxes(dImg);
				dImg.image(depthImg, 0, 0);

			} else {
				dImg.camera(3 * (camX - 200), 3 * (camY - 200), 400, 150, 0, 0,
						0, 1, 0); // centerX, centerY, centerZ
				dImg.rotateX(PI / 2);
				plot3DAxes(dImg);
				drawDepthMap(dImg);
			}

			dImg.endDraw();
			image(dImg, 10, 0);
			if (lockCam)
				dispMouseTip3D();
			break;
		}

		fill(3, 20, 111);
		textAlign(CENTER);
		textFont(axesFont, 24);
		text("Simplicial Depth Visualization", 205, 492);
	}

	public void dispMouseTip() {
		if (detailOn) {
			noStroke();
			fill(200, 200);
			rect(mouseX + 20, mouseY, 60, 24);
			textFont(axesFont, 20);
			textAlign(LEFT);

			fill(0);
			text(
					"X: " + ((mouseX) / gridSize) + " Y: "
							+ ((mouseY) / gridSize), mouseX + 20, mouseY + 20);
		}
	}

	public void dispMouseTip3D() {
		if (detailOn) {
			if (mouseX > 10 && mouseX < 410 && mouseY > 0 && mouseY < 410) {
				noStroke();
				fill(200, 200);
				rect(mouseX + 20, mouseY, 60, 45);
				textFont(axesFont, 20);
				textAlign(LEFT);
				int i =  ((mouseX - 10) / gridSize);
				int j = ((mouseY - 10) / gridSize);
				fill(0);
				text("X: " +i + " Y: "
						+j +"\n Depth"+depthValue[(mouseX - 10)][(mouseY - 10)], mouseX + 20, mouseY + 20);
			}
		}
	}

	// axes plotting
	public void plot2DAxes() {

		// grid
		stroke(100);
		strokeWeight(1);
		fill(100);
		if (gridOn) {
			for (int i = 1; i < xMax; i++)
				line(i * gridSize, 0, i * gridSize, lineyMax);
			for (int j = 1; j < yMax; j++)
				line(0, j * gridSize, linexMax, j * gridSize);
			strokeWeight(3);
			for (int i = 0; i <= xMax; i += 10)
				line(i * gridSize, 0, i * gridSize, lineyMax);
			for (int j = 0; j <= xMax; j += 10)
				line(0, j * gridSize, linexMax, j * gridSize);
		}
		// x-axis
		strokeWeight(3);
		stroke(255, 0, 0);
		line(-100, 0, linexMax, 0);
		// y-axis
		stroke(0, 255, 0);
		line(0, -100, 0, lineyMax);

	}

	void drawLabel() {

		if (gridOn) {
			textFont(axesFont, 20);
			textAlign(CENTER);
			noStroke();
			int linexMax = xMax * gridSize;
			int lineyMax = yMax * gridSize;
			pushMatrix();
			translate(gridXOffSet, 0);
			fill(200, 200);
			rect(-10, 400, linexMax + 30, 20);
			fill(0);
			for (int i = 0; i <= xMax; i += 10)
				text(i, i * gridSize, 418);
			popMatrix();

			pushMatrix();
			translate(0, gridYOffSet);
			fill(200, 200);
			rect(400, -10, 20, lineyMax + 30);
			fill(0);
			for (int j = 0; j <= yMax; j += 10)
				text(j, 410, j * gridSize + 8);
			popMatrix();

		}
	}

	// in visual mode

	public void plot3DAxes(PGraphics d) {
		d.strokeWeight(1);
		d.stroke(255, 0, 0);
		d.line(-200, 0, 0, 200, 0, 0);
		d.stroke(0, 255, 0);
		d.line(0, -200, 0, 0, 200, 0);
		d.stroke(0, 0, 255);
		d.line(0, 0, -200, 0, 0, 200);
		d.fill(0);
		d.textFont(axesFont, 32);
		d.text("-> X", 200, 0, 0);
		d.text("-> Y", 0, 200, 0);
		d.text("-> Z", 0, 0, 200);

		d.stroke(100);
		d.strokeWeight(1);
		d.noFill();
		if (gridOn) {
			if (gridOn) {
				for (int i = 1; i < xMax; i++)
					d.line(i * gridSize, 0, i * gridSize, lineyMax);
				for (int j = 1; j < yMax; j++)
					d.line(0, j * gridSize, linexMax, j * gridSize);
				d.strokeWeight(2);
				for (int i = 0; i <= xMax; i += 10)
					d.line(i * gridSize, 0, i * gridSize, lineyMax);
				for (int j = 0; j <= xMax; j += 10)
					d.line(0, j * gridSize, linexMax, j * gridSize);
			}

		}
	}

	void loadFlyPts() {
		String[] temp = loadStrings("flyPts.txt");
		flyPtsFromFile = new int[temp.length][2];
		for (int i = 0; i < temp.length; i++) {
			String[] temp1 = temp[i].split(",");
			flyPtsFromFile[i][0] = Integer.parseInt(temp1[0]);
			flyPtsFromFile[i][1] = Integer.parseInt(temp1[1]);
		}
	}

	void drawPointList() {
		strokeWeight(20);
		stroke(0);
		for (int i = 0; i < pointList.size(); i++)
			point(pointList.get(i).x, pointList.get(i).y);
	}

	boolean checkPoint(MyPoint p) {
		for (int i = 0; i < pointList.size(); i++)
			if (pointList.get(i).equals(p)) {
				pointList.remove(i);
				return false;
			}
		return true;
	}

	boolean checkMouse() {
		if (mouseX > 0 && mouseX < 400 && mouseY > 0 && mouseY < 400)
			return true;
		else
			return false;
	}

	public void keyPressed() {
		if (key == ' ')
			isFlying = !isFlying;
	}

	public void mousePressed() {
		switch (state) {
		case 0:

			if (checkMouse()) {
				MyPoint temp = new MyPoint(
						(int) ((mouseX + gridSize / 2.0) / gridSize) * gridSize,
						(int) ((mouseY + gridSize / 2.0) / gridSize) * gridSize);
				if (checkPoint(temp))
					pointList.add(temp);
			}
			break;
		}
	}

	public void mouseDragged() {
		if (state == 1 && !lockCam) {
			camX += mouseX - pmouseX;
			camY += mouseY - pmouseY;
		}
	}

	public void mouseReleased() {
		/*
		 * if(state==0 && mouseButton ==RIGHT) {
		 * gridXOffSet=constrain(gridXOffSet,-550,300);
		 * gridYOffSet=constrain(gridYOffSet,-550,300); }
		 */
	}

	// button control events
	public void GRID(int theValue) {
		gridOn = !gridOn;
	}

	public void DETAIL(int theValue) {
		detailOn = !detailOn;
	}

	public void RESET(int theValue) {
		reset();
	}

	public void TOPVIEW(int theValue) {
		lockCam = !lockCam;
	}

	public void VISUALIZE(int theValue) {
		if (state == 0 && pointList.size() >= 3) {
			state = 1;
			myFr = new FindRegions(pointList, 400, 400);
			depthMap = myFr.computeDepth();
			System.out.println("found depth map");

			depthColor = new int[myFr.getMaxDepth() + 1];
			System.out.println(myFr.getMaxDepth());
			System.out.println("depth " + depthColor.length);
			for (int i = 1; i < depthColor.length; i++)
				depthColor[i] = color(255 - 230 / i);
			System.out.println("done coloring");
			depthValue = new int[depthMap.length][depthMap[0].length];
			depthImg.beginDraw();
			depthImg.background(255, 0);
			depthImg.strokeWeight(1);
			// draw in the regions
			for (int i = 0; i < depthMap.length; i++)
				for (int j = 0; j < depthMap[0].length; j++) {
					depthValue[i][j] = depthMap[i][j].depth;
					if (depthValue[i][j] > 0) {
						depthImg.stroke(depthColor[depthValue[i][j]]);
						depthImg.point(i, j);
					}

				}
			// draw the vertices
			depthImg.stroke(0, 0, 255);
			depthImg.strokeWeight(10);
			for (MyPoint p : pointList)
				depthImg.point(p.x, p.y);
			depthImg.endDraw();
		}

	}

	public void drawDepthMap(PGraphics depthImg) {
		// depthImg.stroke(0);
		depthImg.strokeWeight(3);
		for (int i = 0; i < depthMap.length; i++)
			for (int j = 0; j < depthMap[0].length; j++)
				if (depthMap[i][j].depth > 0) {
					depthImg.stroke(depthColor[depthValue[i][j]]);
					depthImg.point(i, j, depthMap[i][j].depth * depthScale);
				}

		depthImg.fill(0, 0, 255);
		depthImg.noStroke();
		for (int i = 0; i < pointList.size(); i++) {
			MyPoint p = pointList.get(i);
			depthImg.translate(p.x, p.y, 10);
			depthImg.sphere(10);
			depthImg.translate(-p.x, -p.y, -10);
		}

	}

	static public void main(String args[]) {
		PApplet.main(new String[] { "--bgcolor=#F0F0F0", "SimplicialDepth" });
	}

}
