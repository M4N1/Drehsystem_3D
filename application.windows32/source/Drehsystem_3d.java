import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Iterator; 
import java.util.Map; 
import java.text.DecimalFormat; 
import java.math.RoundingMode; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Drehsystem_3d extends PApplet {




final static boolean output = true;
long startTime;
long time = 0;
boolean pressed = false;
int min = 0;
char lastPressedKey = ' ';
int lastPressedKeyCode = -1;
long lastKeyEvent = 0;
int currWindowWidth;
int currWindowHeight;
TextBoxListener textEditedListener;
TextView tvW;
MenuItem menuItem;
ArrayList<GraphApplet> applets;
ArrayList<TextView> textviews;
ArrayList<TextBox> textboxes;
ArrayList<Button> buttons;
int tbStartX;
int tbStartY;
int tbWidth;
ArrayList<Character> keys;
ArrayList<Integer> keyCodes;
ArrayList<Integer> mouseButtons;
ArrayList<Point> points = new ArrayList<Point>();
Point pointToAdd;
int nameCounter = 65;
PGraphics xySurface, yzSurface, xzSurface;
ArrayList<Checkbox> checkboxes;
TextBox tbW;
Button bReset, bStart, bClearPath, bAlign;
int checkBoxY = 140;
int checkBoxOffset = 30;
Checkbox cLines;
Checkbox cVelocity;
Checkbox cAcceleration;
Checkbox cOutput;
Checkbox cPath;
PVector[] lastPos;
PVector[] v;
float size = 20;
float scale = 0.2f;
float scaleD = 40;
PVector[] w = { 
  new PVector(0, 0, 0), 
  new PVector(0, 0, 150), 
  new PVector(0, 0, 100) };
float[] phi = {0, 0, 0};
float[] A = {0, 2, 5};
float[] a = {0, 0, 0};
long lastTime = 0;
float startPhi = 0;
float speed = 1.0f;
boolean setup = true;
boolean inputWindowOpened = false;
PVector mouseReference = new PVector(0, 0, 0);
boolean centerButtonPressed = false;
boolean rightButtonPressed = false;
boolean rotation = false;
boolean zooming = false;
boolean translation = false;
float[] currentAngle = new float[]{0, 0, 0};
float[] angle = new float[]{0, 0, 0};
float[] lastSetAngle = new float[]{0, 0, 0};
float zoom = 1;
float setZoom = 1;
PVector pos;
PVector lastSetPos;
boolean removePoints = false;

HashMap<Integer, Integer[]> objects = new HashMap<Integer, Integer[]>();
int idCount = 0;
Integer[] colorCount = {100,0,0};
PGraphics detectionCanvas;

boolean reset = false;
boolean stopped = false;
boolean clearPath = false;

Toast toast;

public void settings() {
  size(1500, 800, P3D);
}

public void setup()
{
  
  pos = new PVector(width/2, height/2, 0);
  lastSetPos = new PVector(width/2, height/2, 0);
  detectionCanvas = createGraphics(width, height, P3D);
  this.currWindowWidth = this.width;
  this.currWindowHeight = this.height;
  surface.setResizable(true);
  tbStartX = 50;
  tbStartY = height-450;
  tbWidth = 120;
  applets = new ArrayList<GraphApplet>();
  buttons = new ArrayList<Button>();
  textviews = new ArrayList<TextView>();
  textboxes = new ArrayList<TextBox>();
  checkboxes = new ArrayList<Checkbox>();
  keys = new ArrayList<Character>();
  keyCodes = new ArrayList<Integer>();
  mouseButtons = new ArrayList<Integer>();

  if (!output)
    min = 1;
  background(0);

  cLines = addCheckBox("lines", true);
  cVelocity = addCheckBox("v", true);
  cAcceleration = addCheckBox("acc", false);
  cOutput = addCheckBox("out", false);
  cPath = addCheckBox("path", true);
  
  xySurface = createGraphics(100, 100, P2D);
  xySurface.beginDraw();
  xySurface.background(0, 0, 255,50);
  xySurface.endDraw();
  
  yzSurface = createGraphics(100, 100, P2D);
  yzSurface.beginDraw();
  yzSurface.background(0, 255, 0,50);
  yzSurface.endDraw();
  
  xzSurface = createGraphics(100, 100, P2D);
  xzSurface.beginDraw();
  xzSurface.background(255, 0, 0,50);
  xzSurface.endDraw();

  float yOff = 0;
  Point point;
  //addNewPoint(null, new PVector(0, 0, 0), new PVector(0, 0, 0), 0);
  //addNewPoint(getLastPoint(), new PVector(0, -5, 0), new PVector(0, 0, -100), 0);
  //point = addNewPoint(getLastPoint(), new PVector(0, -4, 0), new PVector(100, 0, 300), 0);
  //getLastPoint().setPathColor(new int[]{255,255,0});
  //getLastPoint().drawPath();
  //addNewPoint(point, new PVector(0, 0, 4), new PVector(0, 0, 0), 0);
  //getLastPoint().setPathColor(new int[]{255,0,255});
  //getLastPoint().drawPath();
  //addNewPoint(point, new PVector(0, 0, -4), new PVector(0, 0, 0), 0);
  //getLastPoint().setPathColor(new int[]{0,255,255});
  //getLastPoint().drawPath();
  addNewPoint(null, new PVector(0, 0, 0), new PVector(0, 0, 0), 0);
  addNewPoint(getLastPoint(), new PVector(0, -5, 0), new PVector(0, 0, 0), 0);
  point = addNewPoint(getLastPoint(), new PVector(0, -4, 0), new PVector(100, 100, 100), 0);
  addNewPoint(getLastPoint(), new PVector(0, -4, 0), new PVector(-100, -100, -100), 0);
  //getLastPoint().setPathColor(new int[]{255,255,0});
  //getLastPoint().drawPath();
  //addNewPoint(point, new PVector(0, 0, 4), new PVector(0, 0, 0), 0);
  //getLastPoint().setPathColor(new int[]{255,0,255});
  //getLastPoint().drawPath();
  //addNewPoint(point, new PVector(0, 0, -4), new PVector(0, 0, 0), 0);
  //getLastPoint().setPathColor(new int[]{0,255,255});
  //getLastPoint().drawPath();
  //addNewPoint(point, new PVector(-4, 0, 0), new PVector(0, 0, 100), 0);

  this.bReset = new Button(this, this.tbStartX-40, this.tbStartY+yOff, 120, 50, "Remove All");
  this.bReset.setBackground(0);
  this.bReset.setTextColor(255);
  this.bReset.setCornerRadius(15);
  this.bReset.setTextAlignment(15);
  this.bReset.setOnClickListener(new OnClickListener() {
    @Override
      public void onClick(int id) {
      removePoints = true;
    }
  }
  );
  buttons.add(this.bReset);
  yOff += 70;

  this.bStart = new Button(this, this.tbStartX-40, this.tbStartY+yOff, 120, 50, "Start Pos");
  this.bStart.setBackground(0);
  this.bStart.setTextColor(255);
  this.bStart.setCornerRadius(15);
  this.bStart.setTextAlignment(15);
  this.bStart.setOnClickListener(new OnClickListener() {
    @Override
      public void onClick(int id) {
      reset = true;
    }
  }
  );
  buttons.add(this.bStart);
  yOff += 70;
  
  this.bClearPath = new Button(this, this.tbStartX-40, this.tbStartY+yOff, 120, 50, "Clear Path");
  this.bClearPath.setBackground(0);
  this.bClearPath.setTextColor(255);
  this.bClearPath.setCornerRadius(15);
  this.bClearPath.setTextAlignment(15);
  this.bClearPath.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(int id) {
      clearPath = true;
    }
  }
  );
  buttons.add(this.bClearPath);
  yOff += 70;
  
  this.bAlign = new Button(this, this.tbStartX-40, this.tbStartY+yOff, 120, 50, "Align");
  this.bAlign.setBackground(0);
  this.bAlign.setTextColor(255);
  this.bAlign.setCornerRadius(15);
  this.bAlign.setTextAlignment(15);
  this.bAlign.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(int id) {
      angle = new float[]{0, 0, 0};
      lastSetAngle = new float[]{0, 0, 0};
      pos = new PVector(width/2, height/2, 0);
      lastSetPos = new PVector(width/2, height/2, 0);
      zoom = 1;
      setZoom = 1;
    }
  }
  );
  this.bAlign.setId(1);
  buttons.add(this.bAlign);
  
//angle = new float[]{0, 0, 0};
//    lastSetAngle = new float[]{0, 0, 0};
  println("\n");
  for (Point p : points) {
    println(p.name);
    println(p.pos + "\n");
  }

  lastTime = millis();
  startTime = millis();
  stopped = true;
  setup = false;

  toast = new Toast(this, "Welcome!", Toast.DURATION_LONG);

  //sa = new SecondApplet();
  //sa.pause();
  //sa.createDataSet("v", 0, 0, 255);
  //sa.createDataSet("a", 255, 0, 0);
}

public void update()
{
  if (!this.setup) {
    if (this.reset) println("reset update");
    for (Point p : points) {
      p.update();
    }
    for (GraphApplet sa : applets) {
      for (Point p : points) {
        if (p.getName().equals(sa.getName())) {
          PVector pos = new PVector(p.pos.x, p.pos.y, p.pos.z);
          float v = pos.sub(p.v).mag();
          pos = new PVector(p.pos.x, p.pos.y, p.pos.z);
          float a = pos.sub(p.a).mag();
          sa.addPoint("v", v);
          sa.addPoint("a", a);
        }
      }
    }
  }
}

public void draw()
{
  if (keyPressed && (millis()-lastKeyEvent > 100) && (lastPressedKeyCode == 139 || lastPressedKeyCode == 93 || lastPressedKeyCode == 140 || lastPressedKeyCode == 47)) {
    handleKeyPressedEvent(lastPressedKeyCode, lastPressedKey);
  }
  if (this.pointToAdd != null) {
      //Point addNewPoint(Point parent, PVector pos, PVector w, float alpha) 
      addNewPoint(this.pointToAdd.parent, this.pointToAdd.setPos, this.pointToAdd.setW, this.pointToAdd.setAlpha); 
      this.pointToAdd = null;
    }
  if (removePoints) {
    points = new ArrayList<Point>();
    this.nameCounter = 65;
    addNewPoint(null, new PVector(0, 0, 0), new PVector(0, 0, 0), 0);
    removePoints = false;
  } else if (clearPath) {
     for (Point p : points) {
        p.clearPath(); 
     }
     clearPath = false;
  }
  noLights();
  pushMatrix();
  if (rotation) {
    float maxAngle = PI;
    angle[0] = map((float)(mouseX - mouseReference.x), -width, width, -maxAngle, maxAngle) * 2 + lastSetAngle[0]; //mouseReference.x
    angle[1] = map((float)(mouseY - mouseReference.y), -height, height, maxAngle, -maxAngle) * 2 + lastSetAngle[1]; //mouseReference.y
    if (angle[0] < -PI) angle[0] += TWO_PI;
    else if (angle[0] > PI) angle[0] -= TWO_PI;
    if (angle[1] < -PI) angle[1] += TWO_PI;
    else if (angle[1] > PI) angle[1] -= TWO_PI;
    println("rotation y:" + angle[0]);
    println("rotation x:" + angle[1]);
    //if (angle[0] < -PI || angle[0] > PI) angle[0] *= -1;
    //if (angle[1] < -PI || angle[1] > PI) angle[1] *= -1;
    //println("angle x:" + angle[1]);
    //println("angle y:" + angle[0]);
    //println("last angle x:" + lastSetAngle[1]);
    //println("last angle y:" + lastSetAngle[0]);
  } else if (translation) {
    pos = new PVector(lastSetPos.x+(mouseX-mouseReference.x), lastSetPos.y+(mouseY-mouseReference.y), 0);
  }
  boolean windowResized = !(this.currWindowWidth == this.width && this.currWindowHeight == this.height);
  if (windowResized) {
    toast.windowResized(this.currWindowWidth, this.currWindowHeight);
    this.currWindowWidth = this.width;
    this.currWindowHeight = this.height;
    this.pos = new PVector(width/2, height/2, 0);
    this.lastSetPos = new PVector(width/2, height/2, 0);
    detectionCanvas = createGraphics(width, height, P3D);
    erasePath();
  }
  background(0);
  detectionCanvas.beginDraw();
  detectionCanvas.background(0);
  detectionCanvas.endDraw();
  if (this.reset) {
    //angle = new float[]{0, 0, 0};
    //lastSetAngle = new float[]{0, 0, 0};
    for (Point p : points) {
      p.moveToStart();
      //println("\nlastPos:" + p.lastPos);
      //println("pos:" + p.pos);
    }
    this.stopped = true;
    this.reset = false;
  }
  //image(canvas, 0, 0);
  toast.draw();

  for (Button b : buttons) {
    if (b.id == 1) {
      boolean visible = !(
          this.currentAngle[0] == 0 
      &&  this.currentAngle[1] == 0 
      &&  this.currentAngle[2] == 0 
      &&  this.pos.x == width/2 
      &&  this.pos.y == height/2
      &&  this.zoom == 1);
      b.setVisibility(visible);
    }
    b.draw();
  }

  for (Checkbox c : checkboxes) c.draw();

  for (TextView tv : textviews) tv.draw();

  for (TextBox tb : textboxes) tb.draw();

  translate(pos.x, pos.y, 0);
  
  pushMatrix();
  //rotateY(angle[0]*angle[1]/HALF_PI+angle[0]);
  //rotateZ(-angle[1]*(PI-angle[0])/HALF_PI+angle[1]);
  //rotateX(-angle[1]*angle[0]/HALF_PI+angle[1]);
  //PVector v1 = new PVector(0, cos(currentAngle[0])+1, 0);
  //PVector v2 = new PVector(cos(currentAngle[1])+1, 0, 0);
  //PVector v3 = new PVector(0, 0, cos(currentAngle[2])+1);
  //detectionCanvas.beginDraw();
  //detectionCanvas.background(0);
  //detectionCanvas.translate(width/2, height/2);
  //detectionCanvas.rotate(angle[0], 0, cos(currentAngle[0])+1, 0);
  //detectionCanvas.rotate((currentAngle[0] > HALF_PI || currentAngle[0] < -HALF_PI ? -1 : 1) * angle[1], cos(currentAngle[1])+1, 0, 0);
  //detectionCanvas.rotate(angle[2], 0, 0, cos(currentAngle[2])+1);
  //detectionCanvas.endDraw();
    //zoom = setZoom - (mouseY - mouseReference.y) / 50;
  zoom = zooming ? setZoom - (mouseY - mouseReference.y) / 50 : setZoom;
  scale(zoom);
  //rotate(angle[0], 0, cos(currentAngle[0]), 0);
  rotateY(angle[0]);
  currentAngle[0] = angle[0];
  
  //rotate((currentAngle[0] > HALF_PI || currentAngle[0] < -HALF_PI ? -1 : 1) * angle[1], cos(currentAngle[1]), 0, 0);
  rotateX(angle[1]);
  currentAngle[1] = angle[1];
  
  //rotate(angle[2], 0, 0, cos(currentAngle[2]));
  currentAngle[2] = angle[2];
  
  if (!this.stopped) {
    update();
    //this.stopped = true;
  }
  
  //println("x:" + v1.x);
  //println("y:" + v1.y);
  //println("z:" + v1.z);
  //currentAngle[0] += angle[0]*angle[1]/HALF_PI+angle[0];
  //currentAngle[1] += -angle[1]*angle[0]/HALF_PI+angle[1];
  //currentAngle[2] += -angle[1]*(PI-angle[0])/HALF_PI+angle[1];
  stroke(255, 0, 0);
  //line(0, 0, v1.x*100, v1.y*100);
  //line(0, 0, v2.x*100, v2.y*100);
  //line(0, 0, v3.x*100, v3.y*100);
  image(xySurface, -50, -50);
  pushMatrix();
  rotateY(HALF_PI);
  image(yzSurface, -50, -50);
  popMatrix();
  pushMatrix();
  rotateX(HALF_PI);
  rotateZ(HALF_PI);
  image(xzSurface, -50, -50);
  popMatrix();
  for (Point p : points) {
    p.setVisibilityL(cLines.isChecked());
    p.setVisibilityV(cVelocity.isChecked());
    p.setVisibilityA(cAcceleration.isChecked());
    p.draw();
    int id = p.getId();
    //println("point id:" + id);
    Integer[] colorValue = objects.get(id);
    detectionCanvas.fill(colorValue[0], colorValue[1], colorValue[2]);  //colorValue
    //detectionCanvas.fill(255);  //colorValue
    detectionCanvas.beginDraw();
    detectionCanvas.noStroke();
    detectionCanvas.pushMatrix();
    detectionCanvas.translate(pos.x, pos.y);
    detectionCanvas.scale(zoom);
    detectionCanvas.rotate(angle[0], 0, cos(currentAngle[0]), 0);
    detectionCanvas.rotate((currentAngle[0] > HALF_PI || currentAngle[0] < -HALF_PI ? -1 : 1) * angle[1], cos(currentAngle[1]), 0, 0);
    detectionCanvas.rotate(angle[2], 0, 0, cos(currentAngle[2]));
    detectionCanvas.translate(p.pos.x*scaleD, p.pos.y*scaleD, p.pos.z*scaleD);
    detectionCanvas.sphere(10);
    detectionCanvas.popMatrix();
    detectionCanvas.endDraw();
  }
  

  if (cPath.isChecked() && !windowResized) {
    if (points.size() > 1) {
      for (int i=0; i<points.size(); i++) {
        Point p = points.get(i);
        //if (!(p.lastPos.x == p.pos.x && p.lastPos.y == p.pos.y))
        if (p.getPathVisibility()) {
          int[] c = p.getPathColor();
          stroke(c[0], c[1], c[2]);
          ArrayList<PVector> path = p.getPath();
          if (path != null && path.size() > 1) {
            for (int j=0; j<path.size()-1; j++) {
                PVector lastPos = path.get(j);
                PVector pos = path.get(j+1);
                line(lastPos.x*scaleD, lastPos.y*scaleD, lastPos.z*scaleD, pos.x*scaleD, pos.y*scaleD, pos.z*scaleD);
            }
          }
        }
      }
    }
  }
  
  popMatrix();
  popMatrix();
  hint(DISABLE_DEPTH_TEST);
  pushMatrix();
  translate(width/2, height/2);
  noLights();
  fill(255);
  stroke(255);
  textSize(20);
  text("X:" + mouseX, -width/2+40, height/2-60);
  text("Y:" + mouseY, -width/2+40, height/2-40);
  text("Elapsed time:" + (millis()-this.startTime), -width/2+40, height/2-20);
  
  //text("ReferenceX:"+mouseReference.x, -width/2+120, height/2-60);
  //text("ReferenceY:"+mouseReference.y, -width/2+120, height/2-40);
  
  //text("AngleX:"+angle[1], -width/2+300, height/2-60);
  //text("AngleY:"+angle[0], -width/2+300, height/2-40);
  //text("Last key:" + lastPressedKeyCode + " / '" + lastPressedKey + "'", -width/2+400, height/2-20);

  textSize(25);
  if (output)
  {
    fill(255);
    stroke(255);

    text("Scale:", -width/2+20, -height/2+40);
    text("1m/s : " + scale + "px\n1m : " + scaleD + "px", -width/2+120, -height/2+40);

    if (stopped) text("paused", -width/2+10, height/2-100);

    String speedOutput = "Speed: " + "x" + speed;
    text(speedOutput, -textWidth(speedOutput)/2, height/2-20);
    //text(speedOutput, -width/2+700, height/2-20);
    /*if (cOutput.isChecked()) {
     String v1 = ((v[1].mag() < 100) ? ("  " + Integer.toString((int)(v[1].mag()+0.5))) : (Integer.toString((int)(v[1].mag()+0.5))));
     String v2 = ((v[3].mag() < 100) ? ("  " + Integer.toString((int)(v[3].mag()+0.5))) : (Integer.toString((int)(v[3].mag()+0.5))));
     String v3 = ((v[2].mag() < 100) ? ("  " + Integer.toString((int)(v[2].mag()+0.5))) : (Integer.toString((int)(v[2].mag()+0.5))));
     text("v_oa: " + v1 + " m/s", -width/2+10, height/2-100);
     text("v_ap: " + v2 + " m/s", -width/2+10, height/2-60);
     text("v_op: " + v3 + " m/s", -width/2+10, height/2-20);
     
     String w1 = w[1].z < 10 ? ("  " + String.format("%.02f", w[1].z)) : String.format("%.02f", w[1].z);
     String w2 = w[2].z < 10 ? ("  " + String.format("%.02f", w[2].z)) : String.format("%.02f", w[2].z);
     
     text("omega_oa: " + w1 + " 1/s", -width/2+275, height/2-100);
     text("omega_ap: " + w2 + " 1/s", -width/2+275, height/2-60);
     
     float timeS = (float)time/1000;
     text("Time: " + String.format("%.02f", timeS) + " s", -width/2+275, height/2-20);
     
     
     text( "alpha_oa: " + a[1] + " 1/s^2", -width/2+700, height/2-100);
     text("alpha_ap: " + a[2] + " 1/s^2", -width/2+700, height/2-60);
     }*/
  }
  popMatrix();
  if (menuItem != null)
    menuItem.draw();
  //image(detectionCanvas,0,0);
  hint(ENABLE_DEPTH_TEST);
}

