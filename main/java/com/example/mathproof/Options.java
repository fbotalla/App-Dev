package com.example.mathproof;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

/**
 * The Options class is used to accept and store the options selected by the user.
 *
 * <p>
 *   This class keeps track of what the teacher or parent selects in the way of mathematical
 *   operations. Thus, the values of difficulty, subtract, add, multiply, divide, exponent, square
 *   root, and doNegativeNums are filled so the SimpleMath class will create the appropriate
 *   problems based on these selections. Further, the parent or student can also select whether the
 *   default mode of testing the student is run or if teach mode is run by selecting the teach
 *   option. This will tell the Display class to show the answer and hide the input keypad for each
 *   problem displayed. The student will be able to swipe left and right to view the problems if
 *   this value is set to true. The reward options signals the Display class to show a visual and
 *   audible "reward" to display upon completion of the problem set. Finally, the time and finished
 *   variables will be used to track whether or not the student finished the problem set with these
 *   other variables set, and if so, how much time in milliseconds it took.
 * </p>
 *
 * @author Fabrizio Botalla, Alberto Contreras, Jerry Lane, and Adam Tipton
 */
public class Options extends AppCompatActivity {

  private int difficulty; // Necessary for SimpleMath
  private boolean doNegativeNums; //Necessary to determine if negative numbers will be used
  private boolean teach;
  private boolean subtract;
  private boolean add;
  private boolean multiply;
  private boolean divide;
  private boolean exponent;
  private boolean squarert;
  private boolean simulation;
  private boolean reward;
  private long time;
  private boolean finished;

  /**
   * This is the onCreate method required for starting this activity.
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_options);
  }

  /**
   * This is the default constructor for the Options object.
   */
  public Options() {
  }

  /**
   * The setDifficulty method takes all the parent or teacher selections and bundles them.
   *
   * <p>
   *   Once this method is called by the teacher or parent pressing one of the difficulty buttons,
   *   it takes all of the variables in their current state and bundles them. Once the bundle has
   *   been created, it is passed to the Main activity to begin processing the data, calling
   *   the SimpleMath class for problem creation based upon these variables, and starting the
   *   Display processing.
   * </p>
   * @param view
   */
  public void setDifficulty(View view) {
    switch (view.getId()) {
      case R.id.Difficulty1:
        this.difficulty = 1;
        break;
      case R.id.Difficulty2:
        this.difficulty = 2;
        break;
      case R.id.Difficulty3:
        this.difficulty = 3;
        break;
      default:
        this.difficulty = 1;
    }

    //set variables appropriate to switch positions
    setAllConfigs();

    //logic check for at least one math operation, if good, create intent for Main, start activity
    if (add || subtract || multiply || divide || exponent || squarert) {

      //bundle all option data
      Bundle data = new Bundle();
      data.putBoolean("reward", this.reward);
      data.putBoolean("sim", this.simulation);
      data.putBoolean("add", this.add);
      data.putBoolean("sub", this.subtract);
      data.putBoolean("mul", this.multiply);
      data.putBoolean("div", this.divide);
      data.putBoolean("exp", this.exponent);
      data.putBoolean("sqrt", this.squarert);
      data.putBoolean("reward", this.reward);
      data.putBoolean("teach", this.teach);
      data.putBoolean("neg", this.doNegativeNums);
      data.putInt("diff", this.difficulty);
      data.putLong("time", this.time);
      data.putBoolean("finished", this.finished);
      data.putBoolean("quit", false);

      //send bundled data to MainActivity and start it
      Intent intent = new Intent(this, MainActivity.class);
      intent.putExtras(data);
      startActivity(intent);
    }
  }

  /**
   * All of the following are the getters and setters for Option object variables.
   *
   * <p>
   *   All of the following are getters and setters used by the Options class variables. These
   *   allow storage and processing of the options object variables. A couple, setAllConfigs and
   *   doSimOperations, set multiple values at one time, based upon the options the teacher or
   *   parent has selected onscreen.
   * </p>
   *
   * @return
   */
  public int getDifficulty() {
    return difficulty;
  }

