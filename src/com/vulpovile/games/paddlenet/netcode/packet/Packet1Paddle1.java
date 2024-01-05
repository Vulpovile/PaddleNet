package com.vulpovile.games.paddlenet.netcode.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.vulpovile.games.paddlenet.netcode.NetworkHandler;

public class Packet1Paddle1 extends Packet {
	public short pos;

	public Packet1Paddle1() {
	}

	public Packet1Paddle1(int pos) {
		this.pos = (short)pos;
	}

	public void onIncoming(DataInputStream in, NetworkHandler networkHandler) throws IOException {
		pos = in.readShort();
		networkHandler.handlePacket(this);
	}

	public void sendPacket(DataOutputStream out) throws IOException {
		super.sendPacket(out);
		out.writeShort(pos);
	}

	@Override
	public Packet cloneTypeOnly() {
		return new Packet1Paddle1();
	}
}
