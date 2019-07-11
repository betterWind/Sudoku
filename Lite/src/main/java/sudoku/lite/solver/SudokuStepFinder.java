package sudoku.lite.solver;

import sudoku.lite.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SudokuStepFinder {
    private SimpleSolver simpleSolver;
    private FishSolver fishSolver;
    private SingleDigitPatternSolver singleDigitPatternSolver;
    private UniquenessSolver uniquenessSolver;
    private WingSolver wingSolver;
    private ColoringSolver coloringSolver;
    private ChainSolver chainSolver;
    private AlsSolver alsSolver;
    private MiscellaneousSolver miscellaneousSolver;
    private TablingSolver tablingSolver;
    private TemplateSolver templateSolver;
    private BruteForceSolver bruteForceSolver;
    private IncompleteSolver incompleteSolver;
    private GiveUpSolver giveUpSolver;
    private AbstractSolver[] solvers;
    private Sudoku sudoku;

    private int stepNumber = 0;

    private boolean initialized;

    private SudokuSet[] candidates = new SudokuSet[10];
    private boolean candidatesDirty = true;
    private SudokuSet[] positions = new SudokuSet[10];
    private boolean positionsDirty = true;
    private SudokuSet[] candidatesAllowed = new SudokuSet[10];
    private boolean candidatesAllowedDirty = true;
    private SudokuSet emptyCells = new SudokuSet();
    private SudokuSet[] setValueTemplates = new SudokuSet[10];
    private SudokuSet[] delCandTemplates = new SudokuSet[10];
    private List<List<SudokuSetBase>> candTemplates;
    private boolean templatesDirty = true;
    private boolean templatesListDirty = true;
    private List<Als> alsesOnlyLargerThanOne = null;
    private int alsesOnlyLargerThanOneStepNumber = -1;
    private List<Als> alsesWithOne = null;
    private int alsesWithOneStepNumber = -1;
    private List<RestrictedCommon> restrictedCommons = null;
    private int[] startIndices = null;
    private int[] endIndices = null;
    private boolean lastRcAllowOverlap;
    private int lastRcStepNumber = -1;
    private List<Als> lastRcAlsList = null;
    private boolean lastRcOnlyForward = true;
    private boolean rcOnlyForward = true;

    private SudokuSet indexSet = new SudokuSet();
    private short[] candSets = new short[10];

    private SudokuSet restrictedCommonBuddiesSet = new SudokuSet();
    private SudokuSet restrictedCommonIndexSet = new SudokuSet();
    private SudokuSet intersectionSet = new SudokuSet();

    public SudokuStepFinder() {
        initialized = false;
    }

    private void initialize() {
        if (initialized) {
            return;
        }

        for (int i = 0; i < candidates.length; i++) {
            candidates[i] = new SudokuSet();
            positions[i] = new SudokuSet();
            candidatesAllowed[i] = new SudokuSet();
        }

        candTemplates = new ArrayList<>(10);
        for (int i = 0; i < setValueTemplates.length; i++) {
            setValueTemplates[i] = new SudokuSet();
            delCandTemplates[i] = new SudokuSet();
            candTemplates.add(i, new LinkedList<SudokuSetBase>());
        }

        simpleSolver = new SimpleSolver(this);
        fishSolver = new FishSolver(this);
        singleDigitPatternSolver = new SingleDigitPatternSolver(this);
        uniquenessSolver = new UniquenessSolver(this);
        wingSolver = new WingSolver(this);
        coloringSolver = new ColoringSolver(this);
        chainSolver = new ChainSolver(this);
        alsSolver = new AlsSolver(this);
        miscellaneousSolver = new MiscellaneousSolver(this);
        tablingSolver = new TablingSolver(this);
        templateSolver = new TemplateSolver(this);
        bruteForceSolver = new BruteForceSolver(this);
        incompleteSolver = new IncompleteSolver(this);
        giveUpSolver = new GiveUpSolver(this);
        solvers = new AbstractSolver[]{
                simpleSolver,
                fishSolver,
                singleDigitPatternSolver,
                uniquenessSolver,
                wingSolver,
                coloringSolver,
                chainSolver,
                alsSolver,
                miscellaneousSolver,
                tablingSolver,
                templateSolver,
                bruteForceSolver,
                incompleteSolver,
                giveUpSolver
        };
        initialized = true;
    }

    public void cleanUp() {
        if (solvers == null) {
            return;
        }
        for (AbstractSolver solver : solvers) {
            solver.cleanUp();
        }
    }

    public SolutionStep getStep(SolutionType type) {
        initialize();
        SolutionStep result = null;
        for (AbstractSolver solver : solvers) {
            if ((result = solver.getStep(type)) != null) {
                stepNumber++;
                return result;
            }
        }
        return result;
    }

    public void doStep(SolutionStep step) {
        initialize();
        for (AbstractSolver solver : solvers) {
            if (solver.doStep(step)) {
                setSudokuDirty();
                return;
            }
        }
        throw new RuntimeException("Invalid solution step in doStep() (" + step.getType() + ")");
    }

    public void setSudokuDirty() {
        candidatesDirty = true;
        candidatesAllowedDirty = true;
        positionsDirty = true;
        templatesDirty = true;
        templatesListDirty = true;
        stepNumber++;
    }

    public void setSudoku(Sudoku sudoku) {
        if (sudoku != null && this.sudoku != sudoku) {
            this.sudoku = sudoku;
        }
        setSudokuDirty();
    }

    public Sudoku getSudoku() {
        return sudoku;
    }

    protected TablingSolver getTablingSolver() {
        return tablingSolver;
    }

    /******************************************************************************************************************/
    /* EXPOSE PUBLIC APIs                                                                                             */

    /******************************************************************************************************************/

    public List<SolutionStep> findAllFullHouses(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = simpleSolver.findAllFullHouses();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> findAllNakedSingles(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = simpleSolver.findAllNakedSingles();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> findAllNakedXle(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = simpleSolver.findAllNakedXle();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> findAllHiddenSingles(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = simpleSolver.findAllHiddenSingles();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> findAllHiddenXle(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = simpleSolver.findAllHiddenXle();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> findAllLockedCandidates(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = simpleSolver.findAllLockedCandidates();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> findAllLockedCandidates1(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = simpleSolver.findAllLockedCandidates();
        setSudoku(oldSudoku);
        // filter the steps
        List<SolutionStep> resultList = new ArrayList<SolutionStep>();
        for (SolutionStep step : steps) {
            if (step.getType().equals(SolutionType.LOCKED_CANDIDATES_1)) {
                resultList.add(step);
            }
        }
        return resultList;
    }

    public List<SolutionStep> findAllLockedCandidates2(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = simpleSolver.findAllLockedCandidates();
        setSudoku(oldSudoku);
        // filter the steps
        List<SolutionStep> resultList = new ArrayList<SolutionStep>();
        for (SolutionStep step : steps) {
            if (step.getType().equals(SolutionType.LOCKED_CANDIDATES_2)) {
                resultList.add(step);
            }
        }
        return resultList;
    }

    public List<SolutionStep> findAllEmptyRectangles(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = singleDigitPatternSolver.findAllEmptyRectangles();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> findAllSkyScrapers(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = singleDigitPatternSolver.findAllSkyscrapers();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> findAllTwoStringKites(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = singleDigitPatternSolver.findAllTwoStringKites();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> getAllUniqueness(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = uniquenessSolver.getAllUniqueness();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> getAllWings(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = wingSolver.getAllWings();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> findAllSimpleColors(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = coloringSolver.findAllSimpleColors();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> findAllMultiColors(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = coloringSolver.findAllMultiColors();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> getAllChains(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = chainSolver.getAllChains();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> getAllAlses(Sudoku newSudoku, boolean doXz, boolean doXy, boolean doChain) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = alsSolver.getAllAlses(doXz, doXy, doChain);
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> getAllDeathBlossoms(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = alsSolver.getAllDeathBlossoms();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> getAllSueDeCoqs(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = miscellaneousSolver.getAllSueDeCoqs();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> getAllNiceLoops(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = tablingSolver.getAllNiceLoops();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> getAllGroupedNiceLoops(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = tablingSolver.getAllGroupedNiceLoops();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> getAllForcingChains(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = tablingSolver.getAllForcingChains();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> getAllForcingNets(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = tablingSolver.getAllForcingNets();
        setSudoku(oldSudoku);
        return steps;
    }

    public List<SolutionStep> getAllTemplates(Sudoku newSudoku) {
        initialize();
        Sudoku oldSudoku = getSudoku();
        setSudoku(newSudoku);
        List<SolutionStep> steps = templateSolver.getAllTemplates();
        setSudoku(oldSudoku);
        return steps;
    }

    /******************************************************************************************************************/
    /* END EXPOSE PUBLIC APIs                                                                                         */
    /******************************************************************************************************************/

    /******************************************************************************************************************/
    /* SETS                                                                                                           */
    /******************************************************************************************************************/

    /**
     * Returns the {@link #candidates}. Recalculates them if they are dirty.
     *
     * @return
     */
    public SudokuSet[] getCandidates() {
        if (candidatesDirty) {
            initCandidates();
        }
        return candidates;
    }

    /**
     * Returns the {@link #positions}. Recalculates them if they are dirty.
     *
     * @return
     */
    public SudokuSet[] getPositions() {
        if (positionsDirty) {
            initPositions();
        }
        return positions;
    }

    /**
     * Create the sets that contain all cells, in which a specific candidate is still present.
     */
    private void initCandidates() {
        if (candidatesDirty) {
            for (int i = 1; i < candidates.length; i++) {
                candidates[i].clear();
            }
            short[] cells = sudoku.getCells();
            for (int i = 0; i < cells.length; i++) {
                int[] cands = Sudoku.POSSIBLE_VALUES[cells[i]];
                for (int j = 0; j < cands.length; j++) {
                    candidates[cands[j]].add(i);
                }
            }
            candidatesDirty = false;
        }
    }

    /**
     * Create the sets that contain all cells, in which a specific candidate is already set.
     */
    private void initPositions() {
        if (positionsDirty) {
            for (int i = 1; i < positions.length; i++) {
                positions[i].clear();
            }
            int[] values = sudoku.getValues();
            for (int i = 0; i < values.length; i++) {
                if (values[i] != 0) {
                    positions[values[i]].add(i);
                }
            }
            positionsDirty = false;
        }
    }

    /**
     * Returns the {@link #candidatesAllowed}. Recalculates them if they are dirty.
     *
     * @return
     */
    public SudokuSet[] getCandidatesAllowed() {
        if (candidatesAllowedDirty) {
            initCandidatesAllowed();
        }
        return candidatesAllowed;
    }

    /**
     * Returns the {@link #emptyCells}. Recalculates them if they are dirty.
     *
     * @return
     */
    public SudokuSet getEmptyCells() {
        if (candidatesAllowedDirty) {
            initCandidatesAllowed();
        }
        return emptyCells;
    }

    /**
     * Create the sets that contain all cells, in which a specific candidate is still valid.
     */
    private void initCandidatesAllowed() {
        if (candidatesAllowedDirty) {
            emptyCells.setAll();
            for (int i = 1; i < candidatesAllowed.length; i++) {
                candidatesAllowed[i].setAll();
            }
            int[] values = sudoku.getValues();
            for (int i = 0; i < values.length; i++) {
                if (values[i] != 0) {
                    candidatesAllowed[values[i]].andNot(Sudoku.buddies[i]);
                    emptyCells.remove(i);
                }
            }
            for (int i = 1; i < candidatesAllowed.length; i++) {
                candidatesAllowed[i].and(emptyCells);
            }
            candidatesAllowedDirty = false;
        }
    }

    /******************************************************************************************************************/
    /* END SETS                                                                                                       */
    /******************************************************************************************************************/

    /******************************************************************************************************************/
    /* TEMPLATES                                                                                                      */
    /******************************************************************************************************************/

    /**
     * Returns delCandTemplates.
     *
     * @param initLists
     * @return
     */
    protected SudokuSet[] getDelCandTemplates(boolean initLists) {
        if ((initLists && templatesListDirty) || (!initLists && templatesDirty)) {
            initCandTemplates(initLists);
        }
        return delCandTemplates;
    }

    /**
     * Returns setValueTemplates.
     *
     * @param initLists
     * @return
     */
    protected SudokuSet[] getSetValueTemplates(boolean initLists) {
        if ((initLists && templatesListDirty) || (!initLists && templatesDirty)) {
            initCandTemplates(initLists);
        }
        return setValueTemplates;
    }

    private void initCandTemplates(boolean initLists) {
        if ((initLists && templatesListDirty) || (!initLists && templatesDirty)) {
            SudokuSetBase[] allowedPositions = getCandidates();
            SudokuSet[] setPositions = getPositions();
            SudokuSetBase[] templates = Sudoku.templates;
            SudokuSetBase[] forbiddenPositions = new SudokuSetBase[10];

            for (int i = 1; i <= 9; i++) {
                setValueTemplates[i].setAll();
                delCandTemplates[i].clear();
                candTemplates.get(i).clear();

                forbiddenPositions[i] = new SudokuSetBase();
                forbiddenPositions[i].set(setPositions[i]);
                forbiddenPositions[i].or(allowedPositions[i]);
                forbiddenPositions[i].not();
            }

            for (SudokuSetBase template : templates) {
                for (int j = 1; j <= 9; j++) {
                    if (!setPositions[j].andEquals(template)) {
                        continue;
                    }
                    if (!forbiddenPositions[j].andEmpty(template)) {
                        continue;
                    }
                    setValueTemplates[j].and(template);
                    delCandTemplates[j].or(template);
                    if (initLists) {
                        candTemplates.get(j).add(template);
                    }
                }
            }

            if (initLists) {
                int removals;
                do {
                    removals = 0;
                    for (int j = 1; j <= 9; j++) {
                        setValueTemplates[j].setAll();
                        delCandTemplates[j].clear();
                        ListIterator<SudokuSetBase> it = candTemplates.get(j).listIterator();
                        while (it.hasNext()) {
                            SudokuSetBase template = it.next();
                            boolean removed = false;
                            for (int k = 1; k <= 9; k++) {
                                if (k != j && !template.andEmpty(setValueTemplates[k])) {
                                    it.remove();
                                    removed = true;
                                    removals++;
                                    break;
                                }
                            }
                            if (!removed) {
                                setValueTemplates[j].and(template);
                                delCandTemplates[j].or(template);
                            }
                        }
                    }
                } while (removals > 0);
            }

            for (int i = 1; i <= 9; i++) {
                delCandTemplates[i].not();
            }
            templatesDirty = false;
            if (initLists) {
                templatesListDirty = false;
            }
        }
    }

    /**
     * @return the stepNumber
     */
    public int getStepNumber() {
        return stepNumber;
    }
    /******************************************************************************************************************/
    /* END TEMPLATES                                                                                                  */
    /******************************************************************************************************************/

    /******************************************************************************************************************/
    /* ALS AND RC CACHE                                                                                               */
    /******************************************************************************************************************/

    /**
     * Convenience method for {@link #getAlses(boolean) }.
     *
     * @return
     */
    public List<Als> getAlses() {
        return getAlses(false);
    }

    public List<Als> getAlses(boolean onlyLargerThanOne) {
        if (onlyLargerThanOne) {
            if (alsesOnlyLargerThanOneStepNumber == stepNumber) {
                return alsesOnlyLargerThanOne;
            } else {
                alsesOnlyLargerThanOne = doGetAlses(onlyLargerThanOne);
                alsesOnlyLargerThanOneStepNumber = stepNumber;
                return alsesOnlyLargerThanOne;
            }
        } else {
            if (alsesWithOneStepNumber == stepNumber) {
                return alsesWithOne;
            } else {
                alsesWithOne = doGetAlses(onlyLargerThanOne);
                alsesWithOneStepNumber = stepNumber;
                return alsesWithOne;
            }
        }
    }

    /**
     * Does some statistics and starts the recursive search for every house.
     *
     * @param onlyLargerThanOne
     * @return
     */
    private List<Als> doGetAlses(boolean onlyLargerThanOne) {
        long actNanos = System.nanoTime();

        // this is the list we will be working with
        List<Als> alses = new ArrayList<Als>(300);
        alses.clear();

        // recursion is started once for every cell in every house
        for (int i = 0; i < Sudoku.ALL_UNITS.length; i++) {
            for (int j = 0; j < Sudoku.ALL_UNITS[i].length; j++) {
                indexSet.clear();
                candSets[0] = 0;
                checkAlsRecursive(0, j, Sudoku.ALL_UNITS[i], alses, onlyLargerThanOne);
            }
        }

        // compute fields
        for (Als als : alses) {
            als.computeFields(this);
        }

        return alses;
    }

    private void checkAlsRecursive(int anzahl, int startIndex, int[] indexe,
                                   List<Als> alses, boolean onlyLargerThanOne) {
        anzahl++;
        if (anzahl > indexe.length - 1) {
            // end recursion (no more than 8 cells in an ALS possible)
            return;
        }
        for (int i = startIndex; i < indexe.length; i++) {
            int houseIndex = indexe[i];
            if (sudoku.getValue(houseIndex) != 0) {
                // cell already set -> ignore
                continue;
            }
            indexSet.add(houseIndex);
            candSets[anzahl] = (short) (candSets[anzahl - 1] | sudoku.getCell(houseIndex));

            // if the number of candidates is excatly one larger than the number
            // of cells, an ALS was found
            if (Sudoku.ANZ_VALUES[candSets[anzahl]] - anzahl == 1) {
                if (!onlyLargerThanOne || indexSet.size() > 1) {
                    // found one -> save it if it doesnt exist already
                    Als newAls = new Als(indexSet, candSets[anzahl]);
                    if (!alses.contains(newAls)) {
                        alses.add(newAls);
                    } else {
                    }
                }
            }

            // continue recursion
            checkAlsRecursive(anzahl, i + 1, indexe, alses, onlyLargerThanOne);

            // remove current cell
            indexSet.remove(houseIndex);
        }
    }

    public List<RestrictedCommon> getRestrictedCommons(List<Als> alses, boolean allowOverlap) {
        if (lastRcStepNumber != stepNumber || lastRcAllowOverlap != allowOverlap ||
                lastRcAlsList != alses || lastRcOnlyForward != rcOnlyForward) {
            // recompute
            if (startIndices == null || startIndices.length < alses.size()) {
                startIndices = new int[(int) (alses.size() * 1.5)];
                endIndices = new int[(int) (alses.size() * 1.5)];
            }
            restrictedCommons = doGetRestrictedCommons(alses, allowOverlap);
            // store caching flags
            lastRcStepNumber = stepNumber;
            lastRcAllowOverlap = allowOverlap;
            lastRcOnlyForward = rcOnlyForward;
            lastRcAlsList = alses;
        }
        return restrictedCommons;
    }

    public int[] getStartIndices() {
        return startIndices;
    }

    public int[] getEndIndices() {
        return endIndices;
    }

    public void setRcOnlyForward(boolean rof) {
        rcOnlyForward = rof;
    }

    private List<RestrictedCommon> doGetRestrictedCommons(List<Als> alses, boolean withOverlap) {
        lastRcOnlyForward = rcOnlyForward;
        List<RestrictedCommon> rcs = new ArrayList<>(2000);
        for (int i = 0; i < alses.size(); i++) {
            Als als1 = alses.get(i);
            startIndices[i] = rcs.size();
            //if (DEBUG) System.out.println("als1: " + SolutionStep.getAls(als1));
            int start = 0;
            if (rcOnlyForward) {
                start = i + 1;
            }
            for (int j = start; j < alses.size(); j++) {
                if (i == j) {
                    continue;
                }
                Als als2 = alses.get(j);
                // check whether the ALS overlap (intersectionSet is needed later on anyway)
                intersectionSet.set(als1.indices);
                intersectionSet.and(als2.indices);
                if (!withOverlap && !intersectionSet.isEmpty()) {
                    // overlap is not allowed!
                    continue;
                }
                //if (DEBUG) System.out.println("als2: " + SolutionStep.getAls(als2));
                // restricted common: all buddies + the positions of the candidates themselves ANDed
                // check whether als1 and als2 have common candidates
                /**
                 * All candidates common to two ALS.
                 */
                short possibleRestrictedCommonsSet = als1.candidates;
                possibleRestrictedCommonsSet &= als2.candidates;
                // possibleRestrictedCommons now contains all candidates common to both ALS
                if (possibleRestrictedCommonsSet == 0) {
                    // nothing to do!
                    continue;
                }
                // number of RC candidates found for this ALS combination
                int rcAnz = 0;
                RestrictedCommon newRC = null;
                int[] prcs = Sudoku.POSSIBLE_VALUES[possibleRestrictedCommonsSet];
                for (int k = 0; k < prcs.length; k++) {
                    int cand = prcs[k];
                    // Get all positions of cand in both ALS
                    restrictedCommonIndexSet.set(als1.indicesPerCandidat[cand]);
                    restrictedCommonIndexSet.or(als2.indicesPerCandidat[cand]);
                    // non of these positions may be in the overlapping area of the two ALS
                    if (!restrictedCommonIndexSet.andEmpty(intersectionSet)) {
                        // at least on occurence of cand is in overlap -> forbidden
                        continue;
                    }
                    // now check if all those candidates see each other
                    restrictedCommonBuddiesSet.setAnd(als1.buddiesAlsPerCandidat[cand],
                            als2.buddiesAlsPerCandidat[cand]);
                    // we now know all common buddies, all common candidates must be in that set
                    if (restrictedCommonIndexSet.andEquals(restrictedCommonBuddiesSet)) {
                        // found -> cand is RC
                        if (rcAnz == 0) {
                            newRC = new RestrictedCommon(i, j, cand);
                            rcs.add(newRC);
                        } else {
                            newRC.setCand2(cand);
                        }
                        rcAnz++;
                    }
                }
                if (rcAnz > 0) {
                    //if (DEBUG) System.out.println(newRC + ": " + rcAnz + " RCs for ALS " + SolutionStep.getAls(als1) + "/" + SolutionStep.getAls(als2));
                }
            }
            endIndices[i] = rcs.size();
        }
        return rcs;
    }

    public void printStatistics() {
//        double per = ((double)templateNanos) / templateAnz;
//        per /= 1000.0;
//        double total = ((double)templateNanos) / 1000000.0;
//        System.out.printf("Templates: %d calls, %.2fus per call, %.2fms total%n", templateAnz, per, total);
//        fishSolver.printStatistics();
//        chainSolver.printStatistics();
    }
}
