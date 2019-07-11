package sudoku.lite.solver;

import sudoku.lite.*;

import java.util.ArrayList;
import java.util.List;

public class SudokuSolver {
    private SudokuStepFinder stepFinder = new SudokuStepFinder();
    private Sudoku sudoku;
    private List<SolutionStep> steps = new ArrayList<>();

    public boolean solve(Sudoku sudoku) {
        setSudoku(sudoku);

        SolutionStep step;

        do {
            step = getHint();
            if (step != null) {
                steps.add(step);
                getStepFinder().doStep(step);
                if (step.getType() == SolutionType.GIVE_UP) {
                    step = null;
                }
            }
        } while (step != null);

        return sudoku.isSolved();
    }

    public SolutionStep getHint(Sudoku sudoku) {
        Sudoku save = this.sudoku;
        setSudoku(sudoku);
        SolutionStep step = getHint();
        setSudoku(save);
        return step;
    }

    private SolutionStep getHint() {
        return getHint(Config.DEFAULT_SOLVER_STEPS);
    }

    private SolutionStep getHint(StepConfig[] solverSteps) {
        if (sudoku.isSolved()) {
            return null;
        }

        SolutionStep hint;
        for (StepConfig solverStep : solverSteps) {
            if (!solverStep.isEnabled()) {
                continue;
            }
            SolutionType type = solverStep.getType();

            hint = getStepFinder().getStep(type);

            if (hint != null) {
                return hint;
            }
        }

        return null;
    }

    public void doStep(Sudoku sudoku, SolutionStep step) {
        Sudoku oldSudoku = getSudoku();
        getStepFinder().setSudoku(sudoku);
        getStepFinder().doStep(step);
        getStepFinder().setSudoku(oldSudoku);
    }

    public Sudoku getSudoku() {
        return sudoku;
    }

    public void setSudoku(Sudoku sudoku, List<SolutionStep> partSteps) {
        steps = new ArrayList<>();
        steps.addAll(partSteps);
        this.sudoku = sudoku;
        getStepFinder().setSudoku(sudoku);
    }

    public void setSudoku(Sudoku sudoku) {
        SudokuUtil.clearStepList(steps);
        this.sudoku = sudoku;
        getStepFinder().setSudoku(sudoku);
    }

    public List<SolutionStep> getSteps() {
        return steps;
    }

    public void setSteps(List<SolutionStep> steps) {
        this.steps = steps;
    }

    public SudokuStepFinder getStepFinder() {
        return stepFinder;
    }
}
