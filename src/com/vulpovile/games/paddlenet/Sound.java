package com.vulpovile.games.paddlenet;

import javax.sound.sampled.*;

public class Sound {
	public static float SAMPLE_RATE = 14000f;

	public static void tone(int hz, int msecs, double vol) throws LineUnavailableException {
		byte[] buf = new byte[1];
		AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
		SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
		sdl.open(af);
		sdl.start();
		for (int i = 0; i < msecs * 8; i++)
		{
			double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
			buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
			sdl.write(buf, 0, 1);
		}
		sdl.drain();
		sdl.stop();
		sdl.close();
	}

	public static void toneAsync(final int hz, final int msecs, final double vol) {
		new Thread(new Runnable() {

			public void run() {
				try
				{
					tone(hz, msecs, vol);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}

		}).start();
	}
}