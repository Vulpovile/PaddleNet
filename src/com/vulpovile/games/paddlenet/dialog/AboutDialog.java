package com.vulpovile.games.paddlenet.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.UIManager;

import com.vulpovile.games.paddlenet.PaddleNet;

import javax.swing.border.EmptyBorder;

public class AboutDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AboutDialog(JFrame parent) {
		super(parent, "About");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(450, 459);
		setLocationRelativeTo(parent);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		StringBuilder builder = new StringBuilder();
		builder.append(PaddleNet.PRODUCT_NAME);
		builder.append(" ");
		builder.append(PaddleNet.ERA);
		builder.append(PaddleNet.GENERATION);
		builder.append(".");
		builder.append(PaddleNet.MAJOR_VERSION);
		builder.append(".");
		builder.append(PaddleNet.MINOR_VERSION);
		if(PaddleNet.PATCH_VERSION > 0)
		{
			builder.append("_");
			//oh god
			builder.append(String.format("%02d", PaddleNet.PATCH_VERSION));
		}
		builder.append("\n\n");
		builder.append("PaddleNet is a network version of a classic 70's TV sports game. There is a single player mode, which is now possible to win! (although hard!). ");
		builder.append("To host a server, you can use the Network menu. The client (person joining) is always the left side, and the host is always the right.\n\n");
		builder.append("Server sided options:\n");
		builder.append("Q - Right Paddle Difficulty (Default: Easy)\n");
		builder.append("P - Left Paddle Difficulty (Default: Easy)\n");
		builder.append("A - Ball Angle (Default: Easy)\n");
		builder.append("B - Ball Speed (Default: Easy)\n");
		builder.append("C - Colour (Default: B/W)\n");
		builder.append("R - Reset\n");
		builder.append("Wheel - English\n\n");
		builder.append("1 - Paddle\n");
		builder.append("2 - Tennis\n");
		builder.append("3 - Hockey\n");
		builder.append("4 - Squash\n");
		

		contentPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane);

		JTextArea textArea = new JTextArea(builder.toString());
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setBackground(UIManager.getColor("control"));
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});

	}
}
