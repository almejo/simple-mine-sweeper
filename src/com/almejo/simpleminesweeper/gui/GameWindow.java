package com.almejo.simpleminesweeper.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import java.awt.Dimension;

public class GameWindow extends JFrame {

	public GameWindow() {
		setTitle("Simple MineSweeper");
		setSize(new Dimension(240, 338));
		setPreferredSize(new Dimension(240, 300));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		BoardPanel boardPanel = new BoardPanel();
		add(boardPanel);
		ScorePanel scorePanel = new ScorePanel();
		boardPanel.addGameChangeListener(scorePanel);
		add(scorePanel);
	}
}