public void mousePressed() {
  if (!mouseButtons.contains(mouseButton)) mouseButtons.add(mouseButton);
  println("mouse pressed : '" + mouseButton + "'");
  toast.onMousePressed();
  for (Button b : buttons) b.mousePressedEvent();
  if (menuItem != null)
    if (menuItem.onMousePressed()) return;
  if (mouseButton == LEFT) {
    mouseReference = new PVector(mouseX, mouseY, 0);
    translation = true;
  } else if (mouseButton == CENTER) {
    centerButtonPressed = true;
  } else if (mouseButton == RIGHT) {
    rightButtonPressed = true;
    detectionCanvas.loadPixels();
    int objectId = -1;
    Iterator it = objects.entrySet().iterator();
    while (it.hasNext()) {
        //println("Iterator");
        Map.Entry pair = (Map.Entry)it.next();
        Integer[] colorValue = (Integer[])pair.getValue();
        int c = detectionCanvas.pixels[mouseX+mouseY*width];
        //println("cV:" + color(colorValue[0], colorValue[1], colorValue[2]));
        //println("c:" + c);
        if (color(colorValue[0], colorValue[1], colorValue[2]) == c) {
          objectId = (int)pair.getKey();
          //println("id:" + objectId);
          break;
        }
        //System.out.println(pair.getKey() + " = " + pair.getValue());
        //it.remove(); // avoids a ConcurrentModificationException
    }
    //for (int colorValue : objects) {
    //  if (color(colorValue) == detectionCanvas.pixels[mouseX+mouseY*width)) {
    //  }
    //}
    if (objectId != -1) {
      for (Point p : points) {
        final Point point = p;
        //final Point point = objects.get(objectId);
        if (point.getId() == objectId) {
        //if (p.mousePressedEvent(mouseX-width/2, mouseY-height/2)) {
          println("point " + p.getName() + " pressed");
          final String[] possibleValues = new String[]{"Add Point", "Change value", "Graph", "Hide Path", "Draw Path", "Remove", "Change Color"};
          final String[] values;
          if (point.parent != null)
            values = new String[]{possibleValues[0], possibleValues[1], possibleValues[2], (point.visibilityPath ? possibleValues[3] : possibleValues[4]), possibleValues[5], possibleValues[6]};
          else
            values = new String[]{possibleValues[0]};
          PVector scaledPos = new PVector(point.pos.x, point.pos.y, point.pos.z);
          scaledPos = scaledPos.mult(scaleD);
          pushMatrix();
          translate(pos.x, pos.y);
          scale(zoom);
          rotate(angle[0], 0, cos(currentAngle[0])+1, 0);
          rotate((currentAngle[0] > HALF_PI || currentAngle[0] < -HALF_PI ? -1 : 1) * angle[1], cos(currentAngle[1])+1, 0, 0);
          rotate(angle[2], 0, 0, cos(currentAngle[2])+1);
          translate(scaledPos.x, scaledPos.y, scaledPos.z);
          float x = screenX(0,0,0);
          float y = screenY(0,0,0);
          popMatrix();
          println("MenuX:"+x);
          println("MenuY:"+y);
          menuItem = new MenuItem(this, x, y, "Title", values);  //, "Change value", "Graph", (point.visibilityPath ? "Hide Path" : "Draw Path")
          menuItem.setOnItemClickListener(new OnItemClickListener() {
            @Override
              public void onItemClick(int itemIdx, String item) {
              InputBox ib;
              //switch (itemIdx) {
              //case 1:
              
              // add point
              if (item.equals(possibleValues[0])) {
                ib = new InputBox("Input", new String[]{"x", "y", "z", "wx", "wy", "wz", "alpha"}, new String[]{"0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0"});
                ib.setMaxLimits(new float[]{
                  15,//(width/2-getLastPoint().absSetPos.x*scaleD)/scaleD, 
                  15,//(height/2-getLastPoint().absSetPos.y*scaleD)/scaleD,
                  100,
                  400, 
                  400, 
                  400, 
                  200
                  });
                //println("width/2:"+width/2);
                //println("lastPointPos:"+getLastPoint().absSetPos.mult(scaleD));
                ib.setMinLimits(new float[]{
                  -width/2, 
                  -height/2,
                  -100,
                  -400, 
                  -400, 
                  -400, 
                  -200
                  });
                ib.setOnEditingFinishedListener(new InputBoxListener() {
                  @Override
                    public void finishedEditing(String... data) {
                    inputWindowOpened = false;
                    if (data.length == 7) {
                      float x = data[0] == "" ? 0.0f : Float.parseFloat(data[0]);
                      float y = data[1] == "" ? 0.0f : Float.parseFloat(data[1]);
                      float z = data[2] == "" ? 0.0f : Float.parseFloat(data[2]);
                      float wx = data[3] == "" ? 0.0f : Float.parseFloat(data[3]);
                      float wy = data[4] == "" ? 0.0f : Float.parseFloat(data[4]);
                      float wz = data[5] == "" ? 0.0f : Float.parseFloat(data[5]);
                      float alpha = data[6] == "" ? 0.0f : Float.parseFloat(data[6]);
                      println("x:"+x);
                      println("y:"+y);
                      println("z:"+z);
                      println("wx:"+wx);
                      println("wy:"+wy);
                      println("wz:"+wz);
                      println("alpha:"+alpha);
                      reset = true;
                      if (!(x == 0 && y == 0 && z == 0)) {
                        for (Point p : points) {
                          p.reset();
                        }
                        pointToAdd = new Point(idCount, point, new PVector(x, y, z), new PVector(wx, wy, wz), alpha);
                        reset = true;
                      }
                    }
                  }
                  @Override
                    public void onExit() {
                    inputWindowOpened = false;
                  }
                }
                );
                inputWindowOpened = true;
              }
                //break;
  
              //case 2:
              else if (item.equals(possibleValues[1])) {
                if (!inputWindowOpened) {
                  String[] values = new String[]{"x", "y", "z", "wx", "wy", "wz", "alpha"};
                  String[] standardValues = new String[]{
                    Float.toString(point.setPos.x), 
                    Float.toString(point.setPos.y), 
                    Float.toString(point.setPos.z), 
                    Float.toString(point.setW.x),
                    Float.toString(point.setW.y),
                    Float.toString(point.setW.z), 
                    Float.toString(point.setAlpha)
                  };
                  ib = new InputBox("Change " + point.getName(), values, standardValues);
                  ib.setMaxLimits(new float[]{
                    15,//(width/2-getLastPoint().absSetPos.x*scaleD)/scaleD, 
                    15,//(height/2-getLastPoint().absSetPos.y*scaleD)/scaleD,
                    100,
                    400, 
                    400, 
                    400, 
                    200
                    });
                  //println("width/2:"+width/2);
                  //println("lastPointPos:"+getLastPoint().absSetPos.mult(scaleD));
                  ib.setMinLimits(new float[]{
                    -width/2, 
                    -height/2,
                    -100,
                    -400, 
                    -400, 
                    -400, 
                    -200
                    });
                  ib.setOnEditingFinishedListener(new InputBoxListener() {
                    @Override
                      public void finishedEditing(String... data) {
                        inputWindowOpened = false;
                        if (data.length == 7) {
                          float x = data[0] == "" ? 0.0f : Float.parseFloat(data[0]);
                          float y = data[1] == "" ? 0.0f : Float.parseFloat(data[1]);
                          float z = data[2] == "" ? 0.0f : Float.parseFloat(data[2]);
                          float wx = data[3] == "" ? 0.0f : Float.parseFloat(data[3]);
                          float wy = data[4] == "" ? 0.0f : Float.parseFloat(data[4]);
                          float wz = data[5] == "" ? 0.0f : Float.parseFloat(data[5]);
                          float alpha = data[6] == "" ? 0.0f : Float.parseFloat(data[6]);
                          println("x:"+x);
                          println("y:"+y);
                          println("z:"+z);
                          println("wx:"+wx);
                          println("wy:"+wy);
                          println("wz:"+wz);
                          println("alpha:"+alpha);
                          if (!(x == 0 && y == 0 && z == 0)) {
                            point.setPos(new PVector(x, y, z));
                            point.setW(new PVector(wx, wy, wz));
                            point.setAlpha(alpha);
                            for (Point p : points) {
                              p.reset();
                            }
                            reset = true;
                          }
                        }
                      }
                      @Override
                        public void onExit() {
                        inputWindowOpened = false;
                      }
                    }
                  );
                  inputWindowOpened = true;
                }
                //break;
              }
  
              //case 3:
              else if (item.equals(possibleValues[2])) {
                String name = point.getName();
                boolean exists = false;
                for (GraphApplet a : applets) {
                  if (a.getName().equals(name)) {
                    exists = true;
                    break;
                  }
                }
                if (!exists) {
                  GraphApplet sa = new GraphApplet(name);
                  sa.resume();
                  sa.createDataSet("v", 0, 0, 255);
                  sa.createDataSet("a", 255, 0, 0);
                  cOutput.setChecked(true);
                  applets.add(sa);
                }
                //break;
              }
  
              //case 4:
              else if (item.equals(possibleValues[3]) || item.equals(possibleValues[4])) {
                boolean newVisiblityPath = !point.visibilityPath;
                point.drawPath(newVisiblityPath);
                if (!newVisiblityPath) erasePath();
                //break;
              }
                
              //case 5:
              else if (item.equals(possibleValues[5])) {
                points.remove(point);
              }
              
              else if (item.equals(possibleValues[6])) {
                if (!inputWindowOpened) {
                  String[] values = new String[]{"r", "g", "b"};
                  String[] standardValues = new String[3];
                  int[] c = point.getPathColor();
                  for (int i=0; i<standardValues.length; i++)
                    standardValues[i] = Integer.toString(c[i]);
                  ib = new InputBox("Pathcolor " + point.getName(), values, standardValues);
                  ib.setInputType(InputTypes.INTEGER);
                  ib.setMaxLimits(new float[]{
                    255, 
                    255, 
                    255
                    });
                  //println("width/2:"+width/2);
                  //println("lastPointPos:"+getLastPoint().absSetPos.mult(scaleD));
                  ib.setMinLimits(new float[]{
                    0,
                    0,
                    0
                    });
                  ib.setOnEditingFinishedListener(new InputBoxListener() {
                    @Override
                      public void finishedEditing(String... data) {
                      int necessaryDataLength = 3;
                      inputWindowOpened = false;
                      if (data.length == necessaryDataLength) {
                        int r = data[0] == "" ? 0 : Integer.parseInt(data[0]);
                        int g = data[1] == "" ? 0 : Integer.parseInt(data[1]);
                        int b = data[2] == "" ? 0 : Integer.parseInt(data[2]);
                        println("\nr:"+r);
                        println("g:"+g);
                        println("b:"+b);
                        point.setPathColor(new int[]{r, g, b});
                        point.drawPath();
                      }
                    }
                    @Override
                      public void onExit() {
                      inputWindowOpened = false;
                    }
                  }
                  );
                  inputWindowOpened = true;
                }
              }
              //}
            }
          }
          );
        }
      }
    }
  }

  for (Checkbox c : checkboxes) {
    if (c.mousePressedEvent()) {
      println("pressed " + c.text);
    }
  }

  for (TextBox tb : textboxes) {
    tb.mousePressedEvent();
  }

  for (GraphApplet sa : applets) {
    if (sa.waitingForExit()) {
      cOutput.setChecked(false);
      sa.exited();
    }
    if (cOutput.isChecked() && !sa.isVisible() && !sa.exited) {
      sa.resume();
    } else if (!cOutput.isChecked() && sa.isVisible()) {
      sa.pause();
    }
  }
  
  if (centerButtonPressed && rightButtonPressed && !rotation) {
    mouseReference = new PVector(mouseX, mouseY, 0);
    rotation = true;
    setZoom = zoom;
    zooming = false;
  } else if (centerButtonPressed && !zooming) {
    mouseReference = new PVector(mouseX, mouseY, 0);
    zooming = true; 
  }
}

