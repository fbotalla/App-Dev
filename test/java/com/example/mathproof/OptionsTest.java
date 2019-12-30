package com.example.mathproof;

import org.junit.Test;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * This is the unit test class for testing the Options class getters and setters.
 */
public class OptionsTest {

  /**
   * This will test all getters and setters in the class
   */
  @Test
  public void optionsClassTest() {
    difficulty_isSet_and_isGet();
    negativeNumbers_isSet_and_isGet();
    teach_isSet_and_isGet();
    subtract_isSet_and_isGet();
    add_isSet_and_isGet();
    multiply_isSet_and_isGet();
    divide_isSet_and_isGet();
    exponent_isSet_and_isGet();
    squarert_isSet_and_isGet();
    simulation_isSet_and_isGet();
    reward_isSet_and_isGet();
  }

  /**
   * This checks all the possibilities for difficulty
   */
  @Test
  public void difficulty_isSet_and_isGet() {

    Options options = new Options();
    options.setDifficulty(0);
    assertEquals(1, options.getDifficulty());
    options.setDifficulty(1);
    assertEquals(1, options.getDifficulty());
    options.setDifficulty(2);
    assertEquals(2, options.getDifficulty());
    options.setDifficulty(3);
    assertEquals(3, options.getDifficulty());
    options.setDifficulty(9);
    assertEquals(3, options.getDifficulty());
  }

  /**
   * Check to insure negative numbers can be set and get either true or false
   */
  @Test
  public void negativeNumbers_isSet_and_isGet() {

    Options options = new Options();
    options.setDoNegativeNums(true);
    assertEquals(true, options.getDoNegativeNums());
    options.setDoNegativeNums(false);
    assertEquals(false, options.getDoNegativeNums());
  }

  /**
   * Check to insure teach can be set and get either true or false
   */
  @Test
  public void teach_isSet_and_isGet() {

    Options options = new Options();
    options.setTeach(true);
    assertEquals(true, options.getTeach());
    options.setTeach(false);
    assertEquals(false, options.getTeach());
  }

  /**
   * Check to insure subtract can be set and get either true or false
   */
  @Test
  public void subtract_isSet_and_isGet() {

    Options options = new Options();
    options.setSubtract(true);
    assertEquals(true, options.getSubtract());
    options.setSubtract(false);
    assertEquals(false, options.getSubtract());
  }

  /**
   * Check to insure add can be set and get either true or false
   */
  @Test
  public void add_isSet_and_isGet() {

    Options options = new Options();
    options.setAdd(true);
    assertEquals(true, options.getAdd());
    options.setAdd(false);
    assertEquals(false, options.getAdd());
  }

  /**
   * Check to insure multiply can be set and get either true or false
   */
  @Test
  public void multiply_isSet_and_isGet() {

    Options options = new Options();
    options.setMultiply(true);
    assertEquals(true, options.getMultiply());
    options.setMultiply(false);
    assertEquals(false, options.getMultiply());
  }

  /**
   * Check to insure divide can be set and get either true or false
   */
  @Test
  public void divide_isSet_and_isGet() {

    Options options = new Options();
    options.setDivide(true);
    assertEquals(true, options.getDivide());
    options.setDivide(false);
    assertEquals(false, options.getDivide());
  }

  /**
   * Check to insure exponent can be set and get either true or false
   */
  @Test
  public void exponent_isSet_and_isGet() {

    Options options = new Options();
    options.setExponent(true);
    assertEquals(true, options.getExponent());
    options.setExponent(false);
    assertEquals(false, options.getExponent());
  }

  /**
   * Check to insure square root can be set and get either true or false
   */
  @Test
  public void squarert_isSet_and_isGet() {

    Options options = new Options();
    options.setSquarert(true);
    assertEquals(true, options.getSquarert());
    options.setSquarert(false);
    assertEquals(false, options.getSquarert());
  }

  /**
   * Check to insure simulation can be set and get either true or false
   */
  @Test
  public void simulation_isSet_and_isGet() {

    Options options = new Options();
    options.setSimulation(true);
    assertEquals(true, options.getSimulation());
    options.setSimulation(false);
    assertEquals(false, options.getSimulation());
  }

  /**
   * Check to insure reward can be set and get either true or false
   */
  @Test
  public void reward_isSet_and_isGet() {

    Options options = new Options();
    options.setReward(true);
    assertEquals(true, options.getReward());
    options.setReward(false);
    assertEquals(false, options.getReward());
  }

  /**
   * Check to insure time accepts and returns any random long number
   */
  @Test
  public void time_isSet_and_isGet() {
    Options options = new Options();
    Random r = new Random();
    long random = r.nextLong();
    options.setTime(random);
    assertEquals(random, options.getTime());
  }

  /**
   * Check to insure finished can be set and get either true or false
   */
  @Test
  public void finished_isSet_and_isGet() {

    Options options = new Options();
    options.setFinished(true);
    assertEquals(true, options.getFinished());
    options.setFinished(false);
    assertEquals(false, options.getFinished());
  }
}
