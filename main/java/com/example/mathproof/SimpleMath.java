package com.example.mathproof;


import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**SimpleMath class provides the app with math problems.
 * <p>The simpleMath class will provide simple math functions, addition and subtraction,
 * and multiplication and division. It will consider if negative values should be used and populate
 * a list according to Options.java settings.
 * It is extendable for the eventual use of a square root and exponent method.
 * </p>
 * @author Adam Tipton, Jerry Lane, Fabrizio Botalla, and Alberto Contreras
 * @version 3.0
 * @since 2019-12-2
 */
public class SimpleMath {
  //Variables
  public int difficulty = 2; //Needs to be public for unit test
  private int maxProblems;
  private int addition = 1;
  private int subtraction = 2;
  private int multiplication = 3;
  private int division = 4;
  private int exponent = 5;
  private int sqrt = 6;
  private List<Integer[]> math = new ArrayList<Integer[]>();
  private Options options;
  boolean doNegativeNums, doAddition, doSubtraction, doMultiplication, doDivision,
    doExponent, doSqrt, doSimulation;

  /**
   * Default simpleMath constructor.
   * Takes no params
   */
  SimpleMath() {

  }

  /**
   * Non-default simple Math constructor takes an options object.
   * Using the Options object, it initializes a set of variables.
   *
   * @param options
   */
  SimpleMath(Options options) {
    this.options = options;
    this.difficulty = options.getDifficulty();
    this.doAddition = options.getAdd();
    this.doSubtraction = options.getSubtract();
    this.doMultiplication = options.getMultiply();
    this.doDivision = options.getDivide();
    this.doExponent = options.getExponent();
    this.doSqrt = options.getSquarert();
    this.doNegativeNums = options.getDoNegativeNums();
    this.doSimulation = options.getSimulation();
  }

  /**
   * begin() is the starter method for the SimpleMath class.
   * Once options are set, the client need only call Simplemath.begin(); to start
   * populating the problem sets in the math list.
   */
  public void begin() {
    if (doAddition) {
      add();
    }
    if (doSubtraction) {
      subtract();
    }
    if (doMultiplication) {
      multiply();
    }
    if (doDivision) {
      divide();
    }
    if (doExponent) {
      exponent();
    }
    if (doSqrt) {
      squareRoot();
    }
  }

  /**
   * add() checks difficulty and doNegativeNums checks
   * and populates the List<Integer[]>math accordingly.
   */
  public void add() {

    //Create variables for input array
    int first = 0, second = 0, answer = 0;

    //Check to see if difficulty has been set by non-defualt constructor and set it if needed.
    checkDifficulty();

    //Set the max number of problems and check that is in bounds, 1, 2, or 3.
    checkMaxNumsInBounds();

        /* Check for doNegativeNums then
           Populate List<int[]> math with input array
           according to what was found.
        */
    if (!doNegativeNums) {                           //Not using negatives
      for (first = 0; first < maxProblems; first++) {
        for (second = 0; second < maxProblems; second++) {
          //Create input array
          Integer[] input = new Integer[]{first, addition, second, answer};
          //Call fillList
          usingPosFillList("add", first, second, answer, input);
        }//Second for loop
      }//First for loop
    } else {                                                  //Using negatives
      for (first = 1; first < maxProblems; first++) {
        for (second = 0; second < maxProblems; second++) {
          //Create input array
          Integer[] input = new Integer[]{first, addition, second, answer};
          //Call fillList
          usingNegFillList("add", first, second, answer, input);
        }//Second for loop

      }//First for loop
    }
  }

  /**
   * subtract() checks difficulty and doNegativeNums checks
   * and populates the List<Integer[]>math accordingly.
   */
  public void subtract() {

    //Create variables for input array
    int first = 0, second = 0, answer = 0;

    //Check to see if difficulty has been set by non-defualt constructor and set it if needed.
    checkDifficulty();

    //Set the max number of problems and check that is in bounds, 1, 2, or 3.
    checkMaxNumsInBounds();

        /* Check for doNegativeNums then
           Populate List<int[]> math with input array
           according to what was found.
        */
    if (!doNegativeNums) {                           //Not using negatives
      for (first = 0; first < maxProblems; first++) {
        for (second = 0; second < maxProblems; second++) {
          //Initialize array
          Integer[] input = new Integer[]{first, subtraction, second, answer};
          //call fillList
          usingPosFillList("sub", first, second, answer, input);
        }//Second for loop

      }//First for loop
    } else {                                                  //Using negatives
      for (first = 1; first < maxProblems; first++) {
        for (second = 0; second < maxProblems; second++) {
          //Initialize an array of ints for input
          Integer[] input = new Integer[]{first, subtraction, second, answer};
          //Call fillList
          usingNegFillList("sub", first, second, answer, input);
        }//Second for loop

      }//First for loop
    }
  }

