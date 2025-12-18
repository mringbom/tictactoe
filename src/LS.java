//package tictactoe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Class to represent Move objects
class Move {
    int val;   // Value of th emov
    int row;   // Row and column coordinates
    int col;
    
    public Move(int v, int r, int c) {
	val=v;
	row=r;
	col=c;
    }
}

// Class Button extends JButton with (x,y) coordinates
class Button extends javax.swing.JButton {
    public int i;   // The row and column coordinate of the button in a GridLayout
    public int j;

    public Button (int x, int y) {
	// Create a JButton with a blank icon. This also gives the button its correct size.
	super();
	super.setIcon(new javax.swing.ImageIcon(getClass().getResource("None.png")));
	this.i = x;
	this.j = y;
    }

    // Return row coordinate
    public int get_i () {
	return i;
    }

    // Return column coordinate
    public int get_j () {
	return j;
    }

}

public class LS extends javax.swing.JFrame {

    // Marks on the board
    public static final int EMPTY    = 0;
    public static final int HUMAN    = 1;
    public static final int COMPUTER = 2;

    // Outcomes of the game
    public static final int HUMAN_WIN    = 4;
    public static final int DRAW         = 5;
    public static final int CONTINUE     = 6;
    public static final int COMPUTER_WIN = 7;
    
    public static final int SIZE = 3;
    private int[][] board = new int[SIZE][SIZE];  // The marks on the board
    private javax.swing.JButton[][] jB;           // The buttons of the board
    private int turn = HUMAN;                    // HUMAN starts the game

    /* Constructor for the Tic Tac Toe game */
    public LS() {
	// Close the window when the user exits
	setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	initBoard();      // Set up the board with all marks empty
    }

    // Initalize an empty board. 
    private void initBoard(){
	// Create a SIZE*SIZE gridlayput to hold the buttons	
	java.awt.GridLayout layout = new GridLayout(SIZE, SIZE);
	getContentPane().setLayout(layout);

	// The board is a grid of buttons
	jB = new Button[SIZE][SIZE];
	for (int i=0; i<SIZE; i++) {
	    for (int j=0; j<SIZE; j++) {
            // Create a new button and add an actionListerner to it
            jB[i][j] = new Button(i, j);
            // Add an action listener to the button to handle mouse clicks
            jB[i][j].addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent act) {
                    jBAction(act);
                }
            });
            add(jB[i][j]);   // Add the buttons to the GridLayout