  //This functions sets all the TRUE/FALSE variables connected to the switches
  public void setAllConfigs() {
    //Switch variables
    Switch addition;
    Switch subtraction;
    Switch multiplication;
    Switch division;
    Switch negativeNums;
    Switch exponentMode;
    Switch squarertMode;
    Switch teachMode;
    Switch doSim;
    Switch rewardCheck;

    //addition
    addition = findViewById(R.id.Add);
    if (addition.isChecked()) {
      add = true;
    } else {
      add = false;
    }
    //subtraction
    subtraction = findViewById(R.id.Subtract);
    if (subtraction.isChecked()) {
      subtract = true;
    } else {
      subtract = false;
    }

    //multiplication
    multiplication = findViewById(R.id.Multiply);
    if (multiplication.isChecked()) {
      multiply = true;
    } else {
      multiply = false;
    }

    //division
    division = findViewById(R.id.Divide);
    if (division.isChecked()) {
      divide = true;
    } else {
      divide = false;
    }

    //negative numbers
    negativeNums = findViewById(R.id.Negative_Numbers);
    if (negativeNums.isChecked()) {
      doNegativeNums = true;
    } else {
      doNegativeNums = false;
    }

    //exponent
    exponentMode = findViewById(R.id.Exponent);
    if (exponentMode.isChecked()) {
      exponent = true;
    } else {
      exponent = false;
    }

    //square root
    squarertMode = findViewById(R.id.Square_Root);
    if (squarertMode.isChecked()) {
      squarert = true;
    } else {
      squarert = false;
    }

    //Simulation turns to truth all the booleans by calling doSimOperations()
    doSim = findViewById(R.id.DoSim);
    if (doSim.isChecked()) {
      doSimOperations();
    } else {
      simulation = false;
    }

    //teach mode
    teachMode = findViewById(R.id.Teach);
    if (teachMode.isChecked()) {
      teach = true;
    } else {
      teach = false;
    }

    //sound and change to true rewards as well.
    rewardCheck = findViewById(R.id.Reward);
    if (rewardCheck.isChecked()) {
      reward = true;
    } else {
      reward = false;
    }

    //set timer to zero
    time = 0;

    //set finished flag to false
    finished = false;
  }

  public void setDifficulty(int difficulty) {
    if (difficulty < 1)
      difficulty = 1;
    if (difficulty > 3)
      difficulty = 3;
    this.difficulty = difficulty;
  }

  public boolean getSubtract() {
    return subtract;
  }

  public void setSubtract(boolean subtract) {
    this.subtract = subtract;
  }

  public boolean getDoNegativeNums() {
    return doNegativeNums;
  }

  public void setDoNegativeNums(boolean doNegativeNums) {
    this.doNegativeNums = doNegativeNums;
  }

  public boolean getTeach() {
    return teach;
  }

  public void setTeach(boolean teach) {
    this.teach = teach;
  }

  public boolean getAdd() {
    return add;
  }

  public void setAdd(boolean add) {
    this.add = add;
  }

  public boolean getMultiply() {
    return multiply;
  }

  public void setMultiply(boolean multiply) {
    this.multiply = multiply;
  }

  public boolean getDivide() {
    return divide;
  }

  public void setDivide(boolean divide) {
    this.divide = divide;
  }

  public boolean getExponent() {
    return exponent;
  }

  public void setExponent(boolean exponent) {
    this.exponent = exponent;
  }

  public boolean getSquarert() {
    return squarert;
  }

  public void setSquarert(boolean squarert) {
    this.squarert = squarert;
  }

  public void setSimulation(boolean simulation) {
    this.simulation = simulation;
  }

  public boolean getSimulation() {
    return simulation;
  }

  public boolean getReward() {
    return reward;
  }

  public void setReward(boolean reward) {
    this.reward = reward;
  }

  public void doSimOperations() {
    doNegativeNums = true;
    teach = true;
    subtract = true;
    add = true;
    multiply = true;
    divide = true;
    exponent = true;
    squarert = true;
    simulation = true;
    reward = true;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public boolean getFinished() {
    return finished;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }
}