public void mouseReleased() {
  println("mouse released:" + mouseButton);
  if (rotation) {
    lastSetAngle = new float[]{angle[0], angle[1], angle[2]};
    println("last angle x:" + lastSetAngle[1]);
    println("last angle y:" + lastSetAngle[0]);
    rotation = false;
    if (mouseButton != CENTER) {
      mouseReference = new PVector(mouseX, mouseY, 0);
      zooming = true;
    }
  } else if (zooming) {
    setZoom = zoom;
    zooming = false;
  } else if (translation) {
    lastSetPos = new PVector(pos.x, pos.y, 0);
    translation = false;
  }
  if (mouseButton == CENTER) {
    centerButtonPressed = false;
  } else if (mouseButton == RIGHT) {
    rightButtonPressed = false;
  }
  toast.onMouseReleased();
  for (TextBox tb : textboxes) {
    tb.mouseReleasedEvent();
  }
}

public void mouseDragged() {
  for (TextBox tb : textboxes) {
    tb.mouseDraggedEvent();
  }
}

public void keyPressed()
{
  println("key pressed");
  lastPressedKey = key;
  lastPressedKeyCode = keyCode;
  boolean tbClicked = false;
  for (TextBox tb : textboxes) {
    tb.handleKeyPressedEvent(keyCode, key);
    if (tb.isClicked())
      tbClicked = true;
  }
  if (!tbClicked)
    handleKeyPressedEvent(keyCode, key);
}

public void handleKeyPressedEvent(int pressedKeyCode, char pressedKey) {
  lastKeyEvent = millis();
  if (!keyCodes.contains(pressedKeyCode)) {
    keys.add(pressedKey);
    keyCodes.add(pressedKeyCode);
    println("keyCode:" + pressedKeyCode);
    println("key:'" + pressedKey + "'");
  }
  switch (pressedKeyCode) {
   case 139:
   case 93:
    if (isKeyPressed(16) && isKeyPressed(17)) {
      this.scale += 0.2f;
      updateDrawScale();
    } else if (isKeyPressed(17)) {
      this.scaleD++;
      updateDrawScaleD();
    } else {
      if ((speed >= 0 && speed < 1) || (speed < -0.5f && speed >= -1))
        speed = speed + 0.5f;
      else
        speed++;
      updateDrawSpeed();
    } 
    break;

  case 140:
  case 47:
    if (isKeyPressed(16) && isKeyPressed(17)) {
      if (this.scale>0) {
        if (this.scale <= 0.2f)
          this.scale -= 0.1f;
        else
          this.scale -= 0.2f;
        updateDrawScale();
      }
    } else if (isKeyPressed(17)) {
      if (this.scaleD>0) {
        this.scaleD--;
        updateDrawScaleD();
      }
    } else {
      if ((speed > 0.5f && speed <= 1) || (speed <= -0.5f && speed > -1))
        speed = speed - 0.5f;
      else
        speed--;
      updateDrawSpeed();
    }
    break; 
  }
  switch (pressedKey)
  {
  case '1':
    speed=1;
    updateDrawSpeed();
    break;

  case '0':
    speed=1;
    updateDrawSpeed();
    break;

  case 'p':
    cPath.setChecked(!cPath.isChecked());
    break;

  case 'o':
    cOutput.setChecked(!cOutput.isChecked());
    break;

  case 'v':
    cVelocity.setChecked(!cVelocity.isChecked());
    break;

  case 'a':
    cAcceleration.setChecked(!cAcceleration.isChecked());
    break;

  case ' ':
    stop();
    pressed = true;
    break;
  }
}

public void keyReleased() {
  boolean tbClicked = false;
  for (TextBox tb : textboxes) {
    tb.handleKeyReleasedEvent(keyCode, key);
    if (tb.isClicked())
      tbClicked = true;
  }
  if (!tbClicked)
    handleKeyReleasedEvent(keyCode, key);
}

public void handleKeyReleasedEvent(int pressedKeyCode, char pressedKey) {
  for (int i=0; i<keyCodes.size(); i++) {
    int maxId = keyCodes.size()-1;
    if (pressedKeyCode == keyCodes.get(i)) {
      println("Removed:" + keyCodes.get(i) + "\t'" + keys.get(i) + "'");
      keyCodes.remove(i);
      keys.remove(i);
      if (i == maxId) {
        if (keyCodes.size() > 0) {
          lastPressedKeyCode = keyCodes.get(keyCodes.size()-1);
          lastPressedKey = keys.get(keyCodes.size()-1);
        } else {
          lastPressedKeyCode = -1;
          lastPressedKey = ' ';
        }
      }
    }
  }
}

public boolean isKeyPressed(int pressedKeyCode) {
  for (int i=0; i<keyCodes.size(); i++) {
    if (keyCodes.get(i) == pressedKeyCode) {
      return true;
    }
  }
  return false;
}

public void updateDrawSpeed() {
  for (Point p : points) {
    p.setDrawSpeed(this.speed);
  }
}

public void updateDrawScale() {
  for (Point p : points) {
    p.setScale(this.scale);
  }
}

public void updateDrawScaleD() {
  //this.canvas.beginDraw();
  //this.canvas.background(0);
  //this.canvas.endDraw();
  for (Point p : points) {
    p.setScaleD(this.scaleD);
  }
}

public void stop()
{
  stop(!stopped);
}

public void stop(boolean state)
{
  stopped = state;
  lastTime = millis();
  startTime = millis();
  for (Point p : points) {
    p.resetTime();
  }
}

public void erasePath() {
  //canvas.beginDraw();
  //canvas.background(0);
  //canvas.endDraw();
}

public Point getPoint(int idx) {
  if (idx < 0 || idx > points.size()) return null;
  return points.get(idx);
}

public Point getLastPoint() {
  if (points.size() == 0) return null;
  return points.get(points.size()-1);
}

public Point getPreviousPoint(Point p) {
  if (points.size() == 0) return null;
  for (int i=0; i<points.size(); i++) {
    if (points.get(i) == p) return getPoint(i-1);
  }
  return null;
}

public Point getNextPoint(Point p) {
  if (points.size() == 0) return null;
  for (int i=0; i<points.size(); i++) {
    if (points.get(i) == p) return getPoint(i+1);
  }
  return null;
}

public Point addNewPoint(Point parent, float a, float[] angle, PVector w, float alpha) {
  points.add(new Point(idCount, "" + PApplet.parseChar(this.nameCounter++), parent, a, angle, w, alpha));
  Point point = points.get(points.size()-1);
  point.setScale(this.scale);
  point.setScaleD(this.scaleD);
  point.setDrawSpeed(this.speed);
  objects.put(idCount++, colorCount);
  //println("r:" + colorCount[0] + " g:" + colorCount[1] + " b:" + colorCount[2]);
  //println("c:" + color(colorCount[0], colorCount[1], colorCount[2]));
  colorCount = new Integer[]{colorCount[0],colorCount[1],colorCount[2]};
  colorCount[0] += 1;
  if (colorCount[0] > 255) {
     colorCount[0] = 0;
     colorCount[1] += 1;
  } 
  if (colorCount[1] > 255) {
     colorCount[1] = 0;
     colorCount[2] += 1;
  }
  //point.setPathColor(new int[]{255, 0, 0});
  if (nameCounter > 91)
    nameCounter = 65;
  println("Added Point " + point.getName());
  return point;
}

public Point addNewPoint(Point parent, PVector pos, PVector w, float alpha) {
  points.add(new Point(idCount, "" + PApplet.parseChar(this.nameCounter++), parent, pos, w, alpha));
  Point point = points.get(points.size()-1);
  point.setScale(this.scale);
  point.setScaleD(this.scaleD);
  point.setDrawSpeed(this.speed);
  objects.put(idCount++, colorCount);
  if (parent != null)
    parent.addChild(point);
  //println("r:" + colorCount[0] + " g:" + colorCount[1] + " b:" + colorCount[2]);
  //println("c:" + color(colorCount[0], colorCount[1], colorCount[2]));
  colorCount = new Integer[]{colorCount[0],colorCount[1],colorCount[2]};
  colorCount[0] += 1;
  if (colorCount[0] > 255) {
     colorCount[0] = 0;
     colorCount[1] += 1;
  } 
  if (colorCount[1] > 255) {
     colorCount[1] = 0;
     colorCount[2] += 1;
  }
  //points.get(points.size()-1).setPathColor(new int[]{255, 0, 0});
  if (nameCounter >= 91)
    nameCounter = 65;
  println("Added Point " + point.getName());
  return point;
}

public Checkbox addCheckBox(String title) {
  return addCheckBox(title, false, (ArrayList<Checkbox>)null);
}

public Checkbox addCheckBox(String title, boolean checked) {
  return addCheckBox(title, checked, (ArrayList<Checkbox>)null);
}

public Checkbox addCheckBox(String title, Checkbox member) {
  ArrayList<Checkbox> group = new ArrayList<Checkbox>();
  group.add(member);
  return addCheckBox(title, false, group);
}

public Checkbox addCheckBox(String title, ArrayList<Checkbox> group) {
  return addCheckBox(title, false, group);
}

public Checkbox addCheckBox(String title, boolean checked, Checkbox member) {
  ArrayList<Checkbox> group = new ArrayList<Checkbox>();
  group.add(member);
  return addCheckBox(title, checked, group);
}

public Checkbox addCheckBox(String title, boolean checked, ArrayList<Checkbox> group) {
  Checkbox c = new Checkbox(this, 20, this.checkBoxY, 20, title, group);
  c.setChecked(checked);
  checkboxes.add(c);
  this.checkBoxY += this.checkBoxOffset;
  return c;
}
public class Button extends TextView {
  OnClickListener onClickListener = null;
  OnAnimationFinishedListener onAnimationFinishedListener = null;
  boolean clickAnimationVisible = false;
  PVector clickAnimationPos = null;
  long clickAnimationStartTime = 0;
  long clickAnimationLastTime = 0;
  int clickAnimationSize = 0;

  Button(PApplet context, float x, float y) {
    super(context, x, y);
    init();
  }

  Button(PApplet context, float x, float y, int width, int height) {
    super(context, x, y, width, height);
    init();
  }

  Button(PApplet context, PVector pos) {
    super(context, pos);
    init();
  }

  Button(PApplet context, PVector pos, int width, int height) {
    super(context, pos, width, height);
    init();
  }

  Button(PApplet context, float x, float y, String text) {
    super(context, x, y, text);
    init();
  }

  Button(PApplet context, float x, float y, int width, int height, String text) {
    super(context, x, y, width, height, text);
    init();
  }

  Button(PApplet context, PVector pos, String text) {
    super(context, pos, text);
    init();
  }

  Button(PApplet context, PVector pos, int width, int height, String text) {
    super(context, pos, width, height, text);
    init();
  }

  public void init() {
    this.textAlignment = TextView.TEXTALIGNMENT_CENTER;
  }

  ArrayList<PVector> points = new ArrayList<PVector>();

  public @Override
    void draw() {
    super.draw();
    if (this.clickAnimationVisible) {
      this.context.fill(255-this.backgroundColor, 255-this.backgroundColor, 255-this.backgroundColor, 100);
      this.context.noStroke();
      boolean finished = true;
      if (visible) {
        this.context.beginShape();
        float x = 0, y = 0;
        //float r = 400;
        //for (x=this.pos.x; x<=this.pos.x+r; x++) {
        //  y = this.pos.y+r-sqrt(r*r-(r-x)*(r-x));
        //  this.context.stroke(255);
        //  this.context.vertex(x, y);
        //}
        //println("pos:"+this.pos);
        //println("y(r):"+(this.pos.y+r-sqrt(r*r-(r-r)*(r-r))));
        if (points.size() == 0) {
          this.clickAnimationSize = 1;
          for (float angle = 0; angle<TWO_PI; angle += PI/32) {
            x = this.clickAnimationPos.x + this.clickAnimationSize * cos(angle);
            y = this.clickAnimationPos.y + this.clickAnimationSize * sin(angle);
            points.add(new PVector(x, y, 0));
          }
          println("pos:"+this.pos);
        }
        int counter = 0;
        for (float angle = 0; angle<TWO_PI; angle += PI/32) {
          PVector point = points.get(counter);
          PVector shapeDimPos = checkShapeDim(point.x, point.y);
          if (shapeDimPos == null) {
            x = this.clickAnimationPos.x + this.clickAnimationSize * cos(angle);
            y = this.clickAnimationPos.y + this.clickAnimationSize * sin(angle);
            int size = this.clickAnimationSize;
            while (checkShapeDim(x, y) != null && size > 0) {
              size--;
              x = this.clickAnimationPos.x + size * cos(angle);
              y = this.clickAnimationPos.y + size * sin(angle);
            }
            size++;
            x = this.clickAnimationPos.x + size * cos(angle);
            y = this.clickAnimationPos.y + size * sin(angle);
            points.set(counter, new PVector(x, y, 0));
            finished = false;
          } else {
            x = shapeDimPos.x;
            y = shapeDimPos.y;
            //points.set(counter, new PVector(x, y, 0));
          }
          //println("x:"+x);
          //println("y:"+y);
          counter++;
          this.context.stroke(255);
          //this.context.line(x, y, clickAnimationPos.x, clickAnimationPos.y);
          //this.context.ellipse(this.pos.x, this.pos.y, 10, 10);
          this.context.vertex(x, y);
          //println("x:"+x);
          //println("y:"+y);
          //println("\n");
          //if (x > xMin-offset && x < xMax+offset && y > yMin-offset && y < yMax+offset) finished = false;
          //if (x<=this.pos.x+this.cornerRadius) {
          //  float xOff = this.cornerRadius-(x-this.pos.x);
          //  float yOff = this.cornerRadius-(y-this.pos.y);
          //  xMin = this.pos.x+sqrt(this.cornerRadius*this.cornerRadius+yOff*yOff);
          //  xMax = this.pos.x+this.viewWidth-sqrt(this.cornerRadius*this.cornerRadius+yOff*yOff);
          //  yMin = this.pos.y+sqrt(this.cornerRadius*this.cornerRadius+xOff*xOff);
          //  yMax = this.pos.y+this.viewHeight-sqrt(this.cornerRadius*this.cornerRadius+xOff*xOff);
          //  if (!finished) {
          //    println("xOff:"+xOff);
          //    println("yMin:"+yMin);
          //    println("yMax:"+yMax);
          //    println("r:"+this.cornerRadius);
          //    println("\n");
          //  }
          //} else {
          //  yMin = this.pos.y;
          //  yMax = this.pos.y+this.viewHeight;
          //}
          //x = constrain(x, xMin, xMax);
          //y = constrain(y, yMin, yMax);
          //this.context.ellipse(this.pos.x, this.pos.y, 10, 10);
        }
        this.context.endShape();
      }
      //this.context.ellipse(this.clickAnimationPos.x, this.clickAnimationPos.y, this.clickAnimationSize, this.clickAnimationSize);
      if (millis() - this.clickAnimationLastTime > 1) {
        this.clickAnimationSize += 9;
        this.clickAnimationLastTime = millis();
      }

      if (finished) {
        this.clickAnimationVisible = false;
        this.clickAnimationSize = 0;
        points = new ArrayList<PVector>();
        if (this.onAnimationFinishedListener != null)
          this.onAnimationFinishedListener.onAnimationFinished();
      }
    }
  }

  public PVector checkShapeDim(float x, float y) {
    float xOff = 0;
    float f = 0;
    float xMin = 0;
    float xMax = 0;
    float yMin = 0;
    float yMax = 0;
    if (x <= this.pos.x+this.cornerRadius) {
      xMin = this.pos.x;
      if (x < xMin) return new PVector(xMin, y);
      xOff = this.cornerRadius-(x-this.pos.x);
      f = this.cornerRadius-sqrt(this.cornerRadius*this.cornerRadius-xOff*xOff);
      yMin = this.pos.y+f;
      yMax = this.pos.y+this.viewHeight-f;
      if (y < yMin) return new PVector(x, yMin);
      if (y > yMax) return new PVector(x, yMax);
      return null;
    }
    if (x >= this.pos.x+this.viewWidth-this.cornerRadius) {
      xMax = this.pos.x+this.viewWidth;
      if (x > xMax) return new PVector(xMax, y);
      xOff = x-(xMax-this.cornerRadius);
      //println("xOff:"+xOff);
      f = this.cornerRadius-sqrt(this.cornerRadius*this.cornerRadius-xOff*xOff);
      //println("f:"+f);
      yMin = this.pos.y+f;
      yMax = this.pos.y+this.viewHeight-f;
      //println("yMin:"+yMin);
      //println("yMax:"+yMax);
      if (y < yMin) return new PVector(x, yMin);
      if (y > yMax) return new PVector(x, yMax);
      return null;
    }
    xMin = this.pos.x;
    xMax = this.pos.x+this.viewWidth;
    yMin = this.pos.y;
    yMax = this.pos.y+this.viewHeight;
    if (x < xMin) return new PVector(xMin, y);
    if (x > xMax) return new PVector(xMax, y);
    if (y < yMin) return new PVector(x, yMin);
    if (y > yMax) return new PVector(x, yMax);
    return null;
  }

