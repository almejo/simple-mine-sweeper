package com.almejo.simpleminesweeper.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ScorePanel extends JPanel implements GameChangeListener {

	private final JLabel flagsLabel;
	private final JLabel timeLabel;
	private long time = 0;

	ScorePanel() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setPreferredSize(new Dimension(270, 30));
		flagsLabel = new JLabel("Flags: 0/10");
		add(flagsLabel);
		add(new JLabel("     "));
		timeLabel = new JLabel("Time: 0");
		add(timeLabel);
		TimerTask timerTask = new TimeTimerTask();
		//running timer task as daemon thread
		new Timer(true).scheduleAtFixedRate(timerTask, 0, 1000);
		reset();
	}

	@Override
	public void flags(int count) {
		flagsLabel.setText("Flags: " + count + "/10");
	}

	@Override
	public void reset() {
		flagsLabel.setText("Flags: 0/10");
		time = System.currentTimeMillis();
	}

	private class TimeTimerTask extends TimerTask {
		@Override
		public void run() {
			timeLabel.setText("Time: " + timeString(System.currentTimeMillis() - time));
		}

		private String timeString(long milliseconds) {
			long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
			milliseconds -= TimeUnit.HOURS.toMillis(hours);
			long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
			milliseconds -= TimeUnit.MINUTES.toMillis(minutes);
			long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}
	}
}
