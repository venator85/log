package eu.alessiobianchi.log;

import java.io.File;

public final class Log {

	public static boolean isEnabled() {
		return false;
	}

	public static void setEnabled(boolean enabled) {
	}

	public static void init(boolean enableLogcat, File logFile) {
	}

	public static File getLogFile() {
		return null;
	}

	public static void v(String msg, Throwable t, Object tag) {
	}

	public static void d(String msg, Throwable t, Object tag) {
	}

	public static void i(String msg, Throwable t, Object tag) {
	}

	public static void w(String msg, Throwable t, Object tag) {
	}

	public static void e(String msg, Throwable t, Object tag) {
	}

	public static void v(String msg, Object tag) {
	}

	public static void d(String msg, Object tag) {
	}

	public static void i(String msg, Object tag) {
	}

	public static void w(String msg, Object tag) {
	}

	public static void e(String msg, Object tag) {
	}

}
