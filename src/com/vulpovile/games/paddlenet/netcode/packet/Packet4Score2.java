package com.vulpovile.games.paddlenet.netcode.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.vulpovile.games.paddlenet.netcode.NetworkHandler;

public class Packet4Score2 extends Packet {
	public byte score;

	public Packet4Score2() {
	}

	public Packet4Score2(byte score) {
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
		return new Packet4Score2();
	}
}
