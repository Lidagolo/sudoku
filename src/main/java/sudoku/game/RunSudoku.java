package sudoku.game;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunSudoku implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Sudoku");
        frame.setLocation(300, 300);

        // Status and dificulty
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        //status
        JPanel statusPanel = new JPanel();
        JLabel status = new JLabel("Setting up...");
        statusPanel.add(status);

        bottomPanel.add(statusPanel, BorderLayout.NORTH);

        JPanel difficultyPanel = new JPanel();

        // Game board
        final SudokuBoard board = new SudokuBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        //undo button (lowk just like reset but for undo)
        JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        control_panel.add(undo);

        //add hint button
        JButton hint = new JButton("Hint");
        hint.addActionListener(e -> board.hint());
        control_panel.add(hint);

        //save and load
        JButton save = new JButton("Save");
        save.addActionListener(e -> board.save());

        JButton load = new JButton("Load");
        load.addActionListener(e -> board.load());

        control_panel.add(save);
        control_panel.add(load);

        //difficulty buttons
        JButton easy = new JButton("Easy");
        JButton medium = new JButton("Medium");
        JButton hard = new JButton("Hard");
        JButton extreme = new JButton("EXTREME");

        easy.addActionListener(e -> board.startGame("easy"));
        medium.addActionListener(e -> board.startGame("medium"));
        hard.addActionListener(e -> board.startGame("hard"));
        extreme.addActionListener(e -> board.startGame("extreme"));

        difficultyPanel.add(easy);
        difficultyPanel.add(medium);
        difficultyPanel.add(hard);
        difficultyPanel.add(extreme);

        bottomPanel.add(difficultyPanel, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JOptionPane.showMessageDialog(null,
                "Welcome to Sudoku! \n" +
                        "How to play... \n" +
                "Click a cell to select it \n" +
                "Press a number 1-9 to place it \n" +
                "Press backspace/delete to clear a cell \n" +
                "You have 3 mistakes before game over\n" +
                        "*Every row, column, and 3 x 3 box \n" +
                "should contain a digit 1-9 only once* \n" +
                        "Use undo and reset as necessary \n" +
                "Good luck!", "How to Play",
                JOptionPane.INFORMATION_MESSAGE);


        // Start the game
        board.reset();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RunSudoku());
    }
}
