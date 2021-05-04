package com.sudokusolver;

public class Solver {

  private StandardGrid grid;

  public Solver() {
    grid = new StandardGrid();
  }

  /** Solves the puzzle. */
  public int[] solve(int[] input) {
    if (grid.solvePuzzle(input)) {
      return grid.getSolution();
    } else {
      return new int[grid.gridSize()];
    }
  }
}