  /**
   * multiply() checks difficulty and doNegativeNums checks
   * and populates the List<Integer[]>math accordingly.
   */
  public void multiply() {
    //Create variables for input array
    int first = 0, second = 0, answer = 0;

    //Check to see if difficulty has been set by non-defualt constructor and set it if needed.
    checkDifficulty();

    //Set the max number of problems and check that is in bounds, 1, 2, or 3.
    checkMaxNumsInBounds();

        /* Check for doNegativeNums then
           Populate List<int[]> math with input array
           according to what was found.
        */
    if (!doNegativeNums) {                           //Not using negatives
      for (first = 0; first < maxProblems; first++) {
        for (second = 0; second < maxProblems; second++) {
          //Initialize array
          Integer[] input = new Integer[]{first, multiplication, second, answer};
          //call fillList
          usingPosFillList("mult", first, second, answer, input);
        }//Second for loop

      }//First for loop
    } else {                                                  //Using negatives
      for (first = 1; first < maxProblems; first++) {
        for (second = 0; second < maxProblems; second++) {
          //Initialize an array of ints for input
          Integer[] input = new Integer[]{first, multiplication, second, answer};
          //Call fillList
          usingNegFillList("mult", first, second, answer, input);
        }//Second for loop

      }//First for loop
    }
  }//Multiply


  /**
   * divide() checks difficulty and doNegativeNums checks
   * and populates the List<Integer[]>math accordingly.
   * It will take the first variable and divide it by the second variable
   */
  public void divide() {
    //Create variables for input array
    int first = 0, second = 0, answer = 0;

    //Check to see if difficulty has been set by non-defualt constructor and set it if needed.
    checkDifficulty();

    //Set the max number of problems and check that is in bounds, 1, 2, or 3.
    checkMaxNumsInBounds();

        /* Check for doNegativeNums then
           Populate List<int[]> math with input array
           according to what was found.
        */
    if (!doNegativeNums) {                           //Not using negatives
      for (first = 1; first < maxProblems; first++) {
        for (second = 1; second < maxProblems; second++) {
          if (first % second == 0) {
            //Initialize array
            Integer[] input = new Integer[]{first, division, second, answer};
            //call fillList
            usingPosFillList("div", first, second, answer, input);
          }
        }//Second for loop

      }//First for loop
    } else {                                                  //Using negatives
      for (first = 1; first < maxProblems; first++) {
        for (second = 1; second < maxProblems; second++) {
          if (first % second == 0) {
            //Initialize an array of ints for input
            Integer[] input = new Integer[]{first, division, second, answer};
            //Call fillList
            usingNegFillList("div", first, second, answer, input);
          }
        }//Second for loop
      }//First for loop
    }
  }

  /* STRETCH ACTIVITIES */
  /**
   * exponent() checks difficulty and doNegativeNums checks
   * and populates the List<Integer[]>math accordingly.
   * It will take the first variable and raise it to power of the second variable
   */
  public void exponent() {

    //Create variables for input array
    int first = 0, second = 0, answer = 0;

    //Check to see if difficulty has been set by non-defualt constructor and set it if needed.
    checkDifficulty();

    //Set the max number of problems and check that is in bounds, 1, 2, or 3.
    checkMaxNumsInBounds();

        /* Check for doNegativeNums then
           Populate List<int[]> math with input array
           according to what was found.
        */
    for (first = 0; first < maxProblems; first++) {
      for (second = 0; second < maxProblems; second++) {
        //Create input array
        Integer[] input = new Integer[]{first, exponent, second, answer};
        //Call fillList
        usingPosFillList("exp", first, second, answer, input);
      }//Second for loop
    }//First for loop
  }

