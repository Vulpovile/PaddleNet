package com.vulpovile.games.paddlenet.netcode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.vulpovile.games.paddlenet.Util;
import com.vulpovile.games.paddlenet.netcode.packet.*;

public abstract class NetworkHandler {

	protected final Socket sock;
	protected boolean connected = true;
	protected DataInputStream in;
	protected DataOutputStream out;
	protected SendRecieveSystem sendRecieveSystem = new SendRecieveSystem(this);

	public NetworkHandler(Socket sock) throws IOException {
		this.sock = sock;
		this.in = new DataInputStream(sock.getInputStream());
		this.out = new DataOutputStream(sock.getOutputStream());
	}

	public void disconnect() {
		if (connected)
		{
			this.sendRecieveSystem.stop();
			postDisconnect();
			connected = false;
			try
			{
				//bruh
				sock.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			Util.cleanClose(in);
			Util.cleanClose(out);
		}
	}

	protected abstract void postDisconnect();
	
	protected void beginIO() {
		this.sendRecieveSystem.start();
	}

	public void enqueuePacket(Packet packet)
	{
		sendRecieveSystem.enqueuePacket(packet);
	}
	
	public void sendPacket(Packet packet) throws IOException {
		packet.sendPacket(out);
	}

	public void onError(Exception e) {
		disconnect();
	}

	public abstract boolean isServer();

	public void handlePacket(Packet packet) {
		disconnect();
	}

	public abstract void handlePacket(Packet1Paddle1 packet) throws IOException;

	public abstract void handlePacket(Packet2Paddle2 packet) throws IOException;

	public abstract void handlePacket(Packet3Score1 packet) throws IOException;

	public abstract void handlePacket(Packet4Score2 packet) throws IOException;

	public abstract void handlePacket(Packet5BallVelocity packet) throws IOException;

	public abstract void handlePacket(Packet6BallPosition packet) throws IOException;

	public abstract void handlePacket(Packet7PaddleHit packet) throws IOException;
	
	public abstract void handlePacket(Packet8PaddleMiss packet) throws IOException;

	public abstract void handlePacket(Packet9BoardConfiguration packet) throws IOException;

	public void recievePacket() throws IOException {
		byte opcode = in.readByte();
		Packet packet = Packet.opToPacket.get(opcode);
		if (packet != null)
		{
			packet = packet.cloneTypeOnly();
			packet.onIncoming(in, this);
		}
		else disconnect();
	}

}
