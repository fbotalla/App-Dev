package com.example.mathproof;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * A test class for the SimpleMath class
 * <p>
 *     This test class will test the robustness and correctness of the simpleMath class, ensuring
 *     that it is usable for the app.
 * </p>
 * @author Adam Tipton
 */
public class SimpleMathUnitTest {

  /**
   * An option object is needed for the tests.
   * <p>Initializing an options object to be used in testing</p>
   */
  private Options options = new Options();
  /**
   * A List is created for testing
   */
  private List<Integer[]> testList = new ArrayList<Integer[]>();
  /**
   * Initializing an array of ints for testing whats inside the math list.
   */
  private Integer[] testInput = {0, 0, 0, 0};

  /**
   * A TEST TAG is created for logging
   */
  private static final String TAG = "SimpleMath TEST";
  /**
   * Test the non-default constructor
   * <p>This will test if the non-default constructor is working correct and that the option
   * object was passed correctly and the variable assigned appropriately</p>
   */
  @Test
  public void nonDefaultConstructorIsCorrect(){
    System.out.println("Starting nonDefaultConstructorIsCorrect TEST. All options variables are" +
      "set to true");
    this.options.setDifficulty(1);
    this.options.setAdd(true);
    this.options.setSubtract(true);
    this.options.setMultiply(true);
    this.options.setDivide(true);
    this.options.setExponent(true);
    this.options.setSquarert(true);
    this.options.setDoNegativeNums(true);
    this.options.setSimulation(true);

    System.out.println("Setting up the SimpleMath object.");
    //set up the class
    SimpleMath tester = new SimpleMath(this.options);

    System.out.println("Running Assert Tests. All should be true.");
    //Boolean assert statements
    assertTrue(tester.doAddition);
    assertTrue(tester.doSubtraction);
    assertTrue(tester.doMultiplication);
    assertTrue(tester.doDivision);
    assertTrue(tester.doExponent);
    assertTrue(tester.doSqrt);
    assertTrue(tester.doNegativeNums);
    assertTrue(tester.doSimulation);
    //Int assert statements
    assertEquals((int)1, tester.difficulty);

    System.out.println("****Closing nonDefaultConstructorIsCorrect TEST****" + '\n');

  }

  /**
   * Test that the math List will populate
   * <p>
   *     This will ensure that the math List will populate correctly and that it can be accessed
   *     when needed.
   *     If it fills and is not null then it worked.
   * </p>
   */
  @Test
  public void mathListIsCorrect(){
    System.out.println("Starting mathListIsCorrect TEST.");
    System.out.println("Creating SimpleMath object and running add() to populate list.");
    SimpleMath tester = new SimpleMath(this.options);
    tester.add();
    System.out.println("Copying testList from tester.getMath()");
    testList = tester.getMath();
    assertNotNull(testList);
    System.out.println("****Closing MathListIsCorrect TEST****" + '\n');
  }

