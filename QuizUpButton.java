import javax.swing.ImageIcon;
import javax.swing.JButton;

public class QuizUpButton extends JButton {
  
  int tag;
  
  public QuizUpButton (int tag, ImageIcon picture) {
    super(picture);
    this.tag = tag;
  }//end constructor
  
  public int getTag () {
    return tag;
  }//end getTag
  
  public void setTag (int tag) {
    this.tag = tag;
  }//end setTag
  
  public void setIcon (ImageIcon picture) {
    super.setIcon(picture);
  }//end setIcon
  
}//end class