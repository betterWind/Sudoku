package sudoku.lite;

import sudoku.lite.solver.SudokuSolver;

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
//        long start = System.currentTimeMillis();

        Sudoku sudoku = new Sudoku();
//        System.out.println("Time1: " + (System.currentTimeMillis() - start));
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

//        System.out.println("Time2: " + (System.currentTimeMillis() - start));
        SudokuSolver solver = new SudokuSolver();
        solver.solve(sudoku.clone());
//        System.out.println("Time3: " + (System.currentTimeMillis() - start));

        for (SolutionStep step : solver.getSteps()) {
            System.out.println(step);
        }
//        System.out.println("Time4: " + (System.currentTimeMillis() - start));

    }
}
