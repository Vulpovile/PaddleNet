package com.vulpovile.games.paddlenet.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JTextField;

import com.vulpovile.games.paddlenet.PaddlePanel;
import com.vulpovile.games.paddlenet.netcode.ClientHandler;
import com.vulpovile.games.paddlenet.netcode.ServerHandler;

public class IPPortDialog extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtIP;
	private JTextField txtPort;
	private boolean hostServer;
	private PaddlePanel paddlePanel;
	private JButton btnCancel = new JButton("Cancel");
	private JButton btnStart = new JButton("Start");

	/**
	 * Create the dialog.
	 * @param jFrame 
	 * @param paddlePanel 
	 */
	public IPPortDialog(JFrame jFrame, PaddlePanel paddlePanel, boolean hostServer) {
		super(jFrame);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.paddlePanel = paddlePanel;
		this.hostServer = hostServer;
		setSize(456, 214);
		setLocationRelativeTo(jFrame);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel pnlItems = new JPanel();
		contentPanel.add(pnlItems, BorderLayout.NORTH);
		pnlItems.setLayout(new GridLayout(0, 1, 0, 0));

		if (!hostServer)
		{
			JPanel pnlIP = new JPanel();
			pnlIP.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnlItems.add(pnlIP);
			pnlIP.setLayout(new BorderLayout(0, 0));

			JLabel lblNewLabel = new JLabel("Server IP");
			pnlIP.add(lblNewLabel, BorderLayout.NORTH);

			txtIP = new JTextField();
			pnlIP.add(txtIP, BorderLayout.CENTER);
			txtIP.setColumns(10);
		}

		JPanel pnlPort = new JPanel();
		pnlPort.setBorder(new EmptyBorder(5, 5, 5, 5));
		pnlItems.add(pnlPort);
		pnlPort.setLayout(new BorderLayout(0, 0));

		JLabel lblPort = new JLabel("Port");
		pnlPort.add(lblPort, BorderLayout.NORTH);

		txtPort = new JTextField("7777");
		txtPort.setColumns(10);
		pnlPort.add(txtPort, BorderLayout.CENTER);

		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(pnlButtons, BorderLayout.SOUTH);

		btnStart.setActionCommand("OK");
		pnlButtons.add(btnStart);
		getRootPane().setDefaultButton(btnStart);

		btnCancel.setActionCommand("Cancel");
		pnlButtons.add(btnCancel);

		btnStart.addActionListener(this);
		btnCancel.addActionListener(this);
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == btnCancel)
		{
			dispose();
		}
		else if (arg0.getSource() == btnStart)
		{
			try
			{
				int port = Integer.parseInt(txtPort.getText().trim());
				if (port >= 0 && port <= 65525)
				{
					try
					{
						if (this.hostServer)
						{
							ServerHandler.startServerAsync(paddlePanel, port);
						}
						else
						{
							String ip = txtIP.getText().trim();
							InetAddress inetAddress = InetAddress.getByName(ip);
							ClientHandler.connectAsync(paddlePanel, inetAddress, port);
						}
					}
					catch (UnknownHostException e)
					{
						JOptionPane.showMessageDialog(this, "Unknown Host", "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(this, "Oops! Something happened:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Port must be a number between 0 and 65525", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			catch (NumberFormatException ex)
			{
				JOptionPane.showMessageDialog(this, txtPort.getText() + " is not a valid port! (Port must be numeric)", "Error", JOptionPane.ERROR_MESSAGE);
			}
			dispose();
		}
	}
}
