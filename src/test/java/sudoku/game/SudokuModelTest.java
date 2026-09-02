package sudoku.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Below are some example tests for the TicTacToe game.
 */

public class SudokuModelTest {

    @Test
    public void testCorrectPlacement() {
        SudokuModel model = new SudokuModel();

        int[][] puzzle = new int [9][9];
        int[][] solution = new int [9][9];

        solution[0][0] = 5;
        model.loadPuzzle(puzzle, solution);
        boolean result = model.placeNumber(0, 0, 5);

        assertTrue(result);
        assertEquals(5, model.getCell(0, 0));

    }

    @Test
    public void testWrongPlacement() {
        SudokuModel model = new SudokuModel();

        int[][] puzzle = new int[9][9];
        int[][] solution = new int[9][9];

        solution[0][0] = 5;
        model.loadPuzzle(puzzle, solution);
        boolean result = model.placeNumber(0, 0, 3);

        assertFalse(result);
        assertEquals(1, model.getMistakes());
    }

    @Test
    public void testGivenDontChange() {
        SudokuModel model = new SudokuModel();

        int[][] puzzle = new int[9][9];
        int[][] solution = new int[9][9];

        puzzle[0][0] = 8;
        solution[0][0] = 8;

        model.loadPuzzle(puzzle, solution);
        boolean result = model.placeNumber(0, 0, 5);

        assertFalse(result);
        assertEquals(8, model.getCell(0,0));
    }

    @Test
    public void testDelete() {
        SudokuModel model = new SudokuModel();

        int[][] puzzle = new int[9][9];
        int[][] solution = new int[9][9];

        solution[0][0] = 5;
        model.loadPuzzle(puzzle, solution);

        model.placeNumber(0, 0, 5);
        model.placeNumber(0, 0, 0);

        assertEquals(0, model.getCell(0, 0));
    }

    @Test
    public void testThreeMistakes() {
        SudokuModel model = new SudokuModel();

        int[][] puzzle = new int[9][9];
        int[][] solution = new int[9][9];

        solution[0][0] = 5;
        model.loadPuzzle(puzzle, solution);

        model.placeNumber(0, 0, 1);
        model.placeNumber(0, 0, 2);
        model.placeNumber(0, 0, 3);

        assertTrue(model.isGameOver());
    }

    @Test
    public void testGameOverNoPlace() {
        SudokuModel model = new SudokuModel();

        int[][] puzzle = new int[9][9];
        int[][] solution = new int[9][9];

        solution[0][0] = 5;
        model.loadPuzzle(puzzle, solution);

        model.placeNumber(0, 0, 1);
        model.placeNumber(0, 1, 1);
        model.placeNumber(0, 2, 1);

        boolean result = model.placeNumber(1, 1, 5);

        assertFalse(result);
    }

    @Test
    public void testUndo() {
        SudokuModel model = new SudokuModel();

        int[][] puzzle = new int[9][9];
        int[][] solution = new int[9][9];

        solution[0][0] = 5;
        model.loadPuzzle(puzzle, solution);

        model.placeNumber(0,0, 5);
        model.loadPuzzle(puzzle, solution);

        model.placeNumber(0, 0, 5);
        model.undo();

        assertEquals(0, model.getCell(0, 0));
    }

    @Test
    public void testGameWon() {
        SudokuModel model = new SudokuModel();

        int[][] puzzle = new int[9][9];
        int[][] solution = new int[9][9];

        // fill board
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                puzzle[r][c] = 1;
                solution[r][c] = 1;
            }
        }

        model.loadPuzzle(puzzle, solution);

        assertTrue(model.gameWon());
    }

}