  public PVector calcShapeDim(float startX, float startY, float angle) {
    float x = startX - this.pos.x;
    float y = startY - abs(x * tan(angle));
    float k = tan(angle);
    float r = this.cornerRadius;
    float distX = (r-sqrt(2*k*r*r-y+2*y*r-2*y*k*r)-y*k+k*r)/(k*k+1);
    float distY = r-sqrt(r*r-(r-distX)*(r-distX));
    return new PVector(this.pos.x+distX, this.pos.y+distY, 0);
  }

  public void setOnClickListener(OnClickListener listener) {
    this.onClickListener = listener;
  }

  public void setOnAnimationFinishedListener(OnAnimationFinishedListener listener) {
    this.onAnimationFinishedListener = listener;
  }

  public void mousePressedEvent() {
    float mX = this.context.mouseX;
    float mY = this.context.mouseY;
    if (mX >= this.pos.x && mX <= this.pos.x+this.viewWidth && mY >= this.pos.y && mY <= this.pos.y+this.viewHeight) {
      this.clickAnimationVisible = true;
      this.clickAnimationPos = new PVector(mX, mY, 0);
      this.clickAnimationStartTime = millis();
      this.clickAnimationLastTime = millis();
      if (this.onClickListener != null)
        this.onClickListener.onClick(this.id);
    }
  }
}
public class Checkbox extends View {
  ArrayList<Checkbox> group = new ArrayList<Checkbox>();
  ;
  private int posX = 0;
  private int posY = 0;
  private int size = 0;
  private boolean checked = false;
  String text = "";

  Checkbox(PApplet context, int posX, int posY, int size) {
    super(context,posX, posY, size, size);
    init(posX, posY, size, "", null);
  }

  Checkbox(PApplet context, int posX, int posY, int size, String text) {
    super(context,posX, posY, size, size);
    init(posX, posY, size, text, null);
  }

  Checkbox(PApplet context, int posX, int posY, int size, ArrayList<Checkbox> group) {
    super(context,posX, posY, size, size);
    init(posX, posY, size, "", group);
  }

  Checkbox(PApplet context, int posX, int posY, int size, String text, ArrayList<Checkbox> group) {
    super(context,posX, posY, size, size);
    init(posX, posY, size, text, group);
  }

  private void init(int posX, int posY, int size, String text, ArrayList<Checkbox> group) {
    this.posX = posX;
    this.posY = posY;
    this.size = size;
    this.text = text;
    if (group != null)
      addGroupMembers(group);
  }

  public void addGroupMember(Checkbox newMember) {
    if (!containsMember(newMember)) {
      this.group.add(newMember);
      newMember.addGroupMember(this);
    }
  }

  public void addGroupMembers(ArrayList<Checkbox> newGroupMembers) {
    for (Checkbox member : newGroupMembers) {
      if (!containsMember(member)) {
        this.group.add(member);
        member.addGroupMember(this);
      }
    }
  }

  public void dropMember(Checkbox member) {
    this.group.remove(member);
  }

  public boolean containsMember(Checkbox member) {
    for (Checkbox c : this.group) {
      if (c.equals(member)) return true;
    }
    return false;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }

  public boolean isChecked() {
    return this.checked;
  }
  
  @Override
  public boolean isClicked() {return false;}
  
  @Override
  public boolean isHovered() {return false;}

  public boolean mousePressedEvent() {
    if (mouseX >= this.posX && mouseX <= this.posX+size && mouseY >= this.posY && mouseY <= this.posY+size) {
      this.checked = !this.checked;
      if (this.group != null) {
        for (Checkbox c : group) {
          c.setChecked(false);
        }
      }
      return true;
    }
    return false;
  }

  public void draw() {
    noFill();
    stroke(255);
    textSize(size);
    rect(this.posX, this.posY, size, size);
    if (this.checked) {
      line(this.posX, this.posY, this.posX+this.size, this.posY+this.size);
      line(this.posX, this.posY+this.size, this.posX+this.size, this.posY);
    }
    if (!text.equals("")) {
      fill(255);
      text(this.text, this.posX+this.size+10, this.posY+this.size);
    }
  }
}



public class GraphApplet extends PApplet {

  ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
  float graphAddBuffer;
  boolean bufferLoaded = false;
  float dx = 2;
  float yMax = 0;
  int maxSize = 0;
  float margin = 50;
  float xOffset = margin/2;
  boolean visible = true;
  boolean waitingForExit = false;
  boolean exited = false;
  String title = "";
  DecimalFormat df = new DecimalFormat("#");

  public GraphApplet(String title) {
    String[] args = {"Velocity"};
    PApplet.runSketch(args, this);
    this.title = title;
    surface.setTitle(title);
  }

  public void settings() {
    size(1500, 200);
  }

  public void setup() {
    df.setRoundingMode(RoundingMode.CEILING);
    //surface.setTitle("Graph");
  }

  @Override
    public void exit() {
    this.getSurface().setVisible(false);
    this.visible = false;
    this.waitingForExit = true;
    this.noLoop();
  }

  public void exited() {
    this.waitingForExit = false;
    this.exited = true;
  }

  public void pause() {
    this.getSurface().setVisible(false);
    this.visible = false;
    for (int i=0; i<this.dataSets.size(); i++) {
      this.dataSets.get(i).reset();
      //this.dataSets.set(i, new DataSet(this.dataSets.get(i).getName())); //new ArrayList<Float>(); 
      //println("data size:"+d.data.size());
    }
    this.maxSize = 0;
    this.noLoop();
  }

  public void resume() {
    this.getSurface().setVisible(true);
    this.visible = true;
    this.exited = false;
    this.loop();
  }

  public boolean isVisible() {
    return this.visible;
  }

  public boolean waitingForExit() {
    return this.waitingForExit;
  }

  public String getName() {
    return this.title;
  }

  public void keyPressed() {
    handleKeyPressedEvent(keyCode, key);
    println("key pressed");
  }

  public void keyReleased() {
    handleKeyReleasedEvent(keyCode, key);
  }

  public boolean createDataSet(String dataSetName) {
    return createDataSet(dataSetName, 0, 0, 0);
  }

  public boolean createDataSet(String dataSetName, int r, int g, int b) {
    for (DataSet d : dataSets) {
      if (d.name.equals(dataSetName)) {
        return false;
      }
    }
    this.dataSets.add(new DataSet(dataSetName));
    this.dataSets.get(this.dataSets.size()-1).setColor(r, g, b);
    return true;
  }

  public boolean addPoint(String dataSetName, float point) {
    for (DataSet d : this.dataSets) {
      if (d.name.equals(dataSetName)) {
        d.addPointToBuffer(point);
        if (d.data.size()+1 > this.maxSize) {
          this.maxSize = d.data.size()+1;
        }
        return true;
      }
    }
    return false;
  }

  //public float getLastPoint(String name) {
  //  if (this.graphPoints.size() == 0) return 0;
  //  return this.graphPoints.get(this.graphPoints.size()-1);
  //}

  public void addGraphPoint(float point) {
    this.graphAddBuffer = point;
    this.bufferLoaded = true;
  }

  public void setDelta(float dx) {
    this.dx = dx;
  }

  public void draw() {
    background(255);
    noFill();
    float max = 0;
    int startIdx = 0;
    for (DataSet ds : this.dataSets) {
      if (ds.isBufferLoaded()) {
        ds.addPointFromBuffer();
      }
      println("data size:"+ds.data.size());
      startIdx = ds.getDataSize()-floor((width-this.margin-50*this.dataSets.size())/(this.dx));
      if (startIdx > 0) {
        for (int i=0; i<startIdx; i++)
          ds.data.remove(0);
      }
      if (ds.getDataSize() > this.maxSize) {
        this.maxSize = ds.getDataSize();
      }
    }

    float x = xOffset-30;
    strokeWeight(1);
    line(0, height/2, width, height/2);
    
    for (DataSet ds : this.dataSets) {
      x += 50;
      line(x, 0, x, height);
      float dy = ds.getMax() / 10;
      float y = 0;
      for (int i=0; i<11; i++) {
         float mappedY = map(y, 0, ds.getMax(), this.margin/2, height-this.margin/2);
         line(x-5, mappedY, x+5, mappedY);
         fill(0);
         String s = ""+df.format((ds.getMax()-2*y));
         text(s, x-textWidth(s)-4, mappedY+5);
         y += dy;
      }
    }
    
    for (int i=1; i<this.maxSize; i++) {
      for (DataSet d : this.dataSets) {
        float p1 = 0;
        float p2 = 0;
        if (d.getDataSize() > i) {
          p1 = d.getPoint(i-1);
          p2 = d.getPoint(i);
        }
        p1 = map(p1, -d.yMax, d.yMax, height-this.margin/2, this.margin/2);
        p2 = map(p2, -d.yMax, d.yMax, height-this.margin/2, this.margin/2);
        stroke(d.r, d.g, d.b);
        strokeWeight(2);
        line(x, p1, x+this.dx, p2);
      }
      x += this.dx;
    }
    
    
  }

  private class DataSet {
    private ArrayList<Float> data = new ArrayList<Float>();
    private float pointBuffer;
    private boolean bufferLoaded = false;
    private float dx = 2;
    private float yMax = 10;
    private String name = "";
    private int r = 0;
    private int g = 0;
    private int b = 0;

    DataSet(String name) {
      this.name = name;
    }
    
    public void reset() {
    this.data = new ArrayList<Float>();
    this.yMax = 10;
  }

    public String getName() {
      return this.name;
    }

    public int getDataSize() {
      return this.data.size();
    }

    public float getPoint(int idx) {
      return this.data.get(idx);
    }

    public void setColor(int r, int g, int b) {
      this.r = r;
      this.g = g;
      this.b = b;
    }

    public void addPointToBuffer(float point) {
      this.pointBuffer = point;
      this.bufferLoaded = true;
    }

    public void addPointFromBuffer() {
      this.data.add(this.pointBuffer);
      if (this.pointBuffer > this.yMax)
        this.yMax = this.pointBuffer;
      this.bufferLoaded = false;
    }

    public void removePoint(int idx) {
    }
    
    public float getMax() {return this.yMax;}

    public boolean isBufferLoaded() {
      return this.bufferLoaded;
    }
  }
}
public class InputBox extends PApplet implements TextBoxListener, OnClickListener, OnAnimationFinishedListener {
  ArrayList<TextView> textviews = new ArrayList<TextView>();
  ArrayList<TextBox> textboxes = new ArrayList<TextBox>();
  Button bSubmit;
  String[] values;
  String[] standardValues;
  String[] hintValues;
  float[] limitsMax;
  float[] limitsMin;
  boolean visible = true;
  boolean exit = false;
  String title = "";
  int windowWidth;
  int windowHeight;
  int xStart = 0;
  int yStart = 0;
  int xMax = 0;
  int yMax = 0;
  int padding = 10;
  int tvWidth = 100;
  int tvHeight = 50;
  int tbWidth = 150;
  int tbHeight = 50;
  int tvWidthMax = 0;
  int tbWidthMax = 0;
  int counter = 1;
  int inputType = InputTypes.FLOAT;
  InputBoxListener mListener;

  InputBox(String title, String[] values) {
    init(title, values, null, false);
  }

  InputBox(String title, String[] values, String[] standardValues) {
    init(title, values, standardValues, true);
  }

  InputBox(String title, String[] values, String[] descValues, boolean stdValue) {
    init(title, values, descValues, stdValue);
  }

  private void init(String title, String[] values, String[] descValues, boolean stdValue) {
    this.values = values;
    if (descValues != null) {
      if (descValues.length == values.length) {
        if (stdValue)
          this.standardValues = descValues;
        else
          this.hintValues = descValues;
      }
    }
    String[] args = {"InputBox"};
    this.title = title;
    PApplet.runSketch(args, this);
    surface.setTitle(this.title);
  }

  public void settings() {
    size(this.tvWidth+this.tbWidth+20, 200);
    this.windowWidth = this.width;
    this.windowHeight = this.height;
  }

  public void setup() {
    surface.setTitle(this.title);
    this.xStart = this.padding;
    this.yStart = this.padding;
    //println("width:"+this.width);
    //println("height:"+this.height);
    for (int i=0; i<values.length; i++) {
      addTextView(values[i]);
      addTextBox();
    }
    for (TextView tv : textviews) tv.setWidth(tvWidthMax);
    for (TextBox tb : textboxes) tb.setWidth(tbWidthMax);
    bSubmit = new Button(this, xStart, yStart, 100, 50, "Submit");
    bSubmit.setBackground(255);
    bSubmit.setTextColor(0);
    bSubmit.setTextSize(25);
    bSubmit.setPadding(10);
    bSubmit.setHorizontalAlignment(TextView.ALIGNMENT_CENTER);
    bSubmit.setVerticalAlignment(TextView.ALIGNMENT_BOTTOM);
    bSubmit.setOnClickListener(this);
    bSubmit.setOnAnimationFinishedListener(this);
    this.yMax = this.yStart + this.tbHeight + this.padding;
    this.xMax += this.tvWidth + 2 * this.padding;
    if (this.xMax > this.windowWidth) {
      this.windowWidth = this.xMax;
    }
    if (this.yMax > this.windowHeight) {
      this.windowHeight = this.yMax;
    }
    surface.setSize(this.windowWidth, this.windowHeight);
  }

  public void setMaxLimits(float limit) {
    float[] limits = new float[values.length];
    for (int i=0; i<values.length; i++) {
      limits[i] = limit;
    }
    setMaxLimits(limits);
  }

  public void setMaxLimits(float[] limits) {
    if (limits.length != values.length) return;
    this.limitsMax = limits;
  }

  public void setMinLimits(float limit) {
    float[] limits = new float[values.length];
    for (int i=0; i<values.length; i++) {
      limits[i] = limit;
    }
    setMinLimits(limits);
  }

  public void setMinLimits(float[] limits) {
    this.limitsMin = limits;
  }  
  
  public void setInputType(int type) {this.inputType = type;}

  public void addTextView(String text) {
    TextView tv = new TextView(this, this.xStart, this.yStart, this.tvWidth, this.tvHeight);
    tv.setMargin(5);
    tv.setText(text);
    tv.setTextSize(30);
    tv.setTextAlignment(TextView.TEXTALIGNMENT_CENTER);
    if (tv.viewWidth > tvWidthMax)
      tvWidthMax = tv.viewWidth;
    this.textviews.add(tv);
  }