            board[i][j] = EMPTY;     // Initialize all marks on the board to empty
	    }
    }
    // Pack the GridLayout and make it visible
    pack();
    }

    // Action listener which handles mouse clicks on the buttons
    private void jBAction(java.awt.event.ActionEvent act) {

        if (turn != HUMAN) return;

        Button thisButton = (Button) act.getSource();   // Get the button clicked on
        // Get the grid coordinates of the clicked button
        int i = thisButton.get_i();
        int j = thisButton.get_j();
        System.out.println("Button[" + i + "][" + j + "] was clicked");  // DEBUG

        // Check if this square is empty.
        if (board[i][j] != EMPTY){
            return;
        }

        // If it is empty then place a HUMAN mark (X) in it and check if it was a winning move
        // In this version no checks are done, the marks just alter between HUMAN and COMPUTER

        // Set an X mark in the clicked button
        thisButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("X.png")));
        System.out.println("Human placed X at [" + i + "][" + j + "]");
        place (i, j, HUMAN);    // Mark the move on the board

        // Check if we are done (that is COMPUTER or HUMAN wins)
        if (checkResult() != CONTINUE) {
            handleGameOver(checkResult());
            return;
        }

        turn = COMPUTER;
        // The computer chooses a successor position with a maximal value
        computerMove();

    }

    private int checkResult() {
	    // This function should check if one player (HUMAN or COMPUTER) wins
        // if the board is full (DRAW)
	    // or if the game should continue. You implement this.

        // Check rows
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] != EMPTY &&
                    board[i][0] == board[i][1] &&
                    board[i][0] == board[i][2]) {
                return board[i][0] == HUMAN ? HUMAN_WIN : COMPUTER_WIN;
            }
        }

        // Check columns
        for (int j = 0; j < SIZE; j++) {
            if (board[0][j] != EMPTY &&
                    board[0][j] == board[1][j] &&
                    board[0][j] == board[2][j]) {
                return board[0][j] == HUMAN ? HUMAN_WIN : COMPUTER_WIN;
            }
        }

        // Check main diagonal (top-left to bottom-right)
        if (board[1][1] != EMPTY &&
                board[0][0] == board[1][1] &&
                board[1][1] == board[2][2]) {
            return board[1][1] == HUMAN ? HUMAN_WIN : COMPUTER_WIN;
        }

        // Check anti-diagonal (top-right to bottom-left)
        if (board[1][1] != EMPTY &&
                board[0][2] == board[1][1] &&
                board[1][1] == board[2][0]) {
            return board[1][1] == HUMAN ? HUMAN_WIN : COMPUTER_WIN;
        }

        // Check if board is full
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (board[i][j] == EMPTY){
                    return CONTINUE;
                }
            }
        }

	    return DRAW;
    }

    private void handleGameOver(int result){

        String message = "";

        switch (result) {
            case HUMAN_WIN:
                message = "You won!";
                break;
            case COMPUTER_WIN:
                message = "The computer won!";
                break;
            case DRAW:
                message = "Draw!";
                break;
        }

        System.out.println(message);
        JOptionPane.showMessageDialog(this, message, "Game over",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void computerMove() {
        if (checkResult() != CONTINUE) return;

        Move best = minimax(COMPUTER);
        System.out.println("Computer placed 0 at [" + best.row + "][" + best.col + "]");

        if (best.row == -1 || best.col == -1) return;

        // Set a 0 mark in the clicked button
        jB[best.row][best.col].setIcon(new ImageIcon(getClass().getResource("O.png")));
        place(best.row, best.col, COMPUTER);

        // IMPORTANT: after computer move, give turn back to human
        turn = HUMAN;

        // Check game over after computer move
        int res = checkResult();
        if (res != CONTINUE) handleGameOver(res);
    }


    private int terminalScore() {
        int res = checkResult();
        if (res == COMPUTER_WIN) return 10;
        if (res == HUMAN_WIN) return -10;
        if (res == DRAW) return 0;
        return Integer.MIN_VALUE; // "not terminal"
    }

    private Move minimax(int currentPlayer) {
        int tScore = terminalScore();
        if (tScore != Integer.MIN_VALUE) {
            // terminal state: no move to make, only a value matters
            return new Move(tScore, -1, -1);
        }

        Move best;
        if (currentPlayer == COMPUTER) {
            best = new Move(Integer.MIN_VALUE, -1, -1); // maximize
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (board[r][c] == EMPTY) {
                        board[r][c] = COMPUTER;
                        Move reply = minimax(HUMAN);
                        board[r][c] = EMPTY;

                        if (reply.val > best.val) {
                            best = new Move(reply.val, r, c);
                        }
                    }
                }
            }
        } else {
            best = new Move(Integer.MAX_VALUE, -1, -1); // minimize
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (board[r][c] == EMPTY) {
                        board[r][c] = HUMAN;
                        Move reply = minimax(COMPUTER);
                        board[r][c] = EMPTY;

                        if (reply.val < best.val) {
                            best = new Move(reply.val, r, c);
                        }
                    }
                }
            }
        }
        return best;
    }


    // Place a mark for one of the playsers (HUMAN or COMPUTER) in the specified position
    public void place (int row, int col, int player){
	board [row][col] = player;
    }

    
    public static void main (String [] args){

	String threadName = Thread.currentThread().getName();
	LS lsGUI = new LS();      // Create a new user inteface for the game
	lsGUI.setVisible(true);
	
	java.awt.EventQueue.invokeLater (new Runnable() {
		public void run() {
		    while ( (Thread.currentThread().getName() == threadName) &&
		    	    (lsGUI.checkResult() == CONTINUE) ){
			try {
			    Thread.sleep(100);  // Sleep for 100 millisecond, wait for button press
			} catch (InterruptedException e) { };
		    }
		}
	    });
    }
}
