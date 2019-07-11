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

import sudoku.lite.generator.GeneratorPattern;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author hobiwan
 */
public final class Options {

    public static final int RESTRICT_CHAIN_LENGTH = 20;      // maximale Länge von X-/XY-Chains, wenn restrictChainSize gesetzt ist
    public static final int RESTRICT_NICE_LOOP_LENGTH = 10;  // maximale Länge von Nice-Loops, wenn restrictChainSize gesetzt ist
    public static final boolean RESTRICT_CHAIN_SIZE = true;  // Länge der chains beschränken?
    public static final int MAX_TABLE_ENTRY_LENGTH = 1000;
    public static final int ANZ_TABLE_LOOK_AHEAD = 4;
    public static final boolean ONLY_ONE_CHAIN_PER_STEP = true;
    public static final boolean ALLOW_ALS_IN_TABLING_CHAINS = false;
    public static final boolean ONLY_ONE_ALS_PER_STEP = true; // only one step in every ALS elimination
    public static final int MAX_FINS = 5;                 // Maximale Anzahl Fins
    public static final int MAX_ENDO_FINS = 2;            // Maximale Anzahl Endo-Fins
    public static final boolean CHECK_TEMPLATES = true;   // Template-Check um Kandidaten von der Suche auszuschließen
    public static final int KRAKEN_MAX_FISH_TYPE = 1;     // 0: nur basic, 1: basic+franken, 2: basic+franken+mutant
    public static final int KRAKEN_MAX_FISH_SIZE = 4;     // number of units in base/cover sets
    public static final int MAX_KRAKEN_FINS = 2;          // Maximale Anzahl Fins für Kraken-Suche
    public static final int MAX_KRAKEN_ENDO_FINS = 0;     // Maximale Anzahl Endo-Fins für Kraken-Suche
    public static final boolean ONLY_ONE_FISH_PER_STEP = true; // only the smallest fish for every elimination
    public static final int FISH_DISPLAY_MODE = 0;        // 0: normal; 1: statistics numbers; 2: statistics cells
    private int maxFins = MAX_FINS;
    private int maxEndoFins = MAX_ENDO_FINS;
    private boolean checkTemplates = CHECK_TEMPLATES;
    public static final int ALL_STEPS_ALS_CHAIN_LENGTH = 6; // maximum chain length in ALS-Chain search (all steps only)
    public static final boolean ALL_STEPS_ALS_CHAIN_FORWARD_ONLY = true;
    public static final Color[] COLORING_COLORS = {
            new Color(255, 192, 89),  // 'a' - first color of first color pair
            new Color(247, 222, 143), // 'A' - second color of first color pair
            new Color(177, 165, 243), // 'b' - first color of second color pair
            new Color(220, 212, 252), // 'B' - second color of second color pair
            new Color(247, 165, 167), // 'c' - first color of third color pair
            new Color(255, 210, 210), // 'C' - second color of third color pair
            new Color(134, 232, 208), // 'd' - first color of fourth color pair
            new Color(206, 251, 237), // 'D' - second color of fourth color pair
            new Color(134, 242, 128), // 'e' - first color of fifth color pair
            new Color(215, 255, 215) // 'E' - second color of fifth color pair
    };
    private Color[] coloringColors;
    public static final boolean ALLOW_ERS_WITH_ONLY_TWO_CANDIDATES = false; // as it sais...
    public static final boolean ALLOW_DUALS_AND_SIAMESE = false; // Dual 2-String-Kites, Dual Skyscrapers && Siamese Fish
    public static final boolean ALLOW_UNIQUENESS_MISSING_CANDIDATES = true; // allow missing candidates in cells with additional candidates
    public static final boolean USE_OR_INSTEAD_OF_AND_FOR_FILTER = false; // used when filtering more than one candidate
    public static final boolean USE_ZERO_INSTEAD_OF_DOT = false; // as the name says...
    public static final int GENERATOR_PATTERN_INDEX = -1;
    private ArrayList<GeneratorPattern> generatorPatterns = new ArrayList<>();

