package com.vulpovile.games.paddlenet.netcode.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.vulpovile.games.paddlenet.netcode.NetworkHandler;

public class Packet6BallPosition extends Packet {
	public short x;
	public short y;

	public Packet6BallPosition() {
	}

	public Packet6BallPosition(int x, int y) {
		this((short)x, (short)y);
	}
	
	public Packet6BallPosition(short x, short y) {
		this.x = x;
		this.y = y;
	}

	public void onIncoming(DataInputStream in, NetworkHandler networkHandler) throws IOException {
		x = in.readShort();
		y = in.readShort();
		networkHandler.handlePacket(this);
	}

	public void sendPacket(DataOutputStream out) throws IOException {
		super.sendPacket(out);
		out.writeShort(x);
		out.writeShort(y);
	}

	@Override
	public Packet cloneTypeOnly() {
		return new Packet6BallPosition();
	}
}
