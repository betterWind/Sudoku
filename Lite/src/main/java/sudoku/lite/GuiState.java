/*
 * Copyright (C) 2008-12  Bernhard Hobiger
 *
 * This file is part of HoDoKu.
 *
 * HoDoKu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HoDoKu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HoDoKu. If not, see <http://www.gnu.org/licenses/>.
 */
package sudoku.lite;

import sudoku.lite.solver.SudokuSolver;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuiState {
    // items from SudokuPanel
    private Sudoku sudoku = null;
    private SolutionStep step = null;
    private List<SolutionStep> steps;
    private int[] anzSteps;

    private List<String> titels;
    private List<List<SolutionStep>> tabSteps;

    // name and timestamp
    private String name;
    private Date timestamp;

    private SudokuSolver sudokuSolver;

    public GuiState() {

    }

    /**
     * @return the sudoku
     */
    public Sudoku getSudoku() {
        return sudoku;
    }

    /**
     * @param sudoku the sudoku to set
     */
    public void setSudoku(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    public SolutionStep getStep() {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(SolutionStep step) {
        this.step = step;
    }


    /**
     * @return the steps
     */
    public List<SolutionStep> getSteps() {
        // items from SudokuSolver
        return steps;
    }

    /**
     * @param steps the steps to set
     */
    public void setSteps(List<SolutionStep> steps) {
        this.steps = steps;
    }

    /**
     * @return the anzSteps
     */
    public int[] getAnzSteps() {
        return anzSteps;
    }

    /**
     * @param anzSteps the anzSteps to set
     */
    public void setAnzSteps(int[] anzSteps) {
        this.anzSteps = anzSteps;
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
