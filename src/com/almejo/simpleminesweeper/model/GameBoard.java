package com.almejo.simpleminesweeper.model;

import java.util.Random;

public class GameBoard {

	Cell[][] cells = new Cell[8][8];

	public GameBoard() {
		initializeCells();
		initializeMines();
		initializeHints();
	}

	private void initializeCells() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				cells[x][y] = new Cell();
			}
		}
	}

	private void initializeHints() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (!cells[x][y].hasMine) {
					int count = 0;
					count += check(x - 1, y - 1);
					count += check(x, y - 1);
					count += check(x + 1, y - 1);
					count += check(x - 1, y);
					count += check(x, y);
					count += check(x + 1, y);
					count += check(x - 1, y + 1);
					count += check(x, y + 1);
					count += check(x + 1, y + 1);
					cells[x][y].nearMines = count;
				}
			}
		}
	}

	private int check(int x, int y) {
		if (x < 0 || x >= 8 || y < 0 || y >= 8) {
			return 0;
		}
		return cells[x][y].hasMine ? 1 : 0;
	}

	private void initializeMines() {
		int mines = 10;
		while (mines > 0) {
			int x = new Random().nextInt(8);
			int y = new Random().nextInt(8);
			if (!cells[x][y].hasMine) {
				cells[x][y].hasMine = true;
				mines--;
			}

		}
	}

	public boolean hasMine(int x, int y) {
		return cells[x][y].hasMine;
	}

	public int getHintCount(int x, int y) {
		return cells[x][y].nearMines;
	}

	public boolean isClosed(int x, int y) {
		return !cells[x][y].open;
	}

	public boolean isFlagged(int x, int y) {
		return cells[x][y].flagged;
	}

	public void unFlag(int x, int y) {
		cells[x][y].flagged = false;
	}

	public void flag(int x, int y) {
		cells[x][y].flagged = true;
	}

	public boolean isOpen(int x, int y) {
		return cells[x][y].open;
	}

	public void open(int x, int y) {
		cells[x][y].open = true;
		if (cells[x][y].nearMines == 0) {
			boolean[][] tracked = new boolean[8][8];
			clear(x, y, tracked);
		}
	}

	private void clear(int x, int y, boolean[][] tracked) {
		if (x < 0 || y < 0 || x >= 8 || y >= 8 || tracked[x][y]) {
			return;
		}
		cells[x][y].open = true;
		tracked[x][y] = true;

		if (cells[x][y].nearMines == 0) {
			checkAndClear(x - 1, y - 1, tracked);
			checkAndClear(x, y - 1, tracked);
			checkAndClear(x + 1, y - 1, tracked);
			checkAndClear(x - 1, y, tracked);
			checkAndClear(x + 1, y, tracked);
			checkAndClear(x - 1, y + 1, tracked);
			checkAndClear(x, y + 1, tracked);
			checkAndClear(x + 1, y + 1, tracked);
		}

	}

	private void checkAndClear(int x, int y, boolean[][] tracked) {
		if (x < 0 || y < 0 || x >= 8 || y >= 8 || tracked[x][y]) {
			return;
		}
		if (cells[x][y].nearMines > 0) {
			cells[x][y].open = true;
			tracked[x][y] = true;
		} else if (!cells[x][y].hasMine) {
			clear(x, y, tracked);
		}
	}

	public boolean allOpen() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (!cells[x][y].open && !cells[x][y].hasMine) {
					return false;
				}
			}
		}
		return true;
	}

	public int flags() {
		int count = 0;
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (cells[x][y].flagged) {
					count++;
				}
			}
		}
		return count;
	}

	public void openAll() {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				cells[x][y].open = true;
			}
		}
	}
}
