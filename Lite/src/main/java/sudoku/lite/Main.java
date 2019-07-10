package sudoku.lite;

import sudoku.lite.solver.SudokuSolver;

import java.awt.*;

//        sudoku.setSudoku("+-------+-------+-------+\n" +
//                "         | . . . | . . . | . . . |\n" +
//                "         | . . . | . . . | . . . |\n" +
//                "         | . . . | . . . | . . . |\n" +
//                "         +-------+-------+-------+\n" +
//                "         | . . . | . . . | . . . |\n" +
//                "         | . . . | . . . | . . . |\n" +
//                "         | . . . | . . . | . . . |\n" +
//                "         +-------+-------+-------+\n" +
//                "         | . . . | . . . | . . . |\n" +
//                "         | . . . | . . . | . . . |\n" +
//                "         | . . . | . . . | . . . |\n" +
//                "         +-------+-------+-------+");

public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        Sudoku sudoku = new Sudoku();
        sudoku.setSudoku("+-------+-------+-------+\n" +
                "         | . 9 . | . . . | 3 . . |\n" +
                "         | . . . | 3 7 . | . 4 . |\n" +
                "         | 3 . 6 | . 4 . | . 8 1 |\n" +
                "         +-------+-------+-------+\n" +
                "         | 9 . . | . . 1 | . 3 . |\n" +
                "         | . . . | . 2 . | . . . |\n" +
                "         | . 2 . | 6 . . | . . 4 |\n" +
                "         +-------+-------+-------+\n" +
                "         | 5 7 . | . 3 . | 4 . 2 |\n" +
                "         | . 6 . | . 5 4 | . . . |\n" +
                "         | . . 3 | . . . | . 9 . |\n" +
                "         +-------+-------+-------+");

        SudokuSolver solver = new SudokuSolver();
        solver.solve(new DifficultyLevel(DifficultyType.EXTREME, Integer.MAX_VALUE, "Extreme", new Color(255, 100, 100), Color.BLACK),
                sudoku.clone(),
                true,
                false,
                Options.getInstance().solverSteps);

        for (SolutionStep step : solver.getSteps()) {
            System.out.println(step);
        }

        System.out.println("Time: " + (System.currentTimeMillis() - start));
    }
}
