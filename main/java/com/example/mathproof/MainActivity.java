package com.example.mathproof;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import static android.os.Process.killProcess;
import static android.os.Process.myPid;

/**
 * This is the MainActivity class, the entry point for the Math{Proof} application.
 *
 * <p>
 *   Main will offer two buttons to the user: Start and Load. If a game has previously been saved,
 *   pressing the Load button will load it and return the student to where he or she left off. If
 *   the Load button is pressed without a saved game, it will react as if the Start button was
 *   pressed instead. This means the student will be automatically sent to the Options activity for
 *   the parent or teacher to select the mathematical operations and options for the problem set.
 *   Once this is done Main is called again, first to check to see if the quit button is set, if
 *   not, then to load the options chosen by the parent or teacher, call SimpleMath class to
 *   populate the problems list with problem arrays, after which Main will transfer the data and
 *   control to the Display class for display and student input.
 * </p>
 *
 * @author Fabrizio Botalla, Alberto Contreras, Jerry Lane, and Adam Tipton
 */
public class MainActivity extends AppCompatActivity {


  /**
   * The onCreate method is called when the app is started. It displays the Start and Load buttons
   * and waits for input. Once the input is made it proceeds as stated above.
   * @param savedInstanceState
   */
  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //declare new Options object
    Options options = new Options();

    //end application if quit button was pressed or app successfully ended
    Bundle quitOption = getIntent().getExtras();

    if (quitOption != null) {
      if (quitOption.getBoolean("quit")) {
        quitOption = null;
        finishAffinity();
        killProcess(myPid());
        this.finish();
        System.exit(0);
      }
    }

    //if bundle exists, load bundle variable with data
    Bundle optionsData = getIntent().getExtras();

    //if options have been set send data to Display and begin
    if (optionsData != null) {

      //declare problems list object
      List<Integer[]> problems = new ArrayList<Integer[]>();

      //copy option member values from user set object
      options.setAdd(optionsData.getBoolean("add"));
      options.setSubtract(optionsData.getBoolean("sub"));
      options.setMultiply(optionsData.getBoolean("mul"));
      options.setDivide(optionsData.getBoolean("div"));
      options.setExponent(optionsData.getBoolean("exp"));
      options.setSquarert(optionsData.getBoolean("sqrt"));
      options.setTeach(optionsData.getBoolean("teach"));
      options.setDoNegativeNums(optionsData.getBoolean("neg"));
      options.setReward(optionsData.getBoolean("reward"));
      options.setSimulation(optionsData.getBoolean("sim"));
      options.setTime(optionsData.getLong("time"));
      options.setFinished(optionsData.getBoolean("finished"));

      //if simulation is true, set difficult to 1, otherwise set to user specifications
      if (options.getSimulation())
        options.setDifficulty(1);
      else
        options.setDifficulty(optionsData.getInt("diff"));

      //get math data according to options selected
      SimpleMath simpleMath = new SimpleMath(options);
      simpleMath.begin();
      problems = simpleMath.getMath();

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
      data.putLong("time", optionsData.getLong("time"));
      data.putBoolean("finished", optionsData.getBoolean("finished"));

      //prepare intent for display activity
      Intent intent = new Intent(this, Display.class);

      //pass data to Display activity
      intent.putExtras(data);
      startActivity(intent);
    }
  }

  /**
   * This is the button handler code for the Start button. It merely transfers control to Options
   * class where the parent or teacher will select the options for the students experience.
   * @param view
   */
  public void startButtonHandler(View view) {

    //redirect to Options for user input
    Intent intent = new Intent(this, Options.class);
    startActivity(intent);
  }

  /**
   * This is the button handler code for the Load button. It will transfer control to the LoadSaver
   * class for loading and then it will be sent to Display from there.
   * @param view
   */
  public void loadButtonHandler(View view) {

    //redirect to LoadSaver for loading a saved game
    Intent intent = new Intent(this, LoadSaver.class);
    startActivity(intent);
  }
}