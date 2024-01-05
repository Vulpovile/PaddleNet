package com.vulpovile.games.listeners.impl;

import javax.swing.JLabel;

import com.vulpovile.games.listeners.SettingsListener;
import com.vulpovile.games.paddlenet.PaddlePanel;

public class LabelSettingsListener implements SettingsListener {
	private JLabel theLabel;
	
	public LabelSettingsListener(JLabel theLabel)
	{
		this.theLabel = theLabel;
	}
	
	public void settingsChanged(PaddlePanel paddlePanel){
		String ballAngle = "Ball Angle: " + (paddlePanel.getPaddleDivider() == 2 ? "Easy" : "Difficult");
		String ballSpeed = "Ball Speed: " + (Math.abs(paddlePanel.getBallXSpeed()) == 5 ? "Easy" : "Difficult");
		theLabel.setText(ballAngle + " | " + ballSpeed);
	}
}