  public void addTextBox() {
    final TextBox tb = new TextBox(this, this.xStart+this.tvWidth, this.yStart, this.tbWidth, this.tbHeight);
    tb.setMargin(5);
    tb.setTextSize(30);
    tb.setId(this.counter);
    if (this.standardValues != null) {
      tb.setStandardText(this.standardValues[this.counter-1]);
    } else if (this.hintValues != null) {
      tb.setHint(this.hintValues[this.counter-1]);
    }

    //else {
    //  tb.setStandardText("0.0");
    //}
    tb.setInputType(this.inputType);
    tb.setTextAlignment(TextView.TEXTALIGNMENT_RIGHT);
    //tb.setHint("Enter value");
    tb.setTextBoxListener(new TextBoxListener() {
      @Override
        public void textEdited(int id, String text) {
          println("Text Edited");
        if (tb.inputType == InputTypes.FLOAT) {
          float value;
          try {
            value = Float.parseFloat(text);
            if (limitsMax != null && limitsMax.length > id-1) {
              float maxValue = limitsMax[id-1];
              //println("maxValue:"+maxValue);
              if (value > maxValue) {
                tb.setText(Float.toString(maxValue));
                return;
              }
              float minValue = limitsMin[id-1];
              //println("minValue:"+minValue);
              if (value < minValue) {
                tb.setText(Float.toString(minValue));
                return;
              }
            }
          } 
          catch (NumberFormatException e) {
            println(e);
            tb.setText(Float.toString(limitsMax[id-1]));
          }
        } else if (tb.inputType == InputTypes.INTEGER) {
          int value;
          try {
            value = Integer.parseInt(text);
            if (limitsMax != null && limitsMax.length > id-1) {
              int maxValue = (int)limitsMax[id-1];
              //println("maxValue:"+maxValue);
              if (value > maxValue) {
                tb.setText(Integer.toString(maxValue));
                return;
              }
              int minValue = (int)limitsMin[id-1];
              //println("minValue:"+minValue);
              if (value < minValue) {
                tb.setText(Integer.toString(minValue));
                return;
              }
            }
          } 
          catch (NumberFormatException e) {
            println(e);
            tb.setText(Integer.toString((int)limitsMax[id-1]));
          }
        }
      }

      @Override
        public void previousTextBox(int id, int cursorPosX) {
        //println("\nnextTextBox");
        //println("id:"+id);
        //for (TextBox textBox : textboxes) {
        //  println("tb id:" + textBox.getId());
        //}
        for (int i=0; i<textboxes.size(); i++) {
          if (textboxes.get(i).getId() == id) {
            int next = i == 0 ? textboxes.size()-1 : i-1;
            textboxes.get(next).setClicked(true, cursorPosX);
          }
        }
      }

      @Override
        public void nextTextBox(int id, int cursorPosX) {
        //println("\nnextTextBox");
        //println("id:"+id);
        //for (TextBox textBox : textboxes) {
        //  println("tb id:" + textBox.getId());
        //}

        for (int i=0; i<textboxes.size(); i++) {
          if (textboxes.get(i).getId() == id) {
            int next = (i+1) % textboxes.size();
            textboxes.get(next).setClicked(true, cursorPosX);
            println("i:"+i);
            println("tb id:"+id);
            println("size:"+textboxes.size());
            println("next:"+next);
          }
        }
      }
    }
    );
    tb.setKeyListener(new KeyListener() {
      @Override
        public void onKeyPressed(int pressedKeyCode, char pressedKey) {
        if (pressedKeyCode == 10) {
          //println("\nnextTextBox");
          tb.clicked = false;
          int id = tb.getId();
          //println("id:"+id);
          //for (TextBox textBox : textboxes) {
          //  println("tb id:" + textBox.getId());
          //}

          for (int i=0; i<textboxes.size(); i++) {
            if (textboxes.get(i).getId() == id) {
              int next = i+1;
              if (next > textboxes.size()-1) {
                finish();
                return;
              }
              textboxes.get(next).setClicked(true, tb.calcCharPos(tb.cursorPos));
              println("i:"+i);
              println("tb id:"+id);
              println("size:"+textboxes.size());
              println("next:"+next);
              break;
            }
          }
        }
      }

      @Override
        public void onKeyReleased(int pressedKeyCode, char pressedKey) {
      }
    }
    );
    if (tb.viewWidth > this.xMax) {
      this.xMax = tb.viewWidth;
    }
    if (tb.viewWidth > tbWidthMax)
      tbWidthMax = tb.viewWidth;
    this.textboxes.add(tb);
    this.yStart += this.tbHeight+10;
    this.counter++;
  }

  public void setOnEditingFinishedListener(InputBoxListener listener) {
    this.mListener = listener;
  }

  public void mousePressed() {
    bSubmit.mousePressedEvent();
    for (TextBox tb : textboxes) {
      tb.mousePressedEvent();
    }
  }

  public void mouseReleased() {
    for (TextBox tb : textboxes) {
      tb.mouseReleasedEvent();
    }
  }

  public void mouseDragged() {
    for (TextBox tb : textboxes) {
      tb.mouseDraggedEvent();
    }
  }

  public void keyPressed() {
    for (TextBox tb : textboxes) {
      tb.handleKeyPressedEvent(keyCode, key);
    }
  }

  public void keyReleased() {
    for (TextBox tb : textboxes) {
      tb.handleKeyReleasedEvent(keyCode, key);
    }
  }

  public void draw() {
    background(0);
    for (TextView tv : this.textviews) {
      tv.draw();
    }
    for (TextBox tb : this.textboxes) {
      tb.draw();
    }
    bSubmit.draw();

    //noFill();
    //stroke(255);
    //strokeWeight(1);
    //rect(0, 0, 100, 100);
  }

  @Override
    public void exit() {
    this.getSurface().setVisible(false);
    this.visible = false;
    this.exit = true;
    if (this.mListener != null) 
      this.mListener.onExit();
    this.noLoop();
  }

  public void exited() {
    this.exit = false;
  }

  public void pause() {
    this.getSurface().setVisible(false);
    this.visible = false;
    this.noLoop();
  }

  public void resume() {
    this.getSurface().setVisible(true);
    this.visible = true;
    this.exit = false;
    this.loop();
  }

  public boolean isVisible() {
    return this.visible;
  }

  public boolean waitingForExit() {
    return this.exit;
  }

  public String getName() {
    return this.title;
  }

  public void finish() {
    String[] data = new String[this.textboxes.size()];
    for (int i=0; i<this.textboxes.size(); i++) {
      data[i] = this.textboxes.get(i).getText();
    }
    if (this.mListener != null) this.mListener.finishedEditing(data);
    exit();
  }

  @Override
    public void textEdited(int id, String text) {
  }

  @Override
    public void previousTextBox(int id, int cursorPosX) {
  }

  @Override
    public void nextTextBox(int id, int cursorPosX) {
  }

  @Override
    public void onClick(int id) {
  }

  @Override
    public void onAnimationFinished() {
    finish();
  }
}
class InputTypes {
  final static int ALL = 0;
  final static int STRING = 1;
  final static int INTEGER = 2;
  final static int FLOAT = 3;
}
public interface TextBoxListener {
  public void textEdited(int id, String text);
  public void previousTextBox(int id, int cursorPosX);
  public void nextTextBox(int id, int cursorPosX);
}

public interface InputBoxListener {
  public void finishedEditing(String... data);
  public void onExit();
}

public interface KeyListener {
 public void onKeyPressed(int pressedKeyCode, char pressedKey); 
 public void onKeyReleased(int pressedKeyCode, char pressedKey);
}

//public interface ButtonListener implements OnClickListener, OnAnimationFinishedListener {
  
//}

public interface OnClickListener {
  public void onClick(int id);
}

public interface OnHoverListener {
  public void onHover(int id);
}

public interface OnItemClickListener {
  public void onItemClick(int itemIdx, String item);
}

public interface OnAnimationFinishedListener {
  public void onAnimationFinished();
}

public interface UIListener {
 public void drawUI(); 
}
public class MenuItem extends View {
  static final int standardWidth = 120;
  static final int standardHeight = 200;
  static final int standardOffset = 15;

  OnItemClickListener onItemClickListener;
  //int padding = 0;
  int startPosX;
  int startPosY;
  int tvWidth = 120;
  int tvHeight = 40;
  ArrayList<TextView> textviews;
  String[] values;
  String title = "";
  boolean visible = true;

  MenuItem(PApplet context, float x, float y, String title, String[] values) {
    super(context, x+standardOffset, y+standardOffset, standardWidth, standardHeight);
    init(title, values);
  }

  MenuItem(PApplet context, float x, float y, int w, int h, String title, String[] values) {
    super(context, x+standardOffset, y+standardOffset, w, h);
    init(title, values);
  }


  public boolean onMousePressed() {
    super.onMousePressed();
    for (TextView tv : this.textviews) tv.onMousePressed();
    if (!this.clicked)
      this.visible = false;
    return this.clicked;
  }

  private void init(String title, String[] values) {
    this.padding = 10;
    this.startPosX = (int)this.pos.x + this.padding;
    this.startPosY =  (int)this.pos.y + this.padding;
    textviews = new ArrayList<TextView>();
    this.values = values;
    for (int i=0; i<this.values.length; i++) {
      final TextView tv = new TextView(this.context, this.startPosX, this.startPosY, this.tvWidth, this.tvHeight);
      tv.setText(this.values[i]);
      tv.setTextAlignment(TextView.TEXTALIGNMENT_CENTER);
      tv.setTextSize(15);
      tv.setMargin(10);
      tv.setId(i+1);
      tv.setTextColor(0);
      tv.setBackgroundgAlpha(255);
      tv.setBackground(150);
      tv.setStrokeColor(0);
      tv.setStrokeWeight(0);
      tv.setOnClickListener(new OnClickListener() {
        @Override
          public void onClick(int id) {
          println("clicked on menu item " + id);
          if (context.mouseButton == LEFT) {
            if (visible) {
              visible = false;
              if (onItemClickListener != null) onItemClickListener.onItemClick(id, tv.getText());
            }
          }
        }
      }
      );
      //tv.setOnHoverAction(new Runnable() {
      //  @Override
      //  public void run() {

      //  }
      //}
      //);
      //tv.setOnHoverListener(new OnHoverListener() {
      //  @Override
      //    public void onHover(int id) {
      //    println("clicked on menu item " + id);
      //    if (tv.backgroundColor == 150) {
      //      tv.setBackground(255-tv.backgroundColor);
      //    }
      //  }
      //}
      //);
      this.startPosY += this.tvHeight+1;
      textviews.add(tv);
    }
    this.title = title;
    calcWidth();
    calcHeight();
    calcPos();
  }

  public void calcWidth() {
    int nWidth = 2 * this.padding;
    int maxTvWidth = 0;
    for (TextView tv : textviews) {
      //println("tv.viewWidth:"+tv.viewWidth);
      if (tv.viewWidth > maxTvWidth) 
        maxTvWidth = tv.viewWidth;
    }
    //println("maxTvWidth:"+maxTvWidth);
    for (TextView tv : textviews) {
      tv.setWidth(maxTvWidth);
      //println("tv.viewWidth:"+tv.viewWidth);
    }
    nWidth += maxTvWidth;
    this.viewWidth = nWidth;
  }

  public void calcHeight() {
    int nHeight = this.padding;
    for (TextView tv : textviews) {
      nHeight += tv.viewHeight+1;
      println("viewHeight:" + tv.viewHeight);
    }
    nHeight += this.padding;
    this.viewHeight = nHeight;
    println("this.viewHeight:" + this.viewHeight);
  }

  public void calcPos() {
    int xMax = this.context.width - this.viewWidth - 10;
    int yMax = this.context.height - this.viewHeight - 10;
    if (this.pos.x > xMax) {
      updatePos(xMax-this.pos.x, 0);
      this.pos.x = xMax;
    }
    if (this.pos.y > yMax) {
      updatePos(0, yMax-this.pos.y);
      this.pos.y = yMax;
    }
  }

  public void updatePos(float offsetX, float offsetY) {
    for (TextView tv : textviews) {
      println("\nprevious pos:"+tv.pos);
      tv.setPos(new PVector(tv.pos.x+offsetX, tv.pos.y+offsetY, 0));
      println("pos update:" + tv.pos);
    }
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.onItemClickListener = listener;
  }

  @Override
    public void draw() {
    //super.draw();
    if (this.visible) {
      this.context.fill(150, 150, 150, 255);
      this.context.stroke(0);
      this.context.strokeWeight(1);
      this.context.rect(this.pos.x, this.pos.y, this.viewWidth, this.viewHeight);
      //println("this.viewHeight:" + this.viewHeight);
      for (int i=0; i<textviews.size(); i++) {
        TextView tv = textviews.get(i);
        if (tv.hovered && tv.backgroundColor == 150) {
          tv.setBackground(255-tv.backgroundColor);
        } else if (!tv.hovered) {
          tv.setBackground(150);
        }
        tv.draw();
        //if (i < textviews.size()-1) {
        //  this.context.stroke(0);
        //  this.context.strokeWeight(1);
        //  this.context.line(this.pos.x+this.padding, tv.pos.y+tv.viewHeight, this.pos.x+this.viewWidth-this.padding, tv.pos.y+tv.viewHeight);
        //}
      }
    }
  }

  @Override
    public boolean isClicked() {
    if (!visible) return false;
    float mX = this.context.mouseX;
    float mY = this.context.mouseY;
    return (mX >= this.pos.x && mX <= this.pos.x+this.viewWidth && mY >= this.pos.y && mY <= this.pos.y+this.viewHeight);
  }

  @Override
    public boolean isHovered() {
    float mX = this.context.mouseX;
    float mY = this.context.mouseY;
    return (mX >= this.pos.x && mX <= this.pos.x+this.viewWidth && mY >= this.pos.y && mY <= this.pos.y+this.viewHeight);
  }
}
public class Point {
  private Point parent = null;
  private final int id;
  private boolean newPosReceived = false;
  private PVector setPos;
  private PVector absSetPos;
  private PVector lastPos;
  private PVector pos;
  private final int size = 10;
  private PVector lastV = null;
  private PVector a = null;
  private PVector v = null;
  private PVector setW = null;
  private PVector w = null;
  private PVector wAbs = null;
  private float setAlpha = 0;
  private float alpha = 0;
  private float[] phi = new float[]{0, 0, 0};
  private float drawSpeed = 1;
  private long lastTime = 0;
  private float scale = 1;
  private float scaleD = 40;
  private boolean setup = true;
  private boolean reset = false;
  private String name = "";
  private boolean visibilityL = true;
  private boolean visibilityV = true;
  private boolean visibilityA = false;
  private boolean visibilityPath = false;
  private boolean finishedPath = false;
  private ArrayList<Point> childs = new ArrayList<Point>();
  private ArrayList<PVector> path = new ArrayList<PVector>();
  private int[] pathColor = {255, 255, 255};
  private int pathEntryCount = 0;

  Point(int id, Point parent, float amp, float[] angle, PVector w, float alpha) {
    this.id = id;
    init("", parent, getPosFromAngle(amp, angle), w, alpha);
  }

  Point(int id, String name, Point parent, float amp, float[] angle, PVector w, float alpha) {
    this.id = id;
    init(name, parent, getPosFromAngle(amp, angle), w, alpha);
  }

  public PVector getPosFromAngle(float amp, float[] angle) {
    if (angle.length != 2) return null;
    //float x = amp * cos(angle[0]) * cos(angle[1]);
    //float y = amp * sin(angle[1]);
    //float z = amp * sin(angle[0]) * cos(angle[1]);
    float x = amp * cos(angle[0]) * cos(angle[1]);
    float y = amp * sin(angle[1]);
    float z = amp * sin(angle[0]) * cos(angle[1]);
    return new PVector(x, y, z);
  }

  Point(int id, Point parent, PVector pos, PVector w, float alpha) {
    this.id = id;
    init("", parent, pos, w, alpha);
  }

  Point(int id, String name, Point parent, PVector pos, PVector w, float alpha) {
    this.id = id;
    init(name, parent, pos, w, alpha);
  }

  public int getId() {
    return this.id;
  }

  public void setPos(float x, float y, float z) {
    setPos(new PVector(x, y, z));
  }

  public void setPos(PVector pos) {
    this.setPos = new PVector(pos.x, pos.y, pos.z);
    newPosReceived = true;
  }

  public void setW(PVector w) {
    this.setW = getVector(w);
  }

  public void setAlpha(float alpha) {
    this.setAlpha = alpha;
  }

  public String getName() {
    return this.name;
  }

  public void drawPath() {
    drawPath(true);
  }
  public void drawPath(boolean visible) {
    this.visibilityPath = visible;
  }

  public boolean getPathVisibility() {
    return this.visibilityPath;
  }

  public void setPathColor(int[] c) {
    this.pathColor = c;
  }

  public int[] getPathColor() {
    return this.pathColor;
  }

  private void init(String name, Point parent, PVector pos, PVector w, float alpha) {
    println("\n\n" + name);
    this.name = name;
    this.parent = parent;
    this.setPos = new PVector (pos.x, pos.y, pos.z);
    //if (this.parent != null) this.absSetPos = new PVector(this.setPos.x+this.parent.pos.x, this.setPos.y+this.parent.pos.y, this.setPos.z+this.parent.pos.z);
    this.setW = new PVector(w.x, w.y, w.z);
    this.setAlpha = alpha;
    this.lastV = new PVector(0, 0, 0);
    this.v = new PVector(0, 0, 0);
    this.a = new PVector(0, 0, 0);
    calcPos();
  }

