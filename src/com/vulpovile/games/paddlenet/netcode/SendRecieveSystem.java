package com.vulpovile.games.paddlenet.netcode;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.vulpovile.games.paddlenet.netcode.packet.Packet;

public class SendRecieveSystem {

	private final NetworkHandler networkHandler;
	private boolean running;
	private SendThread sendThread = null;
	private RecieveThread recieveThread = null;
	private BlockingQueue<Packet> packetQueue = new LinkedBlockingQueue<Packet>();

	public SendRecieveSystem(NetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
	}

	public void enqueuePacket(Packet packet) {
		this.packetQueue.add(packet);
	}

	public void start() {
		running = true;
		if (sendThread == null)
		{
			sendThread = new SendThread();
			new Thread(sendThread).start();
		}
		if (recieveThread == null)
		{
			recieveThread = new RecieveThread();
			new Thread(recieveThread).start();
		}
	}
	
	public void stop() {
		running = false;
		if (sendThread != null)
		{
			sendThread.interruptThread();
		}
		
		if (recieveThread != null)
		{
			recieveThread.interruptThread();
		}
	}

	private class SendThread implements Runnable {
		private Thread parentThread = null;
		public void run() {
			parentThread = Thread.currentThread();
			while (running)
			{
				try
				{
					Packet packet = packetQueue.take();
					if(packet != null)
						networkHandler.sendPacket(packet);
				}
				catch (IOException e)
				{
					networkHandler.onError(e);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			sendThread = null;
		}

		public void interruptThread() {
			parentThread.interrupt();
		}
	}

	private class RecieveThread implements Runnable {
		private Thread parentThread = null;
		public void run() {
			parentThread = Thread.currentThread();
			while (running)
			{
				try
				{
					networkHandler.recievePacket();
				}
				catch (IOException e)
				{
					networkHandler.onError(e);
				}
			}
			recieveThread = null;
		}

		public void interruptThread() {
			parentThread.interrupt();
		}
	}
}