  /** squareRoot() will populate the List<Integer[]>math with square root problems.
   * <p>
   *     squareRoot() checks the difficulty and maxNums to be in bound, before using a loop to
   *     decide an index of sqaurable numbers to send to usingPosFillList, which will
   *     populate the the List<Integer[]>math with an appropriate amount of square root problems.
   * </p>
   */
  public void squareRoot() {

    //Create variables for input array
    int first = 0, second = 0, answer = 0;
    //Index 0 - 9
    int[] numbersWithSquareRoots= {1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225};
    //Check to see if difficulty has been set by non-default constructor and set it if needed.
    checkDifficulty();

    //Set the max number of problems and check that is in bounds, 1, 2, or 3.
    checkMaxNumsInBounds();

        /* Loop through the int numbersWithSquareRoots array, choose a random position, and assign it to first, then
           call usingPosFillList
        */
    for (int problemCounter = 0; problemCounter <= maxProblems; problemCounter++){
      //Create input array
      Integer[] input = new Integer[]{first, sqrt, second, answer};
      //Get random number 0 - 15 for first
      first = numbersWithSquareRoots[problemCounter];
      //Call fillList
      usingPosFillList("sqrt", first, second, answer, input);
    } //end for loop
  }

  /**
   * selector() will choose a random number
   * between a given bound
   * @param bound
   * @return
   */
  private int selector(int bound) {
    int num;
    Random random = new Random(System.nanoTime());
    // random selection
    num = random.nextInt(bound);
    System.out.println("Random num: " + num);
    return num;
  }

  /**
   * getMath is a getter for the List<integer>math container
   *
   * @return
   */
  public List<Integer[]> getMath() {
    return math;
  }

  /**usingPosFillList() populates the the List<Integer[]>math.
   * <p>UsingPosFillList() fills in the List<Integer[]>math list
   * It uses switch case to choose operation function and fills the list accordingly.
   * </p>
   * @param operatorType
   * @param first
   * @param second
   * @param answer
   * @param input
   */
  private void usingPosFillList(String operatorType, int first, int second, int answer, Integer[] input) {
    switch (operatorType) {
      //Addition Case
      case "add":
        //Initialize an array of ints for input

        input[0] = first;
        input[1] = addition;

        //check for doNegativeNums and set second appropriately
        if (!doNegativeNums) {
          input[2] = second;
        } else {
          input[2] = second * -1;
        }
        answer = first + second;
        input[3] = answer;

        //Even though it should be impossible, double checking that
        // no negative values exist in answer either
        if (answer >= 0) {
          //ADDING IT INTO MATH LIST
          math.add(input);
        } else {
          System.out.println("Negative values exists in SimpleMath.add() when" +
            " there should only be positives");
          assert answer >= 0 : " Negative values exists in SimpleMath.add() when"
            + " there should only be positives";
        }
        break;
      //Subtraction Case
      case "sub":
        //Initialize an array of ints for input
        input[0] = first;
        input[1] = subtraction;

        //check for doNegativeNums and set second appropriately
        if (!doNegativeNums) {
          input[2] = second;
        } else {
          input[2] = second * -1;
        }
        answer = first - second;
        input[3] = answer;

        //Double checking that no negative values exist in answer either
        if (answer >= 0) {
          //ADDING IT INTO MATH LIST
          math.add(input);
        }
        break;
      //Multiplication Case
      case "mult":
        //Initialize an array of ints for input
        input[0] = first;
        input[1] = multiplication;

        //check for doNegativeNums and set second appropriately
        if (!doNegativeNums) {
          input[2] = second;
        } else {
          input[2] = second * -1;
        }
        answer = first * second;
        input[3] = answer;

        //Double checking that no negative values exist in answer either
        if (answer >= 0) {
          //ADDING IT INTO MATH LIST
          math.add(input);
        }
        break;
      //Division Case
      case "div":
        //Initialize an array of ints for input
        input[0] = first;
        input[1] = division;
        //check for doNegativeNums and set second appropriately
        if (!doNegativeNums) {
          if (second == 0) {
            second++;
          }
          input[2] = second;
        } else {
          input[2] = second * -1;
        }
        answer = first / second;
        input[3] = answer;

        //Double checking that no zero's or negative values exist in answer either
        if (answer > 0) {
          //ADDING IT INTO MATH LIST
          math.add(input);
        }
        break;
      //Exponent Case
      case "exp":
        //Initialize an array of ints for input
        input[0] = first;
        input[1] = exponent;
        //Double check second is not a negative, if it is, change it to positive
        if(second < 0) {
          second *= -1;
          input[2] = second;
        } else {
          input[2] = second;
        }
                /*check that the answer <= 1000, if isn't, exclude it from the array, else put
                  it in input[3]*/
        answer = (int)Math.pow(first, second);
        if(answer <= 1000) {
          input[3] = answer;
        } else {
          input[3] = answer;
        }
                /*Double checking that no negative values exist in answer either and that it is
                  <= 1000 */
        if (answer >= 0 && answer <= 1000) {
          //ADDING IT INTO MATH LIST
          math.add(input);
        }

        break;
      //Square Root Case
      case "sqrt":
        //Initialize an array of ints for input
        input[0] = first;
        input[1] = sqrt;
        answer = (int)Math.sqrt(first);
        input[3] = answer;
        //Double checking that no negative values exist in answer
        if (answer >= 0) {
          //ADDING IT INTO MATH LIST
          math.add(input);
        } else {
          break;
        }
        break;
      //Default
      default:
        break;
    }
  }//end

