//import everything that is needed
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.Graphics;
import javax.swing.JOptionPane;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.net.URL;
import java.net.MalformedURLException;
import java.applet.AudioClip;


//create a class that contains a JFrame and has a key listener and an action listener
public class QuizUpFrame extends JFrame implements ActionListener, KeyListener{
  
  //create all necessary variables
  private AudioClip[] sounds;
  private QuizUpButton[] questionButtons = new QuizUpButton[15];
  private String[] questions = new String[15];
  private String[] answers = new String[15];
  private Container gameFrame;
  private GridLayout gameSetup;
  private static final int NUM_QUESTIONS = 15;
  private int scorePlayer1, scorePlayer2, numQuestionsAnswered, questionSet, buttonNumber;
  private int numRoundsPlayed = 0;
  private String playerAnswer, askEnd, player1, player2, userAnswer;
  private boolean canBuzz;
  private ImageIcon buttonPic;
  private JLabel scores1 = new JLabel("");
  private JLabel scores2 = new JLabel("");
  private JLabel spacer1 = new JLabel("");
  private JLabel numGames = new JLabel("");
  private JButton resetButton = new JButton("Reset");
  private Scanner input;
  private QuizUpButton buttonPressed;
  
  //creates the GUI and sets the variables, questions and answers
  public QuizUpFrame () throws FileNotFoundException {
    
    //gives the Frame a title
    super("Quiz Up! Java Edition");
    
    //display the instructions
    JOptionPane.showMessageDialog(null, "Welcome to Quiz Up here on Dr. Java!");
    JOptionPane.showMessageDialog(null, "This is a 2 player game, so go and grab another person!");
    JOptionPane.showMessageDialog(null, "Your job is to beat your opponent by answering the most questions.");
    JOptionPane.showMessageDialog(null, "Each picture represents a different topic, click one to select.");
    JOptionPane.showMessageDialog(null, "After a question is answered, you must press OK, and whoever presses their button first, gets to answer first.");
    JOptionPane.showMessageDialog(null, "If you get it wrong, the other person will try to answer correctly.");
    JOptionPane.showMessageDialog(null, "Don't forget to answer with pronouns when needed!");
    JOptionPane.showMessageDialog(null, "Player one, your buzzer is 'A', Player 2's is 'L'.");
    JOptionPane.showMessageDialog(null, "Good luck!");
    
    //ask the user for a name for player1
    player1 = JOptionPane.showInputDialog("Enter Player 1's name");
    
    //if it's empty, ask again
    while (player1.length() == 0)
      player1 = JOptionPane.showInputDialog("Enter Player 1's name");
    
    //ask the user for a name for player2
    player2 = JOptionPane.showInputDialog("Enter Player 2's name");
    
    //ask again if it's empty
    while (player2.length() == 0)
      player2 = JOptionPane.showInputDialog("Enter Player 2's name");
    
    gameFrame = getContentPane();//create a container
    gameSetup = new GridLayout(4, 5, 5, 5);//create a grid the is 4x5, with 5px between each button
    gameFrame.setLayout(gameSetup);//set the layout of the container to the grid saved in gameSetup
    
    //fill the array questionButtons using a counted loop
    for (int i = 0; i < questionButtons.length; i++) {
      buttonPic = new ImageIcon ("images/" + i + ".jpg");//retrieves an image according to i
      questionButtons[i] = new QuizUpButton(i, buttonPic);//instantiates the button
      questionButtons[i].addActionListener(this);//add the action listener
      gameFrame.add(questionButtons[i]);//add the button to the frame
    }
    
    gameFrame.add(scores1);
    scores1.setText(player1 + ": " + scorePlayer1);//add and display scores1 with the player1 name and their score
    gameFrame.add(spacer1);//add the spacer
    
    //add the reset button to the frame
    resetButton.addActionListener(this);
    resetButton.setEnabled(false);
    resetButton.setText("Reset");
    gameFrame.add(resetButton);
    
    gameFrame.add(numGames);//add the game keeper
    numGames.setText("Number of games played: " + numRoundsPlayed);
    gameFrame.add(scores2);
    scores2.setText(player2 + ": " + scorePlayer2);//add and display scores2 with the player2 name and their score
    
    questionSet = (int)(Math.random()*5) + 1;//generate a random number from 1-5
    
    //fill questions with the corresponding file
    input = new Scanner(new FileReader("questions" + questionSet + ".txt"));
    
    for (int i = 0; i < questions.length; i++)
      questions[i] = input.nextLine();
    
    input.close();
    
    //fill answers with the corresponding file
    input = new Scanner (new FileReader("answers" + questionSet + ".txt"));
    
    for (int i = 0; i < answers.length; i++)
      answers[i] = input.nextLine();
    
    input.close();
    
    sounds = new AudioClip[2];//add 2 audio clips to an array of audio clips
    try {
      URL fileLocation = new URL("file:Buzzer.wav");
      sounds[0] = JApplet.newAudioClip(fileLocation);//add the buzszer noise to the array
      fileLocation = new URL("file:ORASContest.wav");
      sounds[1] = JApplet.newAudioClip(fileLocation);//add the background music to the array
    } 
    catch (MalformedURLException e) { }//catch the exception if the file is not found and do nothing
    
    addKeyListener(this);//add a keybaord listener
    setSize(1000, 700);//set the size of the frame to 1000 x 700
    setVisible(true);//make the frame visible
    sounds[1].loop();//start looping the background music
  }//end constructor
  
