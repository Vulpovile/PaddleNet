package com.vulpovile.games.paddlenet;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.vulpovile.games.listeners.impl.LabelSettingsListener;
import com.vulpovile.games.paddlenet.dialog.AboutDialog;
import com.vulpovile.games.paddlenet.dialog.IPPortDialog;

public class PaddleNet extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String PRODUCT_NAME = "PaddleNet";

	public static final char ERA = 'a';
	public static final byte GENERATION = 0;
	public static final byte MAJOR_VERSION = 0;
	public static final byte MINOR_VERSION = 3;
	public static final byte PATCH_VERSION = 0;

	public PaddleNet() {
		super(PRODUCT_NAME);
		this.setSize(800, 600);
		final PaddlePanel paddlePanel = new PaddlePanel(this);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(paddlePanel, BorderLayout.CENTER);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JToolBar toolBar = new JToolBar();

		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		menuPanel.add(toolBar, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		toolBar.add(menuBar);

		this.getContentPane().add(menuPanel, BorderLayout.NORTH);

		JMenu networkMenu = new JMenu("Networking");
		menuBar.add(networkMenu);
		JMenuItem startServer = new JMenuItem("Create Server...");
		JMenuItem joinServer = new JMenuItem("Join Server...");
		networkMenu.add(startServer);
		networkMenu.add(joinServer);

		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		JMenuItem about = new JMenuItem("About");
		helpMenu.add(about);

		JLabel lblBoardInfo = new JLabel("Angle: Easy | Speed: Easy");
		menuPanel.add(lblBoardInfo, BorderLayout.EAST);

		LabelSettingsListener lbs = new LabelSettingsListener(lblBoardInfo);
		paddlePanel.addSettingsListener(lbs);
		lbs.settingsChanged(paddlePanel);

		new Thread(new GameTickThread(paddlePanel, 16666666L)).start();

		startServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new IPPortDialog(PaddleNet.this, paddlePanel, true).setVisible(true);
			}
		});

		joinServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new IPPortDialog(PaddleNet.this, paddlePanel, false).setVisible(true);
			}
		});

		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new AboutDialog(PaddleNet.this).setVisible(true);
			}
		});
	}

	public static void main(String[] args) {
		new PaddleNet();
	}
}
