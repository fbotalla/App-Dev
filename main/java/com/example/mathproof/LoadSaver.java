package com.example.mathproof;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * LoadSaver() activity makes it possible to load and save the state of the app.
 *
 * @version 2.0
 * @authors Adam Tipton, Jerry Lane, Fabrizio Botalla, and Alberto Contreras
 * @since 2019-12-4
 */
public class LoadSaver extends AppCompatActivity {

  //high score table variables and objects
  private TextView operators;
  private TextView best_difficulty;
  private TextView highscore;
  private TextView best_time;
  private int latest_score;
  private int current_score;
  private String oper;


  //Options Variables
  private int difficulty; // Necessary for SimpleMath
  private boolean negativeNumbers; //Necessary to determine if negative numbers will be used
  private boolean teach;
  private boolean subtract;
  private boolean add;
  private boolean multiply;
  private boolean divide;
  private boolean exponent;
  private boolean squareRoot;
  private boolean simulation;
  private boolean reward;

  //flags for problem set finished and time it took
  private boolean finished;
  private long time;

  //problem set list
  private List<Integer[]> problems = new ArrayList<Integer[]>();

  //unused except to hold all the variables above - for use in future development
  private Options options = new Options();

  /**
   * This is the onCreate method, called when the activity starts.
   *
   * <p>
   * It will collect the data bundled for it, populate the variables with it, and then display the
   * high score table.
   * </p>
   *
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_load_saver);

    //get any bundled data from the intent
    Bundle data = getIntent().getExtras();

    //populate variables (including unused options object meant for future development) with data
    if (data != null) {
      Log.d("LOAD", "IF STATEMENT");
      this.teach = data.getBoolean("teach");
      options.setTeach(teach);
      this.reward = data.getBoolean("reward");
      options.setReward(reward);
      simulation = data.getBoolean("sim");
      options.setSimulation(simulation);
      add = data.getBoolean("add");
      options.setAdd(add);
      subtract = data.getBoolean("sub");
      options.setSubtract(subtract);
      multiply = data.getBoolean("mul");
      options.setMultiply(multiply);
      divide = data.getBoolean("div");
      options.setDivide(divide);
      exponent = data.getBoolean("exp");
      options.setExponent(exponent);
      squareRoot = data.getBoolean("sqrt");
      options.setSquarert(squareRoot);
      negativeNumbers = data.getBoolean("neg");
      options.setDoNegativeNums(negativeNumbers);
      difficulty = data.getInt("diff");
      options.setDifficulty(difficulty);
      time = data.getLong("time");
      options.setTime(time);
      finished = data.getBoolean("finished");
      options.setFinished(finished);

      //if student didn't finish the problem set, try to restore the remaining problems list
      if (!finished) {
        try {

          //attempt to get problems array list and populate it with math problems
          Transfer myArray = (Transfer) data.getSerializable("problems");
          problems = myArray.getArray();
        } catch (NullPointerException error) {

          //if problems fires a npe, record the issue in an error log
          Log.e("Display", "This problem occurred: " + error);
        }

        //just in case put a default problem array into the list 0 + 0 = 0
      } else {
        Integer[] input = {0, 1, 0, 0};
        problems.add(input);
      }

      //if there's no bundle, intent came from MainActivity, so load previous saved problem set
    } else
      load();

    //ALL THIS FOR TABLE DISPLAY
    tableDisplay();
  }

  /**
   * LoadSaver default constructor
   */
  public LoadSaver() {

  }

  /**
   * Load() will load a previously saved state and restore it for the user.
   * Gets data from SharedPref. If no game is saved, then focus is sent to Options so teacher or
   * parent can set options for new problem set. If there is a saved game, the variables are loaded
   * and focus sent to Display class to resume problem set.
   */
  private void load() {

    //create shared preferences object and retrieve saved data (if any)
    SharedPreferences sharedpreferences =
      getSharedPreferences("saveData", Context.MODE_PRIVATE);

    //create flag for saveData object load and set to 'nothing loaded' value
    boolean loaded = false;

    //populate negative numbers variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("negativeNumbers")) {
      negativeNumbers = sharedpreferences.getBoolean("negativeNumbers", false);
      loaded = true;
    }

