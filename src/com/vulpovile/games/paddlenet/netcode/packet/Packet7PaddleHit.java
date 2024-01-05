package com.vulpovile.games.paddlenet.netcode.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.vulpovile.games.paddlenet.netcode.NetworkHandler;

public class Packet7PaddleHit extends Packet {
	public short x;
	public short y;
	public byte xVelocity;
	public byte yVelocity;
	public boolean resetPaddleFlags;

	public Packet7PaddleHit() {
	}

	public Packet7PaddleHit(int x, int y, int xVelocity, int yVelocity) {
		this((short)x, (short)y, (byte)xVelocity, (byte)yVelocity);
	}
	
	public Packet7PaddleHit(short x, short y, byte xVelocity, byte yVelocity) {
		this(x, y, xVelocity, yVelocity, true);
	}
	
	public Packet7PaddleHit(short x, short y, byte xVelocity, byte yVelocity, boolean resetPaddleFlags) {
		this.x = x;
		this.y = y;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		this.resetPaddleFlags = resetPaddleFlags;
	}

	public void onIncoming(DataInputStream in, NetworkHandler networkHandler) throws IOException {
		x = in.readShort();
		y = in.readShort();
		xVelocity = in.readByte();
		yVelocity = in.readByte();
		resetPaddleFlags = in.readBoolean();
		networkHandler.handlePacket(this);
	}

	public void sendPacket(DataOutputStream out) throws IOException {
		super.sendPacket(out);
		out.writeShort(x);
		out.writeShort(y);
		out.writeByte(xVelocity);
		out.writeByte(yVelocity);
		out.writeBoolean(resetPaddleFlags);
	}

	@Override
	public Packet cloneTypeOnly() {
		return new Packet7PaddleHit();
	}
}
