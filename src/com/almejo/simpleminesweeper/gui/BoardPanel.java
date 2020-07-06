package com.almejo.simpleminesweeper.gui;

import com.almejo.simpleminesweeper.model.GameBoard;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

public class BoardPanel extends JPanel {
	private final List<GameChangeListener> gameChangeListeners = new LinkedList<>();
	private GameBoard gameBoard;
	private final Color[] colors = new Color[]{
			Color.BLUE,
			Color.GREEN,
			Color.RED,
			Color.ORANGE,
			Color.DARK_GRAY,
			Color.BLACK,
			Color.PINK,
			Color.MAGENTA,
			Color.CYAN,
	};

	public BoardPanel() {
		this.gameBoard = new GameBoard();
		setSize(240, 300);
		setPreferredSize(new Dimension(270, 270));
		addMouseListener(new BoardPanel.MouseController(this));
	}

	public void addGameChangeListener(GameChangeListener listener) {
		gameChangeListeners.add(listener);
	}

	static class MouseController extends MouseAdapter {

		private final BoardPanel board;

		public MouseController(BoardPanel gameWindow) {
			this.board = gameWindow;
		}

		@Override
		public void mouseClicked(MouseEvent event) {
			if (event.getButton() == MouseEvent.BUTTON1) {
				board.open(BoardPanel.toBoardX(event.getX()), BoardPanel.toBoardY(event.getY()));
			} else if (event.getButton() == MouseEvent.BUTTON3) {
				board.flag(BoardPanel.toBoardX(event.getX()), BoardPanel.toBoardY(event.getY()));
			}
		}
	}

	private void flag(int x, int y) {
		if (gameBoard.isFlagged(x, y)) {
			gameBoard.unFlag(x, y);
		} else if (gameBoard.isClosed(x, y)) {
			gameBoard.flag(x, y);
		}
		emitFlags(gameBoard.flags());
		if (gameBoard.allOpen()) {
			changeStatus(GameStatus.WIN);
		}
	}

	private void emitFlags(int flags) {
		for (GameChangeListener changeListener : gameChangeListeners) {
			changeListener.flags(flags);
		}
	}

	private void changeStatus(GameStatus status) {
		switch (status) {
			case LOSE:
				JOptionPane.showMessageDialog(this, "You lose!!!");
				reset();
				break;
			case WIN:
				JOptionPane.showMessageDialog(this, "You win!!!");
				reset();
				break;
		}
	}

	private void reset() {
		gameBoard = new GameBoard();
		emitReset();
	}

	private void emitReset() {
		for (GameChangeListener changeListener : gameChangeListeners) {
			changeListener.reset();
		}
	}

	private void open(int x, int y) {
		if (gameBoard.isOpen(x, y) || gameBoard.isFlagged(x, y)) {
			return;
		}
		gameBoard.open(x, y);
		if (gameBoard.hasMine(x, y)) {
			changeStatus(GameStatus.LOSE);
		} else if (gameBoard.allOpen()) {
			changeStatus(GameStatus.WIN);
		}
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		drawScreen((Graphics2D) graphics);
		repaint();
	}

	private void drawScreen(Graphics2D graphics2D) {
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.clearRect(0, 0, 400, 400);
		drawBoard(graphics2D);
		drawTiles(graphics2D);
	}

	private void drawTiles(Graphics2D graphics2D) {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				int deltaX = x * 30;
				int deltaY = y * 30;
				if (gameBoard.isClosed(x, y)) {
					drawSlab(graphics2D, deltaX, deltaY);
					if (gameBoard.isFlagged(x, y)) {
						drawFlag(graphics2D, deltaX, deltaY);
					}
				} else if (gameBoard.hasMine(x, y)) {
					drawMine(graphics2D, deltaX, deltaY);
				} else {
					int hintCount = gameBoard.getHintCount(x, y);
					if (hintCount > 0) {
						drawHintNumber(graphics2D, hintCount, deltaX, deltaY);
					}
				}
			}
		}
	}

	private void drawHintNumber(Graphics2D graphics2D, int hintCount, int deltaX, int deltaY) {
		graphics2D.setColor(colors[hintCount]);
		graphics2D.drawString("" + hintCount, deltaX + 10, deltaY + 20);
		graphics2D.setColor(Color.BLACK);
	}

	private void drawSlab(Graphics2D graphics2D, int deltaX, int deltaY) {
		graphics2D.drawRect(deltaX + 5, deltaY + 5, 20, 20);
	}

	private void drawMine(Graphics2D graphics2D, int x, int y) {
		Color color = graphics2D.getColor();
		graphics2D.setColor(Color.BLACK);
		graphics2D.fillOval(x + 7, y + 7, 16, 16);
		graphics2D.setColor(color);
	}

	private void drawFlag(Graphics2D graphics2D, int x, int y) {
		Color color = graphics2D.getColor();
		graphics2D.setColor(Color.BLACK);
		graphics2D.drawLine(x + 15, y + 5, x + 15, y + 22);
		graphics2D.setColor(Color.RED);
		graphics2D.fillRect(x + 15, y + 7, 8, 5);
		graphics2D.setColor(color);
	}

	private void drawBoard(Graphics2D graphics2D) {
		for (int i = 0; i < 9; i++) {
			graphics2D.drawLine(i * 30, 0, i * 30, +240);
			graphics2D.drawLine(0, i * 30, 240, i * 30);
		}
	}

	static private int toBoardY(int y) {
		return y / 30;
	}

	static private int toBoardX(int x) {
		return x / 30;
	}
}
