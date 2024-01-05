package com.vulpovile.games.paddlenet.netcode;

import java.io.IOException;
import java.net.InetAddress;
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

/**
 * Client is ALWAYS Paddle 1
 */
public class ClientHandler extends NetworkHandler{
	private final PaddlePanel paddlePanel;
	public ClientHandler(Socket sock, PaddlePanel paddlePanel) throws IOException {
		super(sock);
		this.paddlePanel = paddlePanel;
	}

	@Override
	protected void postDisconnect() {
		JOptionPane.showMessageDialog(paddlePanel, "Disconnected from server");
		paddlePanel.setNetworkHandler(null);
		paddlePanel.reset();
	}

	@Override
	public boolean isServer() {
		return false;
	}
	
	public static void connectAsync(final PaddlePanel paddlePanel, final InetAddress address, final int port)
	{
		new Thread(new Runnable(){
			public void run() {
				paddlePanel.setWaiting(true);
				try
				{
					Socket socket = new Socket(address, port);
					ClientHandler handler = new ClientHandler(socket, paddlePanel);
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

	/**
	 * Paddle 1 is the client paddle, generally this *should not* be sent but who knows
	 */
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
