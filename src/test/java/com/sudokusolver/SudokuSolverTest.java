package com.sudokusolver;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public final class SudokuSolverTest {

  /** Test various puzzles. */
  public void testSolver(String testName) {
    int[] input = parseUnitTestFile(testName + "_input.txt");
    int[] expected = parseUnitTestFile(testName + "_expected.txt");

    Solver mySolver = new Solver();

    int[] output = mySolver.solve(input);

    assertTrue("Need to solve puzzle", Arrays.equals(output, expected));
  }

  private int[] parseUnitTestFile(String fileName) {

    String file = "./src/test/resources/" + fileName;
    int[] output = new int[81];
    int index = 0;

    try {
      Scanner scanner = new Scanner(new File(file));

      while (scanner.hasNextInt()) {
        output[index++] = scanner.nextInt();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return output;
  }

  @Test
  public void testHard1() {
    testSolver("testHard1");
  }

  @Test
  public void testHard2() {
    testSolver("testHard2");
  }

  @Test
  public void testVeryHard() {
    testSolver("testVeryHard");
  }
}