package com.example.mathproof;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

import static android.os.Process.killProcess;
import static android.os.Process.myPid;
import static java.lang.Math.abs;


/**
 * The Display class handles the display of problem set data and user input.
 *
 * <p>
 * The Display class accepts bundled data from the main activity consisting of particular
 * boolean flags such as teach, simulation, and reward, as well as serialized data run
 * through the Transfer class containing a list of all the problem set arrays generated in the
 * SimpleMath class. It also displays a keypad when the teach flag is false, allowing user
 * input to check against the answer contained in the problem array. If the teach flag is
 * set, then Display allows the user to browse the fully formed equations by swiping the
 * screen from left to right to go back or from right to left to move forward through the
 * list of problem arrays.
 * </p>
 *
 * @author Fabrizio Botalla, Alberto Contreras, Jerry Lane, and Adam Tipton
 */
/*
 * Current variables used include:
 * @param problems is a list of integer arrays, each array holding data for one problem
 * @param input is an integer array holding the data for a single problem
 * @param teach is a boolean flag used to show problems with answers for students to examine
 * @param reward is a boolean flag used to determine whether or not a visual/audio reward is used
 * @param simulation is a boolean flag used to determine whether or not a "demo" mode is run
 * @param r is a random number seed used to access problems in a random order
 * @param random is the random number generated using r
 * @param displayValue is a string containing the string representation of the problem array
 * @param x is a variable used to set the initial x coordinate of the screen for swiping
 * @param inputValue is an int used to calculate the user input answer
 * @param sign is a boolean flag set when the user prepares to enter a negative number
 * @param index is an integer used during the teach sessions to track problem position in the array
 * @param gifImageView is a View object allowing a gif to be shown during the reward method
 * */
public class Display extends AppCompatActivity {

  //set global variables
  private List<Integer[]> problems = new ArrayList<Integer[]>();
  private Integer[] input = {0, 0, 0, 0};
  private Options options = new Options();
  private boolean teach = false;
  private boolean reward = false;
  private boolean simulation = false;
  private Random r = new Random();
  private int random = 0;
  private float x = 0;
  private float y = 0;
  private String displayValue = "";
  private int inputValue = 0;
  private boolean sign = false;
  private int index = 1;
  private GifImageView gifImageView;
  private long start = System.currentTimeMillis();
  private MediaPlayer ring;
  private boolean quit = false;