  /**
   * Test that addition works
   * <p>
   *     This will test that the addition method correctly populates the math List with correct
   *     problems.
   * </p>
   */
  @Test
  public void additionIsCorrect(){
    System.out.println("Starting additionIsCorrect TEST.");
    System.out.println("Creating SimpleMath object and running add() to populate list." + '\n');
    SimpleMath tester = new SimpleMath(this.options);
    tester.add();
    System.out.println('\n' + "Copying testList from tester.getMath()");
    testList = tester.getMath();
    testInput = testList.get(0);

    System.out.println("Asserting Conditions");
    double answer = testInput[0] + testInput[2];
    assertEquals(testInput[3], answer, 0);

    System.out.println("****Closing additionIsCorrect TEST****" + '\n');
  }
  /**
   * Test that subtraction works
   * <p>
   *     This will test that the subtraction method correctly populates the math List with correct
   *     problems.
   * </p>
   */
  @Test
  public void subtractionIsCorrect(){
    System.out.println("Starting subtractionIsCorrect TEST.");
    System.out.println("Creating SimpleMath object and running subtract() to populate list." + '\n');
    SimpleMath tester = new SimpleMath(this.options);
    tester.subtract();
    System.out.println('\n' + "Copying testList from tester.getMath()");
    testList = tester.getMath();
    testInput = testList.get(0);

    System.out.println("Asserting Conditions");
    double answer = testInput[0] - testInput[2];
    assertEquals(testInput[3], answer, 0);

    System.out.println("****Closing subtractionIsCorrect TEST****" + '\n');
  }
  /**
   * Test that multiplication works
   * <p>
   *     This will test that the multiplication method correctly populates the math List with correct
   *     problems.
   * </p>
   */
  @Test
  public void multiplicationIsCorrect(){
    System.out.println("Starting multiplicationIsCorrect TEST.");
    System.out.println("Creating SimpleMath object and running multiply() to populate list." + '\n');
    SimpleMath tester = new SimpleMath(this.options);
    tester.multiply();
    System.out.println('\n' + "Copying testList from tester.getMath()");
    testList = tester.getMath();
    testInput = testList.get(0);

    System.out.println("Asserting Conditions");
    System.out.println("first = " + testInput[0] + " Second = " + testInput[2]);
    double answer = testInput[0] * testInput[2];
    assertEquals(testInput[3], answer, 0);

    System.out.println("****Closing multiplicationIsCorrect TEST****" + '\n');
  }
  /**
   * Test that division works
   * <p>
   *     This will test that the division method correctly populates the math List with correct
   *     problems.
   * </p>
   */
  @Test
  public void divisionIsCorrect(){
    System.out.println("Starting divisionIsCorrect TEST.");
    System.out.println("Creating SimpleMath object and running multiply() to populate list." + '\n');
    SimpleMath tester = new SimpleMath(this.options);
    tester.divide();
    System.out.println('\n' + "Copying testList from tester.getMath()");
    testList = tester.getMath();
    testInput = testList.get(0);

    System.out.println("Asserting Conditions");
    System.out.println("first = " + testInput[0] + " Second = " + testInput[2]);
    double answer = testInput[0] / testInput[2];
    assertEquals(testInput[3], answer, 0);

    System.out.println("****Closing divisionIsCorrect TEST****" + '\n');
  }
  /**
   * Test that exponent works
   * <p>
   *     This will test that the exponent method correctly populates the math List with correct
   *     problems.
   * </p>
   */
  @Test
  public void exponentIsCorrect(){
    System.out.println("Starting exponentIsCorrect TEST.");
    System.out.println("Creating SimpleMath object and running multiply() to populate list." + '\n');
    SimpleMath tester = new SimpleMath(this.options);
    tester.exponent();
    System.out.println('\n' + "Copying testList from tester.getMath()");
    testList = tester.getMath();
    assertNotNull(testList);
    testInput = testList.get(0);

    System.out.println("Asserting Conditions");
    double answer = Math.pow(testInput[0], testInput[2]);
    assertEquals(testInput[3], answer, 0);
    //If answer is larger than 1000, no joy!
    if (answer > 1000) {
      boolean tooLarge = true;
      assertFalse(tooLarge);
    }
    //If any numbers are negative, no joy
    if (answer < 0 || testInput[0] < 0 || testInput[2] < 0) {
      boolean isNegative = true;
      assertFalse(isNegative);
    }

    System.out.println("****Closing exponentIsCorrect TEST****" + '\n');
  }
  /**
   * Test that squareroot works
   * <p>
   *     This will test that the squareroot method correctly populates the math List with correct
   *     problems.
   * </p>
   */
  @Test
  public void squarerootIsCorrect(){
    System.out.println("Starting squarerootIsCorrect TEST.");
    System.out.println("Creating SimpleMath object and running multiply() to populate list." + '\n');
    SimpleMath tester = new SimpleMath(this.options);
    tester.squareRoot();
    System.out.println('\n' + "Copying testList from tester.getMath()");
    testList = tester.getMath();
    assertNotNull(testList);
    testInput = testList.get(0);

    System.out.println("Asserting Conditions");
    double answer = Math.sqrt(testInput[0]);
    assertEquals(testInput[3], answer, 0);
    //Assert no negative values exist
    if (answer < 0 || testInput[0] < 0 || testInput[2] < 0){
      boolean isNegative = true;
      assertFalse(isNegative);
    }

    System.out.println("****Closing squarerootIsCorrect TEST****" + '\n');
  }


}
