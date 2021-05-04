package com.sudokusolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class StandardGrid {

  public static final int NUM_ROWS = 9;
  public static final int NUM_COLS = 9;
  public static final int GRID_SIZE = NUM_ROWS * NUM_COLS;

  private static final boolean DEBUG = false;

  private static int calledCount = 0;

  private StandardTile[] tiles = new StandardTile[GRID_SIZE];

  private ArrayList<ArrayList<StandardTile>> rows = new ArrayList<>(9);
  private ArrayList<ArrayList<StandardTile>> cols = new ArrayList<>(9);
  private ArrayList<ArrayList<StandardTile>> sets = new ArrayList<>(9);

  private ArrayList<ArrayList<StandardTile>> allGroups = new ArrayList<>();

  public StandardGrid() {

    calledCount++;

    for (int x = 0; x < 9; x++) {
      rows.add(new ArrayList<StandardTile>());
      cols.add(new ArrayList<StandardTile>());
      sets.add(new ArrayList<StandardTile>());
    }

    for (int x = 0; x < GRID_SIZE; x++) {
      int row = x / 9;
      int col = x - (row * 9);
      int set = 3 * (row / 3) + (col / 3);

      tiles[x] = new StandardTile(row, col, set);

      rows.get(row).add(tiles[x]);
      cols.get(col).add(tiles[x]);
      sets.get(set).add(tiles[x]);
    }

    for (int x = 0; x < 9; x++) {
      allGroups.add(rows.get(x));
      allGroups.add(cols.get(x));
      allGroups.add(sets.get(x));
    }
  }

  public boolean solvePuzzle(int[] input) {
    processInputs(input);

    if (!isSolved()) {

      processGroups(allGroups);

      if (!isSolved()) {
        int indx = getSmallestPossibilityUnsolvedIndex();
        Set<Integer> possibleValues = tiles[indx].getPossibleValues();

        for (Integer value : possibleValues) {
          int[] tempSolution = getSolution();
          tempSolution[indx] = value;

          StandardGrid testGrid = new StandardGrid();
          if (testGrid.solvePuzzle(tempSolution)) {
            tiles = testGrid.tiles;
            return true;
          }
        }
      }
    }

    return isSolutionValid();
  }

  private void processGroups(ArrayList<ArrayList<StandardTile>> groups) {
    int solvedTileCount;

    do {
      solvedTileCount = 0;

      for (ArrayList<StandardTile> group : groups) {
        for (int x = 1; x <= 9; x++) {

          StandardTile uniqueTile = null;

          for (StandardTile tile : group) {
            if (!tile.isSolved() && tile.getPossibleValues().contains(x)) {
              if (uniqueTile != null) {
                uniqueTile = null;
                break;
              } else {
                uniqueTile = tile;
              }
            }
          }

          if (uniqueTile != null) {
            uniqueTile.setValue(x);
            updateAssociatedTiles(uniqueTile, x);
            solvedTileCount++;
          }
        }
      }
    } while (solvedTileCount > 0);
  }

  private int getSmallestPossibilityUnsolvedIndex() {
    int fewestPossibilities = 999;
    int fewestPossibilitiesIndex = -1;

    for (int x = 0; x < tiles.length; x++) {
      if (!tiles[x].isSolved() && tiles[x].getPossibleValues().size() < fewestPossibilities) {
        fewestPossibilities = tiles[x].getPossibleValues().size();
        fewestPossibilitiesIndex = x;
      }
    }

    return fewestPossibilitiesIndex;
  }

  private boolean isSolved() {
    for (int x = 0; x < tiles.length; x++) {
      if (!tiles[x].isSolved()) {
        return false;
      }
    }

    return true;
  }

  private boolean isSolutionValid() {
    if (!isSolved()) {
      return false;
    }

    final ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

    for (ArrayList<StandardTile> group : allGroups) {

      ArrayList<Integer> values = new ArrayList<>();

      for (StandardTile tile : group) {
        values.add(tile.getValue());
      }

      if (!values.containsAll(expected))
        return false;
    }

    if (DEBUG)
      System.out.println(calledCount);

    return true;
  }

  private void processInputs(int[] input) {
    for (int x = 0; x < GRID_SIZE; x++) {
      if (input[x] != 0) {
        setTileValue(x, input[x]);
      }
    }

    if (DEBUG)
      printGrid();
  }

  private void printGrid() {
    for (int x = 0; x < GRID_SIZE; x += 9) {
      System.out.println(Arrays.toString(Arrays.copyOfRange(tiles, x, x + 9)));
    }

    System.out.println("");
  }

  private void setTileValue(int tileIndex, int tileValue) {
    tiles[tileIndex].setValue(tileValue);
    updateAssociatedTiles(tiles[tileIndex], tileValue);
  }

  private void updateAssociatedTiles(StandardTile tile, int tileValue) {
    removeValueFromGroup(rows.get(tile.getRow()), tileValue);
    removeValueFromGroup(cols.get(tile.getCol()), tileValue);
    removeValueFromGroup(sets.get(tile.getSet()), tileValue);
  }

  private void removeValueFromGroup(ArrayList<StandardTile> group, int tileValue) {
    for (StandardTile tile : group) {
      if (!tile.isSolved()) {
        boolean solved = tile.removeValue(tileValue);
        if (solved) {
          updateAssociatedTiles(tile, tile.getValue());
        }
      }
    }
  }

  public int[] getSolution() {
    int[] solution = new int[GRID_SIZE];

    for (int x = 0; x < GRID_SIZE; x++)
      solution[x] = tiles[x].getValue();

    return solution;
  }

  public int gridSize() {
    return GRID_SIZE;
  }
}