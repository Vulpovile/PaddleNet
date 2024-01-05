package com.vulpovile.games.paddlenet.netcode.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.vulpovile.games.paddlenet.netcode.NetworkHandler;

public class Packet8PaddleMiss extends Packet {

	public Packet8PaddleMiss() {
	}

	public void onIncoming(DataInputStream in, NetworkHandler networkHandler) throws IOException {
		networkHandler.handlePacket(this);
	}

	public void sendPacket(DataOutputStream out) throws IOException {
		super.sendPacket(out);
	}

	@Override
	public Packet cloneTypeOnly() {
		return new Packet8PaddleMiss();
	}
}