  private void initPos(PVector pos) {
    if (this.parent != null) 
      this.absSetPos = new PVector(pos.x+this.parent.pos.x, pos.y+this.parent.pos.y, pos.z+this.parent.pos.z);
    else
      this.absSetPos = new PVector(pos.x, pos.y, pos.z);
    this.lastPos = new PVector (pos.x, pos.y, pos.z);
    this.pos = new PVector (pos.x, pos.y, pos.z);
    //this.amp = sqrt(pos.x*pos.x+pos.y*pos.y);
    //this.amp = this.pos.mag();
    this.phi[0] = 0;
    this.phi[1] = 0;
    if (!(this.pos.x == 0 && this.pos.z == 0)) {
      //if (this.pos.x != 0) {
        println("phiXY atan:" + atan(this.pos.z / this.pos.x));
        this.phi[0] = map(atan(this.pos.z / this.pos.x), -PI, PI, -180, 180);
        println("phiXY mapped:" + this.phi[0]);
        //if (this.pos.x < 0 || this.pos.z < 0) {
        //  this.phi[0] += 180;
        //}
      //}
    }
    if (!(this.pos.z == 0 && this.pos.y == 0)) {
      //if (this.pos.z != 0) {
        println("phiYZ atan:" + atan(this.pos.y / this.pos.z));
        this.phi[1] = map(atan(this.pos.y / this.pos.z), -PI, PI, -180, 180);
        println("phiYZ mapped:" + this.phi[1]);
        //if (this.pos.z < 0 || this.pos.y < 0) {
        //  this.phi[1] += 180;
        //}
      //}
    }
    println("Initialised position:" + this.pos);
    println("Initialised angle:[ " + this.phi[0] + ", " + this.phi[1] + " ]");
    println("Abs Set position:" + this.absSetPos);
  }

  public void calcPos() {
    println("\n\n");
    this.w = new PVector(this.setW.x, this.setW.y, this.setW.z);
    this.alpha = this.setAlpha;
    //println(this.name);
    initPos(this.setPos);
    if (this.parent != null) {
      PVector parentPos = new PVector(this.parent.pos.x, this.parent.pos.y, this.parent.pos.z);
      this.lastPos.add(parentPos);
      this.pos.add(parentPos);
      this.wAbs = new PVector(this.parent.wAbs.x+this.w.x, this.parent.wAbs.y+this.w.y, this.parent.wAbs.z+this.w.z);
    } else {
      this.wAbs = new PVector(w.x, w.y, w.z);
    }
    //println("x:" + this.pos.x);
    //println("y:" + this.pos.y);
    //println("phi:" + this.phi);
    println("Position before update:" + this.pos);
    update();
    println("Position calculation finished:" + this.pos);
  }

  public void moveToStart() {
    this.reset = true;
    this.pathEntryCount = 0;
    resetTime();
    calcPos();
  }

  public void reset() {
    initPos(this.setPos);
    newPosReceived = false;
    moveToStart();
    this.path = new ArrayList<PVector>();
    this.finishedPath = false;
  }

  public void setDrawSpeed(float speed) {
    this.drawSpeed = speed;
  }

  public void setVisibilityL(boolean visible) {
    this.visibilityL = visible;
  }

  public void setVisibilityV(boolean visible) {
    this.visibilityV = visible;
  }

