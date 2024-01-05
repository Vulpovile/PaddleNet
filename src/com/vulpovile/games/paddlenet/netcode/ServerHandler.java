package com.vulpovile.games.paddlenet.netcode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.vulpovile.games.paddlenet.PaddlePanel;
import com.vulpovile.games.paddlenet.netcode.packet.Packet1Paddle1;
import com.vulpovile.games.paddlenet.netcode.packet.Packet2Paddle2;
import com.vulpovile.games.paddlenet.netcode.packet.Packet3Score1;
import com.vulpovile.games.paddlenet.netcode.packet.Packet4Score2;
import com.vulpovile.games.paddlenet.netcode.packet.Packet5BallVelocity;
import com.vulpovile.games.paddlenet.netcode.packet.Packet6BallPosition;
import com.vulpovile.games.paddlenet.netcode.packet.Packet7PaddleHit;
import com.vulpovile.games.paddlenet.netcode.packet.Packet8PaddleMiss;
import com.vulpovile.games.paddlenet.netcode.packet.Packet9BoardConfiguration;

public class ServerHandler extends NetworkHandler {
	private final PaddlePanel paddlePanel;
	private final ServerSocket serverSocket;
	public ServerHandler(Socket sock, ServerSocket serverSocket, PaddlePanel paddlePanel) throws IOException {
		super(sock);
		this.paddlePanel = paddlePanel;
		this.serverSocket = serverSocket;
	}

	@Override
	protected void postDisconnect() {
		try
		{
			serverSocket.close();
			JOptionPane.showMessageDialog(paddlePanel, "Disconnected from client");
			paddlePanel.setNetworkHandler(null);
			paddlePanel.reset();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void startServerAsync(final PaddlePanel paddlePanel, final int port)
	{
		new Thread(new Runnable(){
			public void run() {
				paddlePanel.setWaiting(true);
				try
				{
					ServerSocket serverSocket = new ServerSocket(port);
					Socket socket = serverSocket.accept();
					ServerHandler handler = new ServerHandler(socket, serverSocket, paddlePanel);
					paddlePanel.setNetworkHandler(handler);
					handler.beginIO();
					paddlePanel.reset();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				paddlePanel.setWaiting(false);
			}
		}).start();
	}

	@Override
	public boolean isServer() {
		return true;
	}

	@Override
	public void handlePacket(Packet1Paddle1 packet) throws IOException {
		paddlePanel.setPaddle1(packet.pos);
	}
	
	@Override
	public void handlePacket(Packet2Paddle2 packet) throws IOException {
		paddlePanel.setPaddle2(packet.pos);
	}

	@Override
	public void handlePacket(Packet3Score1 packet) throws IOException {
		paddlePanel.setScore1(packet.score);
	}

	@Override
	public void handlePacket(Packet4Score2 packet) throws IOException {
		paddlePanel.setScore2(packet.score);
	}

	@Override
	public void handlePacket(Packet5BallVelocity packet) throws IOException {
		paddlePanel.setBallVelocity(packet.x, packet.y);
	}

	@Override
	public void handlePacket(Packet6BallPosition packet) throws IOException {
		paddlePanel.setBallPosition(packet.x, packet.y);
	}

	@Override
	public void handlePacket(Packet7PaddleHit packet) throws IOException {
		paddlePanel.hit(packet.x, packet.y, packet.xVelocity, packet.yVelocity, packet.resetPaddleFlags);
	}

	@Override
	public void handlePacket(Packet8PaddleMiss packet) throws IOException {
		paddlePanel.miss();
	}
	
	@Override
	public void handlePacket(Packet9BoardConfiguration packet) throws IOException {
		paddlePanel.update(packet.paddle1HeightHalf, packet.paddle2HeightHalf, packet.gameOverScore, packet.ballHeightHalf, packet.ballWidthHalf, packet.paddleDivider, packet.gameMode, packet.gameOver, packet.colour, packet.rightOwner);
	}
}
