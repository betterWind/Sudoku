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

package sudoku.generator;

import sudoku.*;
import sudoku.solver.SudokuSolver;

import java.awt.*;

public class BackgroundGenerator {

    //最多尝试次数
    private static final int MAX_TRY_COUNT = 20000;

    private int tryCount = 0;

    public Sudoku generate(DifficultyLevel level) {
        tryCount = 0;

        SudokuGenerator creator = new SudokuGenerator();
        SudokuSolver solver = new SudokuSolver();

        Sudoku sudoku = null;

        while (true) {
            sudoku = creator.generateSudoku(true);
            if (sudoku == null) {
                return null;
            }
            Sudoku solvedSudoku = sudoku.clone();
            boolean ok = solver.solve(level, solvedSudoku, true, false, Options.getInstance().solverSteps);
            if (ok && solvedSudoku.getLevel().getOrdinal() == level.getOrdinal()) {
                sudoku.setLevel(solvedSudoku.getLevel());
                sudoku.setScore(solvedSudoku.getScore());
                break;
            }

            tryCount++;

            if (tryCount >= MAX_TRY_COUNT) {
                sudoku = null;
                break;
            }
        }

        return sudoku;
    }

    public static void main(String[] args) {
        long s = System.currentTimeMillis();
        System.out.println(s);
        BackgroundGenerator generator = new BackgroundGenerator();
        for (int i = 0; i < 1; i++) {
//            Sudoku sudoku = generator.generate(new DifficultyLevel(DifficultyType.EASY, 500, "Extreme", new Color(255, 100, 100), Color.BLACK));
//            System.out.println(sudoku.getSudoku(ClipboardMode.CLUES_ONLY));

            Sudoku sudoku = new Sudoku();
            sudoku.setSudoku("9.37....1...6.95...4...19.....3..29.8.9.....4..6.97....9.5.6.2...79.3...3....28.9");
            SudokuSolver solver = new SudokuSolver();
            solver.solve(new DifficultyLevel(DifficultyType.EXTREME, Integer.MAX_VALUE, "Extreme", new Color(255, 100, 100), Color.BLACK),
                    sudoku.clone(),
                    true,
                    false,
                    Options.getInstance().solverSteps);
            for (SolutionStep step : solver.getSteps()) {
                System.out.println(step);
            }
        }
        System.out.println(System.currentTimeMillis() - s);
    }
}