    private static Options instance = new Options();

    private Options() {
        coloringColors = new Color[COLORING_COLORS.length];
        for (int i = 0; i < COLORING_COLORS.length; i++) {
            coloringColors[i] = new Color(COLORING_COLORS[i].getRGB());
        }
    }

    public static Options getInstance() {
        return instance;
    }

    /**
     * @return the fishDisplayMode
     */
    public int getFishDisplayMode() {
        return FISH_DISPLAY_MODE;
    }

    /**
     * @return the allowUniquenessMissingCandidates
     */
    public boolean isAllowUniquenessMissingCandidates() {
        return ALLOW_UNIQUENESS_MISSING_CANDIDATES;
    }

    /**
     * @return the generatorPatterns
     */
    public ArrayList<GeneratorPattern> getGeneratorPatterns() {
        return generatorPatterns;
    }

    /**
     * @return the generatorPatternIndex
     */
    public int getGeneratorPatternIndex() {
        return GENERATOR_PATTERN_INDEX;
    }

    public boolean isUseOrInsteadOfAndForFilter() {
        return USE_OR_INSTEAD_OF_AND_FOR_FILTER;
    }

    /**
     * @return the allStepsAlsChainLength
     */
    public int getAllStepsAlsChainLength() {
        return ALL_STEPS_ALS_CHAIN_LENGTH;
    }

    /**
     * @return the allStepsAlsChainForwardOnly
     */
    public boolean isAllStepsAlsChainForwardOnly() {
        return ALL_STEPS_ALS_CHAIN_FORWARD_ONLY;
    }

    public int getRestrictChainLength() {
        return RESTRICT_CHAIN_LENGTH;
    }

    public int getRestrictNiceLoopLength() {
        return RESTRICT_NICE_LOOP_LENGTH;
    }

    public boolean isRestrictChainSize() {
        return RESTRICT_CHAIN_SIZE;
    }

    public int getMaxFins() {
        return maxFins;
    }

    public void setMaxFins(int maxFins) {
        this.maxFins = maxFins;
    }

    public int getMaxEndoFins() {
        return maxEndoFins;
    }

    public void setMaxEndoFins(int maxEndoFins) {
        this.maxEndoFins = maxEndoFins;
    }

    public boolean isCheckTemplates() {
        return checkTemplates;
    }

    public void setCheckTemplates(boolean checkTemplates) {
        this.checkTemplates = checkTemplates;
    }

    public int getMaxTableEntryLength() {
        return MAX_TABLE_ENTRY_LENGTH;
    }

    public int getAnzTableLookAhead() {
        return ANZ_TABLE_LOOK_AHEAD;
    }

    public Color[] getColoringColors() {
        return coloringColors;
    }

    public boolean isUseZeroInsteadOfDot() {
        return USE_ZERO_INSTEAD_OF_DOT;
    }

    public boolean isAllowErsWithOnlyTwoCandidates() {
        return ALLOW_ERS_WITH_ONLY_TWO_CANDIDATES;
    }

    public int getKrakenMaxFishType() {
        return KRAKEN_MAX_FISH_TYPE;
    }

    public int getMaxKrakenFins() {
        return MAX_KRAKEN_FINS;
    }

    public int getMaxKrakenEndoFins() {
        return MAX_KRAKEN_ENDO_FINS;
    }

    public int getKrakenMaxFishSize() {
        return KRAKEN_MAX_FISH_SIZE;
    }

    public boolean isAllowDualsAndSiamese() {
        return ALLOW_DUALS_AND_SIAMESE;
    }

    public boolean isOnlyOneFishPerStep() {
        return ONLY_ONE_FISH_PER_STEP;
    }

    public boolean isOnlyOneAlsPerStep() {
        return ONLY_ONE_ALS_PER_STEP;
    }

    public boolean isOnlyOneChainPerStep() {
        return ONLY_ONE_CHAIN_PER_STEP;
    }

    public boolean isAllowAlsInTablingChains() {
        return ALLOW_ALS_IN_TABLING_CHAINS;
    }
}
