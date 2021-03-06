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

  void init() {
    this.textAlignment = TextView.TEXTALIGNMENT_CENTER;
  }

  ArrayList<PVector> points = new ArrayList<PVector>();

  @Override
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

  PVector checkShapeDim(float x, float y) {
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

  PVector calcShapeDim(float startX, float startY, float angle) {
    float x = startX - this.pos.x;
    float y = startY - abs(x * tan(angle));
    float k = tan(angle);
    float r = this.cornerRadius;
    float distX = (r-sqrt(2*k*r*r-y+2*y*r-2*y*k*r)-y*k+k*r)/(k*k+1);
    float distY = r-sqrt(r*r-(r-distX)*(r-distX));
    return new PVector(this.pos.x+distX, this.pos.y+distY, 0);
  }

  void setOnClickListener(OnClickListener listener) {
    this.onClickListener = listener;
  }

  void setOnAnimationFinishedListener(OnAnimationFinishedListener listener) {
    this.onAnimationFinishedListener = listener;
  }

  void mousePressedEvent() {
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