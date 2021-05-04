package com.sudokusolver;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StandardTile {

  private HashSet<Integer> possibleValues = new HashSet<>();
  private Integer solvedValue = 0;
  private int row;
  private int col;
  private int set;

  public StandardTile(int inRow, int inCol, int inSet) {
    row = inRow;
    col = inCol;
    set = inSet;

    Integer[] startingValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    Collections.addAll(possibleValues, startingValues);
  }

  public boolean isSolved() {
    return solvedValue != 0;
  }

  public int getValue() {
    return solvedValue;
  }

  public Set<Integer> getPossibleValues() {
    return possibleValues;
  }

  public final int getRow() {
    return row;
  }

  public final int getCol() {
    return col;
  }

  public final int getSet() {
    return set;
  }

  public void setValue(int i) {
    possibleValues.clear();
    solvedValue = i;
  }

  public boolean removeValue(int tileValue) {
    possibleValues.remove(tileValue);

    if (possibleValues.size() == 1) {
      solvedValue = possibleValues.stream().findFirst().get();
    }

    return isSolved();
  }

  @Override
  public String toString() {
    return Integer.toString(getValue());
  }
}
