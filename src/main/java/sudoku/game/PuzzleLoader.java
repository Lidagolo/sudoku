package sudoku.game;

import java.io.*;
import java.util.*;

public class PuzzleLoader {
    //stores puzzles and splutions
    public static class Puzzle {
        private int[][] puzzle;
        private int[][] solution;

        public Puzzle(int[][] p, int[][] s) {
            puzzle = p;
            solution = s;
        }

        public int[][] getPuzzle() {
            return puzzle;
        }

        public int[][] getSolution() {
            return solution;
        }
    }

    public Puzzle readPuzzle(String filename) {
        int[][] puzzle = new int[9][9];
        int[][] solution = new int[9][9];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            //read puzzle
            for (int r = 0; r < 9; r++) {
                String line = reader.readLine();
                for (int c = 0; c < 9; c++) {
                    puzzle[r][c] = Character.getNumericValue(line.charAt(c));
                }
            }

            //skip line
            reader.readLine();

            //read solution
            for (int r = 0; r < 9; r++) {
                String line = reader.readLine();
                for (int c = 0; c < 9; c++) {
                    solution[r][c] = Character.getNumericValue(line.charAt(c));
                }
            }
            reader.close();

        } catch (IOException e) {
            System.out.println("Yo, ur file " + filename + " ain't working");
        }
        return new Puzzle(puzzle, solution);
    }

    public Puzzle getRandom(String difficulty) {
        Random ran = new Random();

        int num = ran.nextInt(3) + 1;
        String filename = "files/" + difficulty + num + ".txt";

        return readPuzzle(filename);
    }
}
