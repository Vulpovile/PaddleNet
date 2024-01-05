package com.vulpovile.games.paddlenet.netcode.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.vulpovile.games.paddlenet.netcode.NetworkHandler;

public class Packet3Score1 extends Packet {
	public byte score;

	public Packet3Score1() {
	}

	public Packet3Score1(byte score) {
		this.score = (byte)score;
	}

	public void onIncoming(DataInputStream in, NetworkHandler networkHandler) throws IOException {
		score = in.readByte();
		networkHandler.handlePacket(this);
	}

	public void sendPacket(DataOutputStream out) throws IOException {
		super.sendPacket(out);
		out.writeByte(score);
	}

	@Override
	public Packet cloneTypeOnly() {
		return new Packet3Score1();
	}
}