  public void setVisibilityA(boolean visible) {
    this.visibilityA = visible;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  public void setScaleD(float scale) {
    this.scaleD = scale;
  }
  
  public void clearPath() {
     this.path = new ArrayList<PVector>();
     this.pathEntryCount = 0;
     this.finishedPath = false;
  }

  public void update() {
    println("\n" + this.name);
    //this.time += (millis()-this.lastTime)*this.drawSpeed;
    float dTime = (millis()-this.lastTime)*(float)this.drawSpeed;
    if (this.setup || this.reset)
      dTime = 0;
    if (dTime != 0 || this.setup || this.reset) {
      this.lastPos = getVector(this.pos);
      this.lastV = getVector(this.v);
      this.w.x += this.alpha*this.drawSpeed*dTime/1000;
      this.w.y += this.alpha*this.drawSpeed*dTime/1000;
      this.w.z += this.alpha*this.drawSpeed*dTime/1000;
      println("abs pos:" + this.pos);
      PVector position = getVector(this.pos);
      if (this.parent != null) {
        this.wAbs = new PVector(this.parent.wAbs.x+this.w.x, this.parent.wAbs.y+this.w.y, this.parent.wAbs.z+this.w.z);
        position = position.sub(this.parent.pos);
      } else {
        this.wAbs = getVector(this.w);
      }
      
      println("pos:" + position);
      this.pos = rotateV(this.w, position, dTime);

      if (this.parent != null) {
        PVector p = getVector(this.pos).mult(-1);
        this.v = this.w.cross(p);
        this.pos = this.pos.add(this.parent.pos);
        Point lastParent = this.parent;
        for (;;) {
          Point parent = lastParent.parent;
          if (parent == null) {
            break;
          } else {
            p = getVector(this.pos);
            this.v = this.v.add(lastParent.w.cross(p.sub(parent.pos).mult(-1)));
            lastParent = parent;
          }
        }
        if (!this.setup && !this.reset) {
          PVector velocity = new PVector(this.v.x, this.v.y, this.v.z);
          PVector pos = getVector(this.pos);
          this.a = pos.sub(velocity).sub(this.lastPos.sub(this.lastV)).mult(1000/ dTime);
          this.a.add(this.pos);
        } else {
          this.a = new PVector(0, 0, 0);
        }
      }
      println("pos:" + this.pos);
      println("last pos:" + this.lastPos);
      
      if (this.visibilityPath && !this.reset) {
        boolean distanceCheck = false;
        if (this.pathEntryCount < this.path.size()) {
          path.set(this.pathEntryCount, new PVector(this.pos.x, this.pos.y, this.pos.z));
          println("Override entry");
        } else {
          path.add(new PVector(this.pos.x, this.pos.y, this.pos.z));
          final int minData = 100;
          if (path.size() > 5000 || finishedPath)
            path.remove(0);
          if (this.path.size() > minData) {
            distanceCheck = true;
            for (int i=0; i<6; i++) {
              PVector p1 = this.path.get(this.path.size()-(minData/2-1)-i);
              PVector p2 = this.path.get(i);
              float d = dist(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
              if (d > 0.7f) {
                 distanceCheck = false;
                 break;
              }
            }
          }
          if (distanceCheck) {
            this.finishedPath = true;
          }
        }
        this.pathEntryCount++;
      }
      this.lastTime = millis();
      this.setup = false;
      this.reset = false;
    }
    println("\n");
  }

  public boolean mousePressedEvent(float mX, float mY) {
    float d = dist(mX, mY, this.pos.x*scaleD, this.pos.y*scaleD);
    //println("d:"+d);
    if (d <= this.size/2) return true;
    return false;
  }

  public ArrayList<PVector> getPath() {
    return this.path;
  }

  public void draw() {
    if (newPosReceived) {
       initPos(this.setPos);
       newPosReceived = false;
    }
    fill(255);
    stroke(255);
    strokeWeight(1);
    PVector scaledPos = new PVector(this.pos.x, this.pos.y, this.pos.z);
    scaledPos = scaledPos.mult(scaleD);

    if (this.visibilityL) {
      if (this.parent != null) {
        PVector scaledParentPos = new PVector(this.parent.pos.x, this.parent.pos.y, this.parent.pos.z);
        scaledParentPos = scaledParentPos.mult(scaleD);
        line(scaledParentPos.x, scaledParentPos.y, scaledParentPos.z, scaledPos.x, scaledPos.y, scaledPos.z);
      }
    }

    strokeWeight(2);
    if (this.visibilityV) {
      stroke(0, 0, 255);
      line(scaledPos.x, scaledPos.y, scaledPos.z, scaledPos.x + this.v.x*scale, scaledPos.y + this.v.y*scale, scaledPos.z + this.v.z*scale);
    }
    if (this.visibilityA) {
      stroke(255, 0, 0);
      line(scaledPos.x, scaledPos.y, scaledPos.z, scaledPos.x + this.a.x*scale, scaledPos.y + this.a.y*scale, scaledPos.z + this.a.z*scale);
    }
    if (this.parent != null) {
      stroke(51);
      strokeWeight(4);
      PVector start = getVector(this.parent.pos);
      //stroke(255,255,0);
      //line(start.x*scaleD, start.y*scaleD, start.z*scaleD, (this.lastPos.x), (this.lastPos.y), (this.lastPos.z));
      stroke(255, 0, 0);
      line(start.x*scaleD, start.y*scaleD, start.z*scaleD, start.x*scaleD+this.w.x, start.y*scaleD+this.w.y, start.z*scaleD+this.w.z);
    }
    pushMatrix();
    translate(scaledPos.x, scaledPos.y, scaledPos.z);
    if (this.visibilityPath) {
      fill(this.pathColor[0], this.pathColor[1], this.pathColor[2]);
    } else {
      fill(255, 255, 255, 50);
    }
    lights();
    noStroke();
    try {
      sphere(10);
    } catch (Exception e) {
        
    }
    popMatrix();
    hint(DISABLE_DEPTH_TEST);
    fill(255);
    if (!this.name.equals("")) {
      text(this.name, scaledPos.x+this.size, scaledPos.y+this.size, scaledPos.z+this.size);
    }
    hint(ENABLE_DEPTH_TEST);
  }

  public void resetTime() {
    startTime = millis();
    lastTime = millis();
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public PVector getVector(PVector v) {
    return new PVector(v.x, v.y, v.z); 
  }
  
  public PVector rotateV(PVector a, PVector vector, float dTime) {
    final int dAlpha = 10;
    PVector result = new PVector(0, 0, 0);
    println("\nrotating " + vector + " around " + a);
    PVector position = getVector(vector);
    PVector axis = getVector(a).normalize();
    PVector pn = axis.cross(position);
    if (this.parent != null) {
      stroke(51);
      line(this.parent.pos.x*scaleD, this.parent.pos.y*scaleD, this.parent.pos.z*scaleD, (pn.x+this.parent.pos.x)*scaleD, (pn.y+this.parent.pos.y)*scaleD, (pn.z+this.parent.pos.z)*scaleD);
    }
    //for (int t=0; t<dTime; t++) {
      axis = getVector(a).normalize();
      pn = axis.cross(pn).mult(-1);
      axis = getVector(a);
      float angle = a.mag()*dTime/1000*PI/180;
      println("rotating by:" + angle + " degrees");
      println("mag start:" + vector.mag());
      PVector offset = position.sub(pn);
      position = getVector(vector);
      
      strokeWeight(2);
      println("pn before rotation:" + pn);
      
      ArrayList<PVector> positions = new ArrayList<PVector>();
      for (int i=0; i<this.childs.size(); i++) {
          Point child = this.childs.get(i);
          PVector relPos = getVector(child.pos);
          if (this.parent != null) {
             relPos.sub(this.parent.pos); 
          }
          println("child " + child.getName() + " pos:" + relPos);
          positions.add(relPos);
      }
      float alphaX = atan(w.y/w.z);
      if (w.y != 0) {
        float alpha = alphaX;
        println("angle x:" + alpha);
        axis = rotateVX(axis, alpha);
        pn = rotateVX(pn, alpha);
        for (int i=0; i<positions.size(); i++) {
            PVector childPos = positions.get(i);
            childs.get(i).w = rotateVX(childs.get(i).w, alpha);
            positions.set(i, rotateVX(childPos, alpha));
        }
      }
      float alphaY = -atan(axis.x/axis.z);
      println("Axis:" + axis);
      if (w.x != 0) {
        float alpha = alphaY;
        println("angle y:" + alpha);
        axis = rotateVY(axis, alpha);
        pn = rotateVY(pn, alpha);
        for (int i=0; i<positions.size(); i++) {
            PVector childPos = positions.get(i);
            childs.get(i).w = rotateVY(childs.get(i).w, alpha);
            positions.set(i, rotateVY(childPos, alpha));
        }
      }
      if (axis.z > 0) angle *= -1;
      println("Axis:" + axis);
      println("pn:" + pn);
      pn = rotateVZ(pn, angle);
      if (this.parent != null) {
        strokeWeight(4);
        PVector start = getVector(this.parent.pos);
        stroke(255,0,255);
        line(start.x*scaleD, start.y*scaleD, start.z*scaleD, start.x*scaleD+axis.x, start.y*scaleD+axis.y, start.z*scaleD+axis.z);
        //stroke(51);
        //line(start.x*scaleD, start.y*scaleD, start.z*scaleD, (start.x+offset.x)*scaleD, (start.y+offset.y)*scaleD, (start.z+offset.z)*scaleD);
        //line((start.x+offset.x)*scaleD, (start.y+offset.y)*scaleD, (start.z+offset.z)*scaleD, (start.x+pn.x)*scaleD, (start.y+pn.y)*scaleD, (start.z+pn.z)*scaleD);
      }
      for (int i=0; i<positions.size(); i++) {
          PVector childPos = positions.get(i);
          childs.get(i).w = rotateVZ(childs.get(i).w, angle);
          positions.set(i, rotateVZ(childPos, angle));
      }
      
      if (w.x != 0) {
        float alpha = -alphaY;
        println("angle y:" + alpha);
        axis = rotateVY(axis, alpha);
        pn = rotateVY(pn, alpha);
        for (int i=0; i<positions.size(); i++) {
            PVector childPos = positions.get(i);
            childs.get(i).w = rotateVY(childs.get(i).w, alpha);
            positions.set(i, rotateVY(childPos, alpha));
        }
      }
      if (w.y != 0) {
        float alpha = -alphaX;
        println("angle x:" + alpha);
        axis = rotateVX(axis, alpha);
        pn = rotateVX(pn, alpha);
        for (int i=0; i<positions.size(); i++) {
            PVector childPos = positions.get(i);
            childs.get(i).w = rotateVX(childs.get(i).w, alpha);
            positions.set(i, rotateVX(childPos, alpha));
        }
      }
      result = pn.add(offset);
      for (int i=0; i<positions.size(); i++) {
         PVector newPos = getVector(positions.get(i));
         println("child " + this.childs.get(i).getName() + " pos without parent:" + newPos);
         if (this.parent != null) {
           newPos.add(this.parent.pos);
         }
         this.childs.get(i).updatePos(newPos);
         println("child " + this.childs.get(i).getName() + " new pos:" + this.childs.get(i).getPos());
      }
      if (this.parent != null) {
        strokeWeight(4);
        PVector start = getVector(this.parent.pos);
        stroke(51);
        line(start.x*scaleD, start.y*scaleD, start.z*scaleD, (start.x+offset.x)*scaleD, (start.y+offset.y)*scaleD, (start.z+offset.z)*scaleD);
        line((start.x+offset.x)*scaleD, (start.y+offset.y)*scaleD, (start.z+offset.z)*scaleD, (start.x+pn.x)*scaleD, (start.y+pn.y)*scaleD, (start.z+pn.z)*scaleD);
      }
      println("offset:" + offset);
      println("result:" + result);
      println("result mag:" + result.mag());
      //dTime -= dAlpha;
      //if (dTime > 0) {
      //  return rotateV(axis, result, dTime);
      //}
    //}
    return result;
  }
  
  public void addChild(Point p) {
    this.childs.add(p);
    if (this.parent != null) this.parent.addChild(p);
  }
  public void removeChilds() {
    this.childs = new ArrayList<Point>();
  }
  public boolean removeChild(Point p) {
    if (!this.childs.contains(p)) return false;
    this.childs.remove(p);
    return true;
  }
  
  public PVector rotateVX(PVector p, float alpha) {
    println("rotating around x");
    float x = p.x;
    float y = p.y;
    float z = p.z;
    p.x = x;
    p.y = y * cos(alpha) - z * sin(alpha);
    p.z = y * sin(alpha) + z * cos(alpha);
    return getVector(p);
  }
  
  public PVector rotateVY(PVector p, float alpha) {
      println("rotating around y");
      float x = p.x;
      float y = p.y;
      float z = p.z;
      p.x = z * sin(alpha) + x * cos(alpha);
      p.y = y;
      p.z = z * cos(alpha) - x * sin(alpha);
      return getVector(p);
  }
  public PVector rotateVZ(PVector p, float alpha) {
    println("rotating around z");
    float x = p.x;
    float y = p.y;
    float z = p.z;
    p.x = x * cos(alpha) - y * sin(alpha);
    p.y = x * sin(alpha) + y * cos(alpha);
    p.z = z;
    return getVector(p);
  }
  
  public void updatePos(PVector pos) {this.pos = pos;}
  public PVector getPos() {return this.pos;}
}
class TextBox extends TextView { //<>// //<>//
  ArrayList<Integer> keysPressed = new ArrayList<Integer>();
  boolean mouseDrag = false;
  int markedAreaStart = 0;
  int markedAreaLength = 0;
  int inputType = InputTypes.ALL;
  //int textAlignment = 0;
  TextBoxListener mListener;
  KeyListener keyListener;
  int id = -1;
  int margin = 5;
  int cursorPos = 0;
  int dragCursorPos = 0;
  float cursorPosX;
  float dragCursorPosX;
  PVector pos;
  //int setWidth = 0, setHeight = 0;
  //int viewWidth = 0, viewHeight = 0;
  String hint = "";
  String standardText = "";
  String outputText = "";
  String input = "";
  String text = "";
  int textSize = 20;
  boolean setClicked = false;
  boolean clicked = false;
  int cursorTimer = 0;
  boolean cursorVisible = false;
  PApplet context = null;

  TextBox(PApplet context, int posX, int posY) {
    super(context, posX, posY);
    if (context == null) return;
    this.context = context;
    pos = new PVector(posX, posY, 0);
  }

  TextBox(PApplet context, int posX, int posY, int w, int h) {
    super(context, posX, posY, w, h);
    if (context == null) return;
    this.context = context;
    pos = new PVector(posX, posY, 0);
    this.setWidth = w;
    this.setHeight = h;
    calcWidth();
  }

  TextBox(PApplet context, PVector pos) {
    super(context, pos);
    if (context == null) return;
    this.context = context;
    pos = new PVector(pos.x, pos.y, 0);
  }

  TextBox(PApplet context, PVector pos, int w, int h) {
    super(context, pos, w, h);
    if (context == null) return;
    this.context = context;
    pos = new PVector(pos.x, pos.y, 0);
    this.setWidth = w;
    this.setHeight = h;
    calcWidth();
  }

  public void setTextBoxListener(TextBoxListener listener) {
    this.mListener = listener;
  }

  public void setKeyListener(KeyListener listener) {
    this.keyListener = listener;
  }

  public void setHint(String hint) {
    this.hint = hint;
    updateText();
  }

  public void setStandardText(String standardText) {
    this.standardText = standardText;
    updateText();
  }

  public void setText(String text) {
    this.text = text;
    this.input = text;
    updateText();
  }

  public String getText() {
    String text = this.clicked ? this.input : this.text;
    if (text.equals("") && !this.standardText.equals(""))
      text = this.standardText;
    return text;
  }

  public void setTextSize(int size) {
    this.textSize = size;
    updateText();
  }

  public void setTextAlignment(int alignment) {
    if (alignment >= 0 && alignment < 3)
      this.textAlignment = alignment;
  }

  public void setMargin(int margin) {
    this.margin = margin;
  }

  public void mousePressedEvent() {
    float mX = this.context.mouseX;
    float mY = this.context.mouseY;
    if (mX >= this.pos.x && mX <= this.pos.x+this.viewWidth && mY >= this.pos.y && mY <= this.pos.y+this.viewHeight) {
      this.mouseDrag = true;
      this.clicked = true;
      updateText();
      this.cursorPos = calcClosestCharPos(mX);
      this.dragCursorPos = this.cursorPos;
      this.cursorPosX = calcCharPos(this.cursorPos);  //(int)(posX+textWidth(outputText.substring(0, this.cursorPos)))
      this.dragCursorPosX = this.cursorPosX;
      resetCursor();
    } else if (this.clicked) {
      this.clicked = false;
      this.mouseDrag = false;
      textEdited();
    }
  }

  public void mouseReleasedEvent() {
    this.markedAreaStart = this.dragCursorPos < this.cursorPos ? this.dragCursorPos : this.cursorPos;
    this.markedAreaLength = abs(this.dragCursorPos - this.cursorPos);
    //if (this.markedAreaLength > 0 && this.input.length() > 0) {
    //  String marked = this.input.substring(this.markedAreaStart, this.markedAreaLength+this.markedAreaStart);
    //  println(marked);
    //}
    this.mouseDrag = false;
  }

  public void mouseDraggedEvent() {
    if (this.mouseDrag) {
      float posX = calcAlignment();
      //String outputText = clicked ? this.input : this.text;
      float min = this.viewWidth;
      for (int i=0; i<outputText.length()+1; i++) {
        float mX = this.context.mouseX - posX;
        String subString = outputText.substring(0, i);
        float dist = abs(this.context.textWidth(subString) - mX);
        if (dist < min) {
          min = dist;
          this.cursorPos = i;
        }
      }
      this.cursorPosX = calcCharPos(this.cursorPos);
    }
  }

  public void handleKeyPressedEvent(int pressedKeyCode, char pressedKey) {
    if (this.clicked) {
      if (isPrintableChar(pressedKey) && this.markedAreaLength > 0) {
        deleteMarkedInputChars();
      }
      switch (pressedKeyCode) {
      case 8:
        if (this.markedAreaLength > 0)
          deleteMarkedInputChars();
        else
          deleteInputChar(this.cursorPos, -1);
        break;

      case 10:
        textEdited();
        break;

      case 32:
        if (this.inputType == InputTypes.ALL || this.inputType == InputTypes.STRING) {
          addInputChar(this.cursorPos, pressedKey);
        }
        break;

      case 37:
        if (keyIsPressed(16)) {
          if (this.markedAreaLength == 0) {
            if (this.cursorPos > 0)
              this.markedAreaStart = this.cursorPos-1;
            this.markedAreaLength = 1;
          } else if (this.markedAreaStart == this.cursorPos) {
            if (this.markedAreaStart > 0) {
              this.markedAreaStart--;
              this.markedAreaLength++;
            }
          } else {
            this.markedAreaLength--;
          }
        } else {
          this.markedAreaStart = 0;
          this.markedAreaLength = 0;
        }
        updateCursor(-1);
        break;

      case 38:
        textEdited();
        if (this.mListener != null)
          this.mListener.previousTextBox(this.id, calcCharPos(this.cursorPos));
        break;

      case 39:
        if (keyIsPressed(16)) {
          if (this.markedAreaLength == 0) {
            this.markedAreaStart = this.cursorPos;
            this.markedAreaLength = 1;
          } else if (this.markedAreaStart == this.cursorPos) {
            this.markedAreaStart++;
            this.markedAreaLength--;
          } else {
            if (this.cursorPos < this.input.length())
              this.markedAreaLength++;
          }
        } else {
          this.markedAreaStart = 0;
          this.markedAreaLength = 0;
        }
        updateCursor(1);
        break;

      case 40:
        textEdited();
        if (this.mListener != null)
          this.mListener.nextTextBox(this.id, calcCharPos(this.cursorPos));
        break;

      case 127:
        if (this.markedAreaLength > 0)
          deleteMarkedInputChars();
        else
          deleteInputChar(this.cursorPos, 0);
        break;

      default:
        if (isPrintableChar(pressedKey)) {
          switch (this.inputType) {
          case InputTypes.INTEGER:
            try {
              Integer.parseInt(""+pressedKey);
            } 
            catch (NumberFormatException e) {
              println(e);
              if (!(this.cursorPos == 0 && pressedKey == '-' && !this.input.contains("-")))
                return;
            }
            if (this.cursorPos == 0 && this.input.contains("-")) return;
            addInputChar(this.cursorPos, pressedKey);
            break;

          case InputTypes.FLOAT:
            try {
              Float.parseFloat(""+pressedKey);
            } 
            catch (NumberFormatException e) {
              println(e);
              if (!(!this.input.contains(".") && pressedKey == '.') && pressedKey != '-')
                return;
            }
            if (pressedKey == '-') {
              if (this.input.contains("-")) {
                deleteInputChar(1, -1);
              } else {
                addInputChar(0, pressedKey);
              }
            } else {
              if (this.cursorPos == 0 && this.input.contains("-")) return;
              addInputChar(this.cursorPos, pressedKey);
            }
            break;

          default:
            addInputChar(this.cursorPos, pressedKey);
          }
        } else {
          boolean keyExists = false;
          for (int kc : keysPressed) {
            if (kc == pressedKeyCode) {
              keyExists = true;
              break;
            }
          }
          if (!keyExists)
            keysPressed.add(pressedKeyCode);
        }
      }
      if (this.keyListener != null) {
        this.keyListener.onKeyPressed(pressedKeyCode, pressedKey);
      }
      //println("input:"+this.input);
    }
  }

  public boolean keyIsPressed(int keyCode) {
    boolean keyPressed = false;
    for (int kc : keysPressed) {
      if (kc == keyCode) {
        keyPressed = true;
        break;
      }
    }
    return keyPressed;
  }

  public void handleKeyReleasedEvent(int releasedKeyCode, char releasedKey) {
    for (int i=0; i<keysPressed.size(); i++) {
      if (keysPressed.get(i) == releasedKeyCode) {
        keysPressed.remove(i);
        return;
      }
    }
  }

  public void addInputChar(int pos, char pressedKey) {
    String buffer = "";
    if (pos > 0) {
      buffer += this.input.substring(0, pos);
    }
    buffer += pressedKey;
    if (pos < this.input.length()) {
      buffer += this.input.substring(pos, this.input.length());
    }
    this.input = buffer;
    updateText();
    updateCursor(1);
  }

  public void deleteInputChar(int pos, int dir) {
    if (dir != -1 && dir != 0) return; 
    pos += dir;
    if (this.input.length()>0) {
      if (pos < this.input.length()) {
        String buffer = "";
        if (pos > 0)
          buffer += this.input.substring(0, pos);
        buffer += this.input.substring(pos+1, this.input.length());
        this.input = buffer;
      }
      updateText();
      updateCursor(dir);
    }
  }

  public void deleteMarkedInputChars() {
    String buffer = "";
    if (this.markedAreaStart > 0) {
      buffer += this.input.substring(0, this.markedAreaStart);
    }
    if (this.markedAreaStart+this.markedAreaLength < this.input.length()) {
      buffer += this.input.substring(this.markedAreaStart+this.markedAreaLength, this.input.length());
    }
    this.input = buffer;
    this.cursorPos = this.markedAreaStart;
    this.markedAreaStart = 0;
    this.markedAreaLength = 0;
    updateText();
    updateCursor(0);
  }

  public void textEdited() {
    this.text = this.input;
    this.clicked = false;
    //updateText();
    if (this.inputType == InputTypes.FLOAT) {
      if (this.input.length() > 0) {
        if (this.input.equals("."))
          this.text = "0.0";
        else if (this.input.charAt(this.input.length()-1) == '.')
          this.text += "0";

        if (!this.text.contains(".")) {
          if (this.input.equals("-"))
            this.text = "0.0";
          else
            this.text += ".0";
        } else {
          if (this.input.charAt(0) == '.') {
            String buffer = this.text;
            this.text = "0" + buffer;
          }
        }
      }
      this.input = this.text;
    } else if (this.inputType == InputTypes.INTEGER) {
      if (this.input.length() > 0) {
          if (this.input.equals("-"))
            this.text = "0";
      }
      this.input = this.text;
    }
    updateText();
    this.markedAreaStart = 0;
    this.markedAreaLength = 0;
    if (this.mListener != null) {
      this.mListener.textEdited(this.id, this.text);
    }
  }

  public void updateText() {
    this.outputText = this.clicked ? this.input : this.text;
    if (this.outputText.length() == 0) {
      if (!this.standardText.equals("") && !this.clicked) {
        //println("std text");
        this.outputText = this.standardText;
        this.input = this.outputText;
      } else if (!this.hint.equals("")) {
        this.outputText = this.hint;
      }
    }
    calcWidth();
    calcHeight();
  }

  public @Override
    void calcWidth() {
    this.context.textSize(this.textSize);
    //String shownText = clicked ? this.input : this.text;
    //if (shownText.equals("")) {
    //  if (!this.standardText.equals("")) {
    //    shownText = this.standardText;
    //  } else if (!this.hint.equals("")) {
    //    shownText = this.hint;
    //  }
    //}
    int nWidth = (int) this.context.textWidth(this.outputText)+this.margin*2;
    int newWidth = this.setWidth > nWidth ? this.setWidth : nWidth;
    this.viewWidth = newWidth;
    //if (this.width > this.setWidth) {
    // this.setWidth = this.width; 
    //}
  }

  public @Override
    void calcHeight() {
    int nHeight = this.textSize + 10;
    int newHeight = this.setWidth > nHeight ? this.setHeight : nHeight;
    this.viewHeight = newHeight;
    //if (this.height > this.setHeight) {
    // this.setHeight = this.height; 
    //}
  }

  public void updateCursor(int change) {
    if (change == -1 && this.cursorPos > 0)  
      this.cursorPos--;
    else if (change == 1 && this.cursorPos < this.input.length())
      this.cursorPos++;
    this.cursorPosX = calcCharPos(this.cursorPos);
    resetCursor();
  }

  public void setInputType(int type) {
    this.inputType = type;
  }

  public void resetCursor() {
    this.cursorVisible = true;
    this.cursorTimer = millis();
  }

  public void setClicked(boolean clicked) {
    setClicked(clicked, 0);
  }
  public void setClicked(boolean clicked, int cursorPosX) {
    this.setClicked = clicked;
    if (this.setClicked) {
      this.cursorPos = calcClosestCharPos(cursorPosX);
      updateCursor(0);
    }
  }

  //boolean isClicked() {
  //  return this.clicked;
  //}

  public void showCursor() {
    if (this.setClicked) {
      this.clicked = true;
      this.setClicked = false;
    }
    if (this.clicked) {
      if (millis() - cursorTimer >= 500) {
        cursorVisible = !cursorVisible;
        cursorTimer = millis();
      }
      float offset = (this.viewHeight-this.textSize)/2;
      float y1 = this.pos.y+offset-1;
      float y2 = this.pos.y+this.viewHeight-offset+1;
      if (cursorVisible) {
        this.context.line(this.cursorPosX, y1, this.cursorPosX, y2);
      }
      if (this.mouseDrag) {
        this.context.fill(255, 255, 255, 100);
        this.context.noStroke();
        this.context.rect(this.cursorPosX, y1, this.dragCursorPosX-this.cursorPosX, y2-y1);
      } else if (this.markedAreaLength > 0) {
        this.context.fill(255, 255, 255, 100);
        this.context.noStroke();
        int start = calcCharPos(this.markedAreaStart);
        int stop = calcCharPos(this.markedAreaLength+this.markedAreaStart) - start;
        this.context.rect(start, y1, stop, y2-y1);
      }
    }
  }

  public int calcCharPos(int idx) {
    this.context.textSize(this.textSize);
    //String outputText = this.clicked ? this.input : this.text;
    String displayedText = getEditableText();
    if (displayedText.length() == 0 || idx < 0 || idx > displayedText.length()) return (int) calcAlignment(displayedText);
    return (int)(calcAlignment()+this.context.textWidth(displayedText.substring(0, idx)));
  }

  public int calcClosestCharPos(float x) {
    int idx = -1;
    String displayedText = getEditableText(); //this.outputText; 
    float posX = calcAlignment(displayedText);
    //String outputText = this.clicked ? this.input : this.text;
    float min = this.viewWidth;
    this.context.textSize(this.textSize);
    println();
    for (int i=0; i<displayedText.length()+1; i++) {
      String subString = displayedText.substring(0, i);
      float dist = abs(this.context.textWidth(subString) - (x - posX));
      if (dist < min) {
        min = dist;
        idx = i;
      }
      println("dist:"+dist);
      println("min:"+min);
      println("idx:"+idx);
    }
    return idx;
  }

  public float calcAlignment() {
    return calcAlignment(this.outputText);
  }
  public float calcAlignment(String displayedText) {
    this.context.textSize(this.textSize);
    //String outputText = this.clicked ? this.input : this.text;
    //if (outputText.equals("")) {
    //  if (!this.standardText.equals("")) outputText = this.standardText;
    //  else if (!this.hint.equals("")) outputText = this.hint;
    //}
    float posX = this.pos.x;
    float offset = 0;
    switch (this.textAlignment) {
    case TextView.TEXTALIGNMENT_LEFT:
      posX += this.margin;
      break;

    case TextView.TEXTALIGNMENT_RIGHT:
      offset = this.viewWidth-this.context.textWidth(displayedText)-this.margin;
      if (offset < this.margin)
        offset = this.margin;
      posX += offset;
      break;

    case TextView.TEXTALIGNMENT_CENTER:
      offset = (this.viewWidth-this.context.textWidth(displayedText)) / 2;
      if (offset < this.margin)
        offset = this.margin;
      posX += offset;
      break;
    }
    return posX;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public String getEditableText() {
    return this.outputText.equals(this.hint) ? "" : this.outputText;
  }

  public boolean isPrintableChar( char c ) {
    Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
    return (!Character.isISOControl(c)) &&
      block != null &&
      block != Character.UnicodeBlock.SPECIALS;
  }

  public void draw() {
    super.draw();
    this.context.noFill();
    this.context.stroke(255);
    this.context.strokeWeight(1);
    this.context.textSize(this.textSize);
    this.context.rect(this.pos.x, this.pos.y, this.viewWidth, this.viewHeight);
    //outputText = this.clicked ? this.input : this.text;
    float offset = (this.viewHeight-this.textSize) / 2+2;
    float posX = calcAlignment();
    if (offset < 0) 
      offset = 0;
    //if (!this.clicked && outputText.equals("")) {
    //  if (!this.hint.equals("")) {
    //    outputText = this.hint;
    //    this.context.fill(100);
    //  } else if (!this.standardText.equals("")) {
    //    outputText = this.standardText;
    //    this.context.fill(255);
    //  }
    //} else {
    //  this.context.fill(255);
    //}
    if (this.outputText.equals(this.hint)) this.context.fill(100);
    else this.context.fill(255);
    this.context.text(outputText, posX, this.pos.y+this.viewHeight-offset);
    showCursor();
  }
}
class TextView extends View {
  final static int TEXTALIGNMENT_LEFT = 0;
  final static int TEXTALIGNMENT_RIGHT = 1;
  final static int TEXTALIGNMENT_CENTER = 2;

  int setWidth = 0, setHeight = 0;
  String text = "";
  int margin = 5;
  int textSize = 20;
  int strokeColor = 255;
  int strokeWeight = 1;
  //int backgroundColor = 0;
  //int backgroundgAlpha = 255;
  int textColor = 255;
  int textAlignment = TextView.TEXTALIGNMENT_LEFT;
  int horizontalAlignment = TextView.ALIGNMENT_MANUALL;
  int verticalAlignment = TextView.ALIGNMENT_MANUALL;
  int cornerRadius = 0;
  PApplet context = null;

  TextView(PApplet context, float x, float y) {
    super(context, x, y);
    this.context = context;
    this.pos = new PVector(x, y, 0);
  }

  TextView(PApplet context, float x, float y, int w, int h) {
    super(context, x, y, width, height);
    this.context = context;
    this.pos = new PVector(x, y, 0);
    this.setWidth = w;
    this.setHeight = h;
  }

  TextView(PApplet context, PVector pos) {
    super(context, pos);
    this.context = context;
    this.pos = new PVector(pos.x, pos.y, pos.z);
  }

  TextView(PApplet context, PVector pos, int w, int h) {
    super(context, pos, width, height);
    this.context = context;
    this.pos = new PVector(pos.x, pos.y, pos.z);
    this.setWidth = w;
    this.setHeight = h;
  }

  TextView(PApplet context, float x, float y, String text) {
    super(context, x, y);
    this.context = context;
    this.pos = new PVector(x, y, 0);
    this.text = text;
  }

  TextView(PApplet context, float x, float y, int w, int h, String text) {
    super(context, x, y, width, height);
    this.context = context;
    this.pos = new PVector(x, y, 0);
    this.setWidth = w;
    this.setHeight = h;
    this.text = text;
  }

  TextView(PApplet context, PVector pos, String text) {
    super(context, pos);
    this.context = context;
    this.pos = new PVector(pos.x, pos.y, pos.z);
    this.text = text;
  }

  TextView(PApplet context, PVector pos, int w, int h, String text) {
    super(context, pos, width, height);
    this.context = context;
    this.pos = new PVector(pos.x, pos.y, pos.z);
    this.setWidth = w;
    this.setHeight = h;
    this.text = text;
  }

  @Override
    public void setWidth(int w) {
    this.setWidth = w;
    calcWidth();
  }

  @Override
    public void setHeight(int h) {
    this.setHeight = h;
    calcHeight();
  }

  public void setText(String text) {
    this.text = text;
    calcWidth();
    calcHeight();
  }

  public void setTextSize(int size) {
    this.textSize = size;
    calcWidth();
    calcHeight();
  }

  public void setTextColor(int textColor) {
    this.textColor = textColor;
  }
  
  public String getText() {return this.text;}

  public void setStrokeColor(int strokeColor) {
    this.strokeColor = strokeColor;
  }

  public void setStrokeWeight(int weight) {
    this.strokeWeight = weight;
  }

  public void calcPosX() {
    switch (this.horizontalAlignment) {
    case TextView.ALIGNMENT_MANUALL:
      break;

    case TextView.ALIGNMENT_CENTER:
      this.pos.x = (this.context.width-this.viewWidth) / 2;
      break;

    case TextView.ALIGNMENT_LEFT:
      this.pos.x = this.padding;
      break;

    case TextView.ALIGNMENT_RIGHT:
      this.pos.x = this.context.width-this.viewWidth-this.padding;
      break;
    }
  }

  public void calcPosY() {
    switch (this.verticalAlignment) {
    case TextView.ALIGNMENT_MANUALL:
      break;

    case TextView.ALIGNMENT_CENTER:
      this.pos.y = (this.context.height-this.viewHeight) / 2;
      break;

    case TextView.ALIGNMENT_TOP:
      this.pos.y = this.padding;
      break;

    case TextView.ALIGNMENT_BOTTOM:
      this.pos.y = this.context.height-this.viewHeight-this.padding;
      break;
    }
  }

  public void calcWidth() {
    this.context.textSize(this.textSize);
    int nWidth = (int) this.context.textWidth(this.text)+this.margin*2;
    int newWidth = this.setWidth > nWidth ? this.setWidth : nWidth;
    this.viewWidth = newWidth;
  }

  public void calcHeight() {
    int nHeight = this.textSize + 2 * this.margin;
    int newHeight = this.setHeight > nHeight ? this.setHeight : nHeight;
    this.viewHeight = newHeight;
  }

  public void setHorizontalAlignment(int alignment) {
    this.horizontalAlignment = alignment;
    calcPosX();
  }

  public void setVerticalAlignment(int alignment) {
    this.verticalAlignment = alignment;
    calcPosY();
  }

  public void setTextAlignment(int alignment) {
    if (alignment >= 0 && alignment < 3)
      this.textAlignment = alignment;
  }

  public void setMargin(int margin) {
    this.margin = margin;
  }

  public void setPadding(int padding) {
    this.padding = padding;
  }

  public void setCornerRadius(int radius) {
    this.cornerRadius = radius;
  }

  public float calcAlignment() {
    this.context.textSize(this.textSize);
    float posX = this.pos.x;
    float offset = 0;
    switch (this.textAlignment) {
    case TEXTALIGNMENT_LEFT:
      posX += this.margin;
      break;

    case TEXTALIGNMENT_RIGHT:
      offset = this.viewWidth-this.context.textWidth(this.text)-this.margin;
      if (offset < this.margin)
        offset = this.margin;
      posX += offset;
      break;

    case TEXTALIGNMENT_CENTER:
      offset = (this.viewWidth-this.context.textWidth(this.text)) / 2;
      if (offset < this.margin)
        offset = this.margin;
      posX += offset;
      break;
    }
    return posX;
  }

  @Override
    public boolean isClicked() {
    float mX = this.context.mouseX;
    float mY = this.context.mouseY;
    return (mX >= this.pos.x && mX <= this.pos.x+this.viewWidth && mY >= this.pos.y && mY <= this.pos.y+this.viewHeight);
  }

  @Override
    public boolean isHovered() {
    float mX = this.context.mouseX;
    float mY = this.context.mouseY;
    return (mX >= this.pos.x && mX <= this.pos.x+this.viewWidth && mY >= this.pos.y && mY <= this.pos.y+this.viewHeight);
  }

  public void draw() {
    super.draw();
    if (this.visible) {
      this.context.stroke(this.strokeColor);
      if (this.strokeWeight > 0) 
        this.context.strokeWeight(this.strokeWeight);
      else
        this.context.noStroke();
      this.context.fill(this.backgroundColor, this.backgroundColor, this.backgroundColor, this.backgroundAlpha);
      this.context.rect(this.pos.x, this.pos.y, this.viewWidth, this.viewHeight, this.cornerRadius);
      this.context.fill(this.textColor);
      this.context.textSize(this.textSize);
      calcWidth();
      calcHeight();
      float x = calcAlignment();
      float y = this.pos.y+this.viewHeight-(this.viewHeight-this.textSize)/2-2;
      this.context.text(this.text, x, y);
    }
  }
}
public class Toast extends View {
  private static final int STATE_ENTER = 0;
  private static final int STATE_SHOW = 1;
  private static final int STATE_EXIT = 2;
  private static final int standardWidth = 150;
  private static final int standardHeight = 40;
  private static final int standardRadius = 10;
  static final int DURATION_SHORT = 0;
  static final int DURATION_LONG = 1;
  static final int DURATION_INFINITE = 2;
  private PVector mousePosDiff = new PVector(0, 0, 0);
  long startTime = 0;
  long lastTime = 0;
  int duration = 2000;
  int speed = 5;
  String text = "";
  int state = Toast.STATE_ENTER;

  Toast(PApplet context, String text, int duration) {
    super(context, context.width/2-standardWidth/2, context.height);
    this.context = context;
    this.text = text;
    switch (duration) {
    case Toast.DURATION_SHORT:
      this.duration = 2000;
      break;
      
      case Toast.DURATION_LONG:
      this.duration = 4000;
      break;
      
      case Toast.DURATION_INFINITE:
      this.duration = -1;
      break;
    }
    this.pos = new PVector(context.width/2-standardWidth/2, context.height, 0);
  }

  @Override
    public boolean isClicked() {
    float mX = this.context.mouseX;
    float mY = this.context.mouseY;
    if (mX >= this.pos.x && mX <= this.pos.x+Toast.standardWidth && mY >= this.pos.y && mY <= this.pos.y+Toast.standardHeight) {
      mousePosDiff = new PVector(mX - this.pos.x, mY - this.pos.y, 0);
      return true; 
    }
    return false;
  }
  
  @Override
    public boolean isHovered() {
    float mX = this.context.mouseX;
    float mY = this.context.mouseY;
    return (mX >= this.pos.x && mX <= this.pos.x+Toast.standardWidth && mY >= this.pos.y && mY <= this.pos.y+Toast.standardHeight);
  }

  public void update() {
    switch (this.state) {
    case Toast.STATE_ENTER:
      this.pos.y -= this.speed;
      if (this.pos.y < this.context.height-100) {
        this.state = Toast.STATE_SHOW;
        this.lastTime = millis();
      }
      break;

    case Toast.STATE_SHOW:
      if (millis() - this.lastTime >= this.duration && this.duration > 0)
        this.state = Toast.STATE_EXIT;
      break;

    case Toast.STATE_EXIT:
      this.pos.y += this.speed;
      if (this.pos.y > this.context.height)
        this.visible = false;
      break;
    }
  }
  
  public void windowResized(int w, int h) {
    this.pos.x = (float)(this.context.width/2-Toast.standardWidth/2);
    this.pos.y = this.pos.y/h * this.context.height;
  }

  @Override
    public void draw() {
    if (this.visible) {
      if (!this.clicked)
        update();
      this.context.fill(255);
      this.context.stroke(100);
      this.context.textSize(20);
      float x = this.pos.x;
      float y = this.pos.y;
      if (this.clicked) {
         x = this.context.mouseX - this.mousePosDiff.x;
         y = this.context.mouseY - this.mousePosDiff.y;
      }
      this.context.rect(x, y, Toast.standardWidth, Toast.standardHeight, Toast.standardRadius);
      this.context.fill(0);
      float pX = x + (Toast.standardWidth - this.context.textWidth(this.text)) / 2;
      float pY = y + 18 + (Toast.standardHeight - 20) / 2;
      this.context.text(this.text, pX, pY);
    }
  }
}
public abstract class View {
  final static int ALIGNMENT_MANUALL = 0;
  final static int ALIGNMENT_CENTER = 1;
  final static int ALIGNMENT_LEFT = 2;
  final static int ALIGNMENT_RIGHT = 3;
  final static int ALIGNMENT_TOP = 4;
  final static int ALIGNMENT_BOTTOM = 5;

  protected int id = -1;
  protected OnClickListener onClickListener;
  protected OnHoverListener onHoverListener;
  protected Runnable onHoverAction;
  protected PVector pos;
  protected boolean clicked = false;
  protected boolean visible = true;
  protected boolean hovered = false;
  protected int viewWidth = 0, viewHeight = 0;
  protected int padding = 0;
  protected int backgroundColor = 0;
  protected int backgroundAlpha = 255;
  protected int horizontalAlignment = TextView.ALIGNMENT_MANUALL;
  protected int verticalAlignment = TextView.ALIGNMENT_MANUALL;
  protected PApplet context = null;

  View(PApplet context, float x, float y) {
    this.context = context;
    this.pos = new PVector(x, y, 0);
  }

  View(PApplet context, float x, float y, int w, int h) {
    this.context = context;
    this.pos = new PVector(x, y, 0);
    this.viewWidth = w;
    this.viewHeight = h;
  }

  View(PApplet context, PVector pos) {
    this.context = context;
    this.pos = new PVector(pos.x, pos.y, pos.z);
  }

  View(PApplet context, PVector pos, int w, int h) {
    this.context = context;
    this.pos = new PVector(pos.x, pos.y, pos.z);
    this.viewWidth = w;
    this.viewHeight = h;
  }
  
  public void setVisibility(boolean visible) {this.visible = visible;}
  public boolean getVisibility() {return this.visible;}
  
  public void setPos(PVector pos) {
  this.pos = new PVector(pos.x, pos.y, pos.z);
}

  public void setBackground(int background) {
    this.backgroundColor = background;
  }
  
  public void setBackgroundgAlpha(int alpha) {
    this.backgroundAlpha = alpha;
  }
  
  public void setWidth(int w) {this.viewWidth = w;}
  public void setHeight(int h) {this.viewHeight = h;}

  public void calcPosX() {
    switch (this.horizontalAlignment) {
    case TextView.ALIGNMENT_MANUALL:
      break;

    case TextView.ALIGNMENT_CENTER:
      this.pos.x = (this.context.width-this.viewWidth) / 2;
      break;

    case TextView.ALIGNMENT_LEFT:
      this.pos.x = this.padding;
      break;

    case TextView.ALIGNMENT_RIGHT:
      this.pos.x = this.context.width-this.viewWidth-this.padding;
      break;
    }
  }

  public void calcPosY() {
    switch (this.verticalAlignment) {
    case TextView.ALIGNMENT_MANUALL:
      break;

    case TextView.ALIGNMENT_CENTER:
      this.pos.y = (this.context.height-this.viewHeight) / 2;
      break;

    case TextView.ALIGNMENT_TOP:
      this.pos.y = this.padding;
      break;

    case TextView.ALIGNMENT_BOTTOM:
      this.pos.y = this.context.height-this.viewHeight-this.padding;
      break;
    }
  }

  public void setHorizontalAlignment(int alignment) {
    this.horizontalAlignment = alignment;
    calcPosX();
  }

  public void setVerticalAlignment(int alignment) {
    this.verticalAlignment = alignment;
    calcPosY();
  }

  public void setPadding(int padding) {
    this.padding = padding;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public void setOnClickListener(OnClickListener listener) {
    this.onClickListener = listener;
  }
  
  public void setOnHoverListener(OnHoverListener listener) {
    this.onHoverListener = listener;
  }
  
  public void setOnHoverAction(Runnable action) {
    this.onHoverAction = action;
  }
  
  public abstract boolean isClicked();
  public abstract boolean isHovered();

  public boolean onMousePressed() {
    if (isClicked()) {
      this.clicked = true;
      if (this.onClickListener != null)
        this.onClickListener.onClick(this.id);
    } else if (this.clicked) {
      this.clicked = false;
    }
    return this.clicked;
  }
  
  public void onMouseReleased() {
    this.clicked = false;
  }
  
  public int getWidth() {return this.viewWidth;}
  public int getheight() {return this.viewHeight;}

  public void draw() {
    calcPosX();
    calcPosY();
    this.hovered = isHovered();
    if (this.hovered) {
      if (this.onHoverListener != null) {
      this.onHoverListener.onHover(this.id);
      }
      if (this.onHoverAction !=null) {
       this.onHoverAction.run(); 
      }
    }
    //this.context.stroke(255);
    //this.context.strokeWeight(1);
    //this.context.fill(this.backgroundColor, this.backgroundColor, this.backgroundColor, this.backgroundAlpha);
    //this.context.rect(this.pos.x, this.pos.y, this.viewWidth, this.viewHeight);
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Drehsystem_3d" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