  /**usingNegFillList populates the List<Integer[]>math
   * <p>UsingNegFillList will populate the list using negative numbers.
   * It uses switch cases to determine the operator type and then choose the appropriate
   * code to run.
   * </p>
   * @param operatorType
   * @param first
   * @param second
   * @param answer
   * @param input
   */
  private void usingNegFillList(String operatorType, int first, int second, int answer,
                                Integer[] input) {
    switch (operatorType) {
      case "add":
        input[0] = first;
        input[1] = addition;
        //Flip it to a negative value
        second *= -1;
        input[2] = second;

        //Get the expected answer
        answer = first + second;
        input[3] = answer;

        //ADDING IT INTO MATH LIST
        math.add(input);
        break;
      case "sub":
        input[0] = first;
        input[1] = subtraction;
        //Flip it to a negative value
        second *= -1;
        input[2] = second;

        //Get the expected answer
        answer = first - second;
        input[3] = answer;

        //ADDING IT INTO MATH LIST
        math.add(input);
        break;
      case "mult":
        input[0] = first;
        input[1] = multiplication;
        //Flip it to a negative value
        second *= -1;
        input[2] = second;

        //Get the expected answer
        answer = first * second;
        input[3] = answer;

        //ADDING IT INTO MATH LIST
        math.add(input);
        break;
      case "div":
        input[0] = first;
        input[1] = division;
        //Preventing divide by zero error
        if (second == 0) {
          second++;
        }
        //Flip it to a negative value
        second *= -1;
        input[2] = second;
        //Get the expected answer
        answer = first / second;
        input[3] = answer;

        //ADDING IT INTO MATH LIST
        math.add(input);
        break;
      case "exp":
        //We don't use negatives for exponent
        break;
      case "sqrt":
        //We don't use negatives for square roots
        break;
      default:
        break;
    }
  }

  /**
   * testWhatsInMath() merely tests what has been stored in the List<Integer[]>math List and
   * displays it on the Log.d. Search for the MATH tag.
   */
  public void testWhatsInMath() {
    //TEST Whats in Math
    Log.d("MATH", "math.size = " + math.size());
    System.out.println("math.size = " + math.size());
    for (int i = 0; i < math.size(); i++) {
      System.out.println("Math index: " + i + " input values: "
        + math.get(i)[0]
        + math.get(i)[1]
        + math.get(i)[2]
        + math.get(i)[3]);
    }
  }

  /**
   * checkDifficulty() checks to see if difficulty has been set in Options.java
   * and if it hasn't, it sets it to a default value.
   */
  private void checkDifficulty() {
    //Check to see if difficulty has been set by non-defualt constructor and set it if needed.
    if (difficulty == 0 || options.getDifficulty() == 0) {
      //If difficulty isn't set, set it to 1 as a default
      this.difficulty = 1;
      System.out.println("Difficulty wasn't set properly. It is now: "
        + difficulty);
    }
  }

  /**
   * checkMaxNumsInBounds() sets the max num of problems and checks they are in bounds
   */
  private void checkMaxNumsInBounds() {
    //Set the max number of problems and check that is in bounds, 1, 2, or 3.
    if (difficulty == 1 || difficulty == 2 || difficulty == 3) {
      maxProblems = difficulty * 5;
      System.out.println("Checking Bounds. Difficulty should be 1, 2, or 3. " +
        "It currently is " + difficulty + " and maxProblems is set to: " + maxProblems);
    } else {
      System.out.println("Difficulty is out of bounds. " + "Difficulty is: "
        + difficulty + " and can only be 1, 2, or 3");
    }
  }//checkMaxNumsInBounds
}//SimpleMath
