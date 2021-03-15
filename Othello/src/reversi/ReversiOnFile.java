package reversi;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * ReversiOnFile is a subclass of Reversi, adding File I/O capabilities for
 * loading and saving game.
 * 
 * I declare that the work here submitted is original except for source material
 * explicitly acknowledged, and that the same or closely related material has
 * not been previously submitted for another course. I also acknowledge that I
 * am aware of University policy and regulations on honesty in academic work,
 * and of the disciplinary guidelines and procedures applicable to breaches of
 * such policy and regulations, as contained in the website.
 * 
 * University Guideline on Academic Honesty:
 * http://www.cuhk.edu.hk/policy/academichonesty Faculty of Engineering
 * Guidelines to Academic Honesty:
 * https://www.erg.cuhk.edu.hk/erg/AcademicHonesty
 * 
 * Student Name: Md Fardin Abdullah Chowdhury Student ID :1155130843 Date :
 * 26/11/20
 * 
 */
public class ReversiOnFile extends Reversi {

	public static final char UNICODE_BLACK_CIRCLE = '\u25CF';
	public static final char UNICODE_WHITE_CIRCLE = '\u25CB';
	public static final char UNICODE_WHITE_SMALL_SQUARE = '\u25AB';

	// constructor to give a new look to new subclass game
	public ReversiOnFile() {
		window.setTitle("Othello on File");
		gameBoard.setBoardColor(Color.orange);
	}

	// loading board from a saved txt file
	public void loadBoard(String filename) {

		for (int i = 1; i <= 8; i++)
			for (int j = 1; j <= 8; j++)
				pieces[i][j] = EMPTY;

		char[][] piece = new char[10][10];

		try {
			Scanner sc = new Scanner(new File(filename));

			int i = 1;
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				Scanner l = new Scanner(line);
				l.useDelimiter("");

				int j = 1;
				while (l.hasNext()) {
					piece[i][j] = l.findInLine(".").charAt(0);
					j++;
				}
				i++;
			}

			if (piece[9][1] == '\u25CB') {
				currentPlayer = WHITE;
			} else {
				currentPlayer = BLACK;
			}

			for (int in = 1; in <= 8; in++) {
				for (int j = 1; j <= 8; j++) {
					if (piece[in][j] == '\u25CF') {
						pieces[in][j] = -1;
					}
					if (piece[in][j] == '\u25CB') {
						pieces[in][j] = 1;
					}
					if (piece[in][j] == '\u25AB') {
						pieces[in][j] = 0;
					}
				}
			}

			gameBoard.addText("Loaded board from " + filename);
			System.out.println("Loaded board from " + filename);
			gameBoard.updateStatus(pieces, currentPlayer);

		} catch (FileNotFoundException | NullPointerException
				| ArrayIndexOutOfBoundsException e) {

			// default board configuration when board fails to load
			gameBoard.addText("Cannot load board from " + filename);
			System.out.println("Cannot load board from " + filename);

			pieces[4][4] = WHITE;
			pieces[4][5] = BLACK;
			pieces[5][4] = BLACK;
			pieces[5][5] = WHITE;

			gameBoard.updateStatus(pieces, currentPlayer);
		}

	}

	// Method to save board
	public void saveBoard(String filename) {

		try {
			File f = new File(filename);
			PrintStream p = new PrintStream(f);

			for (int i = 1; i <= 8; i++) {
				String s = "";

				for (int j = 1; j <= 8; j++) {
					if (pieces[i][j] == 0) {
						s = s + '\u25AB';
					} else if (pieces[i][j] == 1) {
						s = s + '\u25CB';
					} else {
						s = s + '\u25CF';
					}
				}
				p.println(s);
			}

			if (currentPlayer == BLACK) {
				p.print('\u25CF');
			} else {
				p.print('\u25CB');
			}

			p.flush();

			gameBoard.addText("Saved board to " + filename);
			System.out.println("Saved board to " + filename);

		} catch (NullPointerException | IOException e) {
			gameBoard.addText("Cannot save board to " + filename);
			System.out.println("Cannot save board to " + filename);
		}
	}

	// overriden sayGoodbye() method
	@Override
	protected void sayGoodbye() {
		super.sayGoodbye();
		String filename = JOptionPane.showInputDialog("Save board filename");
		saveBoard(filename);
	}

	// main() method, starting point of subclass ReversiOnFile
	public static void main(String[] args) {
		ReversiOnFile game = new ReversiOnFile();

		String filename = JOptionPane.showInputDialog("Load board filename");
		game.loadBoard(filename);

		// if the loaded file is already in a state of ended game, then the
		// following codes handles it
		// if already in a state of double pass

		if (game.passCheck()) {
			game.gameBoard.addText("Forced Pass");
			game.currentPlayer = game.FLIP * game.currentPlayer;
			game.gameBoard.updateStatus(game.pieces, game.currentPlayer);
			if (game.passCheck()) {
				game.gameBoard.addText("Double Forced Pass");
				game.gameBoard.addText("End Game!");
			}
		}
	}
}
