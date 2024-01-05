package com.vulpovile.games.paddlenet.netcode.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.vulpovile.games.paddlenet.netcode.NetworkHandler;

public class Packet5BallVelocity extends Packet {
	public byte x;
	public byte y;

	public Packet5BallVelocity() {
	}

	public Packet5BallVelocity(int x, int y) {
		this.x = (byte)x;
		this.y = (byte)y;
	}

	public void onIncoming(DataInputStream in, NetworkHandler networkHandler) throws IOException {
		x = in.readByte();
		y = in.readByte();
		networkHandler.handlePacket(this);
	}

	public void sendPacket(DataOutputStream out) throws IOException {
		super.sendPacket(out);
		out.writeByte(x);
		out.writeByte(y);
	}

	@Override
	public Packet cloneTypeOnly() {
		return new Packet5BallVelocity();
	}
}