  public void randomizeQuestions(String[] arrayOfQuestions, String[] arrayOfAnswers) throws FileNotFoundException {
    questionSet = (int)(Math.random()*5) + 1;//generate a random integer from 1-5
    
    //fill questions and answers with different Strings according to questionSet
    input = new Scanner(new FileReader("questions" + questionSet + ".txt"));
    
    for (int i = 0; i < arrayOfQuestions.length; i++)
      arrayOfQuestions[i] = input.nextLine();
    
    questions = arrayOfQuestions;
    
    input.close();
    
    input = new Scanner (new FileReader("answers" + questionSet + ".txt"));
    
    for (int i = 0; i < arrayOfAnswers.length; i++)
      arrayOfAnswers[i] = input.nextLine();
    
    answers = arrayOfAnswers;
    
    input.close();
  }//end randomizeQuestions
  
  //Resets everything to how it was in the beginning
  public void newGame () {
    try {
      randomizeQuestions(questions, answers);
    } catch (FileNotFoundException excp){
    }
    
    //ask for names again
    player1 = JOptionPane.showInputDialog("Enter Player 1's name");
    
    while (player1.length() == 0)
      player1 = JOptionPane.showInputDialog("Enter Player 1's name");
    
    player2 = JOptionPane.showInputDialog("Enter Player 2's name");
    
    while (player2.length() == 0)
      player2 = JOptionPane.showInputDialog("Enter Player 2's name");
    
    //set the scores and numQuestionAnswered to 0
    scorePlayer1 = 0;
    scorePlayer2 = 0;
    numQuestionsAnswered = 0;
    
    //enables all buttons
    for (int i = 0; i < questionButtons.length; i++)
      questionButtons[i].setEnabled(true);
    resetButton.setEnabled(false);
    
    //updates the scores text
    scores1.setText(player1 + ": " + scorePlayer1);
    scores2.setText(player2 + ": " + scorePlayer2);
    
    numRoundsPlayed ++;
    numGames.setText("Number of games played: " + numRoundsPlayed);
    
    sounds[1].loop();
  }//end newGame
  
  //Reaction to a button press
  public void actionPerformed(ActionEvent buttonClicked) {  
    //checks if players have clicked a question already or not
    if (!canBuzz) {
      //Use the newGame method if the reset button was clicked
      if (buttonClicked.getSource() == resetButton)
        newGame();
      
      //Display the question to the players and allow them to buzz their buzzer
      else if (buttonClicked.getSource()instanceof QuizUpButton) {
        buttonPressed = (QuizUpButton) buttonClicked.getSource();//saves the button that was clicked in a variable
        buttonNumber = buttonPressed.getTag();//gets the tag number
        canBuzz = true;
        JOptionPane.showMessageDialog(null, questions[buttonNumber]);//displays the question
      }
    }
    
    requestFocus();
  }//end actionPerformed
  
  //Checks what letter was typed on the keyboard(buzzer)
  public void keyTyped(KeyEvent buzzerPressed) {
    if (canBuzz) {
      //checks for player1's 'A' buzzer
      if (buzzerPressed.getKeyChar() == 'a'){
        sounds[0].loop();//loop the buzzer sounds
        JOptionPane.showMessageDialog(null, "Someone buzzed!");
        sounds[0].stop();//stop the buzzer sound
        canBuzz = false;
        playerOneFirst();
      }
      
      //checks for player2's 'L' buzzer
      else if (buzzerPressed.getKeyChar() == 'l'){
        sounds[0].loop();//loop the buzzer
        JOptionPane.showMessageDialog(null, "Someone buzzed!");
        sounds[0].stop();//stop the buzzer
        canBuzz = false;
        playerTwoFirst();
      }
    }//check who buzzed first
    
    //check if all questions were answered
    if (numQuestionsAnswered == NUM_QUESTIONS) {
      try {
        recordScores();
      }
      catch (FileNotFoundException e) {}
      
      //display a certain message depeding on the two scores
      if (scorePlayer1 > scorePlayer2) {
        JOptionPane.showMessageDialog(null, player1 + " beats " + player2 + "!!! WOOT WOOT!");
        JOptionPane.showMessageDialog(null, player2 + " step up your game!");
      }//end if
      
      else if (scorePlayer2 > scorePlayer1) {
        JOptionPane.showMessageDialog(null, player2 + " beats " + player1 + "!!! YAY!");
        JOptionPane.showMessageDialog(null, player1 + " step up your game!");
      }//end else if
      
      else
        JOptionPane.showMessageDialog(null, "It's a tie between " + player1 + " and " + player2);
      
      JOptionPane.showMessageDialog(null, "Check out the scores.txt file for results of the game!");
      askEnd = JOptionPane.showInputDialog("Type 'exit' to quit or type anything else to play again.");
      
      if (askEnd.equalsIgnoreCase("exit"))
        System.exit(0);
      
      resetButton.setEnabled(true);//enable the reset button
      sounds[1].stop();
    }//end outer if
  }//end keyTyped
  
