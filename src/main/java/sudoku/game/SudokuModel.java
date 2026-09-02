package sudoku.game;

import java.util.ArrayList;
import java.io.*;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

/**
 * This class is a model for Sudoku.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 */
public class SudokuModel {
    // field
    private int[][] board;
    private int[][] solution;
    private boolean[][] given;
    private int mistakes;
    private int hintsUsed;
    private boolean gameOver;

    private ArrayList<int[][]> history;

    /**
     * Constructor sets up game state.
     */
    // set inital state before anything
    public SudokuModel() {
        board = new int[9][9];
        solution = new int[9][9];
        given = new boolean[9][9];
        mistakes = 0;
        hintsUsed = 0;
        gameOver = false;

        history = new ArrayList<>();
    }

    // load in puzzle and solution
    public void loadPuzzle(int[][] puzzle, int[][] sol) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                board[r][c] = puzzle[r][c];
                solution[r][c] = sol[r][c];
                given[r][c] = (puzzle[r][c] != 0);
            }
        }
        mistakes = 0;
        hintsUsed = 0;
        gameOver = false;
    }

    // copy current state of board
    private int[][] copyBoard() {
        int[][] copy = new int[9][9];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                copy[r][c] = board[r][c];
            }
        }
        return copy;
    }

    //helper for the generative part
    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[9][9];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                copy[r][c] = original[r][c];
            }
        }

        return copy;
    }

    // undo method
    public void undo() {
        if (!history.isEmpty()) {
            board = history.remove(history.size() - 1);
        }
    }

    /**
     * For placing a number: if the game is over or the number is given then
     * nothing happens. if the player presses delete or backspace (input 0)
     * then it allows it nothign really happens. If the placed number is the
     * correct number (matches solution) then allow it to be placed no consequenes
     * If the number is incorrect then icrease mistakes, if > 3 mistakes game over :(
     */
    public boolean placeNumber(int row, int col, int val) {
        if (gameOver || gameWon() || given[row][col]) {
            return false;
        }

        history.add(copyBoard());

        if (val == 0) {
            board[row][col] = 0;
            return true;
        }

        board[row][col] = val;

        if (solution[row][col] != val) {
            mistakes++;
            if (mistakes >= 3) {
                gameOver = true;
            }
            return false;
        }
        return true;
    }

    /**
     * methods to allow other parts of program to accsess info like numbers
     * in each cell, num mistakes, whether or not the game is over ect.
     */

    public int getCell(int row, int col) {
        return board[row][col];
    }

    public boolean isGiven(int row, int col) {
        return given[row][col];
    }

    public boolean isCorrect(int row, int col) {
        return board[row][col] == solution[row][col];
    }

    public int getMistakes() {
        return mistakes;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getHintsUsed() {
        return hintsUsed;
    }

    // if the game is won
    public boolean gameWon() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    return false;
                }
                if (board[r][c] != solution[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    // if the player lost the game lets them restart with 0 mistakes
    public void reset() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (!given[r][c]) {
                    board[r][c] = 0;
                }
            }
        }
        mistakes = 0;
        hintsUsed = 0;
        gameOver = false;
        history.clear();
    }

    public void saveGame(String filename) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename));

            writer.println(mistakes);

            // current board
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    writer.print(board[r][c]);
                }
                writer.println();
            }

            // solution
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    writer.print(solution[r][c]);
                }
                writer.println();
            }

            // given cells
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (given[r][c]) {
                        writer.print("1");
                    } else {
                        writer.print("0");
                    }
                }
                writer.println();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving... :(");
        }
    }

    public void loadGame(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            mistakes = Integer.parseInt(reader.readLine());

            // current board
            for (int r = 0; r < 9; r++) {
                String line = reader.readLine();
                for (int c = 0; c < 9; c++) {
                    board[r][c] = Character.getNumericValue(line.charAt(c));
                }
            }

            // solution
            for (int r = 0; r < 9; r++) {
                String line = reader.readLine();
                for (int c = 0; c < 9; c++) {
                    solution[r][c] = Character.getNumericValue(line.charAt(c));
                }
            }

            // given cells
            for (int r = 0; r < 9; r++) {
                String line = reader.readLine();
                for (int c = 0; c < 9; c++) {
                    given[r][c] = line.charAt(c) == '1';
                }
            }

            gameOver = mistakes >= 3;
            history.clear();

            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading... :(");
        }
    }

    private boolean solve(int[][] b) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (b[r][c] == 0) {
                    for (int val = 1; val <= 9; val++) {
                        if (isValid(b, r, c, val)) {
                            b[r][c] = val;

                            if (solve(b)) {
                                return true;
                            }
                            b[r][c] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] b, int row, int col, int val) {
        for (int i = 0; i < 9; i++) {
            if (b[row][i] == val) {
                return false;
            }
            if (b[i][col] == val) {
                return false;
            }
        }
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;

        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (b[r][c] == val) {
                    return false;
                }
            }
        }
        return true;
    }

    private class HintMove {
        int row;
        int col;
        int value;
        int tokens;

        HintMove(int row, int col, int value, int tokens) {
            this.row = row;
            this.col = col;
            this.value = value;
            this.tokens = tokens;
        }
    }

    public boolean giveHint() {
        if (gameOver || gameWon() || hintsUsed >= 2) {
            return false;
        }

        HintMove best = null;

        HintMove naked = findNakedSingleHint();
        if (naked != null) {
            best = naked;
        }

        HintMove hidden = findHiddenSingleHint();
        if (hidden != null &&
                (best == null || hidden.tokens < best.tokens)) {
            best = hidden;
        }

        if (best == null) {
            return false;
        }

        history.add(copyBoard());
        board[best.row][best.col] = best.value;
        hintsUsed++;
        return true;
    }

    private HintMove findNakedSingleHint() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    int count = 0;
                    int answer = 0;

                    for (int val = 1; val <= 9; val++) {
                        if (isValid(board, r, c, val)) {
                            count++;
                            answer = val;
                        }
                    }

                    if (count == 1) {
                        return new HintMove(r, c, answer, 1);
                    }
                }
            }
        }

        return null;
    }

    private HintMove findHiddenSingleHint() {
        for (int val = 1; val <= 9; val++) {
            HintMove rowMove = hiddenSingleInRows(val);
            if (rowMove != null) {
                return rowMove;
            }

            HintMove colMove = hiddenSingleInCols(val);
            if (colMove != null) {
                return colMove;
            }

            HintMove boxMove = hiddenSingleInBoxes(val);
            if (boxMove != null) {
                return boxMove;
            }
        }
        return null;
    }

    private HintMove hiddenSingleInRows(int val) {
        for (int r = 0; r < 9; r++) {
            int count = 0;
            int placeCol = -1;

            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0 && isValid(board, r, c, val)) {
                    count++;
                    placeCol = c;
                }
            }

            if (count == 1) {
                return new HintMove(r, placeCol, val, 2);
            }
        }

        return null;
    }

    private HintMove hiddenSingleInCols(int val) {
        for (int c = 0; c < 9; c++) {
            int count = 0;
            int placeRow = -1;

            for (int r = 0; r < 9; r++) {
                if (board[r][c] == 0 && isValid(board, r, c, val)) {
                    count++;
                    placeRow = r;
                }
            }

            if (count == 1) {
                return new HintMove(placeRow, c, val, 2);
            }
        }

        return null;
    }

    private HintMove hiddenSingleInBoxes(int val) {
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                int count = 0;
                int placeRow = -1;
                int placeCol = -1;

                for (int r = boxRow; r < boxRow + 3; r++) {
                    for (int c = boxCol; c < boxCol + 3; c++) {
                        if (board[r][c] == 0 && isValid(board, r, c, val)) {
                            count++;
                            placeRow = r;
                            placeCol = c;
                        }
                    }
                }

                if (count == 1) {
                    return new HintMove(placeRow, placeCol, val, 2);
                }
            }
        }
        return null;
    }

    public void generatePuzzle(String difficulty) {
        int[][] fullBoard = new int[9][9];

        fillBoardRand(fullBoard);

        solution = copyBoard(fullBoard);
        board = copyBoard(fullBoard);

        removeNums(difficulty);

        given = new boolean[9][9];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                given[r][c] = board[r][c] != 0;
            }
        }

        mistakes = 0;
        hintsUsed = 0;
        gameOver = false;
        history.clear();
    }


    private boolean fillBoardRand(int[][] b) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {

                if (b[r][c] == 0) {

                    ArrayList<Integer> nums = new ArrayList<>();

                    for (int i = 1; i <= 9; i++) {
                        nums.add(i);
                    }

                    //shuffle them randomly
                    java.util.Collections.shuffle(nums);

                    for (int val : nums) {

                        // check to make sure the number actually can go there!
                        if (isValid(b, r, c, val)) {

                            b[r][c] = val;

                            if (fillBoardRand(b)) {
                                return true;
                            }

                            b[r][c] = 0;
                        }
                    }

                    return false;
                }
            }
        }

        return true;
    }

    private void removeNums(String difficulty) {
        int minRemove;
        int maxRemove;

        if (difficulty.equals("easy")) {
            minRemove = 30;
            maxRemove = 38;
        } else if (difficulty.equals("medium")) {
            minRemove = 39;
            maxRemove = 48;
        } else if (difficulty.equals("hard")) {
            minRemove = 49;
            maxRemove = 55;
        } else {
            minRemove = 56;
            maxRemove = 62; // extreme
        }

        int numsToRemove = minRemove + (int) (Math.random() * (maxRemove - minRemove + 1));

        int removed = 0;

        while (removed < numsToRemove) {
            int row = (int) (Math.random() * 9);
            int col = (int) (Math.random() * 9);

            if (board[row][col] != 0) {
                board[row][col] = 0;
                removed++;
            }
        }
    }
}