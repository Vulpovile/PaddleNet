package com.vulpovile.games.paddlenet;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.imageio.ImageIO;
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

public class PaddleNet extends JFrame implements WindowListener, ActionListener{
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

	public GameTickThread gameTickThread;
	

	private JMenuItem startServer = new JMenuItem("Create Server...");
	private JMenuItem joinServer = new JMenuItem("Join Server...");
	private JMenuItem about = new JMenuItem("About");

	private final PaddlePanel paddlePanel = new PaddlePanel(this);

	public PaddleNet() {
		super(PRODUCT_NAME);
		this.setSize(800, 600);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(paddlePanel, BorderLayout.CENTER);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		try
		{
			this.setIconImage(ImageIO.read(this.getClass().getResource("/PaddleNet128.png")));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		addWindowListener(this);

		JToolBar toolBar = new JToolBar();

		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		menuPanel.add(toolBar, BorderLayout.CENTER);

		JMenuBar menuBar = new JMenuBar();
		toolBar.add(menuBar);

		this.getContentPane().add(menuPanel, BorderLayout.NORTH);

		JMenu networkMenu = new JMenu("Networking");
		menuBar.add(networkMenu);
		networkMenu.add(startServer);
		networkMenu.add(joinServer);

		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		helpMenu.add(about);

		JLabel lblBoardInfo = new JLabel("Angle: Easy | Speed: Easy");
		menuPanel.add(lblBoardInfo, BorderLayout.EAST);

		LabelSettingsListener lbs = new LabelSettingsListener(lblBoardInfo);
		paddlePanel.addSettingsListener(lbs);
		lbs.settingsChanged(paddlePanel);

		gameTickThread = new GameTickThread(paddlePanel, 16666666L);
		new Thread(gameTickThread).start();

		startServer.addActionListener(this);
		joinServer.addActionListener(this);
		about.addActionListener(this);

	}

	public static void main(String[] args) {
		Sound.init();
		new PaddleNet();
	}

	public void windowActivated(WindowEvent arg0) {
	}

	public void windowClosed(WindowEvent arg0) {
	}

	public void windowClosing(WindowEvent arg0) {
		dispose();
		Sound.destroy();
		gameTickThread.end();
		try
		{
			//Wait for all threads to (hopefully) exit gracefully
			Thread.sleep(1000L);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		//Ensure exit if that does not happen
		System.exit(0);
	}

	public void windowDeactivated(WindowEvent arg0) {
	}

	public void windowDeiconified(WindowEvent arg0) {
	}

	public void windowIconified(WindowEvent arg0) {
	}

	public void windowOpened(WindowEvent arg0) {
	}

	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == startServer || arg0.getSource() == joinServer)
		{
			new IPPortDialog(PaddleNet.this, paddlePanel, arg0.getSource() == startServer).setVisible(true);
		}
		else if(arg0.getSource() == about)
		{
			new AboutDialog(PaddleNet.this).setVisible(true);
		}
	}
}
