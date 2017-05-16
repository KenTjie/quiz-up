import java.io.FileNotFoundException;
import javax.swing.JFrame;

public class QuizUp{
  public static void main (String[] args) throws FileNotFoundException {
    QuizUpFrame gameFrame = new QuizUpFrame();
    gameFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
  } 
}