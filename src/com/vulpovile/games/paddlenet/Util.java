package com.vulpovile.games.paddlenet;

import java.io.Closeable;
import java.io.IOException;

public class Util {
	public static boolean cleanClose(Closeable closeable) {
		try
		{
			if (closeable != null)
				closeable.close();
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