  //Unused key events (used to satisfy the compiler
  public void keyPressed(KeyEvent pressed) {}//end keyPressed
  public void keyReleased(KeyEvent release) {}//end keyReleased
  
  public void playerOneFirst () {
    //ask the user for an answer
    userAnswer = JOptionPane.showInputDialog(player1 + "'s turn! Please answer the question.");
    
    while (userAnswer.length() == 0)
      userAnswer = JOptionPane.showInputDialog("You didn't type anything! Please type in your answer!");
    
    //if their answer is correct, display "Correct!!!", add 1 to the score, update the text, disable the questions and add 1 to numQuestionsAnswered
    if (userAnswer.equalsIgnoreCase(answers[buttonNumber])) {
      JOptionPane.showMessageDialog(null, "Correct!!!");
      scorePlayer1 ++;
      scores1.setText(player1 + ": " + scorePlayer1);
      buttonPressed.setEnabled(false);
      numQuestionsAnswered++;
    }//end outer if
    
    //otherwise ask the other player and check their answer as well
    else {
      JOptionPane.showMessageDialog(null, "Wrong answer!");
      userAnswer = JOptionPane.showInputDialog(player2 + "'s turn! Please answer the question.");
      
      while (userAnswer.length() == 0)
        userAnswer = JOptionPane.showInputDialog("You didn't type anything! Please type your answer!");
      
      if (userAnswer.equalsIgnoreCase(answers[buttonNumber])) {
        JOptionPane.showMessageDialog(null, "Correct!!!");
        scorePlayer2 ++;
        scores2.setText(player2 + ": " + scorePlayer2);
        buttonPressed.setEnabled(false);
        numQuestionsAnswered++;
      }//end inner if
      
      //if both are wrong, display the answer, disable the button and add 1 to numQuestionsAnswered
      else {
        JOptionPane.showMessageDialog(null, "Wrong! The correct answer is " + answers[buttonNumber]);
        buttonPressed.setEnabled(false);
        numQuestionsAnswered++;
      }//end inner else
    }//end outer else
  }//end playerOneFirst
  
  public void playerTwoFirst () {
    //ask the user for an answer
    userAnswer = JOptionPane.showInputDialog(player2 + "'s turn! Please answer the question.");
    
    while (userAnswer.length() == 0)
      userAnswer = JOptionPane.showInputDialog("You didn't type anything! Please type in your answer!");
    
    //if their answer is correct, display "Correct!!!", add 1 to the score, update the text, disable the questions and add 1 to numQuestionsAnswered
    if (userAnswer.equalsIgnoreCase(answers[buttonNumber])) {
      JOptionPane.showMessageDialog(null, "Correct!!!");
      scorePlayer2 ++;
      scores2.setText(player2 + ": " + scorePlayer2);
      buttonPressed.setEnabled(false);
      numQuestionsAnswered++;
    }//end outer if
    
    //ask the other player if the first one was wrong
    else {
      JOptionPane.showMessageDialog(null, "Wrong answer!");
      userAnswer = JOptionPane.showInputDialog(player1 + "'s turn! Please answer the question.");
      
      while (userAnswer.length() == 0)
        userAnswer = JOptionPane.showInputDialog("You didn't type anything! Please type your answer!");
      
      if (userAnswer.equalsIgnoreCase(answers[buttonNumber])) {
        JOptionPane.showMessageDialog(null, "Correct!!!");
        scorePlayer1 ++;
        scores1.setText(player1 + ": " + scorePlayer1);
        buttonPressed.setEnabled(false);
        numQuestionsAnswered++;
      }//end inner if
      
      //if both are wrong, display the answer, disable the button and add 1 to numQuestionsAnswered
      else {
        JOptionPane.showMessageDialog(null, "Wrong! The correct answer is " + answers[buttonNumber]);
        buttonPressed.setEnabled(false);
        numQuestionsAnswered++;
      }//end inner else
    }//end outer else
  }//end playerTwoFirst  
  
  //records the results of the latest game into the scores.txt file
  public void recordScores () throws FileNotFoundException {
    PrintWriter output = new PrintWriter ("scores.txt");

      output.println(player1 + ": " + scorePlayer1);
      output.println(player2 + ": " + scorePlayer2);
      output.println("In the last game: ");
      
      if (scorePlayer1 > scorePlayer2)
        output.println(player1 + " beat " + player2);
      else if (scorePlayer2 > scorePlayer1)
        output.println(player2 + " beat " + player1);
      else
        output.println("The game was a tie between " + player1 + " and " + player2);
    
     output.close();
  }//end recordScores
  
}//end class