    //populate teach variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("teach")) {
      teach = sharedpreferences.getBoolean("teach", false);
      loaded = true;
    }

    //populate subtract variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("subtract")) {
      subtract = sharedpreferences.getBoolean("subtract", false);
      loaded = true;
    }

    //populate add variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("add")) {
      add = sharedpreferences.getBoolean("add", false);
      loaded = true;
    }

    //populate multiply variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("multiply")) {
      multiply = sharedpreferences.getBoolean("multiply", false);
      loaded = true;
    }

    //populate divide variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("divide")) {
      divide = sharedpreferences.getBoolean("divide", false);
      loaded = true;
    }

    //populate exponent variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("exponent")) {
      exponent = sharedpreferences.getBoolean("exponent", false);
      loaded = true;
    }

    //populate square root variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("squareRoot")) {
      squareRoot = sharedpreferences.getBoolean("squareRoot", false);
      loaded = true;
    }

    //populate simulation variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("simulation")) {
      simulation = sharedpreferences.getBoolean("simulation", false);
      loaded = true;
    }

    //populate reward variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("reward")) {
      reward = sharedpreferences.getBoolean("reward", false);
      loaded = true;
    }

    //populate time variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("time")) {
      time = sharedpreferences.getLong("time", 0);
      loaded = true;
    }

    //populate difficulty variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("difficulty")) {
      difficulty = sharedpreferences.getInt("difficulty", 1);
      loaded = true;
    }

    //populate high score variable and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("highScore")) {
      oper = sharedpreferences.getString("highScore", "");
      loaded = true;
    }

    //populate list of problem arrays and set loaded flag to 'something loaded'
    if (sharedpreferences.contains("problems")) {

      //load in the string containing the problems list data
      String json = sharedpreferences.getString("problems", "");

      //remove the brackets from the string
      String cleanString = json.replaceAll("\\[", "")
        .replaceAll("\\]", "");

      //turn the string into a string array which holds all the numeric data
      String[] stringArray = cleanString.split(",");

      //use a for loop to recreate the problem arrays and add them to the problems list
      for (int i = 0; i < stringArray.length; ) { //notice no index advance - happens in the body
        Integer[] input = new Integer[]{0, 0, 0, 0};
        input[0] = parseInt(stringArray[i]);
        input[1] = parseInt(stringArray[i + 1]);
        input[2] = parseInt(stringArray[i + 2]);
        input[3] = parseInt(stringArray[i + 3]);
        i += 4;
        problems.add(input);
      }

      //and set loaded flag to 'something loaded'
      loaded = true;
    }

    //if no game variables were loaded, goto Options to set variables for new problems
    if (!loaded) {

      //no game was saved, so go to Options to select parameters for new list of problem arrays
      Intent intent = new Intent(this, Options.class);
      startActivity(intent);

      //otherwise bundle problems list data and options then send intent to start Display activity
    } else {

      //create the editor to erase the former data in the saveData object
      SharedPreferences.Editor saveData = sharedpreferences.edit();

      //clear shared preferences
      saveData.clear();
      saveData.apply();

      //prepare intent for Display activity
      Intent intent = new Intent(this, Display.class);
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
      data.putLong("time", time);
      options.setTime(time);
      data.putBoolean("finished", finished);
      options.setFinished(finished);
      data.putBoolean("quit", false);

      //pass data to Display activity
      intent.putExtras(data);
      startActivity(intent);
    }
  }

  /**
   * Save() will save the state for the user to be retrieved later
   */
  public void save(View view) {

    //if simulation or finished is set, nothing to save so send to quit()
    if (simulation || finished) {

      //open shared preferences object where data is saved
      SharedPreferences sharedpreferences =
        getSharedPreferences("saveData", Context.MODE_PRIVATE);

      //create the editor to erase the former data in the saveData object
      SharedPreferences.Editor saveData = sharedpreferences.edit();

      //clear any old saved data so load will send us to Options for new selections
      saveData.clear();
      saveData.apply();

      //call the load method now that there is nothing for it to load so it will send us to Options
      load();
    }

    else {

      //create the sharedPreferences xml file
      SharedPreferences sharedpreferences =
        getSharedPreferences("saveData", Context.MODE_PRIVATE);

      //create the editor to store the data in the sharedpreferences object
      SharedPreferences.Editor saveData = sharedpreferences.edit();

      // Make a new gson
      Gson gson = new Gson();

      //edit the shared preferences with new data and save it
      saveData.putBoolean("negativeNumbers", this.negativeNumbers);
      saveData.putBoolean("teach", this.teach);
      saveData.putBoolean("subtract", this.subtract);
      saveData.putBoolean("add", this.add);
      saveData.putBoolean("multiply", this.multiply);
      saveData.putBoolean("divide", this.divide);
      saveData.putBoolean("exponent", this.exponent);
      saveData.putBoolean("squareRoot", this.squareRoot);
      saveData.putBoolean("simulation", this.simulation);
      saveData.putBoolean("reward", this.reward);
      saveData.putLong("time", this.time);
      saveData.putInt("difficulty", this.difficulty);
      String json = gson.toJson(problems);
      saveData.putString("problems", json);
      saveData.putString("highScore", this.oper);
      saveData.apply();

      //Toast and quit
      Toast toast = Toast.makeText(getApplicationContext(),
        "Save Successful. Now Exiting...", Toast.LENGTH_LONG);
      View correctView = toast.getView();
      correctView.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
      TextView correctToast = correctView.findViewById(android.R.id.message);
      correctToast.setTextColor(Color.BLACK);
      correctToast.setBackgroundColor(Color.GREEN);
      toast.show();
      final View quitView = view;
      final Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          // Do something after 5s = 5000ms
          //quit application
          quit(quitView);
        }
      }, 2000);
    }
  }

  /**
   * The tableDisplay method puts and displays the tableView based on data passed from the Bundle
   *
   * <p>
   * The tableDisplay first of all checks if any sharedPreferences with a key "current_best_score"
   * have been saved. If some previous score have been saved then it checks if the latest_best_score is
   * larger than the current_best_score. If it is then the method displays data to the table and then saves
   * the latest_best_score to SharedPreferences with the key currrent_best_score.
   * If currrent_best_score is actually larger than than the latest_score then currrent_best_score will be
   * displayed using the sharedPrefs.
   * </p>
   */
  /*
   * Current variables used include:
   * @param operators - an array variable
   * @param difficulty - an integer variable
   * @param highscore - an integer variable
   * @param time - an integer variable
   * @param current_score - an integer variable
   */
  private void tableDisplay() {

    String addition = "+";
    String subtraction = "-";
    String multiplication = "*";
    String division = "/";
    String exponential = "^";
    String sqrting = "√";
    String neg = "neg";
    String oper = "";
    int index = 0;

    if (add == true) {
      oper += addition;
    }

    if (subtract == true) {
      oper += subtraction;
    }

    if (multiply == true) {
      oper += multiplication;
    }

    if (divide == true) {
      oper += division;
    }

    if (exponent == true) {
      oper += exponential;
    }

    if (squareRoot == true) {
      oper += sqrting;
    }

    if (negativeNumbers == true) {
      oper += neg;
    }

    latest_score = calculateHighScore(oper, time, difficulty);

    SharedPreferences mPref = getSharedPreferences("score", Context.MODE_PRIVATE);
    current_score = mPref.getInt("current_best_score", 0);

    SharedPreferences.Editor editor = mPref.edit();

    if (latest_score > current_score && finished) {
      operators = findViewById(R.id.Operators);
      operators.setText(oper);

      best_difficulty = findViewById(R.id.Difficulty);
      best_difficulty.setText("" + difficulty);

      highscore = findViewById(R.id.HighScore);
      highscore.setText("" + latest_score);

      best_time = findViewById(R.id.bestTime);
      best_time.setText(timeOutput(time));

      editor.putString("Operat", oper);
      editor.putInt("Difficulty", difficulty);
      editor.putInt("currrent_best_score", latest_score);
      editor.putInt("Time", (int) time);
      editor.apply();

      System.out.println("Here it is " + oper);
      System.out.println("sum is " + latest_score);
      System.out.println("currently it is " + current_score);
      System.out.println("difficulty is " + difficulty);

      //show new high score toast
      Toast toast = Toast.makeText(getApplicationContext(),
        "Congrats! New high score!", Toast.LENGTH_LONG);
      View correctView = toast.getView();
      correctView.getBackground().setColorFilter(Color.MAGENTA, PorterDuff.Mode.SRC_IN);
      TextView correctToast = correctView.findViewById(android.R.id.message);
      correctToast.setTextColor(Color.WHITE);
      correctToast.setBackgroundColor(Color.MAGENTA);
      toast.show();

    } else if (finished){
      operators = findViewById(R.id.Operators);
      operators.setText(oper);

      best_difficulty = findViewById(R.id.Difficulty);
      best_difficulty.setText(difficulty);

      highscore = findViewById(R.id.HighScore);
      highscore.setText(latest_score);

      best_time = findViewById(R.id.bestTime);
      best_time.setText(timeOutput(time));
    }
  }

  /**
   * The calculateHighScore method creates the highscore result
   *
   * <p>
   * The calculateHighScore takes in three paramenters. With them in memory it creates a number
   * that will be used a score.
   * The calculateHighScore return an integer value.
   * </p>
   *
   * @param operators  - String parameter for the amount of operators.
   * @param time       - long paramenter for the time in milliseconds
   * @param difficulty - integer parameter to use as a multiplier.
   * @return
   */
  static private int calculateHighScore(String operators, long time, int difficulty) {

    int highscore = 0;

    highscore = (int) (operators.length() * difficulty * 60000 - time);

    return highscore;
  }

  /**
   * The quit method is activated by user pressing the quit key on this View.
   *
   * <p>
   * This method will create an intent for the MainActivity, bundle all the data for the app,
   * set a new variable, 'quit', and start the MainActivity. When Main sees the quit flag set
   * it will kill the app.
   * </p>
   *
   * @param view
   */
  public void quit(View view) {

    //prepare intent for main activity
    Intent intent = new Intent(this, MainActivity.class);
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
    data.putLong("time", time);
    options.setTime(time);
    data.putBoolean("finished", finished);
    options.setFinished(finished);
    data.putBoolean("quit", true);

    //pass data to Display activity
    intent.putExtras(data);
    startActivity(intent);
  }

  /**
   * The timeOutput method takes the system time and converts it to an hours, minutes, and seconds
   * string for display.
   *
   * <p>
   *   This method takes the long variable, time, which is the time elapsed during the students
   *   problem solving in milliseconds, and converts it. This is done by first determining the
   *   seconds elapsed by a simple division by 1000. Then minutes is determined by dividing the
   *   seconds by 60, and hours by dividing the minutes by 60. Once these are determined, the
   *   minutes and seconds are reduced to the seconds and minutes actually below 60. Lastly, the
   *   integers representing these units of time are used to build a string used for display.
   * </p>
   * @param time - system time in milliseconds elapsed in problem solving
   * @return timeString - string holding the converted milliseconds to hours, minutes, and seconds.
   */
  static private String timeOutput (long time) {

    //used to build time string for display
    int hours;
    int minutes;
    int seconds;
    String timeString;

    //convert time to hours:minutes:seconds format
    seconds = (int) time/1000;
    minutes = seconds/60;
    hours = minutes/60;
    minutes = minutes%60;
    seconds = seconds%60;

    //build string displaying time in hours:minutes:seconds format
    timeString = hours + " : " + minutes + " : " + seconds;

    //return hours:minutes:seconds time string
    return timeString;
  }
}


