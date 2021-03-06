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

  void calcWidth() {
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

  void calcHeight() {
    int nHeight = this.padding;
    for (TextView tv : textviews) {
      nHeight += tv.viewHeight+1;
      println("viewHeight:" + tv.viewHeight);
    }
    nHeight += this.padding;
    this.viewHeight = nHeight;
    println("this.viewHeight:" + this.viewHeight);
  }

  void calcPos() {
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

  void updatePos(float offsetX, float offsetY) {
    for (TextView tv : textviews) {
      println("\nprevious pos:"+tv.pos);
      tv.setPos(new PVector(tv.pos.x+offsetX, tv.pos.y+offsetY, 0));
      println("pos update:" + tv.pos);
    }
  }

  void setOnItemClickListener(OnItemClickListener listener) {
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