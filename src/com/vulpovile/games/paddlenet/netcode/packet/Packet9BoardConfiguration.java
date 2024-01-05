package com.vulpovile.games.paddlenet.netcode.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.vulpovile.games.paddlenet.netcode.NetworkHandler;

public class Packet9BoardConfiguration extends Packet {
	public short paddle1HeightHalf;
	public short paddle2HeightHalf;
	public byte gameOverScore;
	public byte ballHeightHalf;
	public byte ballWidthHalf;
	public byte paddleDivider;
	public byte gameMode;
	public boolean gameOver;
	public boolean colour;
	public boolean rightOwner;

	public Packet9BoardConfiguration() {

	}

	public Packet9BoardConfiguration(int paddle1HeightHalf, int paddle2HeightHalf, int gameOverScore, int ballHeightHalf, int ballWidthHalf, int paddleDivider, int gameMode, boolean gameOver, boolean colour, boolean rightOwner) {
		this((short) paddle1HeightHalf, (short) paddle2HeightHalf, (byte) gameOverScore, (byte) ballHeightHalf, (byte) ballWidthHalf, (byte) paddleDivider, (byte) gameMode, gameOver, colour, rightOwner);
	}

	public Packet9BoardConfiguration(short paddle1HeightHalf, short paddle2HeightHalf, byte gameOverScore, byte ballHeightHalf, byte ballWidthHalf, byte paddleDivider, byte gameMode, boolean gameOver, boolean colour, boolean rightOwner) {
		this.paddle1HeightHalf = paddle1HeightHalf;
		this.paddle2HeightHalf = paddle2HeightHalf;
		this.gameOverScore = gameOverScore;
		this.ballHeightHalf = ballHeightHalf;
		this.ballWidthHalf = ballWidthHalf;
		this.paddleDivider = paddleDivider;
		this.gameMode = gameMode;
		this.gameOver = gameOver;
		this.colour = colour;
		this.rightOwner = rightOwner;
	}

	public void onIncoming(DataInputStream in, NetworkHandler networkHandler) throws IOException {
		paddle1HeightHalf = in.readShort();
		paddle2HeightHalf = in.readShort();
		gameOverScore = in.readByte();
		ballHeightHalf = in.readByte();
		ballWidthHalf = in.readByte();
		paddleDivider = in.readByte();
		gameMode = in.readByte();
		gameOver = in.readBoolean();
		colour = in.readBoolean();
		rightOwner = in.readBoolean();
		networkHandler.handlePacket(this);
	}

	public void sendPacket(DataOutputStream out) throws IOException {
		super.sendPacket(out);
		out.writeShort(paddle1HeightHalf);
		out.writeShort(paddle2HeightHalf);
		out.writeByte(gameOverScore);
		out.writeByte(ballHeightHalf);
		out.writeByte(ballWidthHalf);
		out.writeByte(paddleDivider);
		out.writeByte(gameMode);
		out.writeBoolean(gameOver);
		out.writeBoolean(colour);
		out.writeBoolean(rightOwner);
	}

	@Override
	public Packet cloneTypeOnly() {
		return new Packet9BoardConfiguration();
	}
}
