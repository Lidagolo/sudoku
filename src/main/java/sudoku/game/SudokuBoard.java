package sudoku.game;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

/**
 * This class instantiates a SUdoku object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 *
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class SudokuBoard extends JPanel {

    private SudokuModel sud; // model for the game
    private JLabel status; // current status text
    private int selectedRow = -1;
    private int selectedCol = -1;

    private boolean gameStarted = false;

    // Game constants
    public static final int BOARD_WIDTH = 600;
    public static final int BOARD_HEIGHT = 600;

    /**
     * Initializes the game board.
     */
    public SudokuBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        sud = new SudokuModel(); // initializes model for the game

        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the fmodel, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!gameStarted) {
                    return;
                }

                Point click = e.getPoint();

                int cellSize = BOARD_WIDTH / 9;

                int col = click.x / cellSize;
                int row = click.y / cellSize;

                // can only click on board
                if (col < 0 || col >= 9 || row < 0 || row >= 9) {
                    selectedRow = -1;
                    selectedCol = -1;
                    repaint();
                    return;
                }

                selectedCol = col;
                selectedRow = row;


                requestFocusInWindow();

                // updates the model given the coordinates of the mouseclick

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (!gameStarted) {
                    return;
                }

                if (selectedRow == -1 || selectedCol == -1) {
                    return;
                }
                int key = e.getKeyCode();

                if (key >= KeyEvent.VK_1 && key <= KeyEvent.VK_9) {
                    int value = key - KeyEvent.VK_1 + 1;
                    sud.placeNumber(selectedRow, selectedCol, value);
                }
                // delete / blackspace
                if (key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE) {
                    sud.placeNumber(selectedRow, selectedCol, 0);
                }
                updateStatus();
                repaint();
            }
        });
    }
    //save
    public void save() {
        sud.saveGame("save.txt");
    }

    //load
    public void load() {
        sud.loadGame("save.txt");
        repaint();
    }

    //undo
    public void undo() {
        sud.undo();
        repaint();
    }

    //hint
    public void hint() {
        boolean worked = sud.giveHint();

        if (!worked) {
            status.setText("No hint available or hints used up");
        } else {
            status.setText("Hints used: " + sud.getHintsUsed() + "/2");
        }
        repaint();
    }


    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        sud.reset();
        status.setText("Sudoku");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    //for choosing a difficulty
    /**
     * Given puzzles
    public void startGame(String difficulty) {
        PuzzleLoader loader = new PuzzleLoader();
        PuzzleLoader.Puzzle p = loader.getRandom(difficulty);

        sud.loadPuzzle(p.getPuzzle(), p.getSolution());

        //make game active
        gameStarted = true;

        selectedRow = -1;
        selectedCol = -1;

        updateStatus();
        repaint();
    }
     */
    //generated puzzles
    public void startGame(String difficulty) {
        sud.generatePuzzle(difficulty);

        gameStarted = true;

        selectedRow = -1;
        selectedCol = -1;

        updateStatus();
        repaint();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        status.setText("Mistakes: " + sud.getMistakes() + "/3");

        if (sud.isGameOver()) {
            status.setText("Game over!");
        }

        if (sud.gameWon()) {
            status.setText("You win!");
        }

    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        int cellSize = BOARD_WIDTH / 9;

        //highlight the col and row
        if (selectedRow != -1 && selectedCol != -1) {
            g.setColor(new Color(220, 235, 255));
            for (int i = 0; i < 9; i++) {
                g.fillRect(i * cellSize, selectedRow * cellSize, cellSize, cellSize);
                g.fillRect(selectedCol * cellSize, i * cellSize, cellSize, cellSize);
            }
            //3x3
            int boxRow = (selectedRow / 3) * 3;
            int boxCol = (selectedCol / 3) * 3;

            g.setColor(new Color(235, 245, 255));
            for (int r = boxRow; r < boxRow + 3; r++) {
                for (int c = boxCol; c < boxCol + 3; c++) {
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
            }
            //selected
            g.setColor(new Color(139, 186, 247));
            g.fillRect(
                    selectedCol * cellSize,
                    selectedRow * cellSize,
                    cellSize,
                    cellSize
            );
        }
        //highlight matching numbers
        int selectedValue = -1;
        if (selectedRow != -1 && selectedCol != -1) {
            selectedValue = sud.getCell(selectedRow, selectedCol);
        }

        if (selectedValue != 0) {
            g.setColor(new Color(255, 251, 219));

            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (sud.getCell(r, c) == selectedValue) {
                        g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                    }
                }
            }
        }


        g.setColor(Color.BLACK);
        // Draws board grid - make lines thicker to amplify 3x3
        for (int i = 0; i <= 9; i++) {
            if (i % 3 == 0) {
                g2.setStroke(new BasicStroke(3)); // thick
            } else {
                g2.setStroke(new BasicStroke(1)); // thin
            }
            g.drawLine(i * cellSize, 0, i * cellSize, BOARD_WIDTH);
            g.drawLine(0, i * cellSize, BOARD_WIDTH, i * cellSize);
        }

        // Draws number
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = sud.getCell(r, c);

                //color changes if it is wrong or right
                if (val != 0) {
                    if (sud.isGiven(r, c)) {
                        g.setColor(Color.BLACK);
                    } else if (sud.isCorrect(r, c)) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.RED);
                    }

                    g.setFont(new Font("Georgia", Font.BOLD, 40));

                    g.drawString(
                            String.valueOf(val),
                            c * cellSize + cellSize / 3,
                            r * cellSize + 2 * cellSize / 3
                    );
                }
            }
        }

        if (sud.isGameOver()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Georgia", Font.BOLD, 75));
            g.drawString("GAME OVER", 50, BOARD_HEIGHT / 2);
        } else if (sud.gameWon()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Georgia", Font.BOLD, 75));
            g.drawString("YOU WIN", 105, BOARD_HEIGHT / 2);
        }
        if (!gameStarted) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Georgia", Font.BOLD, 40));
            g.drawString("Choose Difficulty", 100, BOARD_HEIGHT / 2);
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }


}