  /**
   * This is the thread handler for the Display class.
   *
   * <p>
   * Currently, the thread handler is only handling a background thread for the simulation method,
   * but that will likely change as the LoadSaver class is fully implemented.
   * </p>
   */
  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      showDemo();
    }
  };


  /**
   * This is the initial method for the Display class.
   *
   * <p>
   * This method collects the bundled data from the main activity and restores it to values
   * used throughout this class. If the teach flag is set, the keypad is hidden. Finally, the
   * method runProblem is called to begin display and user input of a given problem array.
   * </p>
   *
   * @throws NullPointerException
   */
  /*
   * Current variables used include:
   * @param data Bundled information transferred from main activity
   * @param teach boolean flag used to determine how much of the problem is shown to the user
   * @param reward boolean flag used to determine whether the reward function is run or not
   * @param simulation boolean flag used to determine whether normal or "demo" mode is run
   * @param myArray is a list of integer arrays serialized through the Transfer class
   * @param problems is the populated list of integer arrays holding problem data
   * @param keypad is a grid layout on the View containing all the user input buttons
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display);

    //get bundle data from main activity by using the Transfer class as a wrapper
    Bundle data = getIntent().getExtras();
    teach = data.getBoolean("teach");
    options.setTeach(teach);
    reward = data.getBoolean("reward");
    options.setReward(reward);
    simulation = data.getBoolean("sim");
    options.setSimulation(simulation);

    boolean add = data.getBoolean("add");
    options.setAdd(add);
    boolean subtract = data.getBoolean("sub");
    options.setSubtract(subtract);
    boolean multiply = data.getBoolean("mul");
    options.setMultiply(multiply);
    boolean divide = data.getBoolean("div");
    options.setDivide(divide);
    boolean exponent = data.getBoolean("exp");
    options.setExponent(exponent);
    boolean squareRoot = data.getBoolean("sqrt");
    options.setSquarert(squareRoot);
    boolean negativeNumbers = data.getBoolean("neg");
    options.setDoNegativeNums(negativeNumbers);
    int difficulty = data.getInt("diff");
    options.setDifficulty(difficulty);
    long time = data.getLong("time");
    if (time != 0)
      start = System.currentTimeMillis() - time;
    options.setTime(time);
    boolean finished = data.getBoolean("finished");
    options.setFinished(finished);

    try {

      //attempt to get problems array list and populate it with math problems
      Transfer myArray = (Transfer) data.getSerializable("problems");
      problems = myArray.getArray();
    } catch (NullPointerException error) {

      //if problems fires a npe, kill the application - considered creating it as a wtf log
      Log.e("Display", "This problem occurred: " + error);
      killProcess(myPid());
      System.exit(1);
    }

    //attempt to remove intent data
    data.clear();
    getIntent().removeExtra("problems");
    getIntent().removeExtra("sim");
    getIntent().removeExtra("neg");
    getIntent().removeExtra("add");
    getIntent().removeExtra("sub");
    getIntent().removeExtra("mul");
    getIntent().removeExtra("div");
    getIntent().removeExtra("exp");
    getIntent().removeExtra("div");
    getIntent().removeExtra("diff");
    getIntent().removeExtra("sqrt");
    getIntent().removeExtra("reward");
    getIntent().removeExtra("teach");

    //if simulation flag is set, reduce problem size and set appropriate flags
    if (simulation) {

      //set teach flag to turn off keypad and show answer, set reward to show finish
      teach = true;
      reward = true;

      //reduce problem set size to 11
      while (problems.size() > 11) {
        random = r.nextInt(problems.size());
        problems.remove(random);
      }
    }

    //if teach mode enabled, hide keypad
    if (teach) {
      GridLayout keypad = findViewById(R.id.gridLayout);
      keypad.setVisibility(View.INVISIBLE);
    }

    //transfer control to runProblems
    runProblems();
  }


  /**
   * This method is called when the problem set list is empty.
   *
   * <p>
   * Once the list of problems has been successfully answered, and if the reward boolean flag
   * has been set, the reward method will display a visual and audio "reward" for the student.
   * </p>
   */
  /*
   * Current variables used include:
   * @param reward is a boolean flag signifying whether or not the reward is to be run
   * @param gifImageView is a View object holding the gif, or visual portion of the user's reward
   * */
  private void reward() {

    //test display for successful problem set solution
    String display = "Finished";
    TextView displayView = findViewById(R.id.display);
    displayView.setText(display);

    //if reward option is set, show reward
    if (reward) {

      //show reward gif and play sound
      gifImageView = findViewById(R.id.gifImageView);
      gifImageView.setVisibility(View.VISIBLE);
      ring = MediaPlayer.create(getApplicationContext(), R.raw.fireworks);
      ring.start();

      //set timer to show and play rewards for four seconds then shut off and send to goodbye()
      new CountDownTimer(4000, 1) {

        @Override
        public void onFinish() {
          gifImageView.setVisibility(View.INVISIBLE);
          ring.stop();
          goodbye(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
      }.start();
    }

    //if reward is not selected, but student finishes problem set, send to goodbye method as true
    else {

      //set timer to one second and then call the goodbye method to pass data to LoadSaver class
      new CountDownTimer(1000, 1) {

        @Override
        public void onFinish() {
          goodbye(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
      }.start();
    }
  }


  /**
   * This is the method which handles the different possible option-selected scenarios
   *
   * <p>
   * The runProblems method looks at the simulation and teach boolean flags and determines
   * whether the Display runs in a "demonstration" mode, where the problems are randomly
   * displayed, a teach mode where the student can browse whole equations, including the answers,
   * by swiping forward and back through the list of problem arrays, or in an implied "test"
   * mode where only the problem is given, not the solution. If all problems are successfully
   * answered, the reward method is called.
   * </p>
   */
  /*
   * Current variables used include:
   * @param simulation is a boolean flag, when set to true, will cause Display to run a "demo"
   * @param random is a integer which holds a number to randomly access the problems array list
   * @param problems is the list of integer arrays holding the problem set data
   * @param teach is a boolean flag which determines whether a full or partial equation is shown
   * @param input is a four element integer array holding a given problem's data
   *
   * */
  private void runProblems() {

    //if simulation, run automatically, else run normally
    if (simulation) {

      //show initial simulation message
      String display = "Loading...";
      TextView displayView = findViewById(R.id.display);
      displayView.setText(display);

      //create runnable for demo to run on background thread
      Runnable demo = new Runnable() {
        @Override
        public void run() {

          while (problems.size() > 0) {

            //set up five second delay for problem display time
            long delay = System.currentTimeMillis() + 5000;
            while (System.currentTimeMillis() < delay) {
              try {
                wait(delay - System.currentTimeMillis());
              } catch (Exception ex) {
              }
            }

            //after delay has passed, and display is set, trigger handler to call next demo item
            handler.sendEmptyMessage(0);
          }
        }
      };

      //run runnable demo on thread
      Thread runDemo = new Thread(demo);
      runDemo.start();
    }

    //run problems in test mode, (student must input answers)
    else if (!teach) {

      //set input for display of random problem
      random = r.nextInt(problems.size());
      input = problems.get(random);

      //call the displayProblems method
      displayProblems();
    }

    //run in teach mode where student can swipe full equations back and forth to see answers
    else {
      input = problems.get(index);
      displayProblems();
    }

    //when all problems have been answered, fire reward method
    if (problems.size() == 0)
      reward();
  }

  /**
   * The displayProblem method creates and displays the problem string from the problems array
   * data.
   *
   * <p>
   * The displayProblem method uses the input, problems list of arrays, and the teach global
   * values to create the problem in a displayable condition. The internal composition of the
   * problem arrays tell the method which mathematical operation is being performed (the second
   * element of the array indicates this with 1 being used for addition, 2 for subtraction, 3 for
   * multiplication, 4 for division, 5 for exponent, and 6 for square roots). This is translated
   * into the appropriate mathematical symbol for the type operation as the string is constructed.
   * The exception is the exponent which requires a special formatting by SpannableStringBuilder.
   * Once the string is constructed, it is displayed into the textView object display directly
   * and stored in the global variable displayValue to be used in other parts of the class.
   * </p>
   */
  /*
   * Current variables used include:
   * @param input - a global variable used to hold the current problem array over all methods
   * @param problems - this is the list of problem arrays accessed globally
   * @param teach - a boolean flag used to determine whether or not to add the answer to the display
   */
  private void displayProblems() {

    //declare variables for displaying string
    String display = "";
    String operator = "";
    TextView displayView = findViewById(R.id.display);

    //set operator for creating display string and set colors for different operations
    switch (input[1]) {
      case 1:
        operator = " + ";
        displayView.setTextColor(Color.RED);
        break;
      case 2:
        operator = " - ";
        displayView.setTextColor(Color.BLUE);
        break;
      case 3:
        operator = " × ";
        displayView.setTextColor(Color.rgb(120, 190, 0));
        break;
      case 4:
        operator = " ÷ ";
        displayView.setTextColor(Color.rgb(0, 120, 190));
        break;

      //in cases 5 & 6 the operators will be altered later, but color will remain throughout
      case 5:
        operator = "^";
        displayView.setTextColor(Color.MAGENTA);
        break;
      default:
        operator = "√";
        displayView.setTextColor(Color.BLACK);
        break;
    }

    //      assemble problem strings for display, allow for teach or test option          //

    //declare last variable for possible answer display
    String last = " = ";


    //assemble superscript string if exponent
    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {

      //if teach flag is set, show answer
      if (teach)
        last += input[3].toString();

      display = input[0].toString() + input[2].toString() + last;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {

      //if teach option was selected, show answer
      if (teach) {
        last += input[3].toString();
      }

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      display = input[0].toString() + operator + input[2].toString() + " = ";

      //if teach option was selected, show answer
      if (teach) {
        display += input[3].toString();
      }

      //access the display object and put the formatted problem data into it
      displayView.setText(display);
    }


    displayValue = display;
  }

  /**
   * This onTouchEvent method watches for swipes onscreen.
   *
   * <p>
   * This method is utilized only when the teach flag is true. The answers are displayed, so no
   * input by the student is necessary. Thus, there is no need for keypad display. This method
   * allows the student to browse the problems forward and back by swiping left and right.
   * </p>
   */
  /*
   * Current variables used include:
   * @param event - the touch event
   * @param teach - this is used to determine whether or not swiping is used in the activity
   * @param x - a global variable used to determine whether the student is swiping left or right
   * @param input - the global variable holding the current problem array data
   * @param index - the global variable tracking which problems list array is being shown
   * @param problems - the global list of problem arrays being browsed by the student's swiping
   * @return
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {

    //if teach is not set, ignore swiping
    if (teach) {

      //declare swipe variables
      float goingUp = 0;
      float distance = 0;
      float trigger = 100;
      float x2 = 0;
      float y2 = 0;

      //set x position of point up and point down
      if (event.getAction() == MotionEvent.ACTION_UP) {
        y2 = event.getY();
        x2 = event.getX();
        distance = x2 - x;
        goingUp = y2 - y;
      }

      //x must be global or it would remain zero forever
      else if (event.getAction() == MotionEvent.ACTION_DOWN) {
        x = event.getX();
        y = event.getY();
      }

      //once distance between points is determined, calculate if it is left or right swipe
      if (abs(distance) > trigger || abs(goingUp) > trigger) {

        //swiping left to see next problem
        if (x2 < x) {
          if (index < (problems.size() - 1)) {
            index++;
            input = problems.get(index);
            displayProblems();
          }
        }

        //swiping right to see previous problem
        else if (x2 >= x){
          if (index > 1) {
            index--;
            input = problems.get(index);
            displayProblems();
          }
        }

        //if student swiped up or down, call goodbye method to end teach mode
        else {
          goodbye(false);
        }
      }
    }
    getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
    return super.onTouchEvent(event);
  }

  /**
   * This method is the button handler for keypad 0.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   */
  public void getZero(View view) {

    //assign the value to this button
    int value = 0;

    //calculate new inputValue
    calculateValue(value);

    //create basic problem string
    String display = displayValue + inputValue;

    //declare name of activity display object
    TextView displayView = findViewById(R.id.display);

    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {
      display = input[0].toString() + input[2].toString() + " = " + inputValue;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      displayView.setText(display);
    }
  }

  /**
   * This method is the button handler for keypad 1.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   */
  public void getOne(View view) {

    //assign the value to this button
    int value = 1;

    //calculate new inputValue
    calculateValue(value);

    //create basic problem string
    String display = displayValue + inputValue;

    //declare name of activity display object
    TextView displayView = findViewById(R.id.display);

    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {
      display = input[0].toString() + input[2].toString() + " = " + inputValue;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      displayView.setText(display);
    }
  }

  /**
   * This method is the button handler for keypad 2.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   */
  public void getTwo(View view) {

    //assign the value to this button
    int value = 2;

    //calculate new inputValue
    calculateValue(value);

    //create basic problem string
    String display = displayValue + inputValue;

    //declare name of activity display object
    TextView displayView = findViewById(R.id.display);

    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {
      display = input[0].toString() + input[2].toString() + " = " + inputValue;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      displayView.setText(display);
    }
  }

  /**
   * This method is the button handler for keypad 3.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   */
  public void getThree(View view) {

    //assign the value to this button
    int value = 3;

    //calculate new inputValue
    calculateValue(value);

    //create basic problem string
    String display = displayValue + inputValue;

    //declare name of activity display object
    TextView displayView = findViewById(R.id.display);

    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {
      display = input[0].toString() + input[2].toString() + " = " + inputValue;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      displayView.setText(display);
    }
  }

  /**
   * This method is the button handler for keypad 4.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   */
  public void getFour(View view) {

    //assign the value to this button
    int value = 4;

    //calculate new inputValue
    calculateValue(value);

    //create basic problem string
    String display = displayValue + inputValue;

    //declare name of activity display object
    TextView displayView = findViewById(R.id.display);

    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {
      display = input[0].toString() + input[2].toString() + " = " + inputValue;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      displayView.setText(display);
    }
  }

  /**
   * This method is the button handler for keypad 5.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   */
  public void getFive(View view) {

    //assign the value to this button
    int value = 5;

    //calculate new inputValue
    calculateValue(value);

    //create basic problem string
    String display = displayValue + inputValue;

    //declare name of activity display object
    TextView displayView = findViewById(R.id.display);

    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {
      display = input[0].toString() + input[2].toString() + " = " + inputValue;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      displayView.setText(display);
    }
  }

  /**
   * This method is the button handler for keypad 6.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   */
  public void getSix(View view) {

    //assign the value to this button
    int value = 6;

    //calculate new inputValue
    calculateValue(value);

    //create basic problem string
    String display = displayValue + inputValue;

    //declare name of activity display object
    TextView displayView = findViewById(R.id.display);

    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {
      display = input[0].toString() + input[2].toString() + " = " + inputValue;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      displayView.setText(display);
    }
  }

  /**
   * This method is the button handler for keypad 7.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   */
  public void getSeven(View view) {

    //assign the value to this button
    int value = 7;

    //calculate new inputValue
    calculateValue(value);

    //create basic problem string
    String display = displayValue + inputValue;

    //declare name of activity display object
    TextView displayView = findViewById(R.id.display);

    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {
      display = input[0].toString() + input[2].toString() + " = " + inputValue;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      displayView.setText(display);
    }
  }

  /**
   * This method is the button handler for keypad 8.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   */
  public void getEight(View view) {

    //assign the value to this button
    int value = 8;

    //calculate new inputValue
    calculateValue(value);

    //create basic problem string
    String display = displayValue + inputValue;

    //declare name of activity display object
    TextView displayView = findViewById(R.id.display);

    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {
      display = input[0].toString() + input[2].toString() + " = " + inputValue;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      displayView.setText(display);
    }
  }

  /**
   * This method is the button handler for keypad 9.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   */
  public void getNine(View view) {

    //assign the value to this button
    int value = 9;

    //calculate new inputValue
    calculateValue(value);

    //create basic problem string
    String display = displayValue + inputValue;

    //declare name of activity display object
    TextView displayView = findViewById(R.id.display);

    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {
      display = input[0].toString() + input[2].toString() + " = " + inputValue;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      displayView.setText(display);
    }
  }

  /**
   * This method is the button handler for keypad sign.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   */
  public void getSign(View view) {

    //declare display string
    String display = "";

    //if user is inputting a negative number, show it; if not, show number as positive
    if (sign) {
      sign = false;
      inputValue = abs(inputValue);
    } else {
      sign = true;
      inputValue *= -1;
    }

    //if sign flag is set to true, and inputValue is zero, show the negative sign
    if (sign && inputValue == 0)
      display = displayValue + "-";

      //if sign is false and inputValue equals zero, blank answer area
    else if (!sign && inputValue == 0)
      display = displayValue;

      //otherwise, show actual inputValue
    else
      display = displayValue + inputValue;

    //declare textView object to be addressed with display string
    TextView displayView = findViewById(R.id.display);

    //if the problem is an exponent, use spannable string builder to create a superscript exponent
    if (input[1] == 5) {
      display = input[0].toString() + input[2].toString() + " = " + inputValue;
      int start = 1;
      int end = 2;
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //now that it is known where the exponent number is at in string, build spannable showing it
      SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
      exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the exponent-formatted problem data into it
      displayView.setText(exponentDisplay);
    }

    //if array is a square root problem, set up new spannable string to display properly
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      //build spannable string to display square root with symbol appropriately
      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    }

    //all other problem strings can be displayed without alteration
    else {
      displayView.setText(display);
    }
  }

  /**
   * This method is the button handler for keypad Enter.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param sign - used to keep track whether user is using positive or negative number
   * @param input - global variable holding current problem array data
   * @param problems - global variable holding entire list of problem arrays
   * @param random - global variable holding the current list index
   */
  public void getEnter(View view) {

    //check to make sure the problems list has problem arrays in it and get one at random
    if (problems.size() > 0) {
      input = problems.get(random);

      //set the regular problem display and name the textView object to be used
      String display = displayValue + inputValue;
      TextView displayView = findViewById(R.id.display);

      //if the problem is an exponent, use spannable string builder to create a superscript exponent
      if (input[1] == 5) {
        display = input[0].toString() + input[2].toString() + " = " + inputValue;
        int start = 1;
        int end = 2;
        if (input[0] > 9) {
          start = 2;
          end = 3;
        }

        //now that it is known where the exponent number is at in string, build spannable showing it
        SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
        exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //access the display object and put the exponent-formatted problem data into it
        displayView.setText(exponentDisplay);
      }

      //if array is a square root problem, set up new spannable string to display properly
      else if (input[1] == 6) {
        String last = " = " + inputValue;

        //build spannable string to display square root with symbol appropriately
        SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
        sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        displayView.setText(sqrt);
      }

      //all other problem strings can be displayed without alteration
      else {
        displayView.setText(display);
      }

      //if input answer is correct, remove the problem from the list and toast correct
      if (input[3] == inputValue) {
        problems.remove(random);

        //set toast parameters - CORRECT! - with green background and black text
        Toast right = Toast.makeText(getApplicationContext(),
          "You Are Correct!", Toast.LENGTH_SHORT);
        View correctView = right.getView();
        correctView.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
        TextView correctToast = correctView.findViewById(android.R.id.message);
        correctToast.setTextColor(Color.BLACK);
        correctToast.setBackgroundColor(Color.GREEN);

        //show correct answer toast message
        right.show();
      }

      //show incorrect toast message
      else {

        //set toast parameters - INCORRECT - with red background and white text
        Toast wrong = Toast.makeText(getApplicationContext(),
          "Incorrect Answer", Toast.LENGTH_SHORT);
        View incorrectView = wrong.getView();
        incorrectView.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        TextView incorrectToast = incorrectView.findViewById(android.R.id.message);
        incorrectToast.setTextColor(Color.WHITE);
        incorrectToast.setBackgroundColor(Color.RED);

        //tell student he or she is incorrect
        wrong.show();
      }

      //if there are more problem arrays in the list, choose one at random and create new string
      if (problems.size() > 0) {
        random = r.nextInt(problems.size());
        input = problems.get(random);
        displayProblems();
      }

      //otherwise, let the student know he or she has finished the problem set
      else {
        reward();
      }

      //zero out the inputValue
      inputValue = 0;

      //quit getting new problems when screen is rotated
      getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
    }
  }

  /**
   * This method is the button handler for keypad Quit.
   *
   * @param view
   */
  public void getQuit(View view) {

    //display goodbye message
    String display = "Goodbye!";
    TextView displayView = findViewById(R.id.display);
    displayView.setText(display);

    //set quit flag
    quit = true;

    //set timer to one second and then call the goodbye method to pass data to LoadSaver class
    new CountDownTimer(1000, 1) {

      @Override
      public void onFinish() {
        goodbye(false);
      }

      @Override
      public void onTick(long millisUntilFinished) {
      }
    }.start();
  }

  /**
   * This method is the button handler for keypad Back.
   *
   * @param view - used by activity to show which button is pressed by student
   */
  /*
   * Current variables used include:
   * @param displayValue - global value string value used in constructing display
   * @param inputValue - global variable holding the current user input value
   * @param input - global variable holding current problem array data
   */
  public void getBack(View view) {

    //if inputValue is less than 10, make it zero, otherwise divide it by 10
    if (abs(inputValue) != 0 && abs(inputValue) < 10)
      inputValue = 0;
    else
      inputValue /= 10;

    //declare string to be used for building output display
    String display;

    //if inputValue is zero, display shows only displayValue problem, otherwise show new inputValue
    if (inputValue == 0) {
      display = displayValue;
    } else {
      display = displayValue + inputValue;
    }

    //show sign when sign is true and inputValue is zero
    if (sign && inputValue == 0) {
      display = displayValue + "-";
    }

    //declare name of activity display element
    TextView displayView = findViewById(R.id.display);

    //if input is a exponent array, use spannable string builder to create display string
    if (input[1] == 5) {
      int start = 1;
      int end = 2;

      //if number to be raised exponentially is over 9, shift superscript character position
      if (input[0] > 9) {
        start = 2;
        end = 3;
      }

      //create and build exponent string
      SpannableStringBuilder exponent = new SpannableStringBuilder(display);
      exponent.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      exponent.setSpan(new RelativeSizeSpan(0.75f), start, end,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      //access the display object and put the formatted problem data into it
      displayView.setText(exponent);
    }

    //if input array is a square root problem, use a spannable string to create the display string
    else if (input[1] == 6) {
      String last = " = " + inputValue;

      if (teach)
        last += input[2].toString();

      SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
      sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      displayView.setText(sqrt);
    } else {
      displayView.setText(display);
    }
  }

  /**
   * This method is a placeholder, doing nothing, for an empty button, which itself is a
   * placeholder.
   *
   * @param view
   */
  public void getSave(View view) {
    //can be used for future functionality if wanted
  }

  /**
   * This method handles the calculation of entered buttons to input value.
   *
   * @param valueIn - value passed by keypad buttons
   */
  /*
   * Current variables used include:
   * @return  inputValue - global variable holding the current user input value, returned to handler
   * @param sign - used to keep track whether user is inputting positive or negative number
   */
  private int calculateValue(int valueIn) {
    if (inputValue <= 1000) {
      if (inputValue == 0)
        inputValue = valueIn;
      else {
        inputValue *= 10;
        inputValue += valueIn;
      }
      if (sign && inputValue >= 0)
        inputValue *= -1;
      if (!sign && inputValue < 0)
        inputValue *= -1;
      return inputValue;
    } else {
      return inputValue;
    }
  }

  /**
   * This method is used exclusively to give a short demonstration of Math{Proof}.
   */
  /*
   * Current variables used include:
   * @param problems - list holding all current problem arrays
   * @param random - used to track current problem array index in problems list
   * @param input - used to hold current problem array data
   * @param displayValue - string variable holding current problem data in display form
   */
  private void showDemo() {
    if (problems.size() > 0) {
      //set input for display of random problem
      random = r.nextInt(problems.size());
      input = problems.get(random);

      //build regular equation string for add, subtract, multiply or divide problems
      displayProblems();
      String display = displayValue;

      //declare name of textView element used to display problem string
      TextView displayView = findViewById(R.id.display);

      //if input is a exponent array, use spannable string builder to create display string
      if (input[1] == 5) {
        int start = 1;
        int end = 2;

        //if number to be raised exponentially is over 9, shift superscript character position
        if (input[0] > 9) {
          start = 2;
          end = 3;
        }

        //create and build exponent string
        SpannableStringBuilder exponentDisplay = new SpannableStringBuilder(display);
        exponentDisplay.setSpan(new SuperscriptSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        exponentDisplay.setSpan(new RelativeSizeSpan(0.75f), start, end,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //access the display object and put the formatted problem data into it
        displayView.setText(exponentDisplay);
      }

      //if input array is a square root problem, use a spannable string to create the display string
      else if (input[1] == 6) {
        String last = " = ";

        if (teach)
          last += input[3].toString();

        SpannableString sqrt = new SpannableString("\u221A" + input[0].toString() + last);
        sqrt.setSpan(new OverlineSpan(), 1, input[0].toString().length() + 1,
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        displayView.setText(sqrt);
      }

      //show the display string for addition, subtraction, multiplication, or division problems
      else {
        displayView.setText(display);
      }

      //remove displayed problem from list of problem arrays
      problems.remove(random);
    } else
      reward();
  }

  /**
   * The goodbye method will bundle all data and transfer control to LoadSaver class.
   */
  /*
   * Current variables used include:
   * @param problems - holds the entire list of problem arrays
   * @param random - current random problem array list index
   */
  private void goodbye(boolean finished) {
    Log.d("Reached Options goodbye", "-------------------------------------> "+finished);

    //create bundle object
    Bundle data = new Bundle();

    //use the wrapper class Transfer to pass bundled math data and bundle options variables
    data.putSerializable("problems", new Transfer(problems));
    data.putBoolean("teach", options.getTeach());
    data.putBoolean("reward", options.getReward());
    data.putBoolean("sim", options.getSimulation());
    data.putBoolean("add", options.getAdd());
    data.putBoolean("sub", options.getSubtract());
    data.putBoolean("mul", options.getMultiply());
    data.putBoolean("div", options.getDivide());
    data.putBoolean("exp", options.getExponent());
    data.putBoolean("sqrt", options.getSquarert());
    data.putBoolean("neg", options.getDoNegativeNums());
    data.putInt("diff", options.getDifficulty());
    data.putLong("time", getTime());
    options.setTime(getTime());
    data.putBoolean("finished", finished);
    options.setFinished(finished);
    data.putBoolean("quit", quit);

    Log.d("Passing to LoadSaver", "----------------------------------------> "+finished);

    //prepare intent for loadsaver activity
    Intent intent = new Intent(this, LoadSaver.class);

    //pass data to Display activity
    intent.putExtras(data);
    startActivity(intent);
  }

  /**
   * The getTime method returns the amount of time the student has accrued until goodbye is
   * called.
   *
   * @return - returns the elapsed time in milliseconds from start to end and returns it in a
   * long.
   */
  private long getTime() {
    return System.currentTimeMillis() - start;
  }
}