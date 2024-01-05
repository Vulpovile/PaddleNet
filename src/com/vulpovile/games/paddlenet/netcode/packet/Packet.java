package com.vulpovile.games.paddlenet.netcode.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.vulpovile.games.paddlenet.netcode.NetworkHandler;

public abstract class Packet {
	public static final int PROTOCOL_VERSION = 5;
	
	public static Map<Class<?>, Byte> packetToOp = new HashMap<Class<?>, Byte>();
	public static Map<Byte, Packet> opToPacket = new HashMap<Byte, Packet>();
	
	static
	{
		registerPacket((byte) 1, new Packet1Paddle1());
		registerPacket((byte) 2, new Packet2Paddle2());
		registerPacket((byte) 3, new Packet3Score1());
		registerPacket((byte) 4, new Packet4Score2());
		registerPacket((byte) 5, new Packet5BallVelocity());
		registerPacket((byte) 6, new Packet6BallPosition());
		registerPacket((byte) 7, new Packet7PaddleHit());
		registerPacket((byte) 8, new Packet8PaddleMiss());
		registerPacket((byte) 9, new Packet9BoardConfiguration());
	}
	
	public abstract Packet cloneTypeOnly();
	
	public static void registerPacket(byte opcode, Packet packet)
    {
		opToPacket.put(Byte.valueOf(opcode), packet);
        packetToOp.put(packet.getClass(), Byte.valueOf(opcode));
    }
	
	public final byte getId()
    {
        return ((Byte)packetToOp.get(this.getClass())).byteValue();
    }
	
	public Packet(){

	}

	public void onIncoming(DataInputStream in, NetworkHandler networkHandler) throws IOException {
		networkHandler.handlePacket(this);
	}

	public void sendPacket(DataOutputStream out) throws IOException {
		out.writeByte(getId());
	}
}
