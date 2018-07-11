public interface TextBoxListener {
  void textEdited(int id, String text);
  void previousTextBox(int id, int cursorPosX);
  void nextTextBox(int id, int cursorPosX);
}

public interface InputBoxListener {
  void finishedEditing(String... data);
  void onExit();
}

public interface KeyListener {
 void onKeyPressed(int pressedKeyCode, char pressedKey); 
 void onKeyReleased(int pressedKeyCode, char pressedKey);
}

//public interface ButtonListener implements OnClickListener, OnAnimationFinishedListener {
  
//}

public interface OnClickListener {
  void onClick(int id);
}

public interface OnHoverListener {
  void onHover(int id);
}

public interface OnItemClickListener {
  void onItemClick(int itemIdx, String item);
}

public interface OnAnimationFinishedListener {
  void onAnimationFinished();
}

public interface UIListener {
 void drawUI(); 
}