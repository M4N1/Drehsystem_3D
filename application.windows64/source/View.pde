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
  
  void setVisibility(boolean visible) {this.visible = visible;}
  boolean getVisibility() {return this.visible;}
  
  void setPos(PVector pos) {
  this.pos = new PVector(pos.x, pos.y, pos.z);
}

  void setBackground(int background) {
    this.backgroundColor = background;
  }
  
  void setBackgroundgAlpha(int alpha) {
    this.backgroundAlpha = alpha;
  }
  
  void setWidth(int w) {this.viewWidth = w;}
  void setHeight(int h) {this.viewHeight = h;}

  void calcPosX() {
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

  void calcPosY() {
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

  void setHorizontalAlignment(int alignment) {
    this.horizontalAlignment = alignment;
    calcPosX();
  }

  void setVerticalAlignment(int alignment) {
    this.verticalAlignment = alignment;
    calcPosY();
  }

  void setPadding(int padding) {
    this.padding = padding;
  }

  void setId(int id) {
    this.id = id;
  }

  int getId() {
    return this.id;
  }

  void setOnClickListener(OnClickListener listener) {
    this.onClickListener = listener;
  }
  
  void setOnHoverListener(OnHoverListener listener) {
    this.onHoverListener = listener;
  }
  
  void setOnHoverAction(Runnable action) {
    this.onHoverAction = action;
  }
  
  abstract boolean isClicked();
  abstract boolean isHovered();

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
  
  int getWidth() {return this.viewWidth;}
  int getheight() {return this.viewHeight;}

  void draw() {
